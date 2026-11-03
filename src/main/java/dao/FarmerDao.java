package dao;

import database.ConnectionFactory;
import model.Farmer;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FarmerDao {

    private final ConnectionFactory connectionFactory;

    public FarmerDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Farmer save(Farmer farmer) throws SQLException {
        String sql = """
                INSERT INTO farmers (full_name, phone, village)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, farmer.getFullName());
            statement.setString(2, farmer.getPhone());
            statement.setString(3, farmer.getVillage());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Farmer could not be saved.");
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    farmer.setId(keys.getLong(1));
                }
            }
        }

        return farmer;
    }

    public List<Farmer> findAll() throws SQLException {
        String sql = """
                SELECT id, full_name, phone, village
                FROM farmers
                ORDER BY id DESC
                """;

        List<Farmer> farmers = new ArrayList<>();

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Farmer farmer = new Farmer();
                farmer.setId(resultSet.getLong("id"));
                farmer.setFullName(resultSet.getString("full_name"));
                farmer.setPhone(resultSet.getString("phone"));
                farmer.setVillage(resultSet.getString("village"));
                farmers.add(farmer);
            }
        }

        return farmers;
    }
}