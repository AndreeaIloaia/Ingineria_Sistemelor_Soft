package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import serv.IServices;
import serv.ServException;

import java.io.IOException;

public class LoginAgentController {
    public Button btnClose;
    private IServices server;
    private ControllerAgent ctrl_agent;

    public TextField textFieldUser;
    public Button btnLog;
    public PasswordField textFieldPass;

    public void setServer(IServices s) {
        server = s;
    }

    @FXML
    public void initialize() throws IOException {
        btnLog.setOnAction(e -> {
            log_in();
        });
    }

    private void log_in() {
        String name = "/view/main_agent.fxml";
        String title = "Login Admin";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(name));
        AnchorPane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        dialogStage.setWidth(600);
        dialogStage.setScene(scene);


        ctrl_agent = loader.getController();
        try {
            String user = textFieldUser.getText();
            String password = textFieldPass.getText();
            server.loginAgent(user, password, ctrl_agent);
            ctrl_agent.setServer(server);
            dialogStage.show();
        } catch (ServException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }
        ctrl_agent.btnLogout.setOnAction(e -> {
            dialogStage.close();
            textFieldPass.clear();
            textFieldUser.clear();
        });


    }
}
