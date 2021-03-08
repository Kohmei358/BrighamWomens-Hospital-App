package edu.wpi.u;

import edu.wpi.u.algorithms.Edge;
import edu.wpi.u.algorithms.Node;
import edu.wpi.u.database.Data;
import edu.wpi.u.database.Database;
import edu.wpi.u.exceptions.InvalidEdgeException;
import edu.wpi.u.models.*;
import edu.wpi.u.requests.*;
import edu.wpi.u.users.Appointment;
import edu.wpi.u.users.Role;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatabaseTest {

    private static String testURL = "jdbc:derby:testDB";

    private static Database dbTest = Database.getDBTest();

    // Only used for live staging
    private static MapService mapServiceTestStaging = new MapService(testURL);
    private static RequestService requestServiceTest = new RequestService(testURL);
    private static UserService userServiceTest = new UserService(testURL);

    /*
    public void main(String[] args) throws InvalidEdgeException {
        // Permissions for testing
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);

        // Nodes for testing
        Node node1 = new Node("TESTNODE1", 100, 100, "1", "Faulkner", "HALL", "LOOOOONG", "SHORT","U");
        Node node2 = new Node("TESTNODE2", 100, 100, "1", "Faulkner", "HALL", "LOOOOONG", "SHORT","U");
        Node node3 = new Node("TESTNODE3", 100, 100, "1", "Faulkner", "HALL", "LOOOOONG", "SHORT","U");
        Node node4 = new Node("TESTNODE4", 100, 100, "1", "Faulkner", "HALL", "LOOOOONG", "SHORT","U");

        // Edges for testing
        Edge edge1 = new Edge("TESTNODE1_TESTNODE2", node1, node2, roles);
        Edge edge2 = new Edge("TESTNODE3_TESTNODE4", node3, node4, roles);

        // Locations for testing
        ArrayList<String> locations = new ArrayList<String>();
        locations.add("TEST1");
        locations.add("Test2");

        // Date for testing
        Date testDate = new GregorianCalendar(2022, 1, 1).getTime();

        // Stafflist for testing
        ArrayList<String> staffList = new ArrayList<String>();
        staffList.add("PersonName1"); // TODO: does the list of staff represent their names or id's (auto generated)

        // Comment for testing
        Comment testComment = new Comment("Comment 1", "This comment is for testing purposes", "PersonName1", CommentType.PRIMARY, new Timestamp(testDate.getTime()));


        // Request for testing
        ArrayList<String> specificFields = new ArrayList<>();
        specificFields.add("Machine 1");
        specificFields.add("High"); // Is this a valid string for priority??
        SpecificRequest result = new RequestFactory().makeRequest("Maintenance");
        Request newRequest = new Request("TestRequest1", new Timestamp(System.currentTimeMillis()), locations, staffList, testComment);
        result.setRequest(newRequest);
        result.setSpecificData(specificFields);

        mapServiceTestStaging.addNode(node1.getNodeID(),node1.getCords()[0],node1.getCords()[1], node1.getFloor(),node1.getBuilding(),node1.getNodeType(), node1.getLongName(), node1.getShortName());
        mapServiceTestStaging.addNode(node2.getNodeID(),node2.getCords()[0],node2.getCords()[1], node2.getFloor(),node2.getBuilding(),node2.getNodeType(), node2.getLongName(), node2.getShortName());
        mapServiceTestStaging.addNode(node3.getNodeID(),node3.getCords()[0],node3.getCords()[1], node3.getFloor(),node3.getBuilding(),node3.getNodeType(), node3.getLongName(), node3.getShortName());
        mapServiceTestStaging.addNode(node4.getNodeID(),node4.getCords()[0],node4.getCords()[1], node4.getFloor(),node4.getBuilding(),node4.getNodeType(), node4.getLongName(), node4.getShortName());

        userServiceTest.addEmployee("PersonName1", "UserName1", "password1", "email1", Role.ADMIN, "1111111111", "TEST1",false);
        userServiceTest.addEmployee("PersonName2", "UserName2", "password2", "email2", Role.MAINTENANCE, "2222222222", "TEST2",false);
        userServiceTest.addEmployee("PersonName3", "UserName3", "password", "email3", Role.ADMIN, "3333333333", "TEST3",false);

        requestServiceTest.addRequest(result);
        ArrayList<SpecificRequest> testRequests = requestServiceTest.getRd().loadActiveRequests();

    }

    */

    // Testing Maps

    @Test
    public void testNodeFromID() throws InvalidEdgeException {
        Node node1 = new Node("TESTNODE1", 100, 100, "1", "Faulkner", "HALL", "LOOOOONG", "SHORT","U");
        MapService mapServiceTest = new MapService(testURL);
        mapServiceTest.addNode(node1.getNodeID(),node1.getCords()[0],node1.getCords()[1], node1.getFloor(),node1.getBuilding(),node1.getNodeType(), node1.getLongName(), node1.getShortName());
        assertEquals(mapServiceTest.getNodeFromID(node1.getNodeID()).getNodeID(),node1.getNodeID());
    }

    @Test
    public void testEdgeFromID() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        mapServiceTest.addNodeWithID("UWALK00401", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("UWALK00501", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addEdge("UWALK00401_UWALK00501", "UWALK00401", "UWALK00501", roles);
        assertEquals(mapServiceTest.getEdgeFromID("UWALK00401_UWALK00501").getEdgeID(),"UWALK00401_UWALK00501");
    }

    @Test
    public void testAddNode1() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        mapServiceTest.addNodeWithID("NODETEST3", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        assertEquals(mapServiceTest.getNodeFromID("NODETEST3").getNodeID(),"NODETEST3"); // Node was found in db/data structure
    }

    @Test
    public void testAddNode2() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        mapServiceTest.addNode(1,1,"1","Faulkner", "TEST","longname","shortname");
        assertEquals(mapServiceTest.getNodeFromID("UTEST00101").getNodeID(),"UTEST00101"); // Node was found in db/data structure
    }

    @Test
    public void testAddNodeWithID() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        mapServiceTest.addNodeWithID("NODETEST4", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        assertEquals(mapServiceTest.getNodeFromID("NODETEST4").getNodeID(),"NODETEST4"); // Node was found in db/data structure
    }

    @Test
    public void testUpdateNode() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        mapServiceTest.addNodeWithID("NODETEST5", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.updateNode("NODETEST5", 5, 5,"WALK","testy", "testywesty");
        assertEquals(mapServiceTest.getNodeFromID("NODETEST5").getCords()[0],5,0);
    }

    @Test
    public void testDeleteNode() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        mapServiceTest.addNodeWithID("NODETEST6", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        assertEquals(mapServiceTest.deleteNode("NODETEST6"), new ArrayList<Edge>());
    }

    @Test
    public void testAddEdge() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        mapServiceTest.addNodeWithID("NODETEST7", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("NODETEST8", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addEdge("NODETEST7_NODETEST8", "NODETEST7", "NODETEST8", roles);
        assertEquals(mapServiceTest.getEdgeFromID("NODETEST7_NODETEST8").getEdgeID(),"NODETEST7_NODETEST8");
    }

    @Test
    public void testUpdateEdgePermissions() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        mapServiceTest.addNodeWithID("NODETEST9", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("NODETEST10", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addEdge("NODETEST9_NODETEST10", "NODETEST9", "NODETEST10", roles);
        roles.remove(Role.ADMIN);
        roles.add(Role.MAINTENANCE);
        mapServiceTest.updateEdgePermissions("NODETEST9_NODETEST10", roles);
        assertEquals(mapServiceTest.getEdgeFromID("NODETEST9_NODETEST10").getUserPermissions().get(0),Role.MAINTENANCE);
    }

    @Test
    public void testUpdateStartEdge() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        mapServiceTest.addNodeWithID("NODETEST11", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("NODETEST12", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("NODETEST13", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addEdge("NODETEST11_NODETEST12", "NODETEST11", "NODETEST12", roles);
        mapServiceTest.updateStartEdge("NODETEST11_NODETEST12", "NODETEST13");
        assertEquals(mapServiceTest.getEdgeFromID("NODETEST13_NODETEST12").getEdgeID(), "NODETEST13_NODETEST12");
    }

    @Test
    public void testUpdateEndEdge() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        mapServiceTest.addNodeWithID("NODETEST14", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("NODETEST15", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("NODETEST16", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addEdge("NODETEST14_NODETEST15", "NODETEST14", "NODETEST15", roles);
        mapServiceTest.updateEndEdge("NODETEST14_NODETEST15", "NODETEST16");
        assertEquals(mapServiceTest.getEdgeFromID("NODETEST14_NODETEST16").getEdgeID(), "NODETEST14_NODETEST16");
    }

    @Test
    public void testDeleteEdge() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        mapServiceTest.addNodeWithID("NODETEST17", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("NODETEST18", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("NODETEST19", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addEdge("NODETEST17_NODETEST18", "NODETEST17", "NODETEST18", roles);
        mapServiceTest.addEdge("NODETEST18_NODETEST19", "NODETEST18", "NODETEST19", roles);
        mapServiceTest.deleteEdge("NODETEST17_NODETEST18");
        assertEquals(mapServiceTest.getEdges().get(0).getEdgeID(),"NODETEST18_NODETEST19");
    }

    @Test
    public void testGetNodes() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        mapServiceTest.addNodeWithID("NODETEST20", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("NODETEST21", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        assertEquals(mapServiceTest.getNodes().get(0).getNodeID(),"NODETEST20"); // TODO: change, current when run alone
    }

    @Test
    public void testGetEdges() throws InvalidEdgeException {
        MapService mapServiceTest = new MapService(testURL);
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        mapServiceTest.addNodeWithID("NODETEST22", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("NODETEST23", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addNodeWithID("NODETEST24", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        mapServiceTest.addEdge("NODETEST22_NODETEST23", "NODETEST22", "NODETEST23", roles);
        mapServiceTest.addEdge("NODETEST23_NODETEST24", "NODETEST23", "NODETEST24", roles);
        assertEquals(mapServiceTest.getEdges().get(1).getEdgeID(),"NODETEST23_NODETEST24");
    }

    // Testing Requests
    @Test
    public void testAddRequest() {
        RequestService requestServiceTest = new RequestService(testURL);
        Date testDate = new GregorianCalendar(2022, 1, 1).getTime();
        ArrayList<String> staffList = new ArrayList<String>();
        staffList.add("PersonName1");
        Comment testComment = new Comment("Comment 1", "This comment is for testing purposes", "PersonName1", CommentType.PRIMARY, new Timestamp(testDate.getTime()));
        ArrayList<String> locations = new ArrayList<String>();
        locations.add("TEST1");
        ArrayList<String> specificFields = new ArrayList<>();
        specificFields.add("Machine 1");
        specificFields.add("High"); // Is this a valid string for priority??
        SpecificRequest result = new RequestFactory().makeRequest("Maintenance");
        Request newRequest = new Request("TestRequest1", new Timestamp(System.currentTimeMillis()), locations, staffList, testComment);
        result.setRequest(newRequest);
        result.setSpecificData(specificFields);
        requestServiceTest.addRequest(result);
        assertEquals(requestServiceTest.getRequests().get(0).getType(), result.getType());
    }

    @Test
    public void testUpdateRequest() {
        RequestService requestServiceTest = new RequestService(testURL);
        Date testDate = new GregorianCalendar(2022, 1, 1).getTime();
        ArrayList<String> staffList = new ArrayList<String>();
        staffList.add("PersonName1");
        Comment testComment = new Comment("Comment 1", "This comment is for testing purposes", "PersonName1", CommentType.PRIMARY, new Timestamp(testDate.getTime()));
        ArrayList<String> locations = new ArrayList<String>();
        locations.add("TEST1");
        locations.add("TEST2");
        ArrayList<String> specificFields = new ArrayList<>();
        specificFields.add("Machine 1");
        specificFields.add("High"); // Is this a valid string for priority??
        SpecificRequest result = new RequestFactory().makeRequest("Maintenance");
        Request newRequest = new Request("TestRequest1", new Timestamp(System.currentTimeMillis()), locations, staffList, testComment);
        result.setRequest(newRequest);
        result.setSpecificData(specificFields);
        requestServiceTest.addRequest(result);
        Comment testComment2 = new Comment("Comment 2", "This comment is for testing purposes", "PersonName1", CommentType.PRIMARY, new Timestamp(testDate.getTime()));
        Request newRequest2 = new Request("TestRequest1", new Timestamp(System.currentTimeMillis()), locations, staffList, testComment2);
        result.setRequest(newRequest2);
        requestServiceTest.updateRequest(result);
        assertEquals(result.getGenericRequest().getPrimaryComment(), testComment2);
    }

    @Test
    public void testResolveRequest() {
        RequestService requestServiceTest = new RequestService(testURL);
        Date testDate = new GregorianCalendar(2022, 1, 1).getTime();
        ArrayList<String> staffList = new ArrayList<String>();
        staffList.add("PersonName1");
        Comment testComment = new Comment("Comment 1", "This comment is for testing purposes", "PersonName1", CommentType.PRIMARY, new Timestamp(testDate.getTime()));
        ArrayList<String> locations = new ArrayList<String>();
        ArrayList<String> specificFields = new ArrayList<>();
        specificFields.add("Machine 1");
        specificFields.add("High"); // Is this a valid string for priority??
        SpecificRequest result = new RequestFactory().makeRequest("Maintenance");
        Request newRequest = new Request("TestRequest1", new Timestamp(System.currentTimeMillis()), locations, staffList, testComment);
        result.setRequest(newRequest);
        result.setSpecificData(specificFields);
        requestServiceTest.addRequest(result);
        Comment testComment2 = new Comment("Comment 2", "XX", "PersonName1", CommentType.PRIMARY, new Timestamp(testDate.getTime()));
        requestServiceTest.resolveRequest(result, testComment2);
        ArrayList <Comment> listOfComments = new ArrayList<>();
        listOfComments.add(testComment);
        listOfComments.add(testComment2);
        ArrayList <Comment> requestComments = newRequest.getComments();
        assertEquals(requestComments, listOfComments );
    }

    @Test
    public void testGetRequest() {
        RequestService requestServiceTest = new RequestService(testURL);
        Date testDate = new GregorianCalendar(2022, 1, 1).getTime();
        ArrayList<String> staffList = new ArrayList<String>();
        staffList.add("PersonName1");
        Comment testComment = new Comment("Comment 1", "This comment is for testing purposes", "PersonName1", CommentType.PRIMARY, new Timestamp(testDate.getTime()));
        ArrayList<String> locations = new ArrayList<String>();
        ArrayList<String> specificFields = new ArrayList<>();
        specificFields.add("Machine 1");
        specificFields.add("High"); // Is this a valid string for priority??
        SpecificRequest result = new RequestFactory().makeRequest("Maintenance");
        SpecificRequest result2 = new RequestFactory().makeRequest("Language");
        Request newRequest = new Request("TestRequest1", new Timestamp(System.currentTimeMillis()), locations, staffList, testComment);
        Request newRequest2 = new Request("TestRequest2", new Timestamp(System.currentTimeMillis()), locations, staffList, testComment);
        result.setRequest(newRequest);
        result2.setRequest(newRequest2);
        result.setSpecificData(specificFields);
        result2.setSpecificData(specificFields);;
        ArrayList<SpecificRequest> testRequests = new ArrayList<>();
        testRequests.add(result);
        testRequests.add(result2);
        requestServiceTest.addRequest(result);
        requestServiceTest.addRequest(result2);
        assertEquals(requestServiceTest.getRequests(), testRequests);
    }

    // Testing Users
    @Test
    public void testAddPatient() throws InvalidEdgeException {
        UserService userServiceTest = new UserService(testURL);
        MapService mapServiceTest = new MapService(testURL);
        mapServiceTest.addNodeWithID("NODETEST25", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        ArrayList<Appointment> tempList = new ArrayList<>();
        userServiceTest.addPatient("Name1", "Name1Username", "Name1Pass", "Name1Email", Role.PATIENT, "Name1Phone", "NODETEST25", false, tempList, "ProviderName", "CurrentParking", "RecommendedParking");
        assertEquals(userServiceTest.getPatients().get(0).getName(), "Name1");
    }

    @Test
    public void testAddLocationID() {
        // TODO: Might delete, not necessary to check for in userService
    } // TODO: wait to test

    @Test
    public void testAddParkingLocation(){} // TODO: wait to test

    @Test
    public void testAddRecommendedParkingLocation(){} // TODO: wait to test

    @Test
    public void testAddEmployee() throws InvalidEdgeException {
        UserService userServiceTest = new UserService(testURL);
        MapService mapServiceTest = new MapService(testURL);
        mapServiceTest.addNodeWithID("NODETEST26", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        ArrayList<Appointment> tempList = new ArrayList<>();
        userServiceTest.addEmployee("Name2", "Name2Username", "Name2Pass", "Name2Email", Role.NURSE, "Name2Phone", "NODETEST26", false);
        assertEquals(userServiceTest.getEmployees().get(0).getName(), "Name2");
    }

    @Test
    public void testAddGuest() throws InvalidEdgeException {
        UserService userServiceTest = new UserService(testURL);
        ArrayList<Appointment> tempList = new ArrayList<>();
        Timestamp t = new Timestamp(1);
        userServiceTest.addGuest("Name3", t, "For testing... ha ha, I'm tired",false);
        assertEquals(userServiceTest.getGuests().get(0).getName(), "Name3");
    }

    @Test
    public void testChangePhoneNumber() throws InvalidEdgeException { // TODO: Contains bug?
        UserService userServiceTest = new UserService(testURL);
        MapService mapServiceTest = new MapService(testURL);
        mapServiceTest.addNodeWithID("NODETEST28", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        ArrayList<Appointment> tempList = new ArrayList<>();
        userServiceTest.getPatients().clear();
        userServiceTest.addPatient("Name5", "Name5Username", "Name5Pass", "Name5Email", Role.PATIENT, "Name5Phone", "NODETEST28", false, tempList, "ProviderName", "CurrentParking", "RecommendedParking");
        //userServiceTest.setUser("Name5Username", "Name5Pass", "Patients");
        //userServiceTest.changePhoneNumber(userServiceTest.getPatients().get(0).getUserID(), "Name5NewPhone","Patients");
        //assertEquals(userServiceTest.getPatients().get(0).getPhoneNumber(), "Name5NewPhone");
    }

    @Test
    public void testChangeEmail(){}

    @Test
    public void testChangePassword(){}

    @Test
    public void testCheckUsername(){}

    @Test
    public void testCheckPassword(){}

    @Test
    public void testCheckPhoneNumber(){}

    @Test
    public void testDeleteEmployee() throws InvalidEdgeException {
        UserService userServiceTest = new UserService(testURL);
        MapService mapServiceTest = new MapService(testURL);
        userServiceTest.getEmployees().clear();
        mapServiceTest.addNodeWithID("NODETEST40", 1, 1, "1", "Faulkner", "WALK", "Long", "Short");
        ArrayList<Appointment> tempList = new ArrayList<>();
        userServiceTest.addEmployee("Name8", "Name8Username", "Name8Pass", "Name8Email", Role.NURSE, "Name8Phone", "NODETEST40", false);
        userServiceTest.deleteEmployee(userServiceTest.getEmployees().get(0).getUserID());
        assertEquals(userServiceTest.getEmployees().size(), 0, 0);
    }

    @Test
    public void testDeleteGuest() throws InvalidEdgeException {
        UserService userServiceTest = new UserService(testURL);
        userServiceTest.getGuests().clear();
        userServiceTest.addGuest("Name9", new Timestamp(0), "For testing purposes", false);
        userServiceTest.deleteGuest(userServiceTest.getGuests().get(0).getGuestID());
        assertEquals(userServiceTest.getGuests().size(), 0, 0);
    }

    @Test
    public void testUpdateEmployee(){

    }

    @Test
    public void testUpdateGuest(){}

}