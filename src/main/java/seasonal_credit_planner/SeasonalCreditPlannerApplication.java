package seasonal_credit_planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"seasonal_credit_planner",
		"controller",
		"service",
		"dao",
		"database",
		"config"
})
public class SeasonalCreditPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeasonalCreditPlannerApplication.class, args);
	}

}

