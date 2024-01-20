// Just using the userdaoexample for this one, assuming that's fine.

import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDao {
    public boolean createUser(User user) {
        boolean bool = false;
        /* insert user into database */
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        // Prepare the SQL query
        String query = "INSERT INTO users (first_name, last_name, email, password, is_doctor) " +
                "VALUES (?, ?, ?, ?, ?)";

        // Database logic to insert data using PREPARED Statement
        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, hashedPassword);
            statement.setBoolean(5, user.isDoctor());

            int updatedRows = statement.executeUpdate();
            if (updatedRows != 0) {
                bool = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bool;

    }

    public boolean createUser(Doctor doctor) {
        boolean bool = false;

        String hashedPassword = BCrypt.hashpw(doctor.getPassword(), BCrypt.gensalt());

        String query = "INSERT INTO users (first_name, last_name, email, password, is_doctor, medical_license_number, specialization) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, doctor.getFirstName());
            statement.setString(2, doctor.getLastName());
            statement.setString(3, doctor.getEmail());
            statement.setString(4, hashedPassword);
            statement.setBoolean(5, doctor.isDoctor());
            statement.setString(6, doctor.getMedicalLicenseNumber());
            statement.setString(7, doctor.getSpecialization());

            int updatedRows = statement.executeUpdate();
            if (updatedRows != 0) {
                bool = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bool;
    }

    public User getUserById(int id) {
        int user_id = 0;
        String firstName = null;
        String lastName = null;
        String email = null;
        String password = null;
        boolean is_doctor = false;

        // Prepare the SQL query
        String query = "SELECT * FROM users WHERE id = ?";

        // Database logic to get data by ID Using Prepared Statement
        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user_id = rs.getInt("id");
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
                email = rs.getString("email");
                password = rs.getString("password");
                is_doctor = rs.getBoolean("is_doctor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new User(user_id, firstName, lastName, email, password, is_doctor);
    }

    public Doctor getDoctorById(int id) {
        int user_id = 0;
        String firstName = null;
        String lastName = null;
        String email = null;
        String password = null;
        boolean is_doctor = true;
        String medical_license_number = null;
        String specialization = null;

        String query = "SELECT * FROM users WHERE id = ?";

        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user_id = rs.getInt("id");
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
                email = rs.getString("email");
                password = rs.getString("password");
                is_doctor = rs.getBoolean("is_doctor");
                medical_license_number = rs.getString("medical_license_number");
                specialization = rs.getString("specialization");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Doctor(user_id, firstName, lastName, email, password, is_doctor, medical_license_number,
                specialization);
    }

    public User getUserByEmail(String email) {
        int id = 0;
        String firstName = null;
        String lastName = null;
        String user_email = null;
        String password = null;
        boolean is_doctor = false;

        // Prepare the SQL query
        String query = "SELECT * FROM users WHERE email = ?";

        // Database logic to get data by ID Using Prepared Statement
        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
                user_email = rs.getString("email");
                password = rs.getString("password");
                is_doctor = rs.getBoolean("is_doctor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new User(id, firstName, lastName, user_email, password, is_doctor);
    }

    public Doctor getDoctorByEmail(String email) {
        int id = 0;
        String firstName = null;
        String lastName = null;
        String user_email = null;
        String password = null;
        boolean is_doctor = true;
        String medical_license_number = null;
        String specialization = null;

        String query = "SELECT * FROM users WHERE email = ?";

        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
                user_email = rs.getString("email");
                password = rs.getString("password");
                is_doctor = rs.getBoolean("is_doctor");
                medical_license_number = rs.getString("medical_license_number");
                specialization = rs.getString("specialization");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Doctor(id, firstName, lastName, user_email, password, is_doctor, medical_license_number,
                specialization);
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        // Prepare the SQL query
        String query = "SELECT * FROM users";

        // Database logic to get data Using Prepared Statement
        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean is_doctor = rs.getBoolean("is_doctor");
                users.add(new User(id, firstName, lastName, email, password, is_doctor));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Realized last minute that I forgot to implement these two. They do work, but
    // just haven't been added to the app.

    // public boolean updateUser(User user) {
    // boolean bool = false;
    // // Prepare the SQL query
    // String query = "UPDATE users " +
    // "SET first_name = ?, last_name = ?, email = ?, password = ?, is_doctor = ? "
    // +
    // "WHERE id = ?";
    // String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

    // // Database logic to get update user Using Prepared Statement
    // try {
    // Connection con = DatabaseConnection.getCon();
    // PreparedStatement statement = con.prepareStatement(query);
    // statement.setString(1, user.getFirstName());
    // statement.setString(2, user.getLastName());
    // statement.setString(3, user.getEmail());
    // statement.setString(4, hashedPassword);
    // statement.setBoolean(5, user.isDoctor());
    // statement.setInt(6, user.getId());
    // int updatedRows = statement.executeUpdate();
    // if (updatedRows != 0) {
    // bool = true;
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // return bool;
    // }

    // public boolean deleteUser(int id) {
    // boolean bool = false;
    // // Prepare the SQL query
    // String query = "DELETE FROM users WHERE id = ?";

    // // Database logic to delete user
    // try {
    // Connection con = DatabaseConnection.getCon();
    // PreparedStatement statement = con.prepareStatement(query);
    // statement.setInt(1, id);
    // int rowsUpdated = statement.executeUpdate();
    // if (rowsUpdated != 0) {
    // bool = true;
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }

    // if (removePatientFromDoctors(id)) {
    // System.out.println("Patient removed from doctors");
    // } else {
    // System.out.println("Patient not removed from doctors");
    // }

    // return bool;
    // }

    // This would have worked with deleteUser. Would have removed doctor_patient
    // entries associated with this user.

    // private boolean removePatientFromDoctors(int patientId) {
    // boolean bool = false;

    // String query = "DELETE FROM doctor_patient WHERE patient_id = ?";

    // try {
    // Connection con = DatabaseConnection.getCon();
    // PreparedStatement statement = con.prepareStatement(query);
    // statement.setInt(1, patientId);
    // int updatedRows = statement.executeUpdate();
    // if (updatedRows != 0) {
    // bool = true;
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }

    // return bool;
    // }

    public boolean verifyPassword(String email, String password) {
        boolean bool = false;
        String query = "SELECT password FROM users WHERE email = ?";
        // Implement logic to retrieve password using the Bcrypt
        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            String hashedPassword = null;
            while (rs.next()) {
                hashedPassword = rs.getString("password");
            }
            if (BCrypt.checkpw(password, hashedPassword)) {
                bool = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }
}
