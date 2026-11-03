package dao;

import database.ConnectionFactory;
import model.Loan;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LoanDao {

    private final ConnectionFactory connectionFactory;

    public LoanDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void save(Loan loan) {
        String sql = """
                INSERT INTO loans
                (farmer_id, loan_type, purpose, loan_amount,
                 annual_interest_rate, tenure_months, start_date, end_date, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, loan.getFarmerId());
            statement.setString(2, loan.getLoanType());
            statement.setString(3, loan.getPurpose());
            statement.setBigDecimal(4, loan.getLoanAmount());
            statement.setBigDecimal(5, loan.getAnnualInterestRate());
            statement.setInt(6, loan.getTenureMonths());
            statement.setObject(7, loan.getStartDate());
            statement.setObject(8, loan.getEndDate());
            statement.setString(9, loan.getStatus());

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException("Could not save loan.", exception);
        }
    }

    public List<Loan> findAll() {
        String sql = """
                SELECT l.id, l.farmer_id, f.full_name AS farmer_name,
                       l.loan_type, l.purpose, l.loan_amount,
                       l.annual_interest_rate, l.tenure_months,
                       l.start_date, l.end_date, l.status
                FROM loans l
                JOIN farmers f ON l.farmer_id = f.id
                ORDER BY l.id DESC
                """;

        List<Loan> loans = new ArrayList<>();

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Loan loan = new Loan();

                loan.setId(resultSet.getLong("id"));
                loan.setFarmerId(resultSet.getLong("farmer_id"));
                loan.setFarmerName(resultSet.getString("farmer_name"));
                loan.setLoanType(resultSet.getString("loan_type"));
                loan.setPurpose(resultSet.getString("purpose"));
                loan.setLoanAmount(resultSet.getBigDecimal("loan_amount"));
                loan.setAnnualInterestRate(
                        resultSet.getBigDecimal("annual_interest_rate")
                );
                loan.setTenureMonths(resultSet.getInt("tenure_months"));
                loan.setStartDate(resultSet.getDate("start_date").toLocalDate());
                loan.setEndDate(resultSet.getDate("end_date").toLocalDate());
                loan.setStatus(resultSet.getString("status"));

                loans.add(loan);
            }

            return loans;

        } catch (SQLException exception) {
            throw new RuntimeException("Could not load loans.", exception);
        }
    }

    public void update(Loan loan) {
        String sql = """
                UPDATE loans
                SET farmer_id = ?,
                    loan_type = ?,
                    purpose = ?,
                    loan_amount = ?,
                    annual_interest_rate = ?,
                    tenure_months = ?,
                    start_date = ?,
                    end_date = ?,
                    status = ?
                WHERE id = ?
                """;

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, loan.getFarmerId());
            statement.setString(2, loan.getLoanType());
            statement.setString(3, loan.getPurpose());
            statement.setBigDecimal(4, loan.getLoanAmount());
            statement.setBigDecimal(5, loan.getAnnualInterestRate());
            statement.setInt(6, loan.getTenureMonths());
            statement.setObject(7, loan.getStartDate());
            statement.setObject(8, loan.getEndDate());
            statement.setString(9, loan.getStatus());
            statement.setLong(10, loan.getId());

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException("Could not update loan.", exception);
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM loans WHERE id = ?";

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException("Could not delete loan.", exception);
        }
    }
}