package dao;

import database.ConnectionFactory;
import model.RepaymentEntry;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class RepaymentTransactionDao {

    private final ConnectionFactory connectionFactory;

    public RepaymentTransactionDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void saveEntryAndMarkSchedulePaid(
            RepaymentEntry repaymentEntry
    ) {
        String scheduleSql = """
                SELECT total_amount, payment_status
                FROM repayment_schedules
                WHERE id = ?
                FOR UPDATE
                """;

        String entrySql = """
                INSERT INTO repayment_entries
                (repayment_schedule_id, payment_date, amount_paid,
                 payment_mode, remarks)
                VALUES (?, ?, ?, ?, ?)
                """;

        String statusSql = """
                UPDATE repayment_schedules
                SET payment_status = 'PAID'
                WHERE id = ?
                """;

        try (Connection connection = connectionFactory.getConnection()) {

            connection.setAutoCommit(false);

            try {
                BigDecimal installmentAmount;

                try (PreparedStatement statement =
                             connection.prepareStatement(scheduleSql)) {

                    statement.setLong(
                            1,
                            repaymentEntry.getRepaymentScheduleId()
                    );

                    try (ResultSet resultSet = statement.executeQuery()) {

                        if (!resultSet.next()) {
                            throw new IllegalArgumentException(
                                    "Selected repayment schedule was not found."
                            );
                        }

                        if ("PAID".equalsIgnoreCase(
                                resultSet.getString("payment_status")
                        )) {
                            throw new IllegalArgumentException(
                                    "This installment is already marked as paid."
                            );
                        }

                        installmentAmount =
                                resultSet.getBigDecimal("total_amount");
                    }
                }

                if (repaymentEntry.getAmountPaid()
                        .compareTo(installmentAmount) != 0) {
                    throw new IllegalArgumentException(
                            "Paid amount must exactly match the installment amount: ₹ "
                                    + installmentAmount
                    );
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(entrySql)) {

                    statement.setLong(
                            1,
                            repaymentEntry.getRepaymentScheduleId()
                    );
                    statement.setObject(
                            2,
                            repaymentEntry.getPaymentDate()
                    );
                    statement.setBigDecimal(
                            3,
                            repaymentEntry.getAmountPaid()
                    );
                    statement.setString(
                            4,
                            repaymentEntry.getPaymentMode()
                    );
                    statement.setString(
                            5,
                            repaymentEntry.getRemarks()
                    );

                    statement.executeUpdate();
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(statusSql)) {

                    statement.setLong(
                            1,
                            repaymentEntry.getRepaymentScheduleId()
                    );

                    if (statement.executeUpdate() != 1) {
                        throw new SQLException(
                                "Could not update repayment schedule status."
                        );
                    }
                }

                connection.commit();

            } catch (Exception exception) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }

                if (exception instanceof RuntimeException) {
                    throw (RuntimeException) exception;
                }

                throw new RuntimeException(
                        "Could not save repayment entry transaction.",
                        exception
                );

            } finally {

                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Could not start repayment transaction.",
                    exception
            );
        }
    }
}