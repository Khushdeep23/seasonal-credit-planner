
package controller;

import model.Loan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.CropSeasonService;
import service.FarmerService;
import service.LoanCalculationService;
import service.LoanService;
import service.RepaymentEntryService;
import service.RepaymentScheduleService;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FarmerController {

    private final FarmerService farmerService;
    private final CropSeasonService cropSeasonService;
    private final LoanService loanService;
    private final RepaymentScheduleService repaymentScheduleService;
    private final RepaymentEntryService repaymentEntryService;
    private final LoanCalculationService loanCalculationService;

    public FarmerController(
            FarmerService farmerService,
            CropSeasonService cropSeasonService,
            LoanService loanService,
            RepaymentScheduleService repaymentScheduleService,
            RepaymentEntryService repaymentEntryService,
            LoanCalculationService loanCalculationService
    ) {

        this.farmerService = farmerService;
        this.cropSeasonService = cropSeasonService;
        this.loanService = loanService;
        this.repaymentScheduleService = repaymentScheduleService;
        this.repaymentEntryService = repaymentEntryService;
        this.loanCalculationService = loanCalculationService;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("farmers", farmerService.getAllFarmers());

        model.addAttribute(
                "cropSeasons",
                cropSeasonService.getAllCropSeasons()
        );

        List<Loan> loans = loanService.getAllLoans();

        model.addAttribute("loans", loans);

        Map<Long, BigDecimal> loanInterestAmounts = new LinkedHashMap<>();
        Map<Long, BigDecimal> loanTotalRepayments = new LinkedHashMap<>();

        for (Loan loan : loans) {

            BigDecimal interest =
                    loanCalculationService.calculateInterest(loan);

            BigDecimal totalRepayment =
                    loanCalculationService.calculateTotalRepayment(loan);

            loanInterestAmounts.put(loan.getId(), interest);

            loanTotalRepayments.put(loan.getId(), totalRepayment);
        }

        model.addAttribute("loanInterestAmounts", loanInterestAmounts);

        model.addAttribute(
                "loanTotalRepayments",
                loanTotalRepayments
        );

        model.addAttribute(
                "repaymentSchedules",
                repaymentScheduleService.getAllRepaymentSchedules()
        );

        model.addAttribute(
                "repaymentEntries",
                repaymentEntryService.getAllRepaymentEntries()
        );

        return "index";
    }

    @PostMapping("/farmers")
    public String addFarmer(
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String village,
            RedirectAttributes redirectAttributes
    ) {

        try {

            farmerService.addFarmer(fullName, phone, village);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Farmer added successfully."
            );

        } catch (RuntimeException exception) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Could not add farmer. Phone number may already exist."
            );
        }

        return "redirect:/";
    }
}
