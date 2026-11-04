package service;

import model.Farmer;
import model.RepaymentFeasibility;
import model.RepaymentSchedule;
import model.RiskAlert;
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

    public RiskAlertService(
            FarmerService farmerService,
            RepaymentFeasibilityService repaymentFeasibilityService,
            RepaymentScheduleService repaymentScheduleService
    ) {
        this.farmerService = farmerService;
        this.repaymentFeasibilityService = repaymentFeasibilityService;
        this.repaymentScheduleService = repaymentScheduleService;
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
                        "INSUFFICIENT INCOME",
                        "HIGH",
                        farmer.getFullName(),
                        "Projected net crop income cannot cover the total loan repayment.",
                        null,
                        feasibility.getRemainingAmount().abs()
                ));
                continue;
            }

            if (income.compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal burdenPercent = repayment
                        .divide(income, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));

                if (burdenPercent.compareTo(HIGH_BURDEN_PERCENT) >= 0) {
                    riskAlerts.add(new RiskAlert(
                            "HIGH REPAYMENT BURDEN",
                            "MEDIUM",
                            farmer.getFullName(),
                            "Loan repayment uses "
                                    + burdenPercent.setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            )
                                    + "% of projected net crop income.",
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
                        "OVERDUE PAYMENT",
                        "HIGH",
                        schedule.getFarmerName(),
                        "Installment "
                                + schedule.getInstallmentNumber()
                                + " is overdue.",
                        schedule.getDueDate(),
                        schedule.getTotalAmount()
                ));
            } else if (!schedule.getDueDate().isAfter(upcomingLimit)) {
                riskAlerts.add(new RiskAlert(
                        "UPCOMING PAYMENT",
                        "LOW",
                        schedule.getFarmerName(),
                        "Installment "
                                + schedule.getInstallmentNumber()
                                + " is due within the next "
                                + UPCOMING_PAYMENT_DAYS
                                + " days.",
                        schedule.getDueDate(),
                        schedule.getTotalAmount()
                ));
            }
        }
    }
}