package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import serv.IServices;

import java.io.IOException;

public class MainController {
    private IServices server;
    private LoginController loginController;
    private Stage stage;

    public Button btnAdmin;
    public Button btnAgent;


    public void setServer(IServices s, Stage primaryStage) {
        server = s;
        stage = primaryStage;
    }

    @FXML
    public void initialize() throws IOException {
        btnAdmin.setOnAction(e -> {
            go_admin();
        });
    }


    private void go_admin() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/login.fxml"));
        AnchorPane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Login Admin");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        //dialogStage.setWidth(500);
        dialogStage.setScene(scene);

        loginController = loader.getController();
        loginController.setServer(server);
        dialogStage.show();
        stage.hide();
        loginController.btnClose.setOnAction(e -> {
            dialogStage.close();
            stage.show();
        });
    }
}
