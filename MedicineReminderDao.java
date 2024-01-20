
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * The MedicineReminderManager class should have methods to add reminders, get
 * reminders
 * 1. for a specific user, and
 * 2. get reminders that are DUE for a specific user.
 *
 * You'll need to integrate this class with your application and database logic
 * to
 * 1. store,
 * 2. update, and
 * 3. delete reminders as needed.
 */

public class MedicineReminderDao {
    // private List<MedicineReminder> reminders;

    public boolean addReminder(MedicineReminder reminder) {
        boolean bool = false;

        String query = "INSERT INTO medicine_reminders (user_id, medicine_name, dosage, schedule, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, reminder.getUserId());
            statement.setString(2, reminder.getMedicineName());
            statement.setString(3, reminder.getDosage());
            statement.setString(4, reminder.getSchedule());

            try {
                // Have to do a lot of work to properly parse the dates to work with SQL. Have
                // to specify which type of Date I'm using.
                java.util.Date start = formatter.parse(reminder.getStartDate());
                java.util.Date end = formatter.parse(reminder.getEndDate());

                java.sql.Date sqlStart = new java.sql.Date(start.getTime());
                java.sql.Date sqlEnd = new java.sql.Date(end.getTime());

                statement.setDate(5, sqlStart);
                statement.setDate(6, sqlEnd);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int updatedRows = statement.executeUpdate();
            if (updatedRows != 0) {
                bool = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bool;
    }

    public List<MedicineReminder> getRemindersForUser(int userId) {
        List<MedicineReminder> userReminders = new ArrayList<>();

        String query = "SELECT * FROM medicine_reminders WHERE user_id = ?";

        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String medicine_name = rs.getString("medicine_name");
                String dosage = rs.getString("dosage");
                String schedule = rs.getString("schedule");
                Date start_date = rs.getDate("start_date");
                Date end_date = rs.getDate("end_date");

                String startString = start_date.toString();
                String endString = end_date.toString();

                userReminders.add(
                        new MedicineReminder(id, user_id, medicine_name, dosage, schedule, startString, endString));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userReminders;
    }

    public List<MedicineReminder> getDueReminders(int userId) {
        List<MedicineReminder> dueReminders = new ArrayList<>();
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String query = "SELECT * FROM medicine_reminders WHERE user_id = ?";

        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String medicine_name = rs.getString("medicine_name");
                String dosage = rs.getString("dosage");
                String schedule = rs.getString("schedule");
                Date start_date = rs.getDate("start_date");
                Date end_date = rs.getDate("end_date");

                String startString = start_date.toString();
                String endString = end_date.toString();

                LocalDate startLocalDate = LocalDate.parse(startString, formatter);
                LocalDate endLocalDate = LocalDate.parse(endString, formatter);

                if (!now.isBefore(startLocalDate) && !now.isAfter(endLocalDate)) {
                    // System.out.println("Adding reminder to list");
                    dueReminders.add(
                            new MedicineReminder(id, user_id, medicine_name, dosage, schedule, startString, endString));
                } else {
                    continue;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dueReminders;
    }
}
