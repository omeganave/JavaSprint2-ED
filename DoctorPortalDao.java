
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class DoctorPortalDao {
    private UserDao userDao;
    private HealthDataDao healthDataDao;

    // Complete all these methods and add more as needed

    public DoctorPortalDao() {
        userDao = new UserDao();
        healthDataDao = new HealthDataDao();
    }

    /**
     * Adds a doctor to the system.
     *
     * @param doctor the doctor to be added
     * @return true if the doctor was added successfully, false otherwise
     */
    public boolean addDoctor(Doctor doctor) {
        return userDao.createUser(doctor);
    }

    /**
     * Associates a patient with a doctor by inserting a new record into the
     * doctor_patient table.
     *
     * @param patientId the ID of the patient to be associated with the doctor
     * @param doctorId  the ID of the doctor to be associated with the patient
     * @return true if the patient was successfully associated with the doctor,
     *         false otherwise
     */
    public boolean associatePatientWithDoctor(int patientId, int doctorId) {
        boolean bool = false;

        String query = "INSERT INTO doctor_patient (doctor_id, patient_id) VALUES (?, ?)";

        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, doctorId);
            statement.setInt(2, patientId);
            int updatedRows = statement.executeUpdate();
            if (updatedRows != 0) {
                bool = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * Removes a patient from a doctor's list.
     *
     * @param patientId the ID of the patient
     * @param doctorId  the ID of the doctor
     * @return true if the patient was successfully removed, false otherwise
     */
    public boolean removePatientFromDoctor(int patientId, int doctorId) {
        boolean bool = false;

        String query = "DELETE FROM doctor_patient WHERE doctor_id = ? AND patient_id = ?";

        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, doctorId);
            statement.setInt(2, patientId);
            int updatedRows = statement.executeUpdate();
            if (updatedRows != 0) {
                bool = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * Retrieves a doctor object by their unique ID.
     *
     * @param doctorId the ID of the doctor to retrieve
     * @return the doctor object corresponding to the ID, or null if no doctor is
     *         found
     */
    public Doctor getDoctorById(int doctorId) {
        return userDao.getDoctorById(doctorId);
    }

    /**
     * Retrieves a doctor from the database based on their email.
     *
     * @param email the email of the doctor
     * @return the Doctor object corresponding to the email, or null if not found
     */
    public Doctor getDoctorByEmail(String email) {
        return userDao.getDoctorByEmail(email);
    }

    /**
     * Retrieves a list of patients associated with a specific doctor ID.
     *
     * @param doctorId the ID of the doctor
     * @return a list of User objects representing the patients
     */
    public List<User> getPatientsByDoctorId(int doctorId) {
        List<Integer> patientIds = getPatientIdsByDoctorId(doctorId);
        List<User> patients = new ArrayList<>();

        for (int patientId : patientIds) {
            try {
                patients.add(userDao.getUserById(patientId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return patients;
    }

    /**
     * Retrieves a list of all patients.
     *
     * @return a List of String containing the information of all patients
     */
    public List<String> getAllPatients() {
        List<String> patientsInfo = new ArrayList<>();
        for (User user : userDao.getAllUsers()) {
            if (!user.isDoctor()) {
                String shortPatientInfo = user.getId() + ": " + user.getFirstName() + " " + user.getLastName();
                patientsInfo.add(shortPatientInfo);
            }
        }
        return patientsInfo;
    }

    /**
     * Retrieves a list of patient IDs associated with a given doctor ID.
     *
     * @param doctorId the ID of the doctor
     * @return a list of patient IDs
     */
    private List<Integer> getPatientIdsByDoctorId(int doctorId) {
        List<Integer> patientIds = new ArrayList<>();

        String query = "SELECT patient_id FROM doctor_patient WHERE doctor_id = ?";

        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, doctorId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int patientId = rs.getInt("patient_id");
                patientIds.add(patientId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patientIds;
    }

    /**
     * Retrieves the health data for a specific patient by their patient ID.
     *
     * @param patientId the ID of the patient
     * @param doctorId  the ID of the doctor
     * @return a list of HealthData objects representing the patient's health data
     */
    public List<HealthData> getHealthDataByPatientId(int patientId, int doctorId) {
        List<Integer> patientIds = getPatientIdsByDoctorId(doctorId);
        if (patientIds.contains(patientId)) {
            return healthDataDao.getHealthDataByUserId(patientId);
        } else {
            System.out.println("You are not authorized to access this patient's health data.");
            return new ArrayList<>();
        }
    }

    // Add more methods for other doctor-specific tasks
}
