
import java.util.ArrayList;
import java.util.List;

/**
 * In this basic version of the
 * RecommendationSystem class, complete the generateRecommendations to take a
 * HealthData object as input and generates recommendations based on the user's
 * heart rate and step count.
 * You can also expand this class to include more health data analysis and
 * generate more specific
 * recommendations based on the user's unique health profile
 * NOTE:
 * To integrate this class into your application, you'll need to pass the
 * HealthData object to the generateRecommendations method
 * and store the generated recommendations in the recommendations table in the
 * database.
 */

public class RecommendationSystem {
        private static final int MIN_HEART_RATE = 60;
        private static final int MAX_HEART_RATE = 100;
        private static final int MIN_STEPS = 10000;
        private static final double MIN_HEALTHY_BMI = 18.5;
        private static final double MAX_HEALTHY_BMI = 24.9;
        private static final double OBESITY_THRESHOLD = 30.0;

        public List<String> generateRecommendations(HealthData healthData) {
                List<String> recommendations = new ArrayList<>();

                // // Analyze heart rate
                int heartRate = healthData.getHeartRate();
                if (heartRate < MIN_HEART_RATE) {
                        recommendations.add("Your heart rate is lower than the recommended range. " +
                                        "Consider increasing your physical activity to improve your cardiovascular health.");
                } else if (heartRate > MAX_HEART_RATE) {
                        recommendations.add("Your heart rate is higher than the recommended range. " +
                                        "Consider taking a rest and decreasing your physical activity.");
                }
                //
                //
                // // Analyze steps
                int steps = healthData.getSteps();
                if (steps < MIN_STEPS) {
                        recommendations.add("You're not reaching the recommended daily step count. " +
                                        "Try to incorporate more walking or other physical activities into your daily routine.");
                }

                double bmi = healthData.calculateBMI();
                if (bmi < MIN_HEALTHY_BMI) {
                        recommendations.add("Your BMI indicates that you may be underweight. "
                                        + "Consider eating more nutrient-dense foods to support healthy weight gain.");
                } else if (bmi > MAX_HEALTHY_BMI && bmi < OBESITY_THRESHOLD) {
                        recommendations.add("Your BMI indicates that you may be overweight. "
                                        + "Consider exercising more regularly and reducing intake of processed and sugary foods.");
                } else if (bmi > OBESITY_THRESHOLD) {
                        recommendations.add("Your BMI indicates that you may be obese. "
                                        + "Consider reducing your calorie intake and eating more whole, unprocessed foods."
                                        + "You may also consider seeking professional advice from a registered dietitian.");
                }

                return recommendations;
        }
}