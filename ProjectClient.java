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

    public static void main(String[] args) throws Exception {

        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int customerID = 0;
        int customerAnswer = 0;
        String query = "";
        int roomNum = 0;
        PreparedStatement preparedStmt = null;

        Class.forName("com.mysql.jdbc.Driver");

        connect = DriverManager.getConnection("jdbc:mysql://mrbartucz.com/vf0975bh_MidtermProject?user=vf0975bh&password=vf0975bh");

        boolean flag = true;
        String userCommand;
        Scanner console = new Scanner(System.in);

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

                            } else {
                                System.out.print("Invalid input, try again.");
                                //initialise the customer answer 
                                customerAnswer = 0;

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

                                query = "SELECT * FROM Records WHERE CustomerID = ?";
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setInt(1, customerID);

                                // execute the preparedstatement
                                resultSet = preparedStmt.executeQuery();

                                while (resultSet.next()) {

                                    roomNum = resultSet.getInt("RoomNum");
                                    String type = resultSet.getString("Type");
                                    String checkIn = resultSet.getString("CheckInDate");
                                    String checkOut = resultSet.getString("CheckOutDate");
                                    customerID = resultSet.getInt("CustomerID");
                                    double charge = resultSet.getDouble("Charge");
                                    String paid = resultSet.getString("Paid");
                                    String paidAns = "";
                                    if (paid.equalsIgnoreCase("TRUE")) {
                                        paidAns = "Yes!";

                                    } else {
                                        paidAns = "No!";
                                    }
                                    System.out.print("\n\nCustomer ID: " + customerID);
                                    System.out.print("\nRoom Number: " + roomNum);
                                    System.out.print("\nRoom Type: " + type);
                                    System.out.print("\nCheck-in Date: " + checkIn);
                                    System.out.print("\nCheck-out Date: " + checkOut);
                                    System.out.print("\nCharge: " + charge);
                                    System.out.print("\nPaid in full?: " + paidAns);

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
                                String type = "";
                                if (userSelection == 1) {
                                    type = "\"1Bed\"";
                                } else if (userSelection == 2) {
                                    type = "\"2Bed\"";
                                } else if (userSelection == 3) {
                                    type = "\"3Bed\"";
                                } else if (userSelection == 4) {
                                    type = "\"Suite\"";
                                }

                                query = "SELECT * FROM Rooms "
                                        + "WHERE Availability = \"TRUE\" AND Type = " + type;

                                preparedStmt = connect.prepareStatement(query);
                                // execute the preparedstatement
                                resultSet = preparedStmt.executeQuery();

                                while (resultSet.next()) {
                                    roomNum = resultSet.getInt("RoomNum");
                                    String roomType = resultSet.getString("Type");

                                    System.out.println("\nRoom: " + roomNum + " "
                                            + ":Type " + roomType);

                                }

                                System.out.print("Enter the room number "
                                        + "you want to book: ");
                                int roomSelection = console.nextInt();

                                query = "SELECT * FROM Rooms "
                                        + "WHERE RoomNum = " + roomSelection;
                                preparedStmt = connect.prepareStatement(query);

                                // execute the preparedstatement
                                resultSet = preparedStmt.executeQuery();
                                //declare availabilty so I can reference it
                                //outside the loop and halt the case if the
                                //room selection is already booked
                                String availability = "";

                                boolean emptySet = true;
                                while (resultSet.next()) {
                                    emptySet = false;
                                    roomNum = resultSet.getInt("RoomNum");
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
                                }

                                System.out.print("Enter check in date(XX/XX/XXXX format)");
                                String checkIn = console.next();

                                System.out.print("Enter check out date(XX/XX/XXXX format)");
                                String checkOut = console.next();
                                //remove the quotes from the type that were there
                                //for search purposes
                                type = type.replace("\"", "");
                                query = " insert into Records (RoomNum, "
                                        + "Type, CheckInDate, CheckOutDate, CustomerID,"
                                        + "Charge, Paid)"
                                        + " values (?, ?, ?, ?, ?, ?, ?)";

                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setInt(1, roomSelection);
                                preparedStmt.setString(2, type);
                                preparedStmt.setString(3, checkIn);
                                preparedStmt.setString(4, checkOut);
                                preparedStmt.setInt(5, customerID);
                                preparedStmt.setDouble(6, 0.00);
                                preparedStmt.setString(7, "FALSE");

                                // execute the preparedstatement
                                preparedStmt.execute();

                                query = "update Rooms set Availability = ? "
                                        + "where RoomNum = " + roomSelection;
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setString(1, "FALSE");
                                preparedStmt.executeUpdate();

                                break;
                            case "4":// cancel booking

                                query = "SELECT * FROM Records WHERE CustomerID = ? "
                                        + "AND Paid = \"FALSE\"";
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setInt(1, customerID);

                                resultSet = preparedStmt.executeQuery();
                                emptySet = true;

                                while (resultSet.next()) {

                                    emptySet = false;
                                    roomNum = resultSet.getInt("RoomNum");
                                    type = resultSet.getString("Type");
                                    checkIn = resultSet.getString("CheckInDate");
                                    checkOut = resultSet.getString("CheckOutDate");
                                    customerID = resultSet.getInt("CustomerID");
                                    System.out.print("\nRoom Number: " + roomNum);
                                    System.out.print("\nType: " + type);
                                    System.out.print("\nCheck-in date: " + checkIn);
                                    System.out.print("\nCheck-out date: " + checkOut);
                                    System.out.print("\nCustomer ID: " + customerID);

                                }
                                if (emptySet) {
                                    System.out.print("\n\nYou have no "
                                            + "rooms booked!\n");
                                    break;

                                }

                                System.out.print("\nEnter the room number you want to cancel: ");
                                int selection = console.nextInt();

                                System.out.print("\nEnter the check-in date: ");
                                checkIn = console.next();

                                query = "DELETE FROM Records WHERE RoomNum = ?"
                                        + " AND CustomerID = ? AND CheckInDate = ?";
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setInt(1, selection);
                                preparedStmt.setInt(2, customerID);
                                preparedStmt.setString(3, checkIn);

                                preparedStmt.execute();
                                
                                

                                break;
                            case "5":// pay charge 

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
                                    break;
                                }

                                System.out.print("\nEnter the room number "
                                        + "you want to pay for: ");
                                int numberSelect = console.nextInt();

                                if (roomNum != numberSelect) {
                                    System.out.print("You did not "
                                            + "stay in that room!");
                                    break;
                                }

                                query = "update Records set Paid = ? "
                                        + "where CustomerID =" + customerID + ""
                                        + " AND RoomNum = " + numberSelect;
                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setString(1, "TRUE");
                                preparedStmt.executeUpdate();

                                query = "update Charges set Amount = ?  "
                                        + "where CustomerID = " + customerID + " "
                                        + "AND RoomNum = " + numberSelect;

                                preparedStmt = connect.prepareStatement(query);
                                preparedStmt.setDouble(1, 0.00);
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
                    break;
                case "3"://admin
                    
                   String passWord = PassWordGenerator("Password", "somesalt");
                   
                   System.out.print("\nEnter the admin password: ");
                   String adminPassWd = console.next();
                   
                   String adminEncrypt = PassWordGenerator(adminPassWd, "somesalt");
                   
                   if(passWord.equals(adminEncrypt)){
                       System.out.print("Welcome!");
                       
                   }else{
                      System.out.print("Get out of here hacker!");
                      break;
                   }
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
}//end class
