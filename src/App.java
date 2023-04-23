
import java.sql.*;
import java.util.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

class Session {

    private final int userId;
    private final String userName;

    public Session(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return userName;
    }
}

public class App {

    static Session session;
    static String currentUser = null;

    public static void main(String[] args) throws Exception {
        clearScreen();
        mainMenu();
    }

    //connect to database module
    static Statement connect() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/pms";
            String username = "root";
            String password = "";
            Connection conn = DriverManager.getConnection(url, username, password); //To Establish connection
            Statement stmt = conn.createStatement();
            return stmt;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //main Menu module
    static void mainMenu() throws Exception {
        int i = 0;
        while (i == 0) {
            clearScreen();
            welcome();
            System.out.println("Please select an option");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println("----------------------------------------");
            System.out.print("Option: ");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    clearScreen();
                    login();
                    break;
                case 2:
                    clearScreen();
                    registration();
                    break;
                case 3:
                    clearScreen();
                    closeCmd();
                    System.exit(0);
                    break;
                case 4:
                    mds();

                    break;
                default:
                    clearScreen();
                    System.out.println("Invalid choice");
                    System.out.println("Please Select a valid option");
                    System.out.println("Press any key to continue");
                    sc.nextLine();
                    sc.nextLine();
                    break;
            }
        }
    }

    //Registration module
    static void registration() throws Exception {
        System.out.println("========================================");
        System.out.println("Registration Form");
        System.out.println("========================================");

        Statement stmt = connect();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name:");
        String name = sc.nextLine();
        // System.out.println("Enter phone number");
        // long number = sc.nextLong();
        // System.out.println("Enter email");
        // String email = sc.next();

        String number = null;
        while (true) {
            System.out.println("Enter phone number (10 digits):");
            number = sc.nextLine();
            if (number.matches("\\d{10}")) {
                break;
            }
            System.out.println("Invalid phone number. Please enter 10 digits.");
        }

        String email = null;
        while (true) {
            System.out.println("Enter email address:");
            email = sc.nextLine();
            if (email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                break;
            }
            System.out.println("Invalid email address. Please enter a valid email address.");
        }

        System.out.println("Enter age:");
        int age = sc.nextInt();

        System.out.println("Enter M/F");
        char ch = sc.next().charAt(0);

        sc.nextLine();
        System.out.println("Enter address:");
        String addr = sc.nextLine();

        System.out.println("Do you have any disability?[True/False]");
        Boolean dis = sc.nextBoolean();
        System.out.println("Enter password");
        String password = sc.next();
        int userid = generateRandomNumber();

        String sql = "INSERT INTO users (userid,name, phonenumber, email, age, gender, address, password, disability)VALUES ("
                + userid + ",'" + name + "'," + number + ",'" + email + "'," + age + ",'" + ch + "','" + addr
                + "','" + password + "'," + dis + ");";
        stmt.executeUpdate(sql);
        System.out.println("Please note down your user id!!!");
        System.out.println(userid);

        clearScreen();
        System.out.println("========================================");
        System.out.println("Registration Successful");
        System.out.println("========================================");

        System.out.println("Press any key to continue");
        sc.nextLine();
        sc.nextLine();
        clearScreen();
    }

    //generate random number module
    public static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }

    //clear screen module
    public static void clearScreen() {
        for(int x=0;x<20;x++)
    {
        System.out.print("\n\n\n\n\n");
    }
    }

    //close CMD if running on windows and exit if cmd running with admin privileges
    public static void closeCmd() {
        ProcessBuilder pb = new ProcessBuilder("taskkill", "/IM", "cmd.exe");
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void welcome() {
        System.out.println("========================================");
        System.out.println("Welcome to the Patient Management System");
        System.out.println("========================================");
    }

    //login Module
    static void login() throws Exception {

        System.out.println("========================================");
        System.out.println("Login Form");
        System.out.println("========================================");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your email or userid:");
        String email = sc.next();
        System.out.println("Enter your password:");
        String password = sc.next();

        Statement stmt = connect();
        String sql = "SELECT * FROM users WHERE (email='" + email + "' OR userid='" + email + "')AND password='" + password + "';";
        // System.out.println("SQL query: " + sql);
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            clearScreen();
            System.out.println("========================================");
            System.out.println("Login Successful");
            System.out.println("========================================");
            // System.out.println("Welcome " + rs.getString("name"));
            int userid = rs.getInt("userid");
            String username = rs.getString("name");
            session = new Session(userid, username);
            afterLogin();
        } else {
            clearScreen();
            System.out.println("========================================");
            System.out.println("Login Failed: Invalid email or password");
            System.out.println("========================================");
        }

        System.out.println("Press any key to continue");
        sc.nextLine();
        sc.nextLine();
        clearScreen();
    }

    //After login module
    static void afterLogin() throws Exception {
        int i = 0;
        while (i == 0) {
            clearScreen();
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Welcome " + session.getUsername() + " to the Patient Management System(PMS)");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Please select an option");
            System.out.println("========================================");
            System.out.println("1. Book Appointment");
            System.out.println("2. View Appointment");
            System.out.println("3. Cancel Appointment");
            System.out.println("4. Medicine Suggestion");
            System.out.println("5. View/Edit Profile");
            System.out.println("6. Logout");
            System.out.println("========================================");
            System.out.print("Option: ");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    clearScreen();
                    bookAppointment();
                    break;
                case 2:
                    clearScreen();
                    viewAppointment();
                    break;
                case 3:
                    clearScreen();
                    cancelAppointment();
                    break;
                case 4:
                    clearScreen();
                    System.out.println("------------------------------------------------------------------------------");
                    System.out.println("Welcome " + currentUser + " to the Patient Management System(PMS)");
                    System.out.println("------------------------------------------------------------------------------");
                    mds();
                    break;
                case 5:
                    displayProfile();
                    // editProfile();
                    break;
                case 6:
                    clearScreen();
                    mainMenu();

                    break;
                default:
                    clearScreen();
                    System.out.println("Invalid choice");
                    System.out.println("Please Select a valid option");
                    System.out.println("Press any key to continue");
                    sc.nextLine();
                    sc.nextLine();
                    break;
            }
        }
    }

    static void mds() {
        Scanner input = new Scanner(System.in);
        System.out.println("USAGE WARNING:");
        System.out.println("Please DO NOT take these suggestions over any prescribed medicines");
        System.out.println("If you have a history of allergic reactions, we advise you to not use this module");
        System.out.println("You are advised not to take any medications WITHOUT CONSULTING a doctor if the symptoms are severe and not improving.");
        System.out.println("Please enter your symptoms (separated by commas): ");
        String symptoms = input.next();
        System.out.println("Enter time in days since the symptoms started");
        int time = input.nextInt();
        //input.nextLine();
        String[] symptomList = symptoms.split(",");

        if (!(isSevere(symptomList, time))) {
            System.out.println("-------------------------------------------------");
            System.out.println("Your symptoms are severe. Please see a doctor immediately.");
            System.out.println("-------------------------------------------------");
            System.out.println("Press any key to continue");
            input.nextLine();
            input.nextLine();
        } else {
            System.out.println("-------------------------------------------------");
            suggestMedicine(symptomList);
            System.out.println("If the symptoms persist, please see a doctor");
            System.out.println("-------------------------------------------------");
            System.out.println("Press any key to continue");
            input.nextLine();
            input.nextLine();
        }
    }

    public static boolean isSevere(String[] symptoms, int time) {
        String[] commonSymptoms = {"fever", "cough", "vomiting", "fatigue", "muscle aches", "body ache", "headache", "sore throat", "congestion", "runny nose", "nausea", "diarrhea"};
        if (time >= 10) {
            return false;
        }
        int count = 0;
        for (String symptom : symptoms) {
            if (Arrays.asList(commonSymptoms).contains(symptom.trim().toLowerCase())) {
                count++;
            }
        }
        return count < 3;
    }

    public static void suggestMedicine(String[] symptoms) {
        List<String> matches = new ArrayList<>();
        String[] commonSymptoms = {"fever", "cough", "vomiting", "fatigue", "muscle aches", "body ache", "headache", "sore throat", "congestion", "runny nose", "nausea", "diarrhea"};
        for (String symptom : symptoms) {
            if (Arrays.asList(commonSymptoms).contains(symptom)) {
                matches.add(symptom);
            }
        }
        if (matches.size() > 0) {
            System.out.println("You have the following symptoms: " + String.join(", ", matches));
            for (String symptom : matches) {
                switch (symptom) {
                    case "fever":
                        System.out.println("Take Dolo. Dosage: 650 mg for adults and 250 mg for children.");
                        break;
                    case "cough":
                        System.out.println("Take Benadryl.");
                        break;
                    case "vomiting":
                        System.out.println("Take small sips of oral rehydration solution (ORS) to prevent dehydration. Eat easy-to-digest foods like toast, crackers, gelatin or other similar foods to ease an upset stomach.");
                        break;
                    case "fatigue":
                        System.out.println("Maintain a regular sleep schedule, eat a healthy diet, and exercise regularly. Consuming less caffeine during the day and avoiding caffeine at night may also help. Take some ORS and eat lots of fruits.");
                        break;
                    case "muscle aches":
                        System.out.println("Take over-the-counter pain relievers such as aspirin, acetaminophen, ibuprofen or naproxen.");
                        break;
                    case "body ache":
                        System.out.println("Take over-the-counter pain relievers such as aspirin, acetaminophen, ibuprofen or naproxen.");
                        break;
                    case "headache":
                        System.out.println("Remedies that may reduce headache pain include aspirin, paracetamol and ibuprofen. Resting in a darkened room may also help.");
                        break;
                    case "sore throat":
                        System.out.println("Taking pain medication such as ibuprofen and paracetamol may help. Avoid giving aspirin to children because this may cause a rare, serious condition.");
                        break;
                    case "congestion":
                        System.out.println("Inhale karvol plus.");
                        break;
                    case "runny nose":
                        System.out.println("Drink plenty of water and use a humidifier to relieve symptoms. If the runny nose is caused by allergies, taking a non-sedating antihistamine may also help.");
                        break;
                    case "nausea":
                        System.out.println("Take Kaopectate or Pepto-Bismol. Resting, eating bland foods and avoiding strong food odours, perfume, smoke and stuffy rooms may help to reduce nausea.");
                        break;
                    case "diarrhea":
                        System.out.println("Replace lost fluids with an oral rehydration solution (ORS) and take antidiarrhoeal drugs such as loperamide to help prevent dehydration.");
                        break;
                }
            }
        } else {
            System.out.println("We could not find any matches for your symptoms or your symptoms are not common. Please see a doctor.");
        }
    }

    public static void displayProfile() throws Exception {
        Statement stmt = connect();
        String url = "Select * from users where userid=" + session.getUserId() + ";";
        ResultSet rs = stmt.executeQuery(url);
        while (rs.next()) {
            Scanner input;
            input = new Scanner(System.in);
            System.out.println("Your profile:");
            System.out.println("User ID:" + rs.getInt("userid"));
            System.out.println("Name:" + rs.getString("name"));
            System.out.println("Phone number:" + rs.getLong("phonenumber"));
            System.out.println("E-mail:" + rs.getString("email"));
            System.out.println("Age:" + rs.getInt("age"));
            System.out.println("Gender:" + rs.getString("gender"));
            System.out.println("Address:" + rs.getString("address"));
            System.out.println("Password:" + rs.getString("password"));
            System.out.println("Disability:" + rs.getBoolean("disability"));
            System.out.println("Do you want to edit your profile?(1/0)");
            int temp = input.nextInt();
            if (temp == 1) {
                editProfile();
            } else {
                afterLogin();
            }

        }
    }

    public static void editProfile() throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("You can edit the following");
        System.out.println("1.Name");
        System.out.println("2.Phone number");
        System.out.println("3.Email");
        System.out.println("4.Address");
        System.out.println("5.Password");
        System.out.println("6.Disability");
        System.out.println("Enter your choice");
        int ch = input.nextInt();
        input.nextLine();
        Statement stmt = connect();
        switch (ch) {
            case 1:
                input.nextLine();
                System.out.println("Enter new name:");
                String name = input.nextLine();
                System.out.print("");
                String url = "update users set name='" + name + "' where userid=" + session.getUserId();
                stmt.executeUpdate(url);
                System.out.println("Name updated!");
                break;
            case 2:
                input.nextLine();
                System.out.println("Enter new number:");
                long num = input.nextLong();
                stmt.executeUpdate("update users set phonenumber=" + num + " where userid=" + session.getUserId());
                System.out.println("Number updated!");
                break;
            case 3:
                input.nextLine();
                System.out.println("Enter new email:");
                input.nextLine();
                String email = input.nextLine();
                stmt.executeUpdate("update users set email='" + email + "' where userid=" + session.getUserId());
                System.out.println("Email updated!");
                break;
            case 4:
                input.nextLine();
                System.out.println("Enter new address:");
                String addr = input.nextLine();
                stmt.executeUpdate("update users set address='" + addr + "' where userid=" + session.getUserId());
                System.out.println("Address updated!");
                break;
            case 5:
                input.nextLine();
                System.out.println("Enter new password:");
                String p = input.nextLine();
                stmt.executeUpdate("update users set password='" + p + "' where userid=" + session.getUserId());
                System.out.println("Password updated!");
                break;
            case 6:
                input.nextLine();
                System.out.println("Change disability?(1/0)");
                int dis = input.nextInt();
                if (dis == 1) {
                    stmt.executeUpdate("update users set disability= NOT disability where userid=" + session.getUserId());
                    System.out.println("Profile updated!");
                } else {
                    break;
                }
                break;
            default:
                System.out.println("Not a valid choice");
                break;
        }
    }

    public static void viewAppointment() throws Exception {
        Statement stmt = connect();
        String url = "Select appointment_datetime,appointment_id from appointments where patient_id=" + session.getUserId() + ";";
        ResultSet rs = stmt.executeQuery(url);
        while (rs.next()) {
            java.sql.Date temp;
            temp = rs.getDate("appointment_datetime");
            if (temp != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = formatter.format(temp);
                System.out.println("Appointment date: " + formattedDate);
                String id = rs.getString("appointment_id");
                System.out.println("Appointment ID:" + id);
            } else {
                System.out.println("No appointments booked yet!!");
            }

        }
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }

    public static void bookAppointment() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/pms";
        String username = "root";
        String password = "";
        Connection conn = DriverManager.getConnection(url, username, password); //To Establish connection
        Statement stmt = conn.createStatement();

        Scanner sc = new Scanner(System.in);
        url = "SELECT doctor_name, specialty, doctor_id FROM doctors";
        ResultSet rs = stmt.executeQuery(url);
        while (rs.next()) {
            System.out.println("Name:" + rs.getString("doctor_name"));
            System.out.println("Specialty:" + rs.getString("specialty"));
            System.out.println("ID:" + rs.getInt("doctor_id"));
            System.out.println();
        }
        System.out.println("Enter the doctor id for booking an appointment");
        int id = sc.nextInt();
        System.out.print("Enter date of appointment (yyyy-mm-dd):");
        String sDate = sc.next();
        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate.trim());
        java.sql.Date mySQLDate = new java.sql.Date(date1.getTime());
        url = "SELECT * FROM dappoint WHERE doctor_id=?";
        PreparedStatement pstmt = conn.prepareStatement(url);
        pstmt.setInt(1, id);
        rs = pstmt.executeQuery();
        if (!(rs.next())) {
            String aid = appointmentIdGenerator();
            pstmt = conn.prepareStatement("INSERT INTO dappoint(date_of_appointment, number_of_appointments, doctor_id) VALUES (?,1,?);");
            pstmt.setDate(1, mySQLDate);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("INSERT INTO appointments VALUES (?, ?, ?, ?)");
            pstmt.setString(1, aid);
            pstmt.setInt(2, session.getUserId());
            pstmt.setDate(3, mySQLDate);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Appointment Booked!");
            System.out.println("Appointment details:-");
            System.out.println("Please note down appointment id!!");
            System.out.println(aid);
            System.out.println("Doctors id=" + id);
            System.out.println("Appointment date=" + date1);
        } else {
            url = "SELECT number_of_appointments FROM dappoint WHERE date_of_appointment=?";
            pstmt = conn.prepareStatement(url);
            pstmt.setDate(1, mySQLDate);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int num_app = rs.getInt("number_of_appointments");
                if (num_app < 40) {
                    String aid = appointmentIdGenerator();
                    pstmt = conn.prepareStatement("UPDATE dappoint SET number_of_appointments=number_of_appointments+1 WHERE date_of_appointment=? AND doctor_id=?;");
                    pstmt.setDate(1, mySQLDate);
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                    pstmt = conn.prepareStatement("INSERT INTO appointments VALUES (?, ?, ?, ?)");
                    pstmt.setString(1, aid);
                    pstmt.setInt(2, session.getUserId());
                    pstmt.setDate(3, mySQLDate);
                    pstmt.setInt(4, id);
                    pstmt.executeUpdate();

                    System.out.println("Appointment Booked!");
                    System.out.println("Appointment details:-");
                    System.out.print("Please note down appointment id!!");
                    System.out.println(aid);
                    System.out.println("Doctors id=" + id);
                    System.out.println("Appointment date=" + date1);
                } else {
                    System.out.println("Doctor is full for " + sDate + "! Please select a different day to book the appointment");
                }
            }
        }
    }

    public static String appointmentIdGenerator() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rand = new Random();
        int length = rand.nextInt(5) + 6;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(chars.length());
            char c = chars.charAt(index);
            sb.append(c);
        }
        String str = sb.toString();
        return str;

    }

    public static void cancelAppointment() throws Exception {
        Statement stmt = connect();
        Statement stmt1 = connect();
        Statement stmt2 = connect();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter appointment id");
        String aid = input.nextLine();
        String url = "select appointment_id,doctor_id from appointments where appointment_id='" + aid + "';";
        ResultSet rs = stmt.executeQuery(url);
        if (rs.next()) {
            url = "delete from appointments where appointment_id='" + aid + "';";
            stmt1.executeUpdate(url);
            System.out.println("Appointment deleted!!");
            int temp = rs.getInt("doctor_id");
            url = "Update dappoint set number_of_appointments=number_of_appointments-1 where doctor_id=" + temp + ";";
            stmt2.executeUpdate(url);
        } else {
            System.out.println("Appointment doesnt exist!");
        }
    }
}