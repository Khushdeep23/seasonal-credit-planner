package service;

import dao.RepaymentEntryDao;
import model.RepaymentEntry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RepaymentEntryService {

    private final RepaymentEntryDao repaymentEntryDao;

    public RepaymentEntryService(RepaymentEntryDao repaymentEntryDao) {
        this.repaymentEntryDao = repaymentEntryDao;
    }

    public void addRepaymentEntry(RepaymentEntry repaymentEntry) {
        validateRepaymentEntry(repaymentEntry);
        repaymentEntryDao.save(repaymentEntry);
    }

    public List<RepaymentEntry> getAllRepaymentEntries() {
        return repaymentEntryDao.findAll();
    }

    public void updateRepaymentEntry(RepaymentEntry repaymentEntry) {
        validateRepaymentEntry(repaymentEntry);
        repaymentEntryDao.update(repaymentEntry);
    }

    public void deleteRepaymentEntry(Long id) {
        repaymentEntryDao.deleteById(id);
    }

    private void validateRepaymentEntry(RepaymentEntry repaymentEntry) {
        if (repaymentEntry.getRepaymentScheduleId() == null
                || repaymentEntry.getRepaymentScheduleId() <= 0) {
            throw new IllegalArgumentException(
                    "Please select a repayment schedule."
            );
        }

        if (repaymentEntry.getPaymentDate() == null) {
            throw new IllegalArgumentException("Payment date is required.");
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
            throw new IllegalArgumentException("Payment mode is required.");
        }
    }
}