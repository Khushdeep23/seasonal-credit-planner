package service;

import model.CropSeason;
import model.Loan;
import model.RepaymentFeasibility;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RepaymentFeasibilityService {

    private final CropSeasonService cropSeasonService;
    private final LoanService loanService;
    private final LoanCalculationService loanCalculationService;

    public RepaymentFeasibilityService(
            CropSeasonService cropSeasonService,
            LoanService loanService,
            LoanCalculationService loanCalculationService
    ) {
        this.cropSeasonService = cropSeasonService;
        this.loanService = loanService;
        this.loanCalculationService = loanCalculationService;
    }

    public RepaymentFeasibility calculateForFarmer(Long farmerId) {

        if (farmerId == null || farmerId <= 0) {
            throw new IllegalArgumentException("Valid farmer ID is required.");
        }

        List<CropSeason> cropSeasons =
                cropSeasonService.getAllCropSeasons();

        List<Loan> loans =
                loanService.getAllLoans();

        BigDecimal totalNetCropIncome = BigDecimal.ZERO;
        BigDecimal totalLoanRepayment = BigDecimal.ZERO;

        for (CropSeason cropSeason : cropSeasons) {
            if (farmerId.equals(cropSeason.getFarmerId())) {
                totalNetCropIncome = totalNetCropIncome.add(
                        cropSeason.getNetCropIncome()
                );
            }
        }

        for (Loan loan : loans) {
            if (farmerId.equals(loan.getFarmerId())) {
                totalLoanRepayment = totalLoanRepayment.add(
                        loanCalculationService.calculateTotalRepayment(loan)
                );
            }
        }

        totalNetCropIncome = totalNetCropIncome.setScale(
                2, RoundingMode.HALF_UP
        );

        totalLoanRepayment = totalLoanRepayment.setScale(
                2, RoundingMode.HALF_UP
        );

        return new RepaymentFeasibility(
                totalNetCropIncome,
                totalLoanRepayment
        );
    }
}