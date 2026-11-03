
package service.interest;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SimpleInterestStrategy implements InterestCalculationStrategy {

    @Override
    public BigDecimal calculateInterest(
            BigDecimal principalAmount,
            BigDecimal annualInterestRate,
            Integer tenureMonths
    ) {

        BigDecimal timeInYears = BigDecimal.valueOf(tenureMonths)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        return principalAmount
                .multiply(annualInterestRate)
                .multiply(timeInYears)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
