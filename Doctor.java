
public class Doctor extends User {
    private String medicalLicenseNumber;
    private String specialization;

    public Doctor(int id, String firstName, String lastName, String email, String password, boolean isDoctor,
            String medicalLicenseNumber, String specialization) {
        super(id, firstName, lastName, email, password, isDoctor);
        this.medicalLicenseNumber = medicalLicenseNumber;
        this.specialization = specialization;
    }

    // Getters and setters for doctor-specific fields

    /**
     * Returns the medical license number.
     *
     * @return the medical license number
     */
    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }

    /**
     * Sets the medical license number.
     *
     * @param medicalLicenseNumber the medical license number to set
     */
    public void setMedicalLicenseNumber(String medicalLicenseNumber) {
        this.medicalLicenseNumber = medicalLicenseNumber;
    }

    /**
     * Retrieves the specialization of the object.
     *
     * @return the specialization of the object
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Sets the specialization of the object.
     *
     * @param specialization the specialization to be set
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * Returns a string representation of the object, including the superclass
     * string representation, the medical license number, and the specialization.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return super.toString() + " | License Number: " + this.medicalLicenseNumber + " | Specialization: "
                + this.specialization;
    }
}
