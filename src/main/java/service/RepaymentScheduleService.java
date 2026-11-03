package service;

import dao.RepaymentScheduleDao;
import model.RepaymentSchedule;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RepaymentScheduleService {

    private final RepaymentScheduleDao repaymentScheduleDao;

    public RepaymentScheduleService(RepaymentScheduleDao repaymentScheduleDao) {
        this.repaymentScheduleDao = repaymentScheduleDao;
    }

    public void addRepaymentSchedule(RepaymentSchedule repaymentSchedule) {
        validateRepaymentSchedule(repaymentSchedule);

        repaymentSchedule.setTotalAmount(calculateTotalAmount(repaymentSchedule));

        if (repaymentSchedule.getPaymentStatus() == null
                || repaymentSchedule.getPaymentStatus().isBlank()) {
            repaymentSchedule.setPaymentStatus("PENDING");
        }

        repaymentScheduleDao.save(repaymentSchedule);
    }

    public List<RepaymentSchedule> getAllRepaymentSchedules() {
        return repaymentScheduleDao.findAll();
    }

    public void updateRepaymentSchedule(RepaymentSchedule repaymentSchedule) {
        validateRepaymentSchedule(repaymentSchedule);

        repaymentSchedule.setTotalAmount(calculateTotalAmount(repaymentSchedule));

        repaymentScheduleDao.update(repaymentSchedule);
    }

    public void deleteRepaymentSchedule(Long id) {
        repaymentScheduleDao.deleteById(id);
    }

    private void validateRepaymentSchedule(RepaymentSchedule repaymentSchedule) {
        if (repaymentSchedule.getLoanId() == null
                || repaymentSchedule.getLoanId() <= 0) {
            throw new IllegalArgumentException("Please select a loan.");
        }

        if (repaymentSchedule.getInstallmentNumber() == null
                || repaymentSchedule.getInstallmentNumber() <= 0) {
            throw new IllegalArgumentException(
                    "Installment number must be at least one."
            );
        }

        if (repaymentSchedule.getDueDate() == null) {
            throw new IllegalArgumentException("Due date is required.");
        }

        if (repaymentSchedule.getPrincipalAmount() == null
                || repaymentSchedule.getPrincipalAmount()
                .compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Principal amount cannot be negative."
            );
        }

        if (repaymentSchedule.getInterestAmount() == null
                || repaymentSchedule.getInterestAmount()
                .compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Interest amount cannot be negative."
            );
        }

        if (repaymentSchedule.getPrincipalAmount()
                .add(repaymentSchedule.getInterestAmount())
                .compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Total installment amount must be greater than zero."
            );
        }
    }

    private BigDecimal calculateTotalAmount(RepaymentSchedule repaymentSchedule) {
        return repaymentSchedule.getPrincipalAmount()
                .add(repaymentSchedule.getInterestAmount())
                .setScale(2, RoundingMode.HALF_UP);
    }
}