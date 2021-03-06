package edu.wpi.u.algorithms;

import edu.wpi.u.App;
import edu.wpi.u.users.Role;

import java.util.LinkedList;

public class Node {
  private String nodeID;
  private double xcoord;
  private double ycoord;
  private String floor;
  private String building;
  private String nodeType;
  private String longName;
  private String shortName;
  private String teamAssigned;
  private LinkedList<Edge> edges;
  private LinkedList<Node> adjNodes;
  private boolean walkable = true;
  // full constructor
  public Node(
          String _nodeID,
          double _xcoord,
          double _ycoord,
          String floor,
          String _building,
          String _nodeType,
          String _LongName,
          String _ShortName,
          String _teamAssigned) {
    this.nodeID = _nodeID;
    this.xcoord = _xcoord;
    this.ycoord = _ycoord;
    this.floor = floor;
    this.building = _building;
    this.nodeType = _nodeType;
    this.longName = _LongName;
    this.shortName = _ShortName;
    this.teamAssigned = _teamAssigned;
    this.edges = new LinkedList<>();
    this.adjNodes = new LinkedList<>();
  }

  public Node(Node n){
    this.nodeID = n.getNodeID();
    this.xcoord = n.getCords()[0];
    this.ycoord = n.getCords()[1];
    this.floor = n.getFloor();
    this.building = n.getBuilding();
    this.nodeType = n.getNodeType();
    this.longName = n.getLongName();
    this.shortName = n.getShortName();
    this.teamAssigned = "U";
    this.edges = n.getEdges();
    this.adjNodes = n.whatAreAdjNodes();
  }

  // simple constructor
  public Node(String _nodeID, double _xcoord, double _ycoord) {
    this.nodeID = _nodeID;
    this.xcoord = _xcoord;
    this.ycoord = _ycoord;
    this.edges = new LinkedList<>();
    this.adjNodes = new LinkedList<>();
  }
  public Node() {
    this.edges = new LinkedList<>();
    this.adjNodes = new LinkedList<>();
  }

  public LinkedList<Node> whatAreAdjNodes(){
    return this.adjNodes;
  }
  public double[] getCords() {
    double[] returnMe = {this.xcoord, this.ycoord};
    return returnMe;
  }

  //These are super temporary
  public String getXString(){
    return String.valueOf(this.xcoord);
  }

  //These are super temporary
  public String getYString(){
    return String.valueOf(this.ycoord);
  }

  public void updateCords(double x, double y){
    this.xcoord = x;
    this.ycoord = y;
  }

  public String getNodeID() {
    return this.nodeID;
  }

  public void addAdjNode(Node _node) {
    this.adjNodes.add(_node);
  }

  public void removeAdjNode(Node _node) {
    // needs work? might never get used but should also remove adjNode from the connected node
    this.adjNodes.remove(_node);
  }

  public void addEdge(Edge _edge) {
    this.edges.add(_edge);
  }

  public void removeEdge(Edge _edge) {
    if (this.equals(_edge.getEndNode())) {
      adjNodes.remove(_edge.getStartNode());
    } else {
      adjNodes.remove(_edge.getEndNode());
    }
    this.edges.remove(_edge);
  }

  public LinkedList<Node> getAdjNodes() {
    LinkedList<Node> returnMe = new LinkedList<>();
    for (Node n : this.adjNodes) {
      if (n.walkable() && this.reachableNode(n) && this.hasPermission(n)) returnMe.add(n);
    }
    return returnMe;
  }

  public boolean walkable() {
    return this.walkable;
  }

  public void setWalkable(Boolean value) {
    this.walkable = value;
  }

  private boolean reachableNode(Node n) {
    for (Edge e : this.edges) {
      if (e.isWalkable()) {
        if (e.getEndNode().equals(n) || e.getStartNode().equals(n)) {
          if (e.getEndNode().equals(this) || e.getStartNode().equals(this)) return true;
        }
      }
    }
    return false;
  }

  /**
   * checks to see if the current user has permission to go down an edge
   * @param n
   * @return
   */
  private boolean hasPermission(Node n) {
    for (Edge e : this.edges) {
        if (e.getEndNode().equals(n) || e.getStartNode().equals(n)) {
          if (e.getEndNode().equals(this) || e.getStartNode().equals(this)) {
            if (App.userService.getActiveUser() != null) {
              if (e.getUserPermissions().equals(Role.DOCTOR) && (!App.userService.getActiveUser().getType().equals(Role.PATIENT))) {
                return true;
              } else if (e.getUserPermissions().equals(Role.ADMIN) && App.userService.getActiveUser().getType().equals(Role.ADMIN)) {
                return true;
              } else if (e.getUserPermissions().equals(Role.DEFAULT)) {
                return true;
              } else {
                return false;
              }
            }else if (e.getUserPermissions().equals(Role.DEFAULT)) {
              return true;
            }else{
              return false;
            }
          }
        }
    }
    return false;
  }

  public LinkedList<Edge> getEdges() {
    return edges;
  }

  public String getFloor() {
    return floor;
  }

  public String getBuilding() {
    return building;
  }

  public String getNodeType() {
    return nodeType;
  }

  public String getLongName() {
    return longName;
  }

  public String getShortName() {
    return shortName;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }
}
