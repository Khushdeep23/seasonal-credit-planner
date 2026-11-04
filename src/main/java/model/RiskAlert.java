package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RiskAlert {

    private final String alertType;
    private final String severity;
    private final String farmerName;
    private final String message;
    private final LocalDate dueDate;
    private final BigDecimal amount;

    public RiskAlert(
            String alertType,
            String severity,
            String farmerName,
            String message,
            LocalDate dueDate,
            BigDecimal amount
    ) {
        this.alertType = alertType;
        this.severity = severity;
        this.farmerName = farmerName;
        this.message = message;
        this.dueDate = dueDate;
        this.amount = amount;
    }

    public String getAlertType() {
        return alertType;
    }

    public String getSeverity() {
        return severity;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}