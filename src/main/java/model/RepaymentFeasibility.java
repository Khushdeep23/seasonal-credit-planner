package model;

import java.math.BigDecimal;

public class RepaymentFeasibility {

    private BigDecimal totalNetCropIncome;
    private BigDecimal totalLoanRepayment;
    private BigDecimal remainingAmount;
    private String status;

    public RepaymentFeasibility(
            BigDecimal totalNetCropIncome,
            BigDecimal totalLoanRepayment
    ) {
        this.totalNetCropIncome = totalNetCropIncome;
        this.totalLoanRepayment = totalLoanRepayment;

        this.remainingAmount = totalNetCropIncome.subtract(totalLoanRepayment);

        this.status = totalNetCropIncome.compareTo(totalLoanRepayment) >= 0
                ? "FEASIBLE"
                : "NOT FEASIBLE";
    }

    public BigDecimal getTotalNetCropIncome() {
        return totalNetCropIncome;
    }

    public BigDecimal getTotalLoanRepayment() {
        return totalLoanRepayment;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public String getStatus() {
        return status;
    }
}