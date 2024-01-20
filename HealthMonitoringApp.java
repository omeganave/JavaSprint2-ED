// Terminal clearing doesn't work 100% correctly. But it's good enough for what I want it to do.

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class HealthMonitoringApp {

    private static UserDao userDao = new UserDao();
    private static DoctorPortalDao doctorPortalDao = new DoctorPortalDao();
    private static HealthDataDao healthDataDao = new HealthDataDao();
    private static RecommendationDao recommendationDao = new RecommendationDao();
    private static RecommendationSystem recommendationSystem = new RecommendationSystem();
    private static MedicineReminderDao medicineReminderDao = new MedicineReminderDao();
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Test the following functionalities within the Main Application
     * 1. Register a new user
     * 2. Log in the user
     * 3. Add health data
     * 4. Generate recommendations
     * 5. Add a medicine reminder
     * 6. Get reminders for a specific user
     * 7. Get due reminders for a specific user
     * 8. test doctor portal
     */

    // Main Menu.
    public static void main(String[] args) {
        // Setting up the message for the top of the screen. Initially set to "Welcome
        // to the Health Monitoring System", underlined.
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sWelcome to the Health Monitoring System%s";

        while (true) {
            System.out.println("\033[H\033[2J"); // Clears the screen. This is used many times throughout the program.
            System.out.println(String.format(message, underline1, underline2));
            System.out.println();
            System.out.println("1. User Menu");
            System.out.println("2. Doctor Menu");
            System.out.println("3. Run Some Tests");
            System.out.println("4. Exit Application");
            System.out.println();
            System.out.println("Enter your selection (1-4): ");
            try {
                int selection = scanner.nextInt();

                switch (selection) {
                    case 1:
                        // Redirects to the user login/register menu. Resets the message when returning
                        // to the main menu.
                        userMenu();
                        message = "%sWelcome to the Health Monitoring System%s";
                        break;
                    case 2:
                        // Redirects to the doctor login/register menu.
                        doctorMenu();
                        message = "%sWelcome to the Health Monitoring System%s";
                        break;
                    case 3:
                        // Runs some tests.

                        testDoctorPortal();

                        testLoginUser();

                        System.out.println("Press enter to continue...");
                        scanner.nextLine();
                        scanner.nextLine();
                        message = "%sWelcome to the Health Monitoring System%s";
                        break;
                    case 4:
                        // Exits the application.
                        System.out.println();
                        System.out.println("Goodbye!");
                        scanner.close();
                        System.exit(0);
                        return;
                    default:
                        message = "%sInvalid selection.%s Please enter 1, 2, 3 or 4.";
                }
            } catch (InputMismatchException e) {
                // Catches any input that is not an integer.
                message = "%sInvalid selection.%s Please enter 1, 2, 3 or 4.";
                scanner.nextLine();
            }
        }
    }

    // Menu for users to register/login.
    public static void userMenu() {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sUser Menu%s";

        userMenu: while (true) { // Labeling the while loop to allow the switch statement to break out of it in
                                 // one step.
            System.out.println("\033[H\033[2J");
            System.out.println(String.format(message, underline1, underline2));
            System.out.println();
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Return to Main Menu");
            System.out.println("4. Exit Application");
            System.out.println();
            System.out.println("Enter your selection (1-4): ");

            try {
                int selection = scanner.nextInt();

                switch (selection) {
                    case 1:
                        // Runs the login method.
                        login(false);
                        message = "%sUser Menu%s";
                        break;
                    case 2:
                        // Runs the register method.
                        register(false);
                        message = "%sUser Menu%s";
                        break;
                    case 3:
                        // Breaks out of the while loop, returning to the main menu.
                        break userMenu;
                    case 4:
                        // Exits the application.
                        System.out.println("Goodbye!");
                        scanner.close();
                        System.exit(0);
                        return;
                    default:
                        message = "%sInvalid selection.%s Please enter 1, 2, 3 or 4.";
                }
            } catch (InputMismatchException e) {
                // Catches any input that is not an integer.
                message = "%sInvalid selection.%s Please enter 1, 2, 3 or 4.";
                scanner.nextLine();
            }

        }
    }

    // Log in an existing user.
    public static void login(boolean isDoctor) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sLogin%s";
        while (true) {
            String messageDisplay = String.format(message, underline1, underline2);
            System.out.println("\033[H\033[2J");
            System.out.println(messageDisplay);
            System.out.println("(Enter 'q' at any time to go back)");
            System.out.println();
            System.out.println("Enter email: ");
            String email = scanner.next();
            // Checking every one of the user's inputs to see if they are equal to 'q'. One
            // if statement for each input.
            if (email.equals("q")) {
                break;
            }

            // Using Console to mask password input. (No, I didn't use ChatGPT for this,
            // figured it out using stackoverflow)
            char[] passwordArray = System.console().readPassword("Enter password: ");
            String password = new String(passwordArray);
            if (password.equals("q")) {
                break;
            }

            if (loginUser(email, password)) {
                if (userDao.getUserByEmail(email).isDoctor() && isDoctor) {
                    loggedInDoctorMenu(doctorPortalDao.getDoctorByEmail(email));
                    break;
                } else if (userDao.getUserByEmail(email).isDoctor() && !isDoctor) {
                    System.out.println("Cannot sign into doctor account from user menu. Please use the doctor menu.");
                    System.out.println("Press enter to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                } else if (!userDao.getUserByEmail(email).isDoctor() && !isDoctor) {
                    loggedInUserMenu(userDao.getUserByEmail(email), true);
                    break;
                } else {
                    System.out.println("Cannot sign into user account from doctor menu. Please use the user menu.");
                    System.out.println("Press enter to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                }
            } else {
                message = "%sInvalid email or password. Please try again.%s";
            }
        }
    }

    // Register a new user.
    public static void register(boolean isDoctor) {
        List<String> fields = new ArrayList<>(); // Setting up an array list to store all user details
        System.out.println("\033[H\033[2J");
        System.out.println("\033[4mRegister User\033[0m");
        System.out.println("(Enter 'q' at any time to go back)");
        System.out.println();
        System.out.println("Enter first name: ");
        fields.add(scanner.next());
        if (fields.get(0).equals("q")) {
            return;
        }
        System.out.println("Enter last name: ");
        fields.add(scanner.next());
        if (fields.get(1).equals("q")) {
            return;
        }
        System.out.println("Enter email: ");
        fields.add(scanner.next());
        if (fields.get(2).equals("q")) {
            return;
        }

        char[] passwordArray = System.console().readPassword("Enter password: ");
        String password = new String(passwordArray);
        fields.add(password);
        if (fields.get(3).equals("q")) {
            return;
        }

        if (isDoctor) {
            System.out.println("Enter medial license number: ");
            fields.add(scanner.next());
            if (fields.get(4).equals("q")) {
                return;
            }

            System.out.println("Enter specialization: ");
            fields.add(scanner.next());
            if (fields.get(5).equals("q")) {
                return;
            }
        }

        if (!isDoctor) {
            User newUser = new User(1, fields.get(0), fields.get(1), fields.get(2), fields.get(3), isDoctor);
            if (userDao.createUser(newUser)) {
                System.out.println("User created successfully.");
            } else {
                System.out.println("Error creating user.");
            }
        } else {
            Doctor newDoctor = new Doctor(1, fields.get(0), fields.get(1), fields.get(2), fields.get(3), isDoctor,
                    fields.get(4), fields.get(5));
            if (doctorPortalDao.addDoctor(newDoctor)) {
                System.out.println("Doctor created successfully.");
            } else {
                System.out.println("Error creating doctor.");
            }
        }

        System.out.println("Press any key to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    // Menu for doctors to register/login.
    public static void doctorMenu() {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sDoctor Menu%s";

        doctorMenu: while (true) {
            System.out.println("\033[H\033[2J");
            System.out.println(String.format(message, underline1, underline2));
            System.out.println();
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Return to Main Menu");
            System.out.println("4. Exit Application");
            System.out.println();
            System.out.println("Enter your selection (1-4): ");

            try {
                int selection = scanner.nextInt();

                switch (selection) {
                    case 1:
                        login(true);
                        message = "%sDoctor Menu%s";
                        break;
                    case 2:
                        register(true);
                        message = "%sDoctor Menu%s";
                        break;
                    case 3:
                        break doctorMenu;
                    case 4:
                        System.out.println("Goodbye!");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        message = "%sInvalid selection. Please try again.%s";
                        break;
                }
            } catch (InputMismatchException e) {
                message = "%sInvalid selection. Please try again.%s";
                scanner.nextLine();
            }
        }
    }

    // Menu for a logged in user.
    public static void loggedInUserMenu(User user, boolean firstTime) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sWelcome, %s%s";

        loggedInUserMenu: while (true) {
            System.out.println("\033[H\033[2J");

            if (medicineReminderDao.getDueReminders(user.getId()) != null && firstTime) {
                firstTime = false;
                System.out.println(underline1 + "Due Medicine Reminders" + underline2);
                System.out.println();
                for (MedicineReminder reminder : medicineReminderDao.getDueReminders(user.getId())) {
                    System.out.println(reminder.toString());
                    System.out.println();
                }
            }
            System.out.println(String.format(message, underline1, user.getFirstName(), underline2));
            System.out.println();
            System.out.println("1. Health Data");
            System.out.println("2. Reminders");
            System.out.println("3. View Recommendations");
            System.out.println("4. Logout");
            System.out.println("5. Exit Application");
            System.out.println();
            System.out.println("Enter your selection (1-5): ");

            try {
                int selection = scanner.nextInt();

                switch (selection) {
                    case 1:
                        healthDataMenu(user);
                        message = "%sWelcome, %s%s";
                        break;
                    case 2:
                        remindersMenu(user);
                        message = "%sWelcome, %s%s";
                        break;
                    case 3:
                        viewRecommendations(user);
                        message = "%sWelcome, %s%s";
                        break;
                    case 4:
                        break loggedInUserMenu;
                    case 5:
                        // Exit application.
                        System.out.println("Goodbye!");
                        scanner.close();
                        System.exit(0);
                    default:
                        message = "%sInvalid selection, %s. Please enter a number between 1 and 5.%s";
                }
            } catch (InputMismatchException e) {
                message = "%sInvalid input, %s. Please enter a number between 1 and 5.%s";
                scanner.nextLine();
            }
        }
        // Health data input. User inputs their daily health data.
        // Recommendations based on the user's health data.
        // Medicine reminders. Users can set reminders, and will be notified on login if
        // it's time to take the medicine.
    }

    // Menu for managing a user's health data.
    public static void healthDataMenu(User user) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sHealth Data for %s%s";

        healthDataMenu: while (true) {
            System.out.println("\033[H\033[2J");
            System.out.println(String.format(message, underline1, user.getFirstName(), underline2));
            System.out.println();
            System.out.println("1. Input Health Data Entry");
            System.out.println("2. View All Health Data Entries");
            System.out.println("3. Update Health Data Entry");
            System.out.println("4. Delete Health Data Entry");
            System.out.println("5. Back");
            System.out.println("6. Exit Application");
            System.out.println();
            System.out.println("Enter your selection (1-6): ");

            try {
                int selection = scanner.nextInt();

                switch (selection) {
                    case 1:
                        inputHealthData(user);
                        break;
                    case 2:
                        viewHealthData(user);
                        break;
                    case 3:
                        updateHealthData(user);
                        break;
                    case 4:
                        deleteHealthData(user);
                        break;
                    case 5:
                        break healthDataMenu;
                    case 6:
                        // Exit application.
                        System.out.println("Goodbye!");
                        scanner.close();
                        System.exit(0);
                    default:
                        message = "%sInvalid selection, %s. Please enter a number between 1 and 6.%s";
                        break;
                }
            } catch (InputMismatchException e) {
                message = "%sInvalid input, %s. Please enter a number between 1 and 6.%s";
                scanner.nextLine();
            }
        }

    }

    // Input a health data entry. Displays recommendations based on health data.
    public static void inputHealthData(User user) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sInput Daily Health Data for %s%s";

        System.out.println("\033[H\033[2J");
        System.out.println(String.format(message, underline1, user.getFirstName(), underline2));
        System.out.println("(Enter '-1' at any time to go back)");
        System.out.println();

        // Weight and height must be below 1000, will add input validation if I have
        // time.
        System.out.println("Enter your current weight (in kg): ");
        double weight = scanner.nextDouble();
        if (weight == -1) {
            return;
        }
        System.out.println("Enter your current height (in cm): ");
        double height = scanner.nextDouble();
        if (height == -1) {
            return;
        }
        System.out.println("Enter your steps taken today: ");
        int steps = scanner.nextInt();
        if (steps == -1) {
            return;
        }
        System.out.println("Enter your heart rate: ");
        int heartRate = scanner.nextInt();
        if (heartRate == -1) {
            return;
        }

        HealthData healthData = new HealthData(1, user.getId(), weight, height, steps, heartRate, "date");
        // ID and date values are unimportant here.

        if (healthDataDao.createHealthData(healthData)) {
            System.out.println("Health data created successfully.");
        } else {
            System.out.println("Error creating health data.");
        }

        System.out.println();
        if (recommendationDao.insertRecommendations(healthData)) {
            System.out.println("Recommendations created successfully.");
            System.out.println();
            for (String recommendation : recommendationSystem.generateRecommendations(healthData)) {
                System.out.println(recommendation);
                System.out.println();
            }

        } else {
            System.out.println("Error creating recommendations.");
        }

        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    // View all health data entries.
    public static void viewHealthData(User user) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sHealth Data Entries for %s%s";
        int numberOfEntries = 0;

        System.out.println("\033[H\033[2J");
        System.out.println(String.format(message, underline1, user.getFirstName(), underline2));
        System.out.println();
        for (HealthData healthData : healthDataDao.getHealthDataByUserId(user.getId())) {
            System.out.println(healthData);
            System.out.println();
            numberOfEntries++;
        }

        if (numberOfEntries == 0) {
            System.out.println("No health data entries found.");
        }

        System.out.println();
        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    // Delete a health data entry.
    public static void deleteHealthData(User user) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sDelete Health Data Entry for %s%s";

        System.out.println("\033[H\033[2J");
        System.out.println(String.format(message, underline1, user.getFirstName(), underline2));
        System.out.println("(Or enter '-1' to go back)");
        System.out.println();

        System.out.println("Enter the ID of the health data entry you'd like to delete:");
        int id = scanner.nextInt();

        if (user.getId() != healthDataDao.getHealthDataById(id).getUserId()) {
            System.out.println("You cannot delete another user's health data.");
        } else {
            if (healthDataDao.deleteHealthData(id)) {
                System.out.println("Health data entry deleted successfully.");
            } else {
                System.out.println("Error deleting health data entry.");
            }
        }

        System.out.println();
        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    // Update a health data entry.
    public static void updateHealthData(User user) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sUpdate Health Data Entry for %s%s";

        System.out.println("\033[H\033[2J");
        System.out.println(String.format(message, underline1, user.getFirstName(), underline2));
        System.out.println();
        for (HealthData healthData : healthDataDao.getHealthDataByUserId(user.getId())) {
            System.out.println(healthData);
            System.out.println();
        }

        System.out.println(
                "Enter the ID of the health data entry you'd like to update (Or enter '-1' at any time to go back):");
        int id = scanner.nextInt();
        if (id == -1) {
            return;
        }

        System.out.println("Enter new weight (in kg): ");
        double weight = scanner.nextDouble();
        if (weight == -1) {
            return;
        }

        System.out.println("Enter new height (in cm): ");
        double height = scanner.nextDouble();
        if (height == -1) {
            return;
        }

        System.out.println("Enter new steps: ");
        int steps = scanner.nextInt();
        if (steps == -1) {
            return;
        }

        System.out.println("Enter new heart rate: ");
        int heartRate = scanner.nextInt();
        if (heartRate == -1) {
            return;
        }

        HealthData updHealthData = new HealthData(id, user.getId(), weight, height, steps, heartRate, "");

        if (healthDataDao.updateHealthData(updHealthData)) {
            System.out.println("Health data entry updated successfully.");
        } else {
            System.out.println("Error updating health data entry.");
        }

        System.out.println();
        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();

    }

    // Menu for managing a user's reminders.
    public static void remindersMenu(User user) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sReminders for %s%s";

        remindersMenu: while (true) {
            System.out.println("\033[H\033[2J");
            System.out.println(String.format(message, underline1, user.getFirstName(), underline2));
            System.out.println();

            System.out.println("1. Create New Reminder");
            System.out.println("2. View All Reminders");
            System.out.println("3. View Due Reminders");
            System.out.println("4. Back");
            System.out.println("5. Exit Application");
            System.out.println();
            System.out.println("Enter your selection (1-5): ");

            try {
                int selection = scanner.nextInt();

                switch (selection) {
                    case 1:
                        createReminder(user);
                        break;
                    case 2:
                        viewReminders(user);
                        break;
                    case 3:
                        viewDueReminders(user);
                        break;
                    case 4:
                        break remindersMenu;
                    case 5:
                        System.out.println("Goodbye!");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        message = "%sInvalid input, %s. Please enter a number between 1 and 5.%s";
                        break;
                }
            } catch (InputMismatchException e) {
                message = "%sInvalid input, %s. Please enter a number between 1 and 5.%s";
                scanner.nextLine();
            }
        }
    }

    // Create a new reminder.
    public static void createReminder(User user) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sCreate New Reminder for %s%s";

        System.out.println("\033[H\033[2J");
        System.out.println(String.format(message, underline1, user.getFirstName(), underline2));
        System.out.println("(Enter 'q' at any time to go back)");
        System.out.println();

        System.out.println("Enter medicine name:");
        String name = scanner.next();
        if (name.equals("q")) {
            return;
        }

        System.out.println("Enter dosage:");
        String dosage = scanner.next();
        if (dosage.equals("q")) {
            return;
        }

        System.out.println("Enter schedule:");
        String schedule = scanner.next();
        if (schedule.equals("q")) {
            return;
        }

        // Dates must be in proper format. Won't have time to add input validation.
        System.out.println("Enter start date (YYYY-MM-DD):");
        System.out.println("(Or enter 'TODAY' to set start date to today)");
        String startDate = scanner.next();
        if (startDate.equals("q")) {
            return;
        } else if (startDate.toUpperCase().equals("TODAY")) {
            startDate = LocalDate.now().toString();
        }

        System.out.println("Enter end date (YYYY-MM-DD):");
        String endDate = scanner.next();
        if (endDate.equals("q")) {
            return;
        }

        MedicineReminder newReminder = new MedicineReminder(1, user.getId(), name, dosage, schedule, startDate,
                endDate);

        if (medicineReminderDao.addReminder(newReminder)) {
            System.out.println("Reminder created successfully.");
        } else {
            System.out.println("Error creating reminder.");
        }

        System.out.println();
        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    // View all reminders for a user.
    public static void viewReminders(User user) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sAll Reminders for %s%s";

        System.out.println("\033[H\033[2J");
        System.out.println(String.format(message, underline1, user.getFirstName(), underline2));
        System.out.println();
        if (medicineReminderDao.getRemindersForUser(user.getId()) != null) {
            for (MedicineReminder reminder : medicineReminderDao.getRemindersForUser(user.getId())) {
                System.out.println(reminder.toString());
                System.out.println();
            }
        } else {
            System.out.println("No reminders found.");
        }

        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    // View all due reminders for a user.
    public static void viewDueReminders(User user) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sDue Reminders for %s%s";

        System.out.println("\033[H\033[2J");
        System.out.println(String.format(message, underline1, user.getFirstName(), underline2));
        System.out.println();
        if (medicineReminderDao.getDueReminders(user.getId()) != null) {
            for (MedicineReminder reminder : medicineReminderDao.getDueReminders(user.getId())) {
                System.out.println(reminder.toString());
                System.out.println();
            }
        } else {
            System.out.println("No reminders due.");
        }

        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    // View all recommendations for a user in the past week.
    public static void viewRecommendations(User user) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sRecommendations from the past week for %s%s";

        System.out.println("\033[H\033[2J");
        System.out.println(String.format(message, underline1, user.getFirstName(), underline2));
        System.out.println();
        for (String recommendation : recommendationDao.getRecommendationsByUserId(user.getId())) {
            System.out.println(recommendation);
            System.out.println();
        }

        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    // Menu for a logged in doctor.
    public static void loggedInDoctorMenu(Doctor doctor) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sWelcome, Dr. %s%s";

        loggedInDoctorMenu: while (true) {

            System.out.println("\033[H\033[2J");
            System.out.println(String.format(message, underline1, doctor.getLastName(), underline2));
            System.out.println();
            System.out.println("1. List All Patients"); // All patient names and IDs
            System.out.println("2. Add Patient to Your List");
            System.out.println("3. Remove Patient from Your List");
            System.out.println("4. View Your Patients"); // All patients associated with the specified doctor
            System.out.println("5. View Your Patients' Health Data");
            System.out.println("6. Logout");
            System.out.println("7. Exit Application");
            System.out.println();
            System.out.println("Enter your selection (1-7): ");

            try {
                int selection = scanner.nextInt();
                switch (selection) {
                    case 1:
                        allPatients();
                        break;
                    case 2:
                        addPatient(doctor);
                        break;
                    case 3:
                        removePatient(doctor);
                        break;
                    case 4:
                        allAssociatedPatients(doctor);
                        break;
                    case 5:
                        viewPatientHealthData(doctor);
                        break;
                    case 6:
                        break loggedInDoctorMenu;
                    case 7:
                        System.out.println("Goodbye!");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        message = "%sInvalid selection, Dr. %s. Please try again.%s";
                        break;
                }
            } catch (InputMismatchException e) {
                message = "%sInvalid selection, Dr. %s. Please try again.%s";
                scanner.nextLine();
            }
        }
    }

    // Lists all patients (ID and name only)
    public static void allPatients() {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sAll Patients%s";

        System.out.print("\033[H\033[2J");
        System.out.println();
        System.out.println();
        System.out.println(String.format(message, underline1, underline2));
        System.out.println();
        for (String patient : doctorPortalDao.getAllPatients()) {
            System.out.println(patient);
            System.out.println();
        }

        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
        // Depending on terminal window size and patient list length, screen may not
        // clear properly. Not too big of a concern but can get annoying during an
        // extended session.
    }

    // Adds a patient to a doctor's list.
    public static void addPatient(Doctor doctor) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sAdd Patient to Your List%s";

        System.out.print("\033[H\033[2J");
        System.out.println(String.format(message, underline1, underline2));
        System.out.println("(Make sure to check the patients list before adding a patient. Enter '-1' to cancel.)");

        System.out.println();
        System.out.println("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        if (patientId == -1) {
            return;
        }

        if (doctorPortalDao.associatePatientWithDoctor(patientId, doctor.getId())) {
            System.out.println(userDao.getUserById(patientId).getFirstName() + " "
                    + userDao.getUserById(patientId).getLastName() + " added to your list.");
        } else {
            System.out.println("Error adding patient.");
        }

        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    // Removes a patient from a doctor's list.
    public static void removePatient(Doctor doctor) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sRemove Patient from Your List%s";

        System.out.print("\033[H\033[2J");
        System.out.println(String.format(message, underline1, underline2));
        System.out.println("(Make sure to check your patients list before removing a patient. Enter '-1' to cancel.)");
        System.out.println();
        System.out.println("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        if (patientId == -1) {
            return;
        }

        if (doctorPortalDao.removePatientFromDoctor(patientId, doctor.getId())) {
            System.out.println(userDao.getUserById(patientId).getFirstName() + " "
                    + userDao.getUserById(patientId).getLastName() + " removed from your list.");
        } else {
            System.out.println("Error removing patient.");
        }

        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();

    }

    // Lists all of a doctor's patients.
    public static void allAssociatedPatients(Doctor doctor) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sDr. %s's Patients%s";

        System.out.print("\033[H\033[2J");
        System.out.println(String.format(message, underline1, doctor.getLastName(), underline2));
        System.out.println();
        for (User patient : doctorPortalDao.getPatientsByDoctorId(doctor.getId())) {
            System.out.println(patient.toString());
            System.out.println();
        }

        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    // View a specific patient's health data.
    public static void viewPatientHealthData(Doctor doctor) {
        String underline1 = "\033[4m";
        String underline2 = "\033[0m";
        String message = "%sView Patient Health Data%s";

        System.out.print("\033[H\033[2J");
        System.out.println(String.format(message, underline1, underline2));
        System.out.println("(Make sure to check your patients list before viewing health data. Enter '-1' to cancel.)");
        System.out.println();

        System.out.println("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        if (patientId == -1) {
            return;
        }
        System.out.println();
        System.out.println("Health data for " + userDao.getUserById(patientId).getFirstName() + " "
                + userDao.getUserById(patientId).getLastName() + ":");

        for (HealthData healthData : doctorPortalDao.getHealthDataByPatientId(patientId, doctor.getId())) {
            System.out.println(healthData.toString());
            System.out.println();
        }

        System.out.println("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();

    }

    // Verifying user login credentials.
    public static boolean loginUser(String email, String password) {
        User user = userDao.getUserByEmail(email);

        if (user != null) {
            if (userDao.verifyPassword(email, password)) {
                return true;
            }
        }
        return false;
    }

    // Doctor-related tests.
    public static void testDoctorPortal() {
        int doctorId = 13;

        if (doctorPortalDao.getDoctorById(doctorId) != null) {
            System.out.println(doctorPortalDao.getDoctorById(doctorId).toString());
            System.out.println("Doctor fetched successfully.");
            System.out.println();
        } else {
            System.out.println("Doctor fetch test failed.");
            System.out.println();
        }

        if (doctorPortalDao.getPatientsByDoctorId(doctorId) != null) {
            for (User patient : doctorPortalDao.getPatientsByDoctorId(doctorId)) {
                System.out.println(patient.toString());
                System.out.println();
            }
            System.out.println("Patients fetched successfully.");
            System.out.println();
        } else {
            System.out.println("Patients fetch test failed.");
            System.out.println();
        }

        if (doctorPortalDao.getHealthDataByPatientId(6, doctorId) != null) {
            for (HealthData healthData : doctorPortalDao.getHealthDataByPatientId(6, doctorId)) {
                System.out.println(healthData.toString());
                System.out.println();
            }
            System.out.println("Health data fetched successfully.");
            System.out.println();
        } else {
            System.out.println("Health data fetch test failed.");
            System.out.println();
        }

        System.out.println("DOCTOR TESTS COMPLETE.");

    }

    // Login test.
    public static void testLoginUser() {
        String userEmail = "whale@gmail.com";
        String userPassword = "hoodz";

        boolean loginSuccess = loginUser(userEmail, userPassword);

        if (loginSuccess) {
            System.out.println("Login test successful.");
        } else {
            System.out.println("Login test failed.");
        }
    }

}
