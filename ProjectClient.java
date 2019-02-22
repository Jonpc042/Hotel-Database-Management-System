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
import java.sql.Statement;
import java.util.*;

/**
 *
 * @author vf0975bh
 */
public class ProjectClient {
    
    private static Scanner console = new Scanner(System.in);
    
    private static Connection connect = null;
    
    private static Statement statement = null;
    
    private static ResultSet resultSet = null;

    public static void main(String[] args) throws Exception {

        int customerID = 0;

        Class.forName("com.mysql.jdbc.Driver");

        connect = DriverManager.getConnection("jdbc:mysql://mrbartucz.com/vf0975bh_MidtermProject?user=fw1006zs&password=fw1006zs");

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

                            System.out.print("\nEnter 1 for new customer, "
                                    + "Enter 2 for returning customer: ");
                            //console.nextLine is to fix skipping problem
                            console.nextLine();
                            int customerAnswer = console.nextInt();

                            if (customerAnswer == 1) {
                                System.out.print("Enter new ID: ");
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

                                String query = " insert into Customers (CustomerID, "
                                        + "fName, lName, Address, PhoneNum,"
                                        + "ZipCode, City, State)"
                                        + " values (?, ?, ?, ?, ?, ?, ?, ?)";

                                PreparedStatement preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setInt(1, customerID);
                                preparedStmt.setString(2, customerFName);
                                preparedStmt.setString(3, customerLName);
                                preparedStmt.setString(4, customerAddress);
                                preparedStmt.setLong(5, customerPhoneNum);
                                preparedStmt.setInt(6, customerZip);
                                preparedStmt.setString(7, customerCity);
                                preparedStmt.setString(8, customerState);
                                //preparedStmt.

                                // execute the preparedstatement
                                preparedStmt.execute();
                                inputCheck = false;
                            } else if (customerAnswer == 2) {
                                System.out.print("Enter your ID: ");
                                customerID = console.nextInt();
                                statement = connect.createStatement();
                                resultSet = statement.executeQuery("SELECT * FROM Customers "
                                        + "WHERE CustomerID = " + customerID);

                                if (resultSet.next() == false) {
                                    System.out.println("CustomerID not found!");

                                } else {
                                    inputCheck = false;
                                }

                            } else {
                                System.out.print("Invalid input, try again.");
                                //initialise the customer answer 
                                customerAnswer = 0;

                            }
                        } catch (InputMismatchException e) {
                            System.out.print("Invalid input!");

                        }
                    }//end inputCheck while

                    boolean customerFlag = true;
                    String customerCommand;
                    Scanner customerConsole = new Scanner(System.in);

                    while (customerFlag) {
                        customerMenu();

                        customerCommand = customerConsole.next();

                        switch (customerCommand) {

                            case "1"://view profile

                                statement = connect.createStatement();

                                resultSet = statement.executeQuery("SELECT * FROM Customers "
                                        + "WHERE CustomerID = " + customerID);

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
                                    System.out.println("Name: " + firstName);
                                    System.out.println("Number: " + lastName);
                                    System.out.println("Address: " + address);
                                    System.out.println("PhoneNum: " + phoneNum);
                                    System.out.println("Zip Code: " + zipCode);
                                    System.out.println("City: " + city);
                                    System.out.println("State: " + state);
                                }
                                break;
                            case "2"://view booked

                                resultSet = statement.executeQuery("SELECT * FROM Records "
                                        + "WHERE CustomerID = " + customerID);

                                if (resultSet.next() == false) {
                                    System.out.print("No Records Found!");
                                    break;
                                }

                                while (resultSet.next()) {
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

                                }

                                break;
                            case "3"://book room
                                
                                System.out.print("\nWhich type of room"
                                        + " would you like?\n"
                                        + "\nEnter 1 for 1 bed"
                                        + "\nEnter 2 for 2 bed"
                                        + "\nEnter 3 for 3 bed\n"
                                        + "Enter 4 for a suite: ");
                                int userSelection = console.nextInt();
                                String type = null;
                                if(userSelection == 1){
                                    type = "\"1Bed\""; 
                                }else if(userSelection == 2){
                                    type = "\"2Bed\"";
                                }else if (userSelection == 3){
                                    type = "\"3Bed\"";
                                }else if(userSelection == 4){
                                    type = "\"Suite\"";
                                }
                                
                                
                                String roomAvailabiltiy = "TRUE";
                               statement = connect.createStatement();
                                resultSet = statement.executeQuery("SELECT * FROM Rooms WHERE "
                                        + "availability = \"TRUE\" AND type = " + type);
                                while (resultSet.next()) {
                                    int roomNum = resultSet.getInt("RoomNum");
                                    String roomType = resultSet.getString("Type");
                                  
                                   
                                    System.out.println("\nRoom: " + roomNum + " "
                                            + ":Type " + roomType);
                                    
                                   
                                  
                                }
                                break;
                            case "4":// cancel bookingd
                                break;
                            case "5":// pay charge 
                                String query = "update Charges set Amount = ? "
                                        + "where CustomerID =" + customerID;
                                PreparedStatement preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setInt(1, 0);  
                                preparedStmt.executeUpdate();

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
    
    public static void employeeLogin() {

        boolean exit = false;
        try {
            while (!exit) {

                System.out.print("Enter your ID: ");
                String employeeID = console.next();
                statement = connect.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM Employees "
                        + "WHERE EmployeeID = " + employeeID);
                
                if (resultSet.next() == false) {
                    System.out.println("EmployeeID not found!");
                }
                else {
                    exit = true;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    
    public static void employeeMenu() throws SQLException {
        
        boolean exit = false;
        
        String userCommand;
        
        while (!exit) {
            
            System.out.print("\n\nWhat would you like to do?\n"
                + "1 -- Check Booking\n"
                + "2 -- Book Room\n"
                + "3 -- Check In Customer\n"
                + "4 -- Check Out Customer\n"
                + "5 -- Cancel Booking\n"
                + "6 -- Pay Charge\n"
                + "7 -- Add a Charge\n"
                + "0 -- to stop\n"
                + "Enter a command: ");
            
            userCommand = console.next();
            
            switch(userCommand) {
                
                case "1"://check customer booking
                    checkCustomerBookings();
                    break;
                case "2"://Book Room
                    bookRoom();
                    break;
                case "3"://Check In Customer
                    checkIn();
                    break;
                case "4"://Check out Customer
                    checkOut();
                    break;
                case "5"://Cancel Booking
                    cancelBooking();
                    break;
                case "6"://Pay Charge
                    payCharge();
                    break;
                case "7"://Add a charge
                    addCharge();
                    break;
                case "0":
                    break;

            }

        }

    }

    public static void checkCustomerBookings() throws SQLException {

        int customerID = 0;

        boolean exit = false;

        while (!exit) {

            System.out.println("Enter the CustomerID to check bookings: ");

            try {

                customerID = console.nextInt();

            } catch (InputMismatchException e) {

            }

        }
        checkCustomerBookings(customerID);
    }

    public static void checkCustomerBookings(int customerID) throws SQLException {

        resultSet = statement.executeQuery("SELECT * FROM Records "
                + "WHERE CustomerID = " + customerID);

        if (resultSet.next() == false) {
            System.out.print("No Records Found!");
        }

        while (resultSet.next()) {
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

        }
    }

    public static void bookRoom() throws SQLException {

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
                        type = "\"1Bed\"";
                        break;
                    case 2:
                        type = "\"2Bed\"";
                        break;
                    case 3:
                        type = "\"3Bed\"";
                        break;
                    case 4:
                        type = "\"Suite\"";
                        break;
                    default:
                        throw new InputMismatchException("Please enter a valid"
                                + "selection");
                }
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
            
        }

        statement = connect.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM Rooms WHERE "
                + "availability = \"TRUE\" AND type = " + type);
        while (resultSet.next()) {
            int roomNum = resultSet.getInt("RoomNum");
            String roomType = resultSet.getString("Type");

            System.out.println("\nRoom: " + roomNum + " "
                    + ":Type " + roomType);

        }
    }
    
    public static void payCharge() throws SQLException {
        int customerID = 0;
        
        boolean exit = false;
        
        int userSelection = 0;
        
        while(!exit) {
            
            try {
                
            }
            catch (InputMismatchException e) {
                System.out.print(e.getMessage());
            }
        }
        
        payCharge(customerID);
    }

    public static void payCharge(int customerID) throws SQLException {
        String query = "update Charges set Amount = ? "
                + "where CustomerID =" + customerID;
        PreparedStatement preparedStmt = connect.prepareStatement(query);
        preparedStmt.setInt(1, 0);
        preparedStmt.executeUpdate();
    }
    
    public static void cancelBooking() throws SQLException {
        int customerID = 0;
        
        boolean exit = false;
        
        int userSelection = 0;
        
        while(!exit) {
            
            try {
                
            }
            catch (InputMismatchException e) {
                System.out.print(e.getMessage());
            }
        }
        
        cancelBooking(customerID);
    }
    
    public static void cancelBooking(int customerID) {
        
        System.out.println("Cancelling bookings is not yet implemented");
        
    }
    
    public static void checkIn() throws SQLException {
        int customerID = 0;
        
        boolean exit = false;
        
        int userSelection = 0;
        
        while(!exit) {
            
            try {
                
            }
            catch (InputMismatchException e) {
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
        
        while(!exit) {
            
            try {
                
            }
            catch (InputMismatchException e) {
                System.out.print(e.getMessage());
            }
        }
        
        payCharge(customerID);
    }

    public static void checkOut(int customerID) {
        System.out.println("You can check out anytime you like\nBut you can"
                + "never leave");
    }

    private static void addCharge() {
         int customerID = 0;
        
        boolean exit = false;
        
        int userSelection = 0;
        
        while(!exit) {
            
            try {
                
            }
            catch (InputMismatchException e) {
                System.out.print(e.getMessage());
            }
        }
        
        addCharge(customerID);
    }
    
    private static void addCharge(int customerID) {
        System.out.println("We can't add charges yet. Everything is free.");
    }

}//end class
