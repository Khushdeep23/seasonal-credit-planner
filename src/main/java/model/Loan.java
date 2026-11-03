package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Loan {

    private Long id;
    private Long farmerId;
    private String farmerName;
    private String loanType;
    private String purpose;
    private BigDecimal loanAmount;
    private BigDecimal annualInterestRate;
    private Integer tenureMonths;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    public Loan() {
    }

    public Loan(
            Long farmerId,
            String loanType,
            String purpose,
            BigDecimal loanAmount,
            BigDecimal annualInterestRate,
            Integer tenureMonths,
            LocalDate startDate,
            LocalDate endDate,
            String status) {

        this.farmerId = farmerId;
        this.loanType = loanType;
        this.purpose = purpose;
        this.loanAmount = loanAmount;
        this.annualInterestRate = annualInterestRate;
        this.tenureMonths = tenureMonths;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(BigDecimal annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public Integer getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}