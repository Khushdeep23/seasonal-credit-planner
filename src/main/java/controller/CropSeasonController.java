package controller;

import model.CropSeason;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.CropSeasonService;

import java.math.BigDecimal;

@Controller
public class CropSeasonController {

    private final CropSeasonService cropSeasonService;

    public CropSeasonController(CropSeasonService cropSeasonService) {
        this.cropSeasonService = cropSeasonService;
    }

    @PostMapping("/crop-seasons")
    public String addCropSeason(
            @RequestParam Long farmerId,
            @RequestParam String seasonName,
            @RequestParam String cropName,
            @RequestParam BigDecimal landAreaAcres,
            @RequestParam BigDecimal expectedYieldQuintals,
            @RequestParam BigDecimal expectedPricePerQuintal,
            @RequestParam BigDecimal estimatedCost,
            RedirectAttributes redirectAttributes) {

        try {
            CropSeason cropSeason = new CropSeason(
                    farmerId,
                    seasonName,
                    cropName,
                    landAreaAcres,
                    expectedYieldQuintals,
                    expectedPricePerQuintal
            );
            cropSeason.setEstimatedCost(estimatedCost);
            cropSeasonService.addCropSeason(cropSeason);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Crop season added successfully."
            );

        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/";
    }
}