package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RepaymentEntry {

    private Long id;
    private Long repaymentScheduleId;
    private Long loanId;
    private String farmerName;
    private Integer installmentNumber;
    private LocalDate paymentDate;
    private BigDecimal amountPaid;
    private String paymentMode;
    private String remarks;

    public RepaymentEntry() {
    }

    public RepaymentEntry(
            Long repaymentScheduleId,
            LocalDate paymentDate,
            BigDecimal amountPaid,
            String paymentMode,
            String remarks) {

        this.repaymentScheduleId = repaymentScheduleId;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.paymentMode = paymentMode;
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRepaymentScheduleId() {
        return repaymentScheduleId;
    }

    public void setRepaymentScheduleId(Long repaymentScheduleId) {
        this.repaymentScheduleId = repaymentScheduleId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}