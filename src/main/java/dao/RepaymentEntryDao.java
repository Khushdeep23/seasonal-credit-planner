package dao;

import database.ConnectionFactory;
import model.RepaymentEntry;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RepaymentEntryDao {

    private final ConnectionFactory connectionFactory;

    public RepaymentEntryDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void save(RepaymentEntry repaymentEntry) {
        String sql = """
                INSERT INTO repayment_entries
                (repayment_schedule_id, payment_date, amount_paid, payment_mode, remarks)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, repaymentEntry.getRepaymentScheduleId());
            statement.setObject(2, repaymentEntry.getPaymentDate());
            statement.setBigDecimal(3, repaymentEntry.getAmountPaid());
            statement.setString(4, repaymentEntry.getPaymentMode());
            statement.setString(5, repaymentEntry.getRemarks());

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException("Could not save repayment entry.", exception);
        }
    }

    public List<RepaymentEntry> findAll() {
        String sql = """
                SELECT re.id, re.repayment_schedule_id, rs.loan_id,
                       f.full_name AS farmer_name, rs.installment_number,
                       re.payment_date, re.amount_paid, re.payment_mode, re.remarks
                FROM repayment_entries re
                JOIN repayment_schedules rs
                    ON re.repayment_schedule_id = rs.id
                JOIN loans l ON rs.loan_id = l.id
                JOIN farmers f ON l.farmer_id = f.id
                ORDER BY re.payment_date DESC, re.id DESC
                """;

        List<RepaymentEntry> repaymentEntries = new ArrayList<>();

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                RepaymentEntry repaymentEntry = new RepaymentEntry();

                repaymentEntry.setId(resultSet.getLong("id"));
                repaymentEntry.setRepaymentScheduleId(
                        resultSet.getLong("repayment_schedule_id")
                );
                repaymentEntry.setLoanId(resultSet.getLong("loan_id"));
                repaymentEntry.setFarmerName(resultSet.getString("farmer_name"));
                repaymentEntry.setInstallmentNumber(
                        resultSet.getInt("installment_number")
                );
                repaymentEntry.setPaymentDate(
                        resultSet.getDate("payment_date").toLocalDate()
                );
                repaymentEntry.setAmountPaid(
                        resultSet.getBigDecimal("amount_paid")
                );
                repaymentEntry.setPaymentMode(
                        resultSet.getString("payment_mode")
                );
                repaymentEntry.setRemarks(resultSet.getString("remarks"));

                repaymentEntries.add(repaymentEntry);
            }

            return repaymentEntries;

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Could not load repayment entries.",
                    exception
            );
        }
    }

    public void update(RepaymentEntry repaymentEntry) {
        String sql = """
                UPDATE repayment_entries
                SET repayment_schedule_id = ?,
                    payment_date = ?,
                    amount_paid = ?,
                    payment_mode = ?,
                    remarks = ?
                WHERE id = ?
                """;

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, repaymentEntry.getRepaymentScheduleId());
            statement.setObject(2, repaymentEntry.getPaymentDate());
            statement.setBigDecimal(3, repaymentEntry.getAmountPaid());
            statement.setString(4, repaymentEntry.getPaymentMode());
            statement.setString(5, repaymentEntry.getRemarks());
            statement.setLong(6, repaymentEntry.getId());

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Could not update repayment entry.",
                    exception
            );
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM repayment_entries WHERE id = ?";

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Could not delete repayment entry.",
                    exception
            );
        }
    }
}