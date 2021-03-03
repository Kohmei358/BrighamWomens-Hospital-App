package edu.wpi.u;

import com.jfoenix.controls.JFXTabPane;
import edu.wpi.u.database.Database;
import edu.wpi.u.models.*;
import edu.wpi.u.users.Employee;
import edu.wpi.u.users.Guest;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  // Allows for globally accessible instance of app to allow instance based editing
  // (separate info per open application)
  // Can be accessed by all controllers and classes by calling App.getInstance();
  public static App app_instance = null;

  public static int leftMenuScreenNum = 1; //Start on the 1st screen (Path Planning)
  public static SimpleStringProperty leftDrawerRoot = new SimpleStringProperty("/edu/wpi/u/views/LeftDrawerMenu.fxml");
  public static SimpleStringProperty rightDrawerRoot = new SimpleStringProperty("/edu/wpi/u/views/ViewRequest.fxml");//This is where we store what scene the right drawer is in.
  public static boolean isEdtingGuest;
  private static Stage primaryStage;

  // We only ever have one primary stage, each time we switch scenes, we swap this out
  public static Database db = Database.getDB();
  public static UserService userService = new UserService();
  public static MapService mapService = new MapService();
  public static RequestService requestService = new RequestService();
  public static AdminToolStorage AdminStorage = new AdminToolStorage();
  public static PathHandling PathHandling = new PathHandling();
  public static SVGPath pathFindingPath;
  public static SVGPath pathFindingPath2;

  public static String lastSelectedNode;
  public static String nodeField1;
  public static String nodeField2;
  public static String lastSelectedEdge;
  public static String edgeField1;
  public static String edgeField2;
  public static String DFS;
  public static String BFS;
  public static String ASTAR;
  public static JFXTabPane tabPaneRoot;

  public static boolean isLightTheme = true;

  public static Integer lastClickedRequestNumber;
  public static Guest selectedGuest;
  public static Employee selectedEmployee;


  public App(){
    System.out.println("App constructor");
    app_instance = this;
  }

  public static App getInstance(){
    if(app_instance == null){
      app_instance = new App();
    }
    return app_instance;
  }

  @Override
  public void init() {
    System.out.println("Starting Up");
//    Font.loadFont(App.class.getResource("/edu/wpi/u/views/css/Rubik-VariableFont_wght.ttf").toExternalForm(), 12);
  }




//      @Override
//      public void init() throws Exception {
//
//      }

//  Font.loadFont(getClass().getResourceAsStream("/resources/fonts/marck.ttf"), 14);

  @Override
  public void start(Stage stage) throws Exception {
    // App.getPrimaryStage.setScene(scene)
    App.primaryStage = stage; // stage is the window given to us

    //Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/UserLogin.fxml"));

    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/CovidSurveyPage.fxml"));
    Scene scene = new Scene(root);
    App.primaryStage.setScene(scene);
//    Label label = new Label("Hello World");
//    label.setStyle("-fx-font-family: Akaya Telivigala; -fx-font-size: 100;");
//    label.setFont(Font.font("Rubik", FontWeight.NORMAL, 50));
//    Scene scene = new Scene(label);
//    scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Akaya+Telivigala&display=swap");

//    scene.getStylesheets().add("/edu/wpi/u/views/css/LightTheme.css");



//    scene.getStylesheets().add("/edu/wpi/u/views/css/BaseStyle.css");
    App.primaryStage.getScene().getStylesheets().add(getClass().getResource("/edu/wpi/u/views/css/BaseStyle.css").toExternalForm());
    App.primaryStage.getScene().getStylesheets().add(getClass().getResource("/edu/wpi/u/views/css/LightTheme.css").toExternalForm());
    App.primaryStage.setFullScreen(true);
    App.primaryStage.show();


    //Font.loadFont(App.class.getResource("/edu/wpi/u/views/css/Rubik-Regular.ttf").toExternalForm(), 10);

    App.primaryStage.getScene().setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ESCAPE) {
        System.out.println("Escape button pressed, exiting");
        App.getInstance().end();
      }
    });
  }

  public static Stage getPrimaryStage() {
    return primaryStage;
  }

  public void end() {
    System.out.println("Shutting Down");
    requestService.saveCSVFile("Requests.csv", "Requests");
    requestService.saveCSVFile("Assignments.csv", "Assignments");
    requestService.saveCSVFile("Locations.csv", "Locations");
    //TODO: Load Database through CSVs only on first invocation (when its empty)
    //Database.getDB().stop();
//    requestService.saveCSVFile("src/main/resources/edu/wpi/u/Requests.csv", "Requests");
//    requestService.saveCSVFile("src/main/resources/edu/wpi/u/Assignments.csv", "Assignments");
//    requestService.saveCSVFile("src/main/resources/edu/wpi/u/Locations.csv", "Locations");
//    mapService.saveCSVFile("src/main/resources/edu/wpi/u/Nodes.csv", "Nodes");
//    mapService.saveCSVFile("src/main/resources/edu/wpi/u/Edges.csv", "Edges");
    Stage stage = (Stage) App.primaryStage.getScene().getWindow();
    stage.close();
  }


  public int requestClicked;

  public void switchTheme() {
    if(App.isLightTheme){
      System.out.println("isLightTheme!");
      App.primaryStage.getScene().getStylesheets().clear();
      App.primaryStage.getScene().getStylesheets().add(getClass().getResource("/edu/wpi/u/views/css/BaseStyle.css").toExternalForm());
      App.primaryStage.getScene().getStylesheets().add(getClass().getResource("/edu/wpi/u/views/css/DarkTheme.css").toExternalForm());
      App.isLightTheme = false;
    }else{
      System.out.println("isDarkTheme!");
      App.primaryStage.getScene().getStylesheets().clear();
      App.primaryStage.getScene().getStylesheets().add(getClass().getResource("/edu/wpi/u/views/css/BaseStyle.css").toExternalForm());
      App.primaryStage.getScene().getStylesheets().add(getClass().getResource("/edu/wpi/u/views/css/LightTheme.css").toExternalForm());
      App.isLightTheme = true;
    }
  }
}
