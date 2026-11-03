package service;

import dao.FarmerDao;
import model.Farmer;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class FarmerService {

    private final FarmerDao farmerDao;

    public FarmerService(FarmerDao farmerDao) {
        this.farmerDao = farmerDao;
    }

    public Farmer addFarmer(String fullName, String phone, String village) {
        if (fullName == null || fullName.isBlank()
                || phone == null || phone.isBlank()
                || village == null || village.isBlank()) {
            throw new IllegalArgumentException("All farmer details are required.");
        }

        Farmer farmer = new Farmer(
                fullName.trim(),
                phone.trim(),
                village.trim()
        );

        try {
            return farmerDao.save(farmer);
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to save farmer.", exception);
        }
    }

    public List<Farmer> getAllFarmers() {
        try {
            return farmerDao.findAll();
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to load farmers.", exception);
        }
    }
}