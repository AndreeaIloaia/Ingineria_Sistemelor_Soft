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

public class MainControllerAgent {
    private IServices server;
    private LoginAgentController loginAgentController;
    private Stage stage;

    public Button btnAgent;


    public void setServer(IServices s, Stage primaryStage) {
        server = s;
        stage = primaryStage;
    }

    @FXML
    public void initialize() throws IOException {
        btnAgent.setOnAction(e -> {
            go_agent();
        });
    }


    private void go_agent() {
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

        loginAgentController = loader.getController();
        loginAgentController.setServer(server);
        dialogStage.show();
        stage.hide();
        loginAgentController.btnClose.setOnAction(e -> {
            dialogStage.close();
            stage.show();
        });
    }
}
