
package service.interest;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ReducingBalanceInterestStrategy implements InterestCalculationStrategy {

    @Override
    public BigDecimal calculateInterest(
            BigDecimal principalAmount,
            BigDecimal annualInterestRate,
            Integer tenureMonths
    ) {

        BigDecimal monthlyRate = annualInterestRate.divide(
                BigDecimal.valueOf(1200),
                12,
                RoundingMode.HALF_UP
        );

        BigDecimal balance = principalAmount;
        BigDecimal totalInterest = BigDecimal.ZERO;

        BigDecimal monthlyPayment;

        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            monthlyPayment = principalAmount.divide(
                    BigDecimal.valueOf(tenureMonths),
                    12,
                    RoundingMode.HALF_UP
            );
        } else {
            BigDecimal factor = BigDecimal.ONE.add(monthlyRate)
                    .pow(tenureMonths);

            monthlyPayment = principalAmount
                    .multiply(monthlyRate)
                    .multiply(factor)
                    .divide(
                            factor.subtract(BigDecimal.ONE),
                            12,
                            RoundingMode.HALF_UP
                    );
        }

        for (int month = 1; month <= tenureMonths; month++) {

            BigDecimal interest = balance.multiply(monthlyRate)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal principalPaid = monthlyPayment
                    .subtract(interest)
                    .setScale(2, RoundingMode.HALF_UP);

            if (month == tenureMonths || principalPaid.compareTo(balance) > 0) {
                principalPaid = balance;
            }

            totalInterest = totalInterest.add(interest);
            balance = balance.subtract(principalPaid);
        }

        return totalInterest.setScale(2, RoundingMode.HALF_UP);
    }
}
