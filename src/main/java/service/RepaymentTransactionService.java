package service;

import dao.RepaymentTransactionDao;
import model.RepaymentEntry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RepaymentTransactionService {

    private final RepaymentTransactionDao repaymentTransactionDao;

    public RepaymentTransactionService(
            RepaymentTransactionDao repaymentTransactionDao
    ) {
        this.repaymentTransactionDao = repaymentTransactionDao;
    }

    public void recordFullPayment(RepaymentEntry repaymentEntry) {

        validateRepaymentEntry(repaymentEntry);

        repaymentTransactionDao.saveEntryAndMarkSchedulePaid(
                repaymentEntry
        );
    }

    private void validateRepaymentEntry(RepaymentEntry repaymentEntry) {

        if (repaymentEntry.getRepaymentScheduleId() == null
                || repaymentEntry.getRepaymentScheduleId() <= 0) {
            throw new IllegalArgumentException(
                    "Please select a repayment schedule."
            );
        }

        if (repaymentEntry.getPaymentDate() == null) {
            throw new IllegalArgumentException(
                    "Payment date is required."
            );
        }

        if (repaymentEntry.getAmountPaid() == null
                || repaymentEntry.getAmountPaid()
                .compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Paid amount must be greater than zero."
            );
        }

        if (repaymentEntry.getPaymentMode() == null
                || repaymentEntry.getPaymentMode().isBlank()) {
            throw new IllegalArgumentException(
                    "Payment mode is required."
            );
        }
    }
}