
package service.interest;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class InterestStrategyFactory {

    private final SimpleInterestStrategy simpleInterestStrategy;
    private final ReducingBalanceInterestStrategy reducingBalanceInterestStrategy;

    public InterestStrategyFactory(
            SimpleInterestStrategy simpleInterestStrategy,
            ReducingBalanceInterestStrategy reducingBalanceInterestStrategy
    ) {
        this.simpleInterestStrategy = simpleInterestStrategy;
        this.reducingBalanceInterestStrategy = reducingBalanceInterestStrategy;
    }

    public InterestCalculationStrategy getStrategy(String loanType) {

        if (loanType == null || loanType.isBlank()) {
            throw new IllegalArgumentException("Loan type cannot be empty.");
        }

        String normalizedType = loanType.trim().toLowerCase(Locale.ROOT);

        return switch (normalizedType) {

            case "equipment loan" -> reducingBalanceInterestStrategy;

            case "crop loan",
                 "kisan credit card",
                 "other" -> simpleInterestStrategy;

            default -> throw new IllegalArgumentException(
                    "Unsupported loan type: " + loanType
            );
        };
    }
}
