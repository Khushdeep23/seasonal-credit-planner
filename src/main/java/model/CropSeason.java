package model;

import java.math.BigDecimal;

public class CropSeason {

    private Long id;
    private Long farmerId;
    private String farmerName;
    private String seasonName;
    private String cropName;
    private BigDecimal landAreaAcres;
    private BigDecimal expectedYieldQuintals;
    private BigDecimal expectedPricePerQuintal;
    private BigDecimal estimatedCost;

    public CropSeason() {
    }

    public CropSeason(
            Long farmerId,
            String seasonName,
            String cropName,
            BigDecimal landAreaAcres,
            BigDecimal expectedYieldQuintals,
            BigDecimal expectedPricePerQuintal) {

        this.farmerId = farmerId;
        this.seasonName = seasonName;
        this.cropName = cropName;
        this.landAreaAcres = landAreaAcres;
        this.expectedYieldQuintals = expectedYieldQuintals;
        this.expectedPricePerQuintal = expectedPricePerQuintal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public BigDecimal getLandAreaAcres() {
        return landAreaAcres;
    }

    public void setLandAreaAcres(BigDecimal landAreaAcres) {
        this.landAreaAcres = landAreaAcres;
    }

    public BigDecimal getExpectedYieldQuintals() {
        return expectedYieldQuintals;
    }

    public void setExpectedYieldQuintals(BigDecimal expectedYieldQuintals) {
        this.expectedYieldQuintals = expectedYieldQuintals;
    }

    public BigDecimal getExpectedPricePerQuintal() {
        return expectedPricePerQuintal;
    }

    public void setExpectedPricePerQuintal(BigDecimal expectedPricePerQuintal) {
        this.expectedPricePerQuintal = expectedPricePerQuintal;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
}