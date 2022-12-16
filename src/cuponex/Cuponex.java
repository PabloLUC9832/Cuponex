package cuponex;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Cuponex extends Application {
    
    double x,y;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
       String ruta = "FXMLInicioSesion.fxml"; //FXMLPrincipal.fxml FXMLInicioSesion.fxml
       //String ruta = "/promociones/FXMLGeneralPromocion.fxml";
       //String ruta = "/promociones/FXMLFormularioAltaPromocion.fxml"; 

       Parent root = FXMLLoader.load(getClass().getResource(ruta));
       root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        
        root.setOnMouseDragged(event->{
            primaryStage.setX(event.getScreenX()-x);
            primaryStage.setY(event.getScreenY()-y);        
        });        
        Scene scene = new Scene(root);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
