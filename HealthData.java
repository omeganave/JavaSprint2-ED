public class HealthData {
    private int id;
    private int userId;
    private double weight;
    private double height;
    private int steps;
    private int heartRate;
    private String date;

    // Constructor
    public HealthData(int id, int userId, double weight, double height, int steps, int heartRate, String date) {
        this.id = id;
        this.userId = userId;
        this.weight = weight; // In kg
        this.height = height; // In cm
        this.steps = steps;
        this.heartRate = heartRate;
        this.date = date; // Should this be set to the current date? Figure out later.
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public int getSteps() {
        return steps;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public String getDate() {
        return date;
    }

    public double calculateBMI() {
        double heightInMeters = this.height / 100;
        return (this.weight / (heightInMeters * heightInMeters));
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return this.date + ": " + this.weight + " kg, " + this.height + " cm, " + this.steps + " steps, "
                + this.heartRate + " bpm" + " (ID: " + this.id + ")";
    }

    // Other methods?
}
