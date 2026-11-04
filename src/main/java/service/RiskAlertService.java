package service;

import model.Farmer;
import model.RepaymentFeasibility;
import model.RepaymentSchedule;
import model.RiskAlert;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiskAlertService {

    private static final BigDecimal HIGH_BURDEN_PERCENT =
            new BigDecimal("50.00");

    private static final int UPCOMING_PAYMENT_DAYS = 7;

    private final FarmerService farmerService;
    private final RepaymentFeasibilityService repaymentFeasibilityService;
    private final RepaymentScheduleService repaymentScheduleService;
    private final MessageSource messageSource;

    public RiskAlertService(
            FarmerService farmerService,
            RepaymentFeasibilityService repaymentFeasibilityService,
            RepaymentScheduleService repaymentScheduleService,
            MessageSource messageSource
    ) {
        this.farmerService = farmerService;
        this.repaymentFeasibilityService = repaymentFeasibilityService;
        this.repaymentScheduleService = repaymentScheduleService;
        this.messageSource = messageSource;
    }

    public List<RiskAlert> getAllRiskAlerts() {

        List<RiskAlert> riskAlerts = new ArrayList<>();

        addIncomeAndBurdenAlerts(riskAlerts);
        addPaymentDateAlerts(riskAlerts);

        return riskAlerts;
    }

    private void addIncomeAndBurdenAlerts(List<RiskAlert> riskAlerts) {

        List<Farmer> farmers = farmerService.getAllFarmers();

        for (Farmer farmer : farmers) {

            RepaymentFeasibility feasibility =
                    repaymentFeasibilityService.calculateForFarmer(
                            farmer.getId()
                    );

            BigDecimal income = feasibility.getTotalNetCropIncome();
            BigDecimal repayment = feasibility.getTotalLoanRepayment();

            if (repayment.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            if (income.compareTo(repayment) < 0) {
                riskAlerts.add(new RiskAlert(
                        message(
                                "risk.type.insufficientIncome",
                                "INSUFFICIENT INCOME"
                        ),
                        "HIGH",
                        farmer.getFullName(),
                        message(
                                "risk.message.insufficientIncome",
                                "Projected net crop income cannot cover "
                                        + "the total loan repayment."
                        ),
                        null,
                        feasibility.getRemainingAmount().abs()
                ));
                continue;
            }

            if (income.compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal burdenPercent = repayment
                        .divide(income, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .setScale(2, RoundingMode.HALF_UP);

                if (burdenPercent.compareTo(HIGH_BURDEN_PERCENT) >= 0) {
                    riskAlerts.add(new RiskAlert(
                            message(
                                    "risk.type.highRepaymentBurden",
                                    "HIGH REPAYMENT BURDEN"
                            ),
                            "MEDIUM",
                            farmer.getFullName(),
                            message(
                                    "risk.message.highRepaymentBurden",
                                    "Loan repayment uses {0}% of projected "
                                            + "net crop income.",
                                    burdenPercent
                            ),
                            null,
                            repayment
                    ));
                }
            }
        }
    }

    private void addPaymentDateAlerts(List<RiskAlert> riskAlerts) {

        LocalDate today = LocalDate.now();
        LocalDate upcomingLimit = today.plusDays(UPCOMING_PAYMENT_DAYS);

        List<RepaymentSchedule> schedules =
                repaymentScheduleService.getAllRepaymentSchedules();

        for (RepaymentSchedule schedule : schedules) {

            if ("PAID".equalsIgnoreCase(schedule.getPaymentStatus())) {
                continue;
            }

            if (schedule.getDueDate().isBefore(today)) {
                riskAlerts.add(new RiskAlert(
                        message(
                                "risk.type.overduePayment",
                                "OVERDUE PAYMENT"
                        ),
                        "HIGH",
                        schedule.getFarmerName(),
                        message(
                                "risk.message.overduePayment",
                                "Installment {0} is overdue.",
                                schedule.getInstallmentNumber()
                        ),
                        schedule.getDueDate(),
                        schedule.getTotalAmount()
                ));
            } else if (!schedule.getDueDate().isAfter(upcomingLimit)) {
                riskAlerts.add(new RiskAlert(
                        message(
                                "risk.type.upcomingPayment",
                                "UPCOMING PAYMENT"
                        ),
                        "LOW",
                        schedule.getFarmerName(),
                        message(
                                "risk.message.upcomingPayment",
                                "Installment {0} is due within the next "
                                        + "{1} days.",
                                schedule.getInstallmentNumber(),
                                UPCOMING_PAYMENT_DAYS
                        ),
                        schedule.getDueDate(),
                        schedule.getTotalAmount()
                ));
            }
        }
    }

    private String message(
            String code,
            String defaultMessage,
            Object... arguments
    ) {
        return messageSource.getMessage(
                code,
                arguments,
                defaultMessage,
                LocaleContextHolder.getLocale()
        );
    }
}