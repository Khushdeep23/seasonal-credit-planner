package controller;

import model.Loan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.LoanService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/loans")
    public String addLoan(
            @RequestParam Long farmerId,
            @RequestParam String loanType,
            @RequestParam String purpose,
            @RequestParam BigDecimal loanAmount,
            @RequestParam BigDecimal annualInterestRate,
            @RequestParam Integer tenureMonths,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            RedirectAttributes redirectAttributes) {

        try {
            Loan loan = new Loan(
                    farmerId,
                    loanType,
                    purpose,
                    loanAmount,
                    annualInterestRate,
                    tenureMonths,
                    startDate,
                    endDate,
                    "ACTIVE"
            );

            loanService.addLoan(loan);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Loan added successfully."
            );

        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/";
    }

    @PostMapping("/loans/update")
    public String updateLoan(
            @RequestParam Long id,
            @RequestParam Long farmerId,
            @RequestParam String loanType,
            @RequestParam String purpose,
            @RequestParam BigDecimal loanAmount,
            @RequestParam BigDecimal annualInterestRate,
            @RequestParam Integer tenureMonths,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "ACTIVE") String status,
            RedirectAttributes redirectAttributes) {

        try {
            Loan loan = new Loan(
                    farmerId,
                    loanType,
                    purpose,
                    loanAmount,
                    annualInterestRate,
                    tenureMonths,
                    startDate,
                    endDate,
                    status
            );

            loan.setId(id);
            loanService.updateLoan(loan);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Loan updated successfully."
            );

        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/";
    }

    @PostMapping("/loans/delete")
    public String deleteLoan(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes) {

        loanService.deleteLoan(id);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Loan deleted successfully."
        );

        return "redirect:/";
    }
}