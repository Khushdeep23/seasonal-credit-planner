package controller;

import model.RepaymentEntry;
import model.RepaymentSchedule;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.RepaymentEntryService;
import service.RepaymentScheduleService;
import service.RepaymentTransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class RepaymentController {

    private final RepaymentScheduleService repaymentScheduleService;
    private final RepaymentEntryService repaymentEntryService;
    private final RepaymentTransactionService repaymentTransactionService;

    public RepaymentController(
            RepaymentScheduleService repaymentScheduleService,
            RepaymentEntryService repaymentEntryService,
            RepaymentTransactionService repaymentTransactionService
    ) {
        this.repaymentScheduleService = repaymentScheduleService;
        this.repaymentEntryService = repaymentEntryService;
        this.repaymentTransactionService = repaymentTransactionService;
    }

    @PostMapping("/repayment-schedules")
    public String addRepaymentSchedule(
            @RequestParam Long loanId,
            @RequestParam Integer installmentNumber,
            @RequestParam LocalDate dueDate,
            @RequestParam BigDecimal principalAmount,
            @RequestParam BigDecimal interestAmount,
            @RequestParam(defaultValue = "PENDING") String paymentStatus,
            RedirectAttributes redirectAttributes
    ) {

        try {
            RepaymentSchedule repaymentSchedule = new RepaymentSchedule(
                    loanId,
                    installmentNumber,
                    dueDate,
                    principalAmount,
                    interestAmount,
                    BigDecimal.ZERO,
                    paymentStatus
            );

            repaymentScheduleService.addRepaymentSchedule(repaymentSchedule);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Repayment schedule added successfully."
            );

        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/";
    }

    @PostMapping("/repayment-schedules/update")
    public String updateRepaymentSchedule(
            @RequestParam Long id,
            @RequestParam Long loanId,
            @RequestParam Integer installmentNumber,
            @RequestParam LocalDate dueDate,
            @RequestParam BigDecimal principalAmount,
            @RequestParam BigDecimal interestAmount,
            @RequestParam(defaultValue = "PENDING") String paymentStatus,
            RedirectAttributes redirectAttributes
    ) {

        try {
            RepaymentSchedule repaymentSchedule = new RepaymentSchedule(
                    loanId,
                    installmentNumber,
                    dueDate,
                    principalAmount,
                    interestAmount,
                    BigDecimal.ZERO,
                    paymentStatus
            );

            repaymentSchedule.setId(id);
            repaymentScheduleService.updateRepaymentSchedule(repaymentSchedule);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Repayment schedule updated successfully."
            );

        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/";
    }

    @PostMapping("/repayment-schedules/delete")
    public String deleteRepaymentSchedule(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes
    ) {

        repaymentScheduleService.deleteRepaymentSchedule(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Repayment schedule deleted successfully."
        );

        return "redirect:/";
    }

    @PostMapping("/repayment-entries")
    public String addRepaymentEntry(
            @RequestParam Long repaymentScheduleId,
            @RequestParam LocalDate paymentDate,
            @RequestParam BigDecimal amountPaid,
            @RequestParam String paymentMode,
            @RequestParam(required = false) String remarks,
            RedirectAttributes redirectAttributes
    ) {

        try {
            RepaymentEntry repaymentEntry = new RepaymentEntry(
                    repaymentScheduleId,
                    paymentDate,
                    amountPaid,
                    paymentMode,
                    remarks
            );

            repaymentTransactionService.recordFullPayment(repaymentEntry);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Repayment entry saved and installment marked as paid."
            );

        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Could not save repayment entry. No changes were made."
            );
        }

        return "redirect:/";
    }

    @PostMapping("/repayment-entries/update")
    public String updateRepaymentEntry(
            @RequestParam Long id,
            @RequestParam Long repaymentScheduleId,
            @RequestParam LocalDate paymentDate,
            @RequestParam BigDecimal amountPaid,
            @RequestParam String paymentMode,
            @RequestParam(required = false) String remarks,
            RedirectAttributes redirectAttributes
    ) {

        try {
            RepaymentEntry repaymentEntry = new RepaymentEntry(
                    repaymentScheduleId,
                    paymentDate,
                    amountPaid,
                    paymentMode,
                    remarks
            );

            repaymentEntry.setId(id);
            repaymentEntryService.updateRepaymentEntry(repaymentEntry);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Repayment entry updated successfully."
            );

        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/";
    }

    @PostMapping("/repayment-entries/delete")
    public String deleteRepaymentEntry(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes
    ) {

        repaymentEntryService.deleteRepaymentEntry(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Repayment entry deleted successfully."
        );

        return "redirect:/";
    }
}