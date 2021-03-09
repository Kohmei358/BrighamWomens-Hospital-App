package edu.wpi.u.controllers.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.u.App;
import io.netty.handler.codec.http.HttpHeaders;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.asynchttpclient.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.Future;

public class Enter2FATokenController {
    @FXML public JFXButton loginButton;
    @FXML public JFXTextField tokenTextFIeld;
    @FXML public JFXProgressBar progressBar;
    @FXML public JFXButton enterAppButton;

    SimpleBooleanProperty valid = new SimpleBooleanProperty(false);
    public void initialize(){
        valid.addListener(e -> {
            try {
                handleAppEntry();
            } catch (IOException ioException) {
                valid.set(!valid.get());
            }
        });
    }


    public void handleTokenSubmit() {
        progressBar.setStyle("-fx-opacity: 1");
        String token = tokenTextFIeld.getText();
        String phoneNumber = App.userService.getActiveUser().getPhoneNumber();
        try {
            System.out.println("Token " + token);
            System.out.println("Phonenumber " + phoneNumber);
            URI uri2 = new URI("https://bw-webapp.herokuapp.com/" +"verify?phonenumber=" + "+1"+ phoneNumber + "&code=" + token);
            URL url2 = uri2.toURL(); // make GET request
            System.out.println("URL: " + url2.toString());
            AsyncHttpClient client = Dsl.asyncHttpClient();
            Future<Integer> whenStatusCode = client.prepareGet(url2.toString())
                    .execute(new AsyncHandler<Integer>() {
                        private Integer status;
                        @Override
                        public State onStatusReceived(HttpResponseStatus responseStatus) throws Exception {
                            status = responseStatus.getStatusCode();
                            System.out.println("At status code in verify");
                            return State.CONTINUE;
                        }
                        @Override
                        public State onHeadersReceived(HttpHeaders headers) throws Exception {
                            System.out.println("At headers code in verify");
                            return State.CONTINUE;
                        }
                        @Override
                        public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                            byte[] b = bodyPart.getBodyPartBytes();
                            String y = new String(b);
                            //System.out.println(y);
//                            System.out.println(y.contains("approved"));
//                            System.out.println(y.contains("pending"));
                            if (y.contains("approved")){
                                enterAppButton.setStyle("-fx-opacity: 1");
                                valid.set(!valid.get());
                            }
                            else {
                                //TODO : Display invalid entry
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
    }

    public void handleForgotPassword(ActionEvent actionEvent) {
    }

    public void handleGoBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/login/UserLoginScreen.fxml"));
        App.getPrimaryStage().getScene().setRoot(root);
    }

    public void handleAppEntry() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/NewMainPage.fxml"));
        App.getPrimaryStage().getScene().setRoot(root);
    }
}
