import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class KeyClickStarter extends  Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("KeyClick.fxml"));
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("KeyClick");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("mainIcon64.png"));
        primaryStage.show();
        root.requestFocus();

    }


    public static void main(String[] args) {
        launch(args);

        System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
    }
}
