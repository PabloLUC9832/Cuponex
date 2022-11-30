package cuponex;

import java.io.IOException;
import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FXMLPrincipalController implements Initializable {

    @FXML
    private Label btnMenu1;
    @FXML
    private Label btnMenu2;
    @FXML
    private Label labelUsuario;
    @FXML
    private AnchorPane slider;
    @FXML
    private Button btnAdministrador;
    @FXML
    private Button btnEmpresa;
    @FXML
    private Button btnSucursal;
    @FXML
    private Button btnPromocion;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private StackPane contentArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        slider.setTranslateX(-176);
        
        btnMenu2.setOnMouseClicked(event->{
            TranslateTransition slide = new TranslateTransition();
            slide.setDelay(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(0);
            slide.play();
            slider.setTranslateX(-176);
            
            slide.setOnFinished((ActionEvent e) -> {
                btnMenu2.setVisible(false);
                btnMenu1.setVisible(true);
            });
            
        });
        
        btnMenu1.setOnMouseClicked(event->{
            TranslateTransition slide = new TranslateTransition();
            slide.setDelay(Duration.seconds(0.2));
            slide.setNode(slider);
            slide.setToX(-176);
            slide.play();
            slider.setTranslateX(0);
            
            slide.setOnFinished((ActionEvent e) -> {
                btnMenu2.setVisible(true);
                btnMenu1.setVisible(false);
            });
            
        });
    }    

    @FXML    
    private void pantallaAdministrador(ActionEvent event) throws IOException {

        /*Parent fxml = FXMLLoader.load(getClass().getResource("/administrador/FXMLEditarAdministrador.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);*/
        cargarPantalla("administrador", "FXMLGeneralAdministrador");
    }

    @FXML
    private void pantallaEmpresa(ActionEvent event) {
    }

    @FXML
    private void pantallaSucursal(ActionEvent event) throws IOException {
        cargarPantalla("sucursales", "FXMLGeneralSucursal");        
    }

    @FXML
    private void pantallaPromocion(ActionEvent event) throws IOException {
        cargarPantalla("promociones", "FXMLGeneralPromocion");    
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        
        Stage stage = (Stage) this.btnCerrarSesion.getScene().getWindow();
        stage.close();
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLInicioSesion.fxml"));
            Parent ventanaPrincipal = (Parent) fxmlLoader.load();
            //Stage
            stage = new Stage();
            stage.setScene(new Scene(ventanaPrincipal));
            stage.show();
            
        }catch(IOException e){
            String errorMessage = "El tiempo de espera se ha agotado o se perdío la conexión\n" +"con la Base Datos.";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al mostrar la ventana principal");
            alert.setHeaderText(" ¡Por favor! intentelo nuevamente");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }      
    }                   
    
    public void getDatos(String datos){
        labelUsuario.setText("Bienvenido "+datos);
    }     
    
    public void cargarPantalla(String paquete,String nombre) throws IOException{
        Parent fxml = FXMLLoader.load(getClass().getResource("/"+paquete+"/"+nombre+".fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);        
    }
    
}
