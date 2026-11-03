
package service;

import model.Loan;
import org.springframework.stereotype.Service;
import service.interest.InterestCalculationStrategy;
import service.interest.InterestStrategyFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LoanCalculationService {

    private final InterestStrategyFactory interestStrategyFactory;

    public LoanCalculationService(
            InterestStrategyFactory interestStrategyFactory
    ) {
        this.interestStrategyFactory = interestStrategyFactory;
    }

    public BigDecimal calculateInterest(Loan loan) {

        InterestCalculationStrategy strategy =
                interestStrategyFactory.getStrategy(loan.getLoanType());

        return strategy.calculateInterest(
                loan.getLoanAmount(),
                loan.getAnnualInterestRate(),
                loan.getTenureMonths()
        );
    }

    public BigDecimal calculateTotalRepayment(Loan loan) {

        BigDecimal interest = calculateInterest(loan);

        return loan.getLoanAmount()
                .add(interest)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
