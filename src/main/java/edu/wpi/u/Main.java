package edu.wpi.u;

import com.sun.javafx.application.LauncherImpl;

public class Main {
  public static void main(String[] args) {
    LauncherImpl.launchApplication(App.class, FirstPreloader.class, args);
    //App.launch(App.class, args);
  }
}
