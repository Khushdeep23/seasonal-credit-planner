package service;

import dao.LoanDao;
import model.Loan;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanDao loanDao;

    public LoanService(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    public void addLoan(Loan loan) {
        validateLoan(loan);
        loan.setStatus("ACTIVE");
        loanDao.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanDao.findAll();
    }

    public void updateLoan(Loan loan) {
        validateLoan(loan);
        loanDao.update(loan);
    }

    public void deleteLoan(Long id) {
        loanDao.deleteById(id);
    }

    private void validateLoan(Loan loan) {
        if (loan.getFarmerId() == null || loan.getFarmerId() <= 0) {
            throw new IllegalArgumentException("Please select a farmer.");
        }

        if (loan.getLoanType() == null || loan.getLoanType().isBlank()) {
            throw new IllegalArgumentException("Loan type is required.");
        }

        if (loan.getPurpose() == null || loan.getPurpose().isBlank()) {
            throw new IllegalArgumentException("Loan purpose is required.");
        }

        if (loan.getLoanAmount() == null
                || loan.getLoanAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be greater than zero.");
        }

        if (loan.getAnnualInterestRate() == null
                || loan.getAnnualInterestRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }

        if (loan.getTenureMonths() == null || loan.getTenureMonths() <= 0) {
            throw new IllegalArgumentException("Loan tenure must be at least one month.");
        }

        if (loan.getStartDate() == null || loan.getEndDate() == null) {
            throw new IllegalArgumentException("Loan start date and end date are required.");
        }

        if (!loan.getEndDate().isAfter(loan.getStartDate())) {
            throw new IllegalArgumentException(
                    "Loan end date must be after the start date."
            );
        }
    }
}