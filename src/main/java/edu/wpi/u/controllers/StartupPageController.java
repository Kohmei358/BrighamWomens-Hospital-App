package edu.wpi.u.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.u.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class StartupPageController {
    public JFXButton fireStickButton;
    public JFXButton mobileButton;
    public JFXButton desktopButton;

    public void handleDesktop(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/login/CovidSurveyScreen.fxml"));
        App.getPrimaryStage().getScene().setRoot(root);
    }

    public void handleMobile(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/mobile/MobileEmbenedGoogleMaps.fxml"));
        App.getPrimaryStage().getScene().setRoot(root);
    }

    public void handleFirestick(ActionEvent actionEvent) {
    }
}