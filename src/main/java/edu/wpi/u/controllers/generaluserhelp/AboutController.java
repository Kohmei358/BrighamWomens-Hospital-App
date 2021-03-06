package edu.wpi.u.controllers.generaluserhelp;

import edu.wpi.u.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import static edu.wpi.u.users.Role.ADMIN;

public class AboutController {

    @FXML ImageView aboutImage;
    @FXML Label aboutDescriptionLabel;
    @FXML Label kohmeiKadoyaLabel;
    @FXML Label nevilleIngramLabel;
    @FXML Label kaamilLokhandwalaLabel;
    @FXML Label charlieKittlerLabel;
    @FXML Label lilyDurkinLabel;
    @FXML Label tylerSandervilleLabel;
    @FXML Label jacobBernardLabel;
    @FXML Label nickGaoLabel;
    @FXML Label willBurkeLabel;
    @FXML Label michaelGearyLabel;
    @FXML Label olaJacksonLabel;

    /**
     * Displays an image when a label is hovered over.
     * @param path
     * Author Lily
     */
    private void showIMG(String path){
        //File file = new File(path);
        Image img = new Image(path);
        aboutImage.setImage(img);
        aboutImage.setVisible(true);

    }

    /**
     * Defaults the image after mouse leaves the label to the hospital logo
     * and the text to a thank you blurb
     * Author Lily
     */
    public void hideIMG() {
        showIMG("/edu/wpi/u/views/Images/brigham-health-logo-600x200.png");
        aboutDescriptionLabel.setText("We would like to give a special thank you to" +
                " Brigham and Women's Hospital, and their representative Andrew Shinn," +
                " for making this project possible with their cooperation, time, and" +
                " input. We would also like to thank Worcester Polytechnic Institute," +
                " and specifically the Department of Computer Science, for making this" +
                " course possible. Additionally, we would like to thank Professor" +
                " Wilson Wong for advising this class, as well as our team coach Andrew" +
                " Bonaventura for providing guidance throughout this course to our team.");
    }

    /**
     * Shows Kohmei Kadoya's picture and a short bio
     * Author Lily
     */
    public void showKohmeiKadoya() {
        showIMG("/edu/wpi/u/views/Images/KohmeiKadoya.jpeg");
        aboutDescriptionLabel.setText("Hi! I'm Kohmei Kadoya a RBE major at WPI" +
                " and the lead software engineer for Team U. I come from Tokyo," +
                " Japan and I enjoy back-country skiing and scuba diving");
    }

    /**
     * Shows Neville Ingram's picture and a short bio
     * Author Lily
     */
    public void showNevilleIngram() {
        showIMG("/edu/wpi/u/views/Images/NevilleIngram.jpg");
        aboutDescriptionLabel.setText("My name is Neville Ingram, I'm of our" +
                " Assistant Leads." +
                " I'm a Mathematical Sciences and Computer Science major" +
                " from New Rochelle, New York." +
                " In my free time I like to go hiking and play video games.");
    }

    /**
     * Shows Kaamil Lokhandwala's picture
     * Author Lily
     */
    public void showKaamilLokhandwala() {
        showIMG("/edu/wpi/u/views/Images/KaamilLokhandwala.jpg");
        aboutDescriptionLabel.setText("Hey all, I'm Kaamil Lokhandwala, an Assistant Lead Dev of Team U," +
                " a CS and IMGD double major. I'm from the far town of Andover, a whopping 40 minutes away" +
                " if you're speeding. I enjoy music and art, and finding how to connect them with technology.");
    }

    /**
     * Shows Charlie Kittler's picture and a short bio
     * Author Lily
     */
    public void showCharlieKittler() {
        showIMG("/edu/wpi/u/views/Images/CharlieKittler.png");
        aboutDescriptionLabel.setText("I'm Charles Kittler, one of our Assistant Leads," +
                " and I'm a Robotics Engineering and Computer Science major from Natick, MA." +
                " One fun fact about me is that I enjoy rock climbing.");
    }

    /**
     * Shows Lily Durkin's picture and a short bio
     * Author Lily
     */
    public void showLilyDurkin() {
        showIMG("/edu/wpi/u/views/Images/LilyDurkin.jpg");
        aboutDescriptionLabel.setText("Hi I'm Lily Durkin, and I'm our" +
                " Documentation Analyst. I'm a Robotics Engineering major" +
                " from Lowell, MA. I enjoy sketching, and I've even chosen to" +
                " pursue my depth in Art.");
    }

    /**
     * Shows Tyler Sanderville's picture and a short bio
     * Author Lily
     */
    public void showTylerSanderville() {
        showIMG("/edu/wpi/u/views/Images/TylerSanderville.jpeg");
        aboutDescriptionLabel.setText("Hi I'm Tyler Sanderville, our product owner, and" +
                " I'm a Robotics Engineering major. I hail from the far west, being birthed"
                + " in Albuquerque, New Mexico and living all across the likes, ranging" +
                " from the sun belt to northern California. I enjoy mountain biking," +
                " cooking, strumming guitar, and gaming when the moment is right." +
                "Fun fact: I'm currently ranking in the global top 300 for the video" +
                " game 'Super Hexagon'");
    }

    /**
     * Shows Jacob Bernard's picture and a short bio.
     * Author Lily
     */
    public void showJacobBernard() {
        showIMG("/edu/wpi/u/views/Images/JacobBernard.jpg");
        aboutDescriptionLabel.setText("Hi, I'm Jacob Bernard, an RBE major." +
                " I live in Maine and enjoy woodworking in my free time.");
    }

    /**
     * Nick Gao's picture
     * Author Lily
     */
    public void showNickGao() {
        showIMG("/edu/wpi/u/views/Images/NickGao.jpg");
        aboutDescriptionLabel.setText("Hi I'm Tianyang (Nick) Gao, the Scrum Master" +
                " for Team U. I'm a RBE/ME and I'm from Zhejiang, China. I like" +
                " video games, traveling, and music");
    }

    /**
     * Shows Will Burke's picture and a short bio
     * Author Lily
     */
    public void showWillBurke() {
        showIMG("/edu/wpi/u/views/Images/WillBurke.jpg");
        aboutDescriptionLabel.setText("My name is Will Burke, I'm a sophomore at" +
                " WPI that studies Computer Science and Data Science and I've been" +
                " a full-time software engineer, primarily working on" +
                " backend-related functionality. Some things I really enjoy are" +
                " using canva presentations, summers in my home-state of Rhode" +
                " Island, and Ultimate Frisbee!");
    }

    /**
     * Shows Michael Geary's picture and a short bio
     * Author Lily
     */
    public void showMichaelGeary() {
        showIMG("/edu/wpi/u/views/Images/MichaelGeary.jpg");
        aboutDescriptionLabel.setText("My name is Michael Geary and I'm a sophomore Computer Science major" +
                " who works as an Algorithms Specialist on Team U. I come from" +
                " Liverpool, New York, and I'm involved in multiple music" +
                " organizations on campus. I also enjoy spending time with my brothers in LCA.");
    }

    /**
     * Shows Ola Jackson's picture
     * Author Lily
     */
    public void showOlaJackson() {
        showIMG("/edu/wpi/u/views/Images/OlajumokeJackson.jpg");
        aboutDescriptionLabel.setText("My name is Olajumoke Jackson and I'm a" +
                " Full-Time Database Engineer for Team U. I'm a Data Science major from" +
                " Providence, RI. My favorite books are After Dark by Haruki Murakami," +
                " Atlas Shrugged by Ayn Rand, and Snow Flower and the Secret Fan by Lisa See.");
    }

    /**
     * Brings AboutPage back to Help Page.
     * @throws Exception
     * Author Lily
     */
    @FXML public void handleBackToMainPageButton() throws IOException {
        AnchorPane anchor = (AnchorPane) App.tabPaneRoot.getSelectionModel().getSelectedItem().getContent();
        if(App.userService.getActiveUser().getType() ==  ADMIN){
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/AdminMainHelpPage.fxml"));
            anchor.getChildren().clear();
            anchor.getChildren().add(root);
        }
        else if(!(App.userService.getActiveUser().getType() ==  ADMIN)){
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/u/views/MainHelpPage.fxml"));
            anchor.getChildren().clear();
            anchor.getChildren().add(root);
        }
    }
}
