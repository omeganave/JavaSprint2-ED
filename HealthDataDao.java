import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class HealthDataDao {

  public boolean createHealthData(HealthData healthData) {
    boolean bool = false;

    String query = "INSERT INTO health_data (user_id, weight, height, steps, heart_rate, date) "
        + "VALUES (?, ?, ?, ?, ?, CURRENT_DATE)";

    try {
      Connection con = DatabaseConnection.getCon();
      PreparedStatement statement = con.prepareStatement(query);
      statement.setInt(1, healthData.getUserId());
      statement.setDouble(2, healthData.getWeight());
      statement.setDouble(3, healthData.getHeight());
      statement.setInt(4, healthData.getSteps());
      statement.setInt(5, healthData.getHeartRate());
      int updatedRows = statement.executeUpdate();
      if (updatedRows != 0) {
        bool = true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return bool;
  }

  public HealthData getHealthDataById(int id) {
    int user_id = 0;
    double weight = 0;
    double height = 0;
    int steps = 0;
    int heart_rate = 0;
    String date = null;

    String query = "SELECT * FROM health_data WHERE id = ?";

    try {
      Connection con = DatabaseConnection.getCon();
      PreparedStatement statement = con.prepareStatement(query);
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        user_id = rs.getInt("user_id");
        weight = rs.getDouble("weight");
        height = rs.getDouble("height");
        steps = rs.getInt("steps");
        heart_rate = rs.getInt("heart_rate");
        date = rs.getString("date");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return new HealthData(id, user_id, weight, height, steps, heart_rate, date);
  }

  public List<HealthData> getHealthDataByUserId(int userId) {
    List<HealthData> healthDatas = new ArrayList<>();

    int id = 0;
    int user_id = userId;
    double weight = 0;
    double height = 0;
    int steps = 0;
    int heart_rate = 0;
    String date = null;

    String query = "SELECT * FROM health_data WHERE user_id = ?";

    try {
      Connection con = DatabaseConnection.getCon();
      PreparedStatement statement = con.prepareStatement(query);
      statement.setInt(1, user_id);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        id = rs.getInt("id");
        user_id = rs.getInt("user_id");
        weight = rs.getDouble("weight");
        height = rs.getDouble("height");
        steps = rs.getInt("steps");
        heart_rate = rs.getInt("heart_rate");
        date = rs.getString("date");

        HealthData healthData = new HealthData(id, user_id, weight, height, steps, heart_rate, date);
        healthDatas.add(healthData);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return healthDatas;
  }

  public boolean updateHealthData(HealthData healthData) {
    boolean bool = false;

    String query = "UPDATE health_data SET weight = ?, height = ?, steps = ?, heart_rate = ? WHERE id = ?";

    try {
      Connection con = DatabaseConnection.getCon();
      PreparedStatement statement = con.prepareStatement(query);
      statement.setDouble(1, healthData.getWeight());
      statement.setDouble(2, healthData.getHeight());
      statement.setInt(3, healthData.getSteps());
      statement.setInt(4, healthData.getHeartRate());
      statement.setInt(5, healthData.getId());
      int updatedRows = statement.executeUpdate();
      if (updatedRows != 0) {
        bool = true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return bool;
  }

  public boolean deleteHealthData(int id) {
    boolean bool = false;

    String query = "DELETE FROM health_data WHERE id = ?";

    try {
      Connection con = DatabaseConnection.getCon();
      PreparedStatement statement = con.prepareStatement(query);
      statement.setInt(1, id);
      int updatedRows = statement.executeUpdate();
      if (updatedRows != 0) {
        bool = true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return bool;
  }
}
