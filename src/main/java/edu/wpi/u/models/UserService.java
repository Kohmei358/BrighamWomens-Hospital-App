package edu.wpi.u.models;

import edu.wpi.u.App;
import edu.wpi.u.database.Database;
import edu.wpi.u.database.UserData;
import edu.wpi.u.users.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class UserService {

    static UserData ud;

    ArrayList<Employee> employees = new ArrayList<>();
    ArrayList<Guest> guests = new ArrayList<>();
    ArrayList<Patient> patients = new ArrayList<>();

    public String typedUsername;

    User activeUser;
    Guest activeGuest;

    //TODO : Add getEmps, getGuests
    public UserService() {
        ud = new UserData();
        this.setEmployees();
        this.setGuests();
        this.setPatients();
    }

    public String getTypedUsername() {
        return typedUsername;
    }

    public void setTypedUsername(String typedUsername) {
        this.typedUsername = typedUsername;
    }

    public ObservableList<User> getUsers(){
        ObservableList<User> result = FXCollections.observableArrayList();
        result.addAll(ud.getEmployees());
        result.addAll(ud.getPatients());
        result.addAll(ud.getGuests());
        return result;
    }

    /**
     * Constructor that takes in a URL to create test database
     * SHOULD BE SAME AS NO ARGS except where instance of ud is created
     * @param testURL - URL of testDB passed down from testing class
     */
    public UserService(String testURL){
        ud = new UserData(testURL);
        this.setEmployees();
        this.setGuests();
    }

    /**
     * Sets the list of patients
     */
    public void setPatients(){this.patients = ud.getPatients();}

    /**
     * Sets the list of employees
     */
    public void setEmployees() {this.employees = ud.getEmployees();}

    /**
     * Sets the list of guests
     */
    public void setGuests() {this.guests = ud.getGuests();}

    /**
     * This function if for debugging purposes and assumes the Guest in already in the database
     * Sets the active user to a guest
     * @param name name of guest
     */
    public void setGuest(String name){
        this.activeGuest = ud.setGuest(name);
        this.activeUser = ud.setGuest(name);
    }

    /**
     * Sets the patient based on an id
     * @param patientID patient id
     */
    public void setPatient(String patientID){
        this.activeUser = ud.setPatient(patientID);
    }

    /**
     * Sets the employee based on an id
     * @param employeeID the id
     */
    public void setEmployee(String employeeID){
        this.activeUser = ud.setEmployee(employeeID);
    }

    /**
     * Sets the active user of the application
     * @param username username of the user
     * @param password password of the user
     * @param tableName Employees or Guests (table name)
     */
    public void setUser(String username, String password, String tableName) {
        if (tableName.equals("Employees")){
            this.activeUser = ud.setEmployee(username, password);
        }
        else if (tableName.equals("Patients")){
            this.activeUser = ud.setPatient(username, password);
        }
        else {
            this.activeGuest = ud.setGuest(username);
            this.activeUser = ud.setGuest(username);
        }
    }

    /**
     * Gets an instance of the active user
     * @return active user
     */
    public User getActiveUser() {
        return this.activeUser;
    }

    /**
     * Gets a list of all the patients
     * @return list of patients
     */
    public ArrayList<Patient> getPatients(){
        return patients;
    }

    /**
     * Gets a hashmap of employees based on a certain type
     * @param type the type
     * @return the hashmap of employee names
     */
    public HashMap<String, String> getEmployeeIDByType(String type){
        return ud.getEmployeeNamesByType(type);
    }

    /**
     * Gets a list of all of the employees
     * @return list of employees
     */
    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    /**
     * Gets a list of all the guests
     * @return list of guests
     */
    public ArrayList<Guest> getGuests() {
        return guests;
    }

    /**
     * Changes the phone number of a user
     * @param userID the id
     * @param newPhoneNumber the new phone number
     * @param type Employees or Patients (table name)
     */
    public void changePhoneNumber(String userID, String newPhoneNumber, String type){
        this.getActiveUser().setPhoneNumber(newPhoneNumber);
        ud.changePhoneNumber(userID,newPhoneNumber, type);
    }

    /**
     * Changes the email of the user
     * @param userID id of the user
     * @param newEmail the new email
     * @param type Employees or Patients (table name)
     */
    public void changeEmail(String userID, String newEmail, String type){
        this.getActiveUser().setEmail(newEmail);
        ud.changeEmail(userID,newEmail,type);
    }

    /**
     * Changes the password of the user
     * @param username username of the user
     * @param newPassword the new password
     * @param type Employees or Patients (table name)
     */
    public void changePassword(String username, String newPassword, String type){
        if(this.getActiveUser() != null){
            this.getActiveUser().setPassword(newPassword);
        }
        ud.changePassword(username,newPassword, type);
    }

    /**
     * Validates a username
     * @param username the username to be validated
     */
    public String checkUsername(String username) {
        return ud.checkUsername(username);
    }

    /**
     * Validates a password
     * @param password the password to be validated
     */
    public String checkPassword(String password, String userName) {
        return ud.checkPassword(password, userName);
    }

    /**
     * Validates the phone number of a given username
     * @param username the username to be validated
     * @return the phonenumber of the username
     */
    public String getPhoneNumberFromUserName(String username) {
        return ud.getPhoneNumberFromUserName(username);
    }

    /**
     * Checks a phonenumber
     * @param phonenumber the phonenumber
     * @return true if the number exists
     */
    public boolean checkPhoneNumber (String phonenumber) {return ud.checkPhonenumber(phonenumber);}

    /**
     * Checks a email
     * @param email the email
     * @return true if the number exists
     */
    public boolean checkEmail(String email) { return ud.checkEmail(email);}

    /**
     * Adds a patient to list and calls database
     * @param name the name
     * @param userName the username
     * @param password the password
     * @param email the email
     * @param role the role
     * @param phonenumber the phonenumber
     * @param deleted whether or not they're deleted
     * @param appointments list of appointments
     * @param providerName insurance provider name
     * @param parkingLocation parking location most recent visit
     * @param recommendedParkingLocation recommended parking location for next visit
     */
    public void addPatient(String name, String userName, String password, String email, Role role, String phonenumber, boolean deleted, ArrayList<Appointment> appointments,String providerName, String parkingLocation,String recommendedParkingLocation){
        Random rand = new Random();
        int patientID = Math.abs(rand.nextInt());
        String id = Integer.toString(patientID);
        // todo: check
        Patient patient = new Patient(id,name,userName,password,email,role,phonenumber,deleted, appointments, providerName, parkingLocation, recommendedParkingLocation);
        ud.addPatient(patient);
        this.patients.add(patient);
    }

    /**
     * Adds a singular appointment
     * @param appointment the appointment
     */
    public void addAppointment(Appointment appointment){
        Random rand = new Random();
        int appointmentID = rand.nextInt();
        String id = Integer.toString(appointmentID);
        //String appointmentID, String patientID, String employeeID, Timestamp appointmentDate, String appointmentType
        Appointment appointment1 = new Appointment(id, appointment.getPatientID(), appointment.getEmployeeID(), appointment.getAppointmentDate(), appointment.getAppointmentType());
        ud.addAppointment(appointment1);
    }

    /**
     * Add a parking location to a patient
     * @param patientID patient id
     * @param parkingLocation parking location
     */
    public void addParkingLocation(String patientID, String parkingLocation){
        ud.addPatientParkingLocation(patientID, parkingLocation);
    }

    /**
     * Add a recommended parking location to a patient
     * @param patientID patient id
     * @param recommendedParkingLocation recommended parking location
     */
    public void addRecommendedParkingLocation(String patientID, String recommendedParkingLocation){
        ud.addPatientRecommendedParkingLocation(patientID, recommendedParkingLocation);
    }

    /**
     * Adds an employee to list and calls database
     * @param name the name
     * @param userName the username
     * @param password the password
     * @param type the type (Stafftype)
     * @param deleted whether or not the user is deleted
     * @param phoneNumber the phonenumber
     * @param email the email
     */
    public void addEmployee(String name, String userName, String password, String email, Role type, String phoneNumber, boolean deleted){
        Random rand = new Random();
        int employeeID = Math.abs(rand.nextInt());
        String id = Integer.toString(employeeID);
        //String userID, String name, String accountName, String password, String email, Role type, String phoneNumber, boolean deleted
        Employee newEmployee = new Employee(id,name,userName,password,email, type, phoneNumber, deleted);
        ud.createEmployee(newEmployee); // Fixed: was trying to add the employee in the print statement, and call to addEmployee
        this.employees.add(newEmployee);
    }

    /**
     * Adds an guest to list and calls database
     * @param name the name
     * @param deleted whether or not the user is deleted
     */
    public void addGuest(String name, Timestamp visitDate, String visitReason, boolean deleted){
        Random rand = new Random();
        int employeeID = Math.abs(rand.nextInt());
        String id = Integer.toString(employeeID);
        Guest newGuest = new Guest(id, name, Role.GUEST, visitDate, visitReason, deleted);
        ud.addGuest(newGuest);
        this.guests.add(newGuest);
    }

    /**
     * Removes employee from list and calls database
     * @param employeeID id of employee
     * @return empty string if success, employeeID on failure
     */
    public String deleteEmployee(String employeeID) {
        for (Employee e : this.employees) {
            if (e.getUserID().equals(employeeID)) {
                this.employees.remove(e);
                //this.users.remove(e);
                ud.delEmployee(e);
                return "";
            }
            return "";
        }
        return employeeID;
    }

    /**
     * Removes guest from list and calls database
     * @param guestID id of guest
     * @return empty string if success, guestID on failure
     */
    public String deleteGuest(String guestID) {
        for (Guest g : this.guests) {
            if (g.getGuestID().equals(guestID)) {
                this.guests.remove(g);
                //this.users.remove(g);
                ud.delGuest(g);
                return "";
            }
            return "";
        }
        return guestID;
    }

    /**
     * Updates the list of employees and calls database
     * @param employee the updated employee
     * @return "" on success and the id on failure
     */
    public String updateEmployee(Employee employee){
        for(Employee e : this.employees){
            if(e.getUserID().equals(employee.getUserID())){
                e.editUser(employee.getName(), employee.getUserName() ,employee.getPassword(),employee.getEmail(),employee.getType(), employee.getPhoneNumber(), employee.isDeleted());
                ud.updEmployee(e);
                return "";
            }
        }
        return employee.getUserID();
    }

    /**
     * Updates the list of guests and calls database
     * @param guestID the id
     * @param name the name
     * @param visitDate the visit date
     * @param visitReason the visit reason
     * @param deleted whether or not the user is deleted
     * @return "" on success and the id on failure
     */
    public String updateGuest(String guestID, String name, Timestamp visitDate, String visitReason, boolean deleted){
        for(Guest g : this.guests){
            if(g.getGuestID().equals(guestID)){
                g.editGuest(name, visitDate, visitReason, deleted);
                ud.updGuest(g);
                return "";
            }
        }
        return guestID;
    }

    /**
     * Updates the list of patients and calls database
     * @param patient the updated patient
     * @return "" on success, the patientID on failure
     */
    public String updatePatient(Patient patient){
        for (Patient p : this.patients){
            if (p.getUserID().equals(patient.getUserID())){
                p.editPatient(patient.getName(),patient.getUserName(),patient.getPassword(),patient.getEmail(),patient.getType(),patient.getPhoneNumber());
                ud.updPatient(p);
                return "";
            }
        }
        return patient.getUserID();
    }

    /**
     * Returns the email associated with the given userName
     * @param userName the userName
     * @return the email or "" if no email found/username doesnt exist
     */
    public String getEmail(String userName){
        for (Employee e : this.employees){
            if (e.getUserName().equals(userName)){
                return e.getEmail();
            }
        }
        return "";
    }
    
    /**
     * Sets the preferred method of a given user by userName
     * @param userName the username of the user
     * @param method the method to be set-> Nothing or Both or SMS or Email
     */
    public void setPreferredContactMethod(String userName, String method){
        ud.setPreferredContactMethod(userName,method);
    }

    /**
     * Gets a preferred contact method
     * @param userName the username to get the method from
     * @return the method of contact either Nothing or Both or SMS or Email
     */
    public String getPreferredContactMethod (String userName){
        return ud.getPreferredContactMethod(userName);
    }

    /**
     * Changes the app theme
     * @param themeName the name of the theme
     */
    public void changeTheme(String themeName) {
        File f = new File(String.valueOf(Paths.get("themes.txt")));
        if (f.delete()) {
            System.out.println("File deleted when saving");
            try {
                FileWriter fw = new FileWriter("themes.txt");
                fw.write(themeName);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
