package dao;

import database.ConnectionFactory;
import model.CropSeason;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CropSeasonDao {

    private final ConnectionFactory connectionFactory;

    public CropSeasonDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void save(CropSeason cropSeason) {
        String sql = """
                INSERT INTO crop_seasons
                (farmer_id, season_name, crop_name, land_area_acres,
                 expected_yield_quintals, expected_price_per_quintal)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, cropSeason.getFarmerId());
            statement.setString(2, cropSeason.getSeasonName());
            statement.setString(3, cropSeason.getCropName());
            statement.setBigDecimal(4, cropSeason.getLandAreaAcres());
            statement.setBigDecimal(5, cropSeason.getExpectedYieldQuintals());
            statement.setBigDecimal(6, cropSeason.getExpectedPricePerQuintal());

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException("Could not save crop season.", exception);
        }
    }

    public List<CropSeason> findAll() {
        String sql = """
        SELECT cs.id, cs.farmer_id, f.full_name AS farmer_name,
               cs.season_name, cs.crop_name, cs.land_area_acres,
               cs.expected_yield_quintals, cs.expected_price_per_quintal
        FROM crop_seasons cs
        JOIN farmers f ON cs.farmer_id = f.id
        ORDER BY cs.id DESC
        """;

        List<CropSeason> cropSeasons = new ArrayList<>();

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                CropSeason cropSeason = new CropSeason();

                cropSeason.setId(resultSet.getLong("id"));
                cropSeason.setFarmerId(resultSet.getLong("farmer_id"));
                cropSeason.setFarmerName(resultSet.getString("farmer_name"));
                cropSeason.setSeasonName(resultSet.getString("season_name"));
                cropSeason.setCropName(resultSet.getString("crop_name"));
                cropSeason.setLandAreaAcres(resultSet.getBigDecimal("land_area_acres"));
                cropSeason.setExpectedYieldQuintals(
                        resultSet.getBigDecimal("expected_yield_quintals")
                );
                cropSeason.setExpectedPricePerQuintal(
                        resultSet.getBigDecimal("expected_price_per_quintal")
                );

                cropSeasons.add(cropSeason);
            }

            return cropSeasons;

        } catch (SQLException exception) {
            throw new RuntimeException("Could not load crop seasons.", exception);
        }
    }
}