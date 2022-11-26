package cuponex;

import com.google.gson.Gson;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.ConexionServiciosweb;
import pojos.RespuestaLogin;
import util.Constantes;
import util.Utilidades;

public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfCorreo;
    @FXML
    private PasswordField pfPassword;
    
    String datosAdministrador;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        
        String correo = tfCorreo.getText();
        String password   = pfPassword.getText();
        
        if( correo.isEmpty() || password.isEmpty() ){
            Utilidades.mostrarAlertaSimple("Campos requeridos","Debes ingresar el número de personal y//o contraseña para ingresar"
            ,Alert.AlertType.WARNING);
        }else{
            consumirServicioLogin(correo,password);
        }        
        
    }
    
    private void consumirServicioLogin(String correo,String password){
     
        try{
            String urlServicio = Constantes.URL_BASE+"acceso/escritorio";
            String parametros = "correo="+correo+"&password="+password;
            String resultadoWS = ConexionServiciosweb.peticionServicioPOST(urlServicio, parametros);
            Gson gson = new Gson() ;
            RespuestaLogin respuestaLogin = gson.fromJson(resultadoWS, RespuestaLogin.class);
            
            if (!respuestaLogin.getError()) {
                
                Utilidades.mostrarAlertaSimple("Credenciales correctas", 
                        "Bienvenido "+ respuestaLogin.getNombre()+" "+ respuestaLogin.getApellidoPaterno()
                        , Alert.AlertType.INFORMATION);
                datosAdministrador = respuestaLogin.getNombre()+" "+ respuestaLogin.getApellidoPaterno();
                irPantallaPrincipal();
            }else{
                Utilidades.mostrarAlertaSimple("Credenciales incorrectas", respuestaLogin.getMensaje(),
                        Alert.AlertType.ERROR);
            }
            
        }catch(Exception e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);
        }
        
    }
    
    private void irPantallaPrincipal(){
        
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPrincipal.fxml"));
            //Parent vista = FXMLLoader.load(getClass().getResource("FXMLPrincipal.fxml"));
            Parent vista = (Parent) fxmlLoader.load();
            
            FXMLPrincipalController administrador = fxmlLoader.getController();
            administrador.getDatos(datosAdministrador);
            
            
            Scene scenaPrincipal = new Scene(vista);
            Stage scenarioBase = (Stage) tfCorreo.getScene().getWindow();
            scenarioBase.setScene(scenaPrincipal);
            scenarioBase.centerOnScreen();
            scenarioBase.show();            
        }catch(Exception e){
            Utilidades.mostrarAlertaSimple("Error", "No se ha podido cargar la ventana principal", Alert.AlertType.ERROR);
        }
    }    
    
    
    
    
}
