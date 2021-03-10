package edu.wpi.u.controllers.login;


import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.u.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.u.exceptions.AccountNameNotFoundException;
import edu.wpi.u.exceptions.PasswordNotFoundException;
import edu.wpi.u.exceptions.PhoneNumberNotFoundException;
import io.netty.handler.codec.http.HttpHeaders;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import org.asynchttpclient.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import java.nio.file.AccessDeniedException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserLoginScreenController {


    // TODO: Properly rename JFX artifacts
    @FXML
    public JFXTextField userNameTextField;
    @FXML
    public JFXPasswordField passWordField;
    @FXML
    public JFXButton loginButton;
    @FXML
    public JFXButton forgotPasswordButton;
    @FXML
    public JFXProgressBar progressBar;
    @FXML public JFXButton submitButton;
    @FXML public Label errorLabel, wrongPasswordLabel;
    @FXML public JFXButton debugLoginAdminButton;
    @FXML public JFXButton debugLoginGuestButton;
    @FXML public JFXButton submitSkipButton;

    public void initialize() throws IOException {
        wrongPasswordLabel.setVisible(false);

        passWordField.focusedProperty().addListener(e->{
           wrongPasswordLabel.setVisible(false);
        });

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Username Required");
        userNameTextField.getValidators().add(validator);
        userNameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                userNameTextField.validate();
            }
        });
        RequiredFieldValidator validator4 = new RequiredFieldValidator();
        validator4.setMessage("Username Invalid");
        userNameTextField.getValidators().add(validator4);
        userNameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal){
                if (App.userService.checkUsername(passWordField.getText()).equals("")) {
                    userNameTextField.validate();
                }
            }
        });


        RequiredFieldValidator validator2 = new RequiredFieldValidator();
        validator2.setMessage("Password Required");
        passWordField.getValidators().add(validator2);
        passWordField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                passWordField.validate();
            }
        });

        RequiredFieldValidator validator5 = new RequiredFieldValidator();
        validator5.setMessage("Username Invalid");
        userNameTextField.getValidators().add(validator5);
        userNameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal){
                if (App.userService.checkUsername(passWordField.getText()).equals("")) {
                    userNameTextField.validate();
                }
            }

        });


        RequiredFieldValidator validator3 = new RequiredFieldValidator();
        validator3.setMessage("Token Required");
        passWordField.getValidators().add(validator3);
        passWordField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                passWordField.validate();
            }
        });
    }


    public void handleLogin() throws IOException {
        System.out.println("HERE");
        progressBar.setStyle("-fx-opacity: 1");
//        Scene scene = new Scene(root);
//        App.getPrimaryStage().setScene(scene);
//        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/Enter2FATokenScreen.fxml"));
//        App.getPrimaryStage().getScene().setRoot(root);
        String username = userNameTextField.getText();
        String password = passWordField.getText();
        String phonenumber = App.userService.getActiveUser().getPhoneNumber();

        try {
            if (!App.userService.checkUsername(username).equals("")) {
                if (!App.userService.checkPassword(password,username).equals("")) {
                    try {
                        Pattern pattern = Pattern.compile("^\\d{10}$");
                        Matcher matcher = pattern.matcher(phonenumber);
                        if (!matcher.matches()){
                            errorLabel.setText("Phonenumber associated with account is invalid");
                            throw new PhoneNumberNotFoundException("Phone number is not valid");
                        }
                        URI uri = new URI("https://bw-webapp.herokuapp.com/" +"login?phonenumber=" + "+1"+ phonenumber + "&channel=sms");
                        URL url = uri.toURL(); // make GET request
                        AsyncHttpClient client = Dsl.asyncHttpClient();
                        Future<Integer> whenStatusCode = client.prepareGet(url.toString())
                                .execute(new AsyncHandler<Integer>() {
                                    private Integer status;
                                    @Override
                                    public State onStatusReceived(HttpResponseStatus responseStatus) throws Exception {
                                        status = responseStatus.getStatusCode();
                                        return State.CONTINUE;
                                    }
                                    @Override
                                    public State onHeadersReceived(HttpHeaders headers) throws Exception {
                                        return State.CONTINUE;
                                    }
                                    @Override
                                    public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                                        byte[] b = bodyPart.getBodyPartBytes();
                                        String y = new String(b);
                                        System.out.println(y.contains("pending"));
                                        if (y.contains("pending")){
                                            progressBar.setStyle("-fx-opacity: 0");
                                            submitButton.setStyle("-fx-opacity: 1");
                                            // TODO : Set alignment
                                        }
                                        return State.CONTINUE;
                                    }

                                    @Override
                                    public Integer onCompleted() throws Exception {
                                        return status;
                                    }
                                    @Override
                                    public void onThrowable(Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new PasswordNotFoundException();
                }
            } else {
                throw new AccountNameNotFoundException();
            }
        } catch (AccountNameNotFoundException | PasswordNotFoundException e) {
            progressBar.setStyle("-fx-opacity: 0");
            errorLabel.setText("Username or Password is Invalid");
            e.printStackTrace();
        }
        if (!App.userService.checkUsername(userNameTextField.getText()).equals("")) {
            if (!App.userService.checkPassword(passWordField.getText(),userNameTextField.getText()).equals("")) {
                App.userService.setUser(userNameTextField.getText(), passWordField.getText(), App.userService.checkPassword(passWordField.getText(),userNameTextField.getText()));
                handleSubmit();
            }else{
                wrongPasswordLabel.setVisible(true);
            }
        }else{
            wrongPasswordLabel.setVisible(true);
        }
    }



//Throws exceptions if username or password not found
        public void handleForgotPassword() throws IOException {
            App.userService.setTypedUsername(userNameTextField.getText());
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/login/ForgotPasswordScreen.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        }

    public void handleSubmit() throws IOException {
        App.loginFlag.set(!App.loginFlag.get());
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/login/Enter2FATokenScreen.fxml"));
        App.getPrimaryStage().getScene().setRoot(root);
    }

    public void handleDebugLogin(ActionEvent actionEvent) throws IOException {
        App.userService.setUser("admin", "admin", "Employees");
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/NewMainPage.fxml"));
        App.getPrimaryStage().getScene().setRoot(root);
    }

    public void handleDebugLoginGuest(ActionEvent actionEvent) throws IOException {
        App.userService.setUser("patient", "patient", "Guests");
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/NewMainPage.fxml"));
        App.getPrimaryStage().getScene().setRoot(root);
    }

    public void handleBackButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/login/SelectUserScreen.fxml"));
        App.getPrimaryStage().getScene().setRoot(root);
    }

    public void handleSkip(ActionEvent actionEvent) throws IOException {
        String username = userNameTextField.getText();
        String password = passWordField.getText();
        try{
            if (!App.userService.checkUsername(username).equals("")) {
                if (!App.userService.checkPassword(password,username).equals("")) {
                    App.userService.setUser(username, password, App.userService.checkPassword(password,username));
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/NewMainPage.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                }
                else {
                    throw new PasswordNotFoundException();
                }
            }
            else {
                throw new AccountNameNotFoundException();
            }
        }catch (AccountNameNotFoundException | PasswordNotFoundException e){
            errorLabel.setText("Username or Password is Invalid");
            e.printStackTrace();
        }

    }

    public void handleLonginWithNo2FA(){
        if (!App.userService.checkUsername(userNameTextField.getText()).equals("")) {
            if (!App.userService.checkPassword(passWordField.getText(),userNameTextField.getText()).equals("")) {
                App.userService.setUser(userNameTextField.getText(), passWordField.getText(), App.userService.checkPassword(passWordField.getText(),userNameTextField.getText()));
                Parent root = null;
                try {
                    App.loginFlag.set(!App.loginFlag.get());
                    root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/NewMainPage.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                App.getPrimaryStage().getScene().setRoot(root);
            }else{
                wrongPasswordLabel.setVisible(true);
            }
        }else{
            wrongPasswordLabel.setVisible(true);
        }
    }
}




