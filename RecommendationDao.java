
// import java.sql.Connection;
// import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.time.LocalDate;

public class RecommendationDao {

    RecommendationSystem recommendationSystem = new RecommendationSystem();

    public boolean insertRecommendations(HealthData healthData) {
        boolean bool = false;

        String query = "INSERT INTO recommendations (user_id, recommendation_text, date) VALUES (?, ?, CURRENT_DATE)";

        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);

            for (String recommendation : recommendationSystem.generateRecommendations(healthData)) {
                statement.setInt(1, healthData.getUserId());
                statement.setString(2, recommendation);
                statement.addBatch();
            }

            int[] updatedRows = statement.executeBatch();

            for (int rows : updatedRows) {
                if (rows != 0) {
                    bool = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bool;
    }

    public List<String> getRecommendationsByUserId(int userId) {
        List<String> recommendations = new ArrayList<>();

        String query = "SELECT * FROM recommendations WHERE user_id = ? ORDER BY date DESC";

        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Date date = rs.getDate("date");

                // Using localDate to easily subtract and compare dates.
                LocalDate now = LocalDate.now();
                LocalDate sevenDaysAgo = now.minusDays(7);

                LocalDate dateAsLocalDate = date.toLocalDate();

                // If the entry's date is within the last 7 days, it's added to the list.
                if (!dateAsLocalDate.isAfter(now) && !dateAsLocalDate.isBefore(sevenDaysAgo)) {
                    recommendations.add(rs.getString("recommendation_text") + " - " + date.toString());
                } else {
                    // Once there is a date that is not within the last 7 days, the loop is broken.
                    // This is because the user may have a lot of recommendations in the database,
                    // and we don't want to check every one of them if we don't need to.
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recommendations;
    }
}
