package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.FarmerService;

@Controller
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("farmers", farmerService.getAllFarmers());
        return "index";
    }

    @PostMapping("/farmers")
    public String addFarmer(
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String village,
            RedirectAttributes redirectAttributes) {

        try {
            farmerService.addFarmer(fullName, phone, village);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Farmer added successfully."
            );
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Could not add farmer. Phone number may already exist."
            );
        }

        return "redirect:/";
    }
}