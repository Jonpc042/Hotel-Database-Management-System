/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author vf0975bh
 */
public class ProjectClient {

    private static Scanner console = new Scanner(System.in);

    private static Connection connect;

    private static Statement statement = null;

    private static ResultSet resultSet = null;

    private static final boolean DEBUG = false;

    private static PreparedStatement checkCustomerLogin;
    private static PreparedStatement checkEmployeeLogin;
    private static PreparedStatement checkAdminLogin;
    private static PreparedStatement addCustomer;
    private static PreparedStatement addEmployee;
    private static PreparedStatement checkBookings;
    private static PreparedStatement cancelBooking;
    private static PreparedStatement bookRoom;
    private static PreparedStatement payCharges;
    private static PreparedStatement setCharge;
    private static PreparedStatement checkBooking;
    private static PreparedStatement getAvailableRooms;
    private static PreparedStatement getRoom;
    private static PreparedStatement getRecordsCharge;
    private static PreparedStatement getCharges;
    private static PreparedStatement createCharge;
    private static PreparedStatement setRecordsCharge;
    private static PreparedStatement getRoomByCustomerName;
    private static PreparedStatement resetChargeAmount;
    private static PreparedStatement getCheckInDate;
    //private static PreparedStatement checkBooking = null;

    public static void main(String[] args) throws Exception {

        int customerID = 0;
        int customerAnswer = 0;
        String query = "";
        int roomNum = 0;
        PreparedStatement preparedStmt = null;

        Class.forName("com.mysql.jdbc.Driver");

        connect = DriverManager.getConnection("jdbc:mysql://mrbartucz.com/vf0975bh_MidtermProject?user=fw1006zs&password=fw1006zs");

        initializePreparedStatements();

        boolean flag = true;
        String userCommand;

        while (flag) {
            showMenu();

            userCommand = console.next();

            switch (userCommand) {

                case "1"://Customer Selection
                    boolean inputCheck = true;
                    while (inputCheck) {
                        try {

                            System.out.print("\n\nEnter 1 for new customer, "
                                    + "\nEnter 2 for returning customer,"
                                    + "\nEnter 0 to exit: ");
                            //console.nextLine is to fix skipping problem
                            console.nextLine();
                            customerAnswer = console.nextInt();

                            if (customerAnswer == 1) {
                                try {
                                    System.out.print("\nEnter new ID: ");
                                    customerID = console.nextInt();
                                    System.out.print("Enter First Name: ");
                                    String customerFName = console.next();
                                    System.out.print("Enter Last Name: ");
                                    String customerLName = console.next();
                                    console.nextLine();
                                    System.out.print("Enter Address: ");
                                    String customerAddress = console.nextLine();
                                    System.out.print("Enter Phone Number(1112223333 format): ");
                                    long customerPhoneNum = console.nextLong();
                                    System.out.print("Enter Zip Code: ");
                                    int customerZip = console.nextInt();
                                    System.out.print("Enter City: ");
                                    String customerCity = console.next();
                                    System.out.print("Enter State(XX format): ");
                                    String customerState = console.next();

                                    query = " insert into Customers (CustomerID, "
                                            + "fName, lName, Address, PhoneNum,"
                                            + "ZipCode, City, State)"
                                            + " values (?, ?, ?, ?, ?, ?, ?, ?)";

                                    preparedStmt = connect.prepareStatement(query);
                                    preparedStmt.setInt(1, customerID);
                                    preparedStmt.setString(2, customerFName);
                                    preparedStmt.setString(3, customerLName);
                                    preparedStmt.setString(4, customerAddress);
                                    preparedStmt.setLong(5, customerPhoneNum);
                                    preparedStmt.setInt(6, customerZip);
                                    preparedStmt.setString(7, customerCity);
                                    preparedStmt.setString(8, customerState);

                                    // execute the preparedstatement
                                    preparedStmt.execute();
                                    inputCheck = false;
                                } catch (SQLException e) {
                                    if (e instanceof SQLIntegrityConstraintViolationException) {
                                        System.out.print("\nThat ID is already taken!\n");
                                        inputCheck = true;
                                    }

                                }

                            } else if (customerAnswer == 2) {
                                System.out.print("Enter your ID: ");
                                customerID = console.nextInt();
                                query = "SELECT * FROM Customers "
                                        + "WHERE CustomerID = ? ";
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setInt(1, customerID);
                                resultSet = preparedStmt.executeQuery();

                                if (resultSet.next() == false) {
                                    System.out.println("\nCustomer ID not found!");

                                } else {
                                    inputCheck = false;
                                }

                            } else if (customerAnswer == 0) {
                                inputCheck = false;

                            } else {
                                System.out.print("Invalid input, try again.");

                            }
                        } catch (InputMismatchException e) {
                            System.out.print("Invalid input!");

                        }
                    }//end inputCheck while

                    if (customerAnswer == 0) {
                        break;
                    }

                    boolean customerFlag = true;
                    String customerCommand;
                    Scanner customerConsole = new Scanner(System.in);

                    while (customerFlag) {
                        customerMenu();

                        customerCommand = customerConsole.next();

                        switch (customerCommand) {

                            case "1"://view profile

                                query = " SELECT * FROM Customers \n"
                                        + " WHERE CustomerID = ?";

                                preparedStmt = connect.prepareStatement(query);

                                preparedStmt.setInt(1, customerID);

                                // execute the preparedstatement
                                resultSet = preparedStmt.executeQuery();

                                while (resultSet.next()) {
                                    customerID = resultSet.getInt("CustomerID");
                                    String firstName = resultSet.getString("fName");
                                    String lastName = resultSet.getString("lName");
                                    String address = resultSet.getString("Address");
                                    long phoneNum = resultSet.getLong("PhoneNum");
                                    String zipCode = resultSet.getString("ZipCode");
                                    String city = resultSet.getString("City");
                                    String state = resultSet.getString("State");

                                    System.out.println("\nCustomerID: " + customerID);
                                    System.out.println("Name: " + firstName + " "
                                            + "" + lastName);

                                    System.out.println("Address: " + address);
                                    System.out.println("PhoneNum: " + phoneNum);
                                    System.out.println("Zip Code: " + zipCode);
                                    System.out.println("City: " + city);
                                    System.out.println("State: " + state);
                                }

                                break;
                            case "2"://view booked

                                checkCustomerBookings(customerID);

                                break;
                            case "3"://book room
                                
                                bookRoom(customerID);
                                break;
                                
                            case "4":// cancel booking

                                cancelBooking(customerID);
                                break;
                                
                            case "5":// pay charge 

                                payCharge(customerID);
                                break;
                                
                            case "0"://stop
                                customerFlag = false;
                                break;

                            default:
                                System.out.println("Invalid command, try again.");

                        }//end switch

                    }//end while

                    break;
                case "2"://employee
                    employeeLogin();
                    employeeMenu();
                    break;
                case "3"://admin

                    String passWord = PassWordGenerator("Password", "somesalt");

                    System.out.print("\nEnter the admin password: ");
                    String adminPassWd = console.next();

                    String adminEncrypt = PassWordGenerator(adminPassWd, "somesalt");

                    if (passWord.equals(adminEncrypt)) {
                        System.out.print("Welcome!");
                    } else {
                        System.out.print("Get out of here hacker!");
                        break;
                    }

                    boolean adminFlag = true;
                    String adminCommand;
                    console = new Scanner(System.in);

                    while (adminFlag) {
                        adminMenu();

                        adminCommand = console.next();

                        switch (adminCommand) {

                            case "1"://Edit Date

                                System.out.print("Enter the Customer "
                                        + "ID whose date you want to edit: ");
                                customerID = console.nextInt();

                                query = "SELECT * FROM Records WHERE CustomerID = ? "
                                        + "AND Paid = \"FALSE\"";
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setInt(1, customerID);

                                resultSet = preparedStmt.executeQuery();
                                Boolean emptySet = true;

                                while (resultSet.next()) {

                                    emptySet = false;
                                    roomNum = resultSet.getInt("RoomNum");
                                    String type = resultSet.getString("Type");
                                    String checkIn = resultSet.getString("CheckInDate");
                                    String checkOut = resultSet.getString("CheckOutDate");
                                    customerID = resultSet.getInt("CustomerID");
                                    System.out.print("\nRoom Number: " + roomNum);
                                    System.out.print("\nType: " + type);
                                    System.out.print("\nCheck-in date: " + checkIn);
                                    System.out.print("\nCheck-out date: " + checkOut);
                                    System.out.print("\nCustomer ID: " + customerID);

                                }
                                if (emptySet) {
                                    System.out.print("\n\nThis customer has no "
                                            + "unpaid charges!\n");
                                    break;

                                }

                                System.out.print("Enter the room number "
                                        + "you want to edit");
                                roomNum = console.nextInt();

                                System.out.print("\nEnter the current check-in date"
                                        + "(XX/XX/XXXX format): ");
                                String checkIn = console.next();

                                System.out.print("\nEnter the new check-in date"
                                        + "(XX/XX/XXXX format): ");
                                String newCheckIn = console.next();

                                System.out.print("\nEnter the new check-out date"
                                        + "(XX/XX/XXXX format): ");
                                String newCheckOut = console.next();

                                query = "UPDATE Records set CheckInDate = ? "
                                        + ", CheckOutDate = ? WHERE RoomNum = ?"
                                        + " AND CustomerID = ? AND CheckInDate = ?";
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setString(1, newCheckIn);
                                preparedStmt.setString(2, newCheckOut);
                                preparedStmt.setInt(3, roomNum);
                                preparedStmt.setInt(4, customerID);
                                preparedStmt.setString(5, checkIn);

                                preparedStmt.executeUpdate();

                                break;
                            case "2"://Add Charge

                                System.out.print("Enter the customer ID "
                                        + "you want to add a charge: ");
                                customerID = console.nextInt();

                                query = "SELECT * FROM Records WHERE CustomerID = ? "
                                        + "AND Paid = \"FALSE\"";
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setInt(1, customerID);

                                resultSet = preparedStmt.executeQuery();
                                emptySet = true;

                                while (resultSet.next()) {

                                    emptySet = false;
                                    roomNum = resultSet.getInt("RoomNum");
                                    String type = resultSet.getString("Type");
                                    checkIn = resultSet.getString("CheckInDate");
                                    String checkOut = resultSet.getString("CheckOutDate");
                                    customerID = resultSet.getInt("CustomerID");
                                    System.out.print("\nRoom Number: " + roomNum);
                                    System.out.print("\nType: " + type);
                                    System.out.print("\nCheck-in date: " + checkIn);
                                    System.out.print("\nCheck-out date: " + checkOut);
                                    System.out.print("\nCustomer ID: " + customerID);

                                }
                                if (emptySet) {
                                    System.out.print("\n\nCustomer "
                                            + "has no unpaid charges!\n");

                                }

                                query = "SELECT * FROM Charges WHERE CustomerID = ?";
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setInt(1, customerID);

                                // execute the preparedstatement
                                resultSet = preparedStmt.executeQuery();

                                emptySet = true;

                                while (resultSet.next()) {

                                    emptySet = false;
                                    int creditCard = resultSet.getInt("CreditCard");
                                    double amount = resultSet.getDouble("Amount");
                                    roomNum = resultSet.getInt("RoomNum");
                                    int employeeID = resultSet.getInt("EmployeeID");
                                    customerID = resultSet.getInt("CustomerID");

                                }
                                if (emptySet) {
                                    System.out.print("\n\nNo Record Found!\n");

                                    System.out.print("\n\nEnter the credit "
                                            + "card number: ");
                                    int card = console.nextInt();

                                    System.out.print("\nEnter the charge amount: ");
                                    double amount = console.nextInt();

                                    System.out.print("\nEnter the room "
                                            + "number you are charging for: ");
                                    roomNum = console.nextInt();

                                    query = " INSERT into Charges (CreditCard, "
                                            + "Amount, RoomNum, EmployeeID, CustomerID)"
                                            + " values (?, ?, ?, ?, ?)";

                                    preparedStmt = connect.prepareStatement(query);
                                    preparedStmt.setInt(1, card);
                                    preparedStmt.setDouble(2, amount);
                                    preparedStmt.setInt(3, roomNum);
                                    preparedStmt.setInt(4, 0);
                                    preparedStmt.setInt(5, customerID);

                                    // execute the preparedstatement
                                    preparedStmt.execute();

                                }

                                System.out.print("\nEnter the room number "
                                        + "you are charging for: ");
                                roomNum = console.nextInt();

                                System.out.print("\nEnter the amount: ");
                                double amount = console.nextInt();

                                System.out.print("Enter the check-in date "
                                        + "(XX/XX/XXXX format): ");
                                checkIn = console.next();

                                query = "UPDATE Charges set Amount = ?, RoomNum = ? WHERE CustomerID = ?";
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setDouble(1, amount);
                                preparedStmt.setInt(2, roomNum);
                                preparedStmt.setInt(3, customerID);

                                preparedStmt.executeUpdate();

                                query = "UPDATE Records set Charge = ? WHERE "
                                        + "CustomerID = ? AND RoomNum = ? AND CheckInDate = ?";
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setDouble(1, amount);
                                preparedStmt.setInt(2, customerID);
                                preparedStmt.setInt(3, roomNum);
                                preparedStmt.setString(4, checkIn);

                                preparedStmt.executeUpdate();

                                break;
                            case "3"://Check Available Rooms
                                
                                break;
                            case "4"://Cancel Booking
                                cancelBooking();
                                break;
                            case "5"://Check Booking 
                                checkCustomerBookings();
                                break;
                            case "6"://Search for Records by Customer name
                                findRecordsByName();
                                break;
                            case "0"://stop
                                adminFlag = false;
                                break;

                            default:
                                System.out.println("Invalid command, try again.");

                        }//end switch

                    }//end while

                    break;
                case "0":
                    flag = false;
                    break;

                default:
                    System.out.println("Invalid command, try again.");

            }//end switch

        }//end while

    }//end main

    public static void showMenu() {

        System.out.print("\n\nWhat are you loging in as?\n"
                + "1 -- Customer\n"
                + "2 -- Employee\n"
                + "3 -- Administrator\n"
                + "0 -- to stop\n"
                + "Enter a command: ");

    }

    public static void customerMenu() {

        System.out.print("\n\nWhat would you like to do?\n"
                + "1 -- View Profile\n"
                + "2 -- View Booked\n"
                + "3 -- Book Room\n"
                + "4 -- Cancel Booking\n"
                + "5 -- Pay Charge\n"
                + "0 -- to stop\n"
                + "Enter a command: ");

    }

    public static void employeeLogin() throws SQLException {

        if (DEBUG) {
            System.out.println("Entered employee login function");
        }

        boolean exit = false;
        try {
            while (!exit) {

                System.out.print("Enter your ID: ");
                int employeeID = console.nextInt();
                checkEmployeeLogin.setInt(1, employeeID);
                resultSet = checkEmployeeLogin.executeQuery();
                //statement = connect.createStatement();
                //resultSet = statement.executeQuery("SELECT * FROM Employees "
                //+ "WHERE EmployeeID = " + employeeID);

                if (resultSet.next() == false) {
                    System.out.println("EmployeeID not found!");
                } else {
                    exit = true;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void employeeMenu() throws SQLException {

        if (DEBUG) {
            System.out.println("\nEntered Employee Menu Display");
        }

        boolean exit = false;

        String userCommand;

        while (!exit) {

            System.out.print("\n\nWhat would you like to do?\n"
                    + "1 -- Check Booking\n"
                    + "2 -- Book Room\n"
                    + "3 -- Cancel Booking\n"
                    + "4 -- Pay Charges\n"
                    + "5 -- Add a Charge\n"
                    + "6 -- Search for Records by name\n"
                    + "0 -- to stop\n"
                    + "Enter a command: ");

            userCommand = console.next();

            switch (userCommand) {

                case "1"://check customer booking
                    checkCustomerBookings();
                    break;
                case "2"://Book Room
                    bookRoom();
                    break;
                case "3"://Cancel Booking
                    cancelBooking();
                    break;
                case "4"://Pay Charges
                    payCharge();
                    break;
                case "5"://Add a charge
                    addCharge();
                    break;
                case "6"://Find Records by customer name
                    findRecordsByName();
                case "0":
                    exit = true;
                    break;

            }

        }

    }

    public static void checkCustomerLogin() throws SQLException {

    }

    public static void checkCustomerBookings() throws SQLException {

        int customerID = 0;

        boolean exit = false;

        while (!exit) {

            System.out.print("\n\nEnter the CustomerID to check bookings "
                    + "(0 to cancel): ");

            try {

                customerID = console.nextInt();

            } catch (InputMismatchException e) {

            }
            
            exit = true;

        }
        if (DEBUG) {
            System.out.println("\nCalling checkCustomerBookings(" + customerID
                    + ")");
        }
        if (customerID != 0) {
            checkCustomerBookings(customerID);
        }
    }

    public static void checkCustomerBookings(int customerID) throws SQLException {
        
        checkBookings.setInt(1, customerID);

        resultSet = checkBookings.executeQuery();

        boolean emptySet = true;

        while (resultSet.next()) {
            emptySet = false;
            int roomNum = resultSet.getInt("RoomNum");
            String type = resultSet.getString("Type");
            String checkIn = resultSet.getString("CheckInDate");
            String checkOut = resultSet.getString("CheckOutDate");
            customerID = resultSet.getInt("CustomerID");
            double charge = resultSet.getDouble("Charge");
            System.out.print("\nCustomer ID: " + customerID);
            System.out.print("\nRoom Number: " + roomNum);
            System.out.print("\nRoom Type: " + type);
            System.out.print("\nCheck-in Date: " + checkIn);
            System.out.print("\nCheck-out Date: " + checkOut);
            System.out.print("\nCharge: " + charge);
            System.out.println();

        }
        
        if (emptySet) {
            System.out.println("No records found");
        }
    }

    public static void bookRoom() throws SQLException {

        int customerID = 0;

        boolean exit = false;

        while (!exit) {

            System.out.print("\nEnter the CustomerID to book a room "
                    + "(0 to cancel): ");

            try {
                customerID = console.nextInt();
                exit = true;
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }

        }
        if (customerID != 0) {
            bookRoom(customerID);
        }
    }

    public static void bookRoom(int customerID) throws SQLException {

        int userSelection;

        String type = null;

        boolean exit = false;

        while (!exit) {

            System.out.print("\nWhich type of room"
                    + " would you like?\n"
                    + "\nEnter 1 for 1 bed"
                    + "\nEnter 2 for 2 bed"
                    + "\nEnter 3 for 3 bed\n"
                    + "Enter 4 for a suite: ");

            try {
                userSelection = console.nextInt();

                switch (userSelection) {
                    case 1:
                        type = "1Bed";
                        exit = true;
                        break;
                    case 2:
                        type = "2Bed";
                        exit = true;
                        break;
                    case 3:
                        type = "3Bed";
                        exit = true;
                        break;
                    case 4:
                        type = "Suite";
                        exit = true;
                        break;
                    default:
                        throw new InputMismatchException("Please enter a valid"
                                + "selection");
                }
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
            
            exit = false;
            
            if (DEBUG) {
                System.out.println("User has chosen type " + type);
            }
            
            getAvailableRooms.setString(1, type);
            
            resultSet = getAvailableRooms.executeQuery();
            
            ArrayList<Integer> availableRooms = new ArrayList<>();

            while (resultSet.next()) {
                int roomNum = resultSet.getInt("RoomNum");
                String roomType = resultSet.getString("Type");
                availableRooms.add(roomNum);

                System.out.println("\nRoom: " + roomNum + " "
                        + "Type: " + roomType);

            }
            //Get the desired room number from the user
            int roomSelection = 0;
            while (!exit) {

                System.out.print("Enter the room number "
                        + "you want to book: ");

                try {
                    roomSelection = console.nextInt();
                    if (!availableRooms.contains(roomSelection)) {
                        throw new InputMismatchException("Please choose from "
                                + "the list of available rooms");
                    }
                    exit = true;
                } catch (InputMismatchException e) {
                    System.out.println(e.getMessage());
                }
            }
            
            getRoom.setInt(1, roomSelection);
            // execute the preparedstatement
            resultSet = getRoom.executeQuery();

            //declare availabilty so I can reference it
            //outside the loop and halt the case if the
            //room selection is already booked
            String availability = "";

            boolean emptySet = true;
            while (resultSet.next()) {
                emptySet = false;
                int roomNum = resultSet.getInt("RoomNum");
                type = resultSet.getString("Type");
                availability = resultSet.getString("Availability");

            }
            if (emptySet) {
                System.out.print("\n\nRoom does not exist!\n");
                break;

            }
            if (availability.equalsIgnoreCase("FALSE")) {
                System.out.print("\nRoom already booked!");
                break;
            } else {

                System.out.print("Enter check in date(XX/XX/XXXX format): ");
                String checkIn = console.next();

                System.out.print("Enter check out date(XX/XX/XXXX format): ");
                String checkOut = console.next();
                //remove the quotes from the type that were there
                //for search purposes
                type = type.replace("\"", "");

                bookRoom.setInt(1, roomSelection);
                bookRoom.setString(2, type);
                bookRoom.setString(3, checkIn);
                bookRoom.setString(4, checkOut);
                bookRoom.setInt(5, customerID);
                bookRoom.setDouble(6, 0.00);
                bookRoom.setString(7, "FALSE");

                // execute the preparedstatement
                bookRoom.execute();
                
                System.out.println("Successfully booked room " 
                        + roomSelection + ".");
            }
            exit = true;
        }

    }
    //This function allows employees and admins to get the user's ID before
    //prodeeding tot he payCharge(customerID) function.
    public static void payCharge() throws SQLException {
        int customerID = 0;

        boolean exit = false;

        int userSelection = 0;

        while (!exit) {
            System.out.println("Enter the CustomerID to pay charges "
                    + "(0 to cancel): ");
            try {
                customerID = console.nextInt();
            } catch (InputMismatchException e) {
                System.out.print(e.getMessage());
            }
            exit = true;
        }
        if (customerID != 0) {
            payCharge(customerID);
        }
    }
    //This function allows users to pay charges.
    public static void payCharge(int customerID) throws SQLException {

        getCharges.setInt(1, customerID);

        // execute the preparedstatement
        resultSet = getCharges.executeQuery();

        boolean emptySet = true;
        
        int roomNum = 0;
        //This loop will print all records for the customer
        while (resultSet.next()) {

            emptySet = false;
            int creditCard = resultSet.getInt("CreditCard");
            double amount = resultSet.getDouble("Amount");
            roomNum = resultSet.getInt("RoomNum");
            int employeeID = resultSet.getInt("EmployeeID");
            customerID = resultSet.getInt("CustomerID");

            if (amount == 0.00) {
                emptySet = true;
            }

            System.out.print("\nCard Number: " + creditCard);
            System.out.print("\nRoom Number: " + roomNum);
            System.out.print("\nAmount: " + amount);
            System.out.print("\nEmployee ID: " + employeeID);
            System.out.print("\nCustomer ID: " + customerID);

        }
        if (emptySet) {
            System.out.print("\n\nYou have no "
                    + "unpaid charges!\n");
        }
        //If we enter this "else" then we have unpaid charges for this customer
        else {
            boolean exit = false;
            int numberSelection = 0;
            String checkIn = "";
            while (!exit) {
                System.out.print("\nEnter the room number "
                        + "you want to pay for: ");
                try {
                    numberSelection = console.nextInt();
                    if (roomNum != numberSelection) {
                        throw new InputMismatchException("You did not stay in "
                                + "that room");
                    }
                    exit = true;
                }
                catch (InputMismatchException e) {
                    System.out.println(e.getMessage());
                }
                
                System.out.print("\nEnter the check in date: ");
                
                try {
                    checkIn = console.next();
                    exit = true;
                }
                catch (InputMismatchException e) {
                    System.out.println(e.getMessage());
                }
                
                payCharges.setInt(1, numberSelection);
                payCharges.setInt(2, customerID);
                payCharges.setString(3, checkIn);
                payCharges.executeUpdate();
                
                resetChargeAmount.setInt(1, numberSelection);
                resetChargeAmount.setInt(2,customerID);
                resetChargeAmount.executeUpdate();
                
                System.out.println("Your charge has been paid.");
            }
        }

    }

    public static void cancelBooking() throws SQLException {
        int customerID = 0;

        boolean exit = false;

        int userSelection = 0;

        while (!exit) {
            System.out.print("\nEnter the CustomerID to cancel a booking "
                    + "(0 to cancel): ");
            try {
                customerID = console.nextInt();
            } catch (InputMismatchException e) {
                System.out.print(e.getMessage());
            }
            exit = true;
        }
        if (customerID != 0) {
            cancelBooking(customerID);
        }
    }

    public static void cancelBooking(int customerID) throws SQLException {

        boolean exit = false;

        int userSelection = 0;

        checkBookings.setInt(1, customerID);

        resultSet = checkBookings.executeQuery();
        
        ArrayList<Integer> bookedRooms = new ArrayList<>();

        boolean emptySet = true;

        while (resultSet.next()) {

            emptySet = false;
            int roomNum = resultSet.getInt("RoomNum");
            String type = resultSet.getString("Type");
            String checkIn = resultSet.getString("CheckInDate");
            String checkOut = resultSet.getString("CheckOutDate");
            customerID = resultSet.getInt("CustomerID");
            System.out.print("\nRoom Number: " + roomNum);
            System.out.print("\nType: " + type);
            System.out.print("\nCheck-in date: " + checkIn);
            System.out.print("\nCheck-out date: " + checkOut);
            System.out.print("\nCustomer ID: " + customerID);
            System.out.println();
            
            bookedRooms.add(roomNum);
            
        }
        if (emptySet) {
            System.out.print("\n\nYou have no "
                    + "rooms booked!\n");

        } else {

            int selection = 0;
            while (selection == 0) {
                
                System.out.print("\nEnter the room number you want to cancel: ");
                
                try {
                selection = console.nextInt();
                if (!bookedRooms.contains(selection)) {
                    throw new InputMismatchException("You have no booking "
                            + "for that room number.");
                }
                
                }
                catch (InputMismatchException e) {
                    System.out.println(e.getMessage());
                    selection = 0;
                }
                
            }
            
            String checkIn = null;
            getCheckInDate.setInt(1, customerID);
            getCheckInDate.setInt(2, selection);
            resultSet = getCheckInDate.executeQuery();
            resultSet.next();
            String correctCheckIn = resultSet.getString("CheckInDate");
            
            while (checkIn == null) {

                System.out.print("\nEnter the check-in date to confirm: ");
                try {
                    checkIn = console.next();
                    

                    if (checkIn.equalsIgnoreCase(correctCheckIn)) {
                        System.out.println("The booking for room " + selection + " has "
                                + "been canceled.\n");
                    }
                    else {
                        throw new InputMismatchException("Incorrect check-in date.");
                    }
                    
                } catch (InputMismatchException e) {
                    System.out.println(e.getMessage());
                    checkIn = null;
                }

            }
            cancelBooking.setInt(1, selection);
            cancelBooking.setInt(2, customerID);
            cancelBooking.setString(3, checkIn);

            cancelBooking.execute();
            
        }

    }

    public static void checkIn() throws SQLException {
        int customerID = 0;

        boolean exit = false;

        int userSelection = 0;

        while (!exit) {
            System.out.println("\nEnter CustomerID: ");
            try {
                customerID = console.nextInt();

            } catch (InputMismatchException e) {
                System.out.print(e.getMessage());
            }
        }

        checkIn(customerID);
    }

    public static void checkIn(int customerID) {
        System.out.println("Check in is not yet implemented");
    }

    public static void checkOut() throws SQLException {
        int customerID = 0;

        boolean exit = false;

        int userSelection = 0;

        while (!exit) {

            try {

            } catch (InputMismatchException e) {
                System.out.print(e.getMessage());
            }
        }

        payCharge(customerID);
    }

    public static void checkOut(int customerID) {
        System.out.println("You can check out anytime you like\nBut you can"
                + "never leave");
    }

    private static void addCharge() throws SQLException {
        int customerID = 0;

        boolean exit = false;

        int userSelection = 0;

        while (!exit) {
            System.out.println("Enter the CustomerID to add a charge "
                    + "(0 to cancel): ");
            try {
                customerID = console.nextInt();
            } catch (InputMismatchException e) {
                System.out.print(e.getMessage());
            }
            exit = true;
        }

        if (customerID != 0) {
            addCharge(customerID);
        }
    }

    private static void addCharge(int customerID) throws SQLException {

        int roomNum;
        String checkIn;
        getRecordsCharge.setInt(1, customerID);

        resultSet = getRecordsCharge.executeQuery();
        boolean emptySet = true;

        while (resultSet.next()) {

            emptySet = false;
            roomNum = resultSet.getInt("RoomNum");
            String type = resultSet.getString("Type");
            checkIn = resultSet.getString("CheckInDate");
            String checkOut = resultSet.getString("CheckOutDate");
            customerID = resultSet.getInt("CustomerID");
            System.out.print("\nRoom Number: " + roomNum);
            System.out.print("\nType: " + type);
            System.out.print("\nCheck-in date: " + checkIn);
            System.out.print("\nCheck-out date: " + checkOut);
            System.out.print("\nCustomer ID: " + customerID);

        }
        if (emptySet) {
            System.out.print("\n\nCustomer "
                    + "has no unpaid charges!\n");

        }

        getCharges.setInt(1, customerID);

        // execute the preparedstatement
        resultSet = getCharges.executeQuery();

        emptySet = true;

        while (resultSet.next()) {

            emptySet = false;
            int creditCard = resultSet.getInt("CreditCard");
            double amount = resultSet.getDouble("Amount");
            roomNum = resultSet.getInt("RoomNum");
            int employeeID = resultSet.getInt("EmployeeID");
            customerID = resultSet.getInt("CustomerID");

        }
        if (emptySet) {
            System.out.print("\n\nNo Record Found!\n");

            System.out.print("\n\nEnter the credit "
                    + "card number: ");
            int card = console.nextInt();

            System.out.print("\nEnter the charge amount: ");
            double amount = console.nextInt();

            System.out.print("\nEnter the room "
                    + "number you are charging for: ");
            roomNum = console.nextInt();

            createCharge.setInt(1, card);
            createCharge.setDouble(2, amount);
            createCharge.setInt(3, roomNum);
            createCharge.setInt(4, 0);
            createCharge.setInt(5, customerID);

            // execute the preparedstatement
            createCharge.execute();

        }

        System.out.print("\nEnter the room number "
                + "you are charging for: ");
        roomNum = console.nextInt();

        System.out.print("\nEnter the amount: ");
        double amount = console.nextInt();

        System.out.print("Enter the check-in date "
                + "(XX/XX/XXXX format): ");
        checkIn = console.next();

        setCharge.setDouble(1, amount);
        setCharge.setInt(2, roomNum);
        setCharge.setInt(3, customerID);

        setCharge.executeUpdate();

        setRecordsCharge.setDouble(1, amount);
        setRecordsCharge.setInt(2, customerID);
        setRecordsCharge.setInt(3, roomNum);
        setRecordsCharge.setString(4, checkIn);

        setRecordsCharge.executeUpdate();

    }//end addCharge

    private static void initializePreparedStatements() throws SQLException {

        //Create the checkCustomerLogin statement
        String query = "SELECT * from Customers WHERE CustomerID = ?";
        checkCustomerLogin = connect.prepareStatement(query);
        //Create the checkEmployeeLogin statement
        query = "SELECT * from Employees WHERE EmployeeID = ?";
        checkEmployeeLogin = connect.prepareStatement(query);
        //Create the checkAdminLogin statement
        //query = "";
        //checkAdminLogin = connect.prepareStatement(query);
        //Set up the add customer statement
        query = " INSERT INTO Customers (CustomerID, "
                + "fName, lName, Address, PhoneNum,"
                + "ZipCode, City, State)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?)";
        addCustomer = connect.prepareStatement(query);
        //Set up the addEmployee statement
        //query = "";
        //addEmployee = connect.prepareStatement(query);
        //Set up the checkBookings statement
        query = "SELECT * from Records WHERE CustomerID = ?";
        checkBookings = connect.prepareStatement(query);
        //Create the deleteBookings statement
        query = "DELETE FROM Records WHERE RoomNum = ? "
                + "AND CustomerID = ? AND CheckInDate = ?";
        cancelBooking = connect.prepareStatement(query);
        //Create the bookRoom statement
        query = "INSERT INTO Records (RoomNum, "
                + "Type, CheckInDate, CheckOutDate, CustomerID, "
                + "Charge, Paid) "
                + "values (?, ?, ?, ?, ?, ?, ?)";
        bookRoom = connect.prepareStatement(query);
        //Create the payCharges statement
        query = "UPDATE Records SET Paid = \"TRUE\""
                + " WHERE RoomNum = ? AND CustomerID = ? AND CheckInDate = ?";
        payCharges = connect.prepareStatement(query);
        //Reset charge amount
        query = "UPDATE Charges SET Amount = 0"
                + " WHERE RoomNum = ? AND CustomerID = ? AND Amount <> 0";
        resetChargeAmount = connect.prepareStatement(query);
        //Set up the addCharge statement
        query = "UPDATE Charges set Amount = ?, RoomNum = ? WHERE CustomerID = ?";
        setCharge = connect.prepareStatement(query);
        //Set up the getAvailableRooms statement
        query = "SELECT * FROM Rooms WHERE Type = ? AND Availability = \"TRUE\"";
        getAvailableRooms = connect.prepareStatement(query);
        //Get a specific room
        query = "SELECT * FROM Rooms WHERE RoomNum = ?";
        getRoom = connect.prepareStatement(query);
        query = "SELECT * FROM Records WHERE CustomerID = ? "
                + "AND Paid = \"FALSE\"";
        getRecordsCharge = connect.prepareStatement(query);
        query = "SELECT * FROM Charges WHERE CustomerID = ?";
        getCharges = connect.prepareStatement(query);
        query = " INSERT into Charges (CreditCard, "
                + "Amount, RoomNum, EmployeeID, CustomerID)"
                + " values (?, ?, ?, ?, ?)";
        createCharge = connect.prepareStatement(query);
        query = "UPDATE Records set Charge = ? WHERE "
                + "CustomerID = ? AND RoomNum = ? AND CheckInDate = ?";
        setRecordsCharge = connect.prepareStatement(query);
        query = "SELECT * FROM Records INNER JOIN Customers "
                + "ON Records.CustomerID = Customers.CustomerID "
                + "WHERE Customers.fName= ? AND Customers.lName = ? "
                + "AND Records.Paid = \"FALSE\"";
        getRoomByCustomerName = connect.prepareStatement(query);
        query = "SELECT * FROM Records "
                + "WHERE CustomerID = ? AND RoomNum = ?";
        getCheckInDate = connect.prepareStatement(query);

    }//end initialize prepared statements

    public static void adminMenu() {

        System.out.print("\n\nWhat would you like to do?\n"
                + "1 -- Edit a Date\n"
                + "2 -- Add charge\n"
                + "3 -- Check Available Rooms\n"
                + "4 -- Cancel Booking\n"
                + "5 -- Check Booking\n"
                + "6 -- Find record by Customer Name\n"
                + "0 -- to stop\n"
                + "Enter a command: ");

    }//end admin menu

    public static String PassWordGenerator(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }//endPassWordGenerator
    
    public static void findRecordsByName() throws SQLException {
        
        String fName = "";
        String lName = "";
        
        boolean exit = false;
        
        while(!exit) {
            
            System.out.println("\n To search for a customer's active records, "
                    + "enter their name below.");
            System.out.print("\nFirst Name: ");
            
            try {
                fName = console.next();
            }
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
            System.out.print("\nLast Name: ");
            try {
                lName = console.next();
            }
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("\n");
            exit = true;
        }
        
        getRoomByCustomerName.setString(1, fName);
        getRoomByCustomerName.setString(2, lName);
        
        if(DEBUG) {
            System.out.println("Calling getRoomByCustomerName with fName = "
                    + fName
                    + " and lName = " + lName);
        }
        
        resultSet = getRoomByCustomerName.executeQuery();
        
        boolean emptySet = true;

        while (resultSet.next()) {

            emptySet = false;
            int roomNum = resultSet.getInt("RoomNum");
            String type = resultSet.getString("Type");
            String checkIn = resultSet.getString("CheckInDate");
            String checkOut = resultSet.getString("CheckOutDate");
            int customerID = resultSet.getInt("CustomerID");
            System.out.print("\nRoom Number: " + roomNum);
            System.out.print("\nType: " + type);
            System.out.print("\nCheck-in date: " + checkIn);
            System.out.print("\nCheck-out date: " + checkOut);
            System.out.print("\nCustomer ID: " + customerID);
            System.out.println();

        }
        if (emptySet) {
            System.out.print("\n\nThis Customer has no active bookings!\n");
        }
        
    }

}//end class
