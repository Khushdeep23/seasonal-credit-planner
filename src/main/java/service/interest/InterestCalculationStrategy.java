package service.interest;

import java.math.BigDecimal;

public interface InterestCalculationStrategy {

    BigDecimal calculateInterest(
            BigDecimal principalAmount,
            BigDecimal annualInterestRate,
            Integer tenureMonths
    );
}