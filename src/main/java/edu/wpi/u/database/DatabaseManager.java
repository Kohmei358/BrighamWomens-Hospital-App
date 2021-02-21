package edu.wpi.u.database;

import edu.wpi.u.algorithms.Node;
import edu.wpi.u.models.GraphManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseManager {

  private static Connection conn = null;

  private static ResultSet rset;
  private static String url = "jdbc:derby:BWdb;user=admin;password=admin;create=true";

  public static void main(String[] args) throws SQLException, IOException {
    //DatabaseManager db = new DatabaseManager();
  }

  public DatabaseManager() {
    driver();
    connect();
    deleteTables();
    init();
    readCSV("src/main/resources/edu/wpi/u/OutsideMapNodes.csv", "Nodes");
    readCSV("src/main/resources/edu/wpi/u/OutsideMapEdges.csv", "Edges");
  }

  public static void readCSV(String filePath, String tableName){

    String tempPath = "src/main/resources/edu/wpi/u/temp.csv";
    String str1 = "CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE ('ADMIN', '" + tableName.toUpperCase() + "', '" + tempPath + "', ', ', null, null,1)";

    try {
      String content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
      String[] columns = content.split("\n", 2);
      String[] attributes = content.split(",");
      columns[1] += "\n";
      File temp = new File(tempPath);
      if(temp.createNewFile()){
        System.out.println("File created");
      }
      System.out.println(temp.exists());
      FileWriter myWriter = new FileWriter(tempPath);
      myWriter.write(columns[1]);
      myWriter.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    //String str1 = "CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE ('ADMIN', 'NODES', 'src/main/resources/edu/wpi/u/temp.csv', ', ', null, null,1)";
    try {
      PreparedStatement p = conn.prepareStatement(str1);
      p.execute();
    }
    catch (Exception e){
      System.out.println("\nTRACE:");
      e.printStackTrace();
      System.out.println("rewrite being weird");
    }
  }
  //moved
  public static void driver() {
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    } catch (Exception e) {
      System.out.println("Driver registration failed");
      e.printStackTrace();
    }
  }
  //moved
  public static void connect() {
    try {
      conn = DriverManager.getConnection(url);
      //conn.setAutoCommit(false);
      //conn.commit();
    } catch (Exception e) {
      System.out.println("Connection failed");
      e.printStackTrace();
    }
  }
  //moved
  public static void init() {
    try {
      if (isTableEmpty()) {
        String tbl1 =
                "create table Nodes (nodeID varchar(50) not null, xcoord int, ycoord int, floor int , building varchar(50), nodeType varchar(4), longName varchar(50), shortName varchar(20), teamAssigned varchar(50), primary key (nodeID))";
        // code for creating table of Museums
        PreparedStatement ps1 = conn.prepareStatement(tbl1);
        ps1.execute();
        String tbl2 =
                "create table Edges (edgeID varchar(50) not null, startID varchar(50), endID varchar(50), primary key(edgeID))";
        PreparedStatement ps2 = conn.prepareStatement(tbl2);
        ps2.execute();
      }
    } catch (SQLException e) {
      System.out.println("Table creation failed");
      e.printStackTrace();
    }
  }
  //moved
  public static void dropValues() {
    try {
      String str = "delete from Nodes";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.execute();
      str = "delete from Edges";
      ps.execute();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
  //moved
  public static boolean isTableEmpty() {
    try {
      DatabaseMetaData dmd = conn.getMetaData();
      ResultSet rs = dmd.getTables(null, "ADMIN", "NODES", null);
      return !rs.next();
      // nowhere to put rs.close
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  //not used
  public static void printNodes() {
    try {
      String str = "select * from Nodes";
      PreparedStatement ps = conn.prepareStatement(str);
      rset = ps.executeQuery();
      while (rset.next()) {
        String nodeID = rset.getString("nodeID");
        int xcoord = rset.getInt("xcoord");
        System.out.println("ID:" + nodeID + " " + "xcoord:" + xcoord + "\n");
      }
      rset.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  //not used
  public static void printEdges() {
    try {
      String str = "select * from Edges";
      PreparedStatement ps = conn.prepareStatement(str);
      rset = ps.executeQuery();
      while (rset.next()) {
        String edgeID = rset.getString("edgeID");
        System.out.println("ID:" + edgeID+ "\n");
      }
      rset.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void saveCSV(String tableName, String filePath, String header){
    File f = new File(filePath);
    if(f.delete()){
      System.out.println("file deleted");
    }
    String str = "CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE ('ADMIN','" + tableName.toUpperCase() + "','" + filePath + "',',',null,null)";
    try {
      PreparedStatement ps = conn.prepareStatement(str);
      ps.execute();
     // ps.close();
    } catch (SQLException throwables) {
      System.out.println("wants new file");
      //throwables.printStackTrace();
    }

    try {
      String content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
      FileWriter fw = new FileWriter(filePath);
      fw.write(header);
      fw.write("\n");
      fw.write(content);
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //moved
  public static void saveNodesCSV() {
    saveCSV("Nodes", "src/main/resources/edu/wpi/u/OutsideMapNodes.csv", "column names");
  }
  //moved
  public static void saveEdgesCSV() {
    saveCSV("Edges", "src/main/resources/edu/wpi/u/OutsideMapEdges.csv", "column names");
  }


  //moved
  public static int addNode(
          String node_id,
          int x,
          int y,
          int floor,
          String building,
          String node_type,
          String longname,
          String shortname) {
    try {
      String str =
              "insert into Nodes (nodeID, xcoord, ycoord, floor, building, nodeType, longname, shortname, teamAssigned) values (?,?,?,?,?,?,?,?,?)";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, node_id);
      ps.setInt(2, x);
      ps.setInt(3, y);
      ps.setInt(4, floor);
      ps.setString(5, building);
      ps.setString(6, node_type);
      ps.setString(7, longname);
      ps.setString(8, shortname);
      ps.setString(9, "u");
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to add node");
      return 0;
    }
    return 1;
  }
  //moved
  public static int updCoords(String node_id, int new_x, int new_y) {
    try {
      String str = "update Nodes set xcoord=?, ycoord=? where nodeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setInt(1, new_x);
      ps.setInt(2, new_y);
      ps.setString(3,node_id);
      //    ps.setString(2, node_id);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to update coordinates");
      return 0;
    }
    return 1;
  }
  //moved
  public static int updFloor(String node_id, int new_floor_number) {
    try {
      String str = "update Nodes set floor=? where nodeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setInt(1, new_floor_number);
      ps.setString(2, node_id);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to update floor #");
      return 0;
    }
    return 1;
  }
  //moved
  public static int updBuilding(String node_id, String new_building) {
    try {
      String str = "update Nodes set building=? where nodeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, new_building);
      ps.setString(2, node_id);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to update building");
      return 0;
    }
    return 1;
  }
  //moved
  public static int updLongname(String node_id, String new_longname) {
    try {
      String str = "update Nodes set longname=? where nodeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, new_longname);
      ps.setString(2, node_id);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to update longname");
      return 0;
    }
    return 1;
  }
  //moved
  public static int updShortname(String node_id, String new_shortname) {
    try {
      String str = "update Nodes set shortname=? where nodeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, new_shortname);
      ps.setString(2, node_id);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to update coordinates");
      return 0;
    }
    return 1;
  }
  //moved
  public static int delNode(String node_id) {
    try {
      String str = "delete from Nodes where nodeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, node_id);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to delete node");
      return 0;
    }
    return 1;
  }
  //moved
  public static int delNodeCoord(int x, int y) {
    try {
      String str = "delete from Nodes where xcoord=? and ycoord=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setInt(1, x);
      ps.setInt(2, y);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to delete node");
      return 0;
    }
    System.out.println("Successful delete");
    return 1;
  }
  //moved
  public static int addEdge(String edge_id, String start_node_id, String end_node_id) {
    try {
      String str = "insert into Edges (edgeId, startID, endID) values (?,?,?)";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, edge_id);
      ps.setString(2, start_node_id);
      ps.setString(3, end_node_id);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to add edge");
      return 0;
    }
    return 1;
  }
  //moved
  public static int updEdgeStart(String edge_id, String new_start_node_id) {
    try {
      String str = "update Edges set startID=? where edgeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, new_start_node_id);
      ps.setString(2, edge_id);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to update start ID");
      return 0;
    }
    return 1;
  }
  //moved
  public static int updEdgeEnd(String edge_id, String new_end_node_id) {
    try {
      String str = "update Edges set endID=? where edgeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, new_end_node_id);
      ps.setString(2, edge_id);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to update end ID");
      return 0;
    }
    return 1;
  }
  //moved
  public static int delEdge(String edge_id) {
    try {
      String str = "delete from Edges where edgeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, edge_id);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to update start ID");
      return 0;
    }
    return 1;
  }
  //moved
  public static int delEdgeByNodes(String start_node_id, String end_node_id){
    try {
      String str = "delete from Edges where startID=?, endID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, start_node_id);
      ps.setString(2, end_node_id);
      ps.execute();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return 0;
    }
    return 1;
  }
  //moved
  public static void loadGraph(GraphManager gm){
    try{
      Statement ps = conn.createStatement();
      String str = "select * from Nodes";
      rset = ps.executeQuery(str);
      while (rset.next()) {
        String id = rset.getString("nodeID");
        int x = rset.getInt("xcoord");
        int y = rset.getInt("ycoord");
        int floor = rset.getInt("floor");
        String building = rset.getString("building");
        String nodeType = rset.getString("nodeType");
        String longName = rset.getString("longName");
        String shortName = rset.getString("shortName");
        gm.makeNode(id,x,y,floor,building,nodeType,longName,shortName,"u");
      }
      String str2 = "select * from Edges";
      PreparedStatement ps2 = conn.prepareStatement(str2);
      ResultSet rs2 = ps2.executeQuery();
      while (rs2.next()){
        String id = rs2.getString("edgeID");
        String start = rs2.getString("startID");
        String end = rs2.getString("endID");
        gm.makeEdge(id,start,end);
      }
      rs2.close();
    }
    catch (Exception e){
      e.printStackTrace();
      System.out.println("Failed to load graph");
    }
  }
  //moved
  public static boolean isNode(String node_id) {
    try {
      String str = "select nodeID from Nodes where nodeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, node_id);
      rset = ps.executeQuery();
      return rset.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
  //moved
  public static boolean isEdge(String edge_id) {
    try {
      String str = "select edgeID from Edges where edgeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ps.setString(1, edge_id);
      rset = ps.executeQuery();
      return rset.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
  //moved
  public static void deleteTables() {
    try {
      String str = "drop table Nodes";
      Statement s = conn.createStatement();
      s.execute(str);
      str = "drop table Edges";
      s.execute(str);
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }
  //moved
  public static Node getNode(String node_id){
    try {
      String str = "select * from Nodes where nodeID=?";
      PreparedStatement ps = conn.prepareStatement(str);
      ResultSet rs = ps.executeQuery();
      Node n = new Node(rs.getString("nodeId"), rs.getInt("xcoord"), rs.getInt("ycoord"), rs.getInt("floor"), rs.getString("building"), rs.getString("node_type"), rs.getString("longname"), rs.getString("shortname"), "u");
      rs.close();
      return n;
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }
  //moved
  public static void stop() {
    saveNodesCSV();
    saveEdgesCSV();
    dropValues();
    deleteTables();
    try{
      conn.close();
    }
    catch (Exception e){
      e.printStackTrace();
    }
    /*
     try {
         DriverManager.getConnection
            ("jdbc:derby:;shutdown=true");
      } catch (SQLException ex) {
         if (((ex.getErrorCode() == 50000) &&
            ("XJ015".equals(ex.getSQLState())))) {
               System.out.println("Derby shut down
                  normally");
         } else {
            System.err.println("Derby did not shut down
               normally");
            System.err.println(ex.getMessage());
         }
      }
   }
     */
  }
}