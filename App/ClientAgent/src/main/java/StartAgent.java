import gui.MainControllerAgent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import serv.IServices;

public class StartAgent extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");

        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:client_n.xml");
        IServices server = (IServices)factory.getBean("appService");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_window.fxml"));
        Pane pane = loader.load();
        MainControllerAgent controller = loader.getController();
        controller.setServer(server, primaryStage);

        Scene scene = new Scene(pane);
        primaryStage.setTitle("My app");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
