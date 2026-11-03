package dao;

import database.ConnectionFactory;
import model.RepaymentSchedule;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RepaymentScheduleDao {

    private final ConnectionFactory connectionFactory;

    public RepaymentScheduleDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void save(RepaymentSchedule repaymentSchedule) {
        String sql = """
                INSERT INTO repayment_schedules
                (loan_id, installment_number, due_date, principal_amount,
                 interest_amount, total_amount, payment_status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, repaymentSchedule.getLoanId());
            statement.setInt(2, repaymentSchedule.getInstallmentNumber());
            statement.setObject(3, repaymentSchedule.getDueDate());
            statement.setBigDecimal(4, repaymentSchedule.getPrincipalAmount());
            statement.setBigDecimal(5, repaymentSchedule.getInterestAmount());
            statement.setBigDecimal(6, repaymentSchedule.getTotalAmount());
            statement.setString(7, repaymentSchedule.getPaymentStatus());

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException("Could not save repayment schedule.", exception);
        }
    }

    public List<RepaymentSchedule> findAll() {
        String sql = """
                SELECT rs.id, rs.loan_id, f.full_name AS farmer_name,
                       l.loan_type, rs.installment_number, rs.due_date,
                       rs.principal_amount, rs.interest_amount,
                       rs.total_amount, rs.payment_status
                FROM repayment_schedules rs
                JOIN loans l ON rs.loan_id = l.id
                JOIN farmers f ON l.farmer_id = f.id
                ORDER BY rs.due_date ASC, rs.installment_number ASC
                """;

        List<RepaymentSchedule> repaymentSchedules = new ArrayList<>();

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                RepaymentSchedule repaymentSchedule = new RepaymentSchedule();

                repaymentSchedule.setId(resultSet.getLong("id"));
                repaymentSchedule.setLoanId(resultSet.getLong("loan_id"));
                repaymentSchedule.setFarmerName(resultSet.getString("farmer_name"));
                repaymentSchedule.setLoanType(resultSet.getString("loan_type"));
                repaymentSchedule.setInstallmentNumber(
                        resultSet.getInt("installment_number")
                );
                repaymentSchedule.setDueDate(
                        resultSet.getDate("due_date").toLocalDate()
                );
                repaymentSchedule.setPrincipalAmount(
                        resultSet.getBigDecimal("principal_amount")
                );
                repaymentSchedule.setInterestAmount(
                        resultSet.getBigDecimal("interest_amount")
                );
                repaymentSchedule.setTotalAmount(
                        resultSet.getBigDecimal("total_amount")
                );
                repaymentSchedule.setPaymentStatus(
                        resultSet.getString("payment_status")
                );

                repaymentSchedules.add(repaymentSchedule);
            }

            return repaymentSchedules;

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Could not load repayment schedules.",
                    exception
            );
        }
    }

    public void update(RepaymentSchedule repaymentSchedule) {
        String sql = """
                UPDATE repayment_schedules
                SET loan_id = ?,
                    installment_number = ?,
                    due_date = ?,
                    principal_amount = ?,
                    interest_amount = ?,
                    total_amount = ?,
                    payment_status = ?
                WHERE id = ?
                """;

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, repaymentSchedule.getLoanId());
            statement.setInt(2, repaymentSchedule.getInstallmentNumber());
            statement.setObject(3, repaymentSchedule.getDueDate());
            statement.setBigDecimal(4, repaymentSchedule.getPrincipalAmount());
            statement.setBigDecimal(5, repaymentSchedule.getInterestAmount());
            statement.setBigDecimal(6, repaymentSchedule.getTotalAmount());
            statement.setString(7, repaymentSchedule.getPaymentStatus());
            statement.setLong(8, repaymentSchedule.getId());

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Could not update repayment schedule.",
                    exception
            );
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM repayment_schedules WHERE id = ?";

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Could not delete repayment schedule.",
                    exception
            );
        }
    }
}