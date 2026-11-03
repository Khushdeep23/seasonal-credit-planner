package service;

import dao.CropSeasonDao;
import model.CropSeason;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CropSeasonService {

    private final CropSeasonDao cropSeasonDao;

    public CropSeasonService(CropSeasonDao cropSeasonDao) {
        this.cropSeasonDao = cropSeasonDao;
    }

    public void addCropSeason(CropSeason cropSeason) {
        validateCropSeason(cropSeason);
        cropSeasonDao.save(cropSeason);
    }

    public List<CropSeason> getAllCropSeasons() {
        return cropSeasonDao.findAll();
    }

    private void validateCropSeason(CropSeason cropSeason) {
        if (cropSeason.getFarmerId() == null || cropSeason.getFarmerId() <= 0) {
            throw new IllegalArgumentException("Please select a farmer.");
        }

        if (cropSeason.getSeasonName() == null || cropSeason.getSeasonName().isBlank()) {
            throw new IllegalArgumentException("Season name is required.");
        }

        if (cropSeason.getCropName() == null || cropSeason.getCropName().isBlank()) {
            throw new IllegalArgumentException("Crop name is required.");
        }

        if (cropSeason.getLandAreaAcres() == null
                || cropSeason.getLandAreaAcres().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Land area must be greater than zero.");
        }

        if (cropSeason.getExpectedYieldQuintals() == null
                || cropSeason.getExpectedYieldQuintals().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Expected yield must be greater than zero.");
        }

        if (cropSeason.getExpectedPricePerQuintal() == null
                || cropSeason.getExpectedPricePerQuintal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Expected price must be greater than zero.");
        }
    }
}