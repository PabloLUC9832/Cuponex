package administrador;

import com.google.gson.Gson;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.ConexionServiciosweb;
import pojos.Respuesta;
import util.Constantes;
import util.Utilidades;

public class FXMLFormularioEdicionAdministradorController implements Initializable {
    
    private int idAdministrador;
    private boolean isEdicion = false;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfPaterno;
    @FXML
    private TextField tfMaterno;
    @FXML
    private TextField tfCorreo;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnActualizar;    
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    
    public void inicializarInformacionVentana(int idAdministrador, String nombre, String aPaterno, String aMaterno, String correo, String password){
        this.idAdministrador = idAdministrador;
        isEdicion = true;        
        tfCorreo.setDisable(true);
        tfNombre.setText(nombre);
        tfPaterno.setText(aPaterno);
        tfMaterno.setText(aMaterno);
        tfCorreo.setText(correo);
        pfPassword.setText(password);
    }

    @FXML
    private void clicActualizarAdministrador(ActionEvent event) {
        
        String nombre = tfNombre.getText();
        String apellidoPaterno = tfPaterno.getText();
        String apellidoMaterno = tfMaterno.getText();
        String correo = tfCorreo.getText();
        String password= pfPassword.getText();
        
        if(nombre.isEmpty() || apellidoPaterno.isEmpty()||apellidoMaterno.isEmpty()||correo.isEmpty()||password.isEmpty()){
            Utilidades.mostrarAlertaSimple("Campos vacios", "Llena los campos vacios",Alert.AlertType.ERROR);            
        }else{
            consumirServicioModificar(idAdministrador, nombre, correo, correo, correo, password);            
        }
        
        
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();        
        stage.close();           
    }
    
    private void consumirServicioModificar(int idAdministrador, String nombre, String aPaterno, String aMaterno, String correo, String password){
        
        try{
            
            String urlServicio = Constantes.URL_BASE+"administradores/modificar";
            
            String parametros = "idAdministrador=" + idAdministrador+ "&" +
                                "nombre=" + nombre + "&" +
                                "apellidoPaterno=" + aPaterno + "&" +
                                "apellidoMaterno=" + aMaterno + "&" +
                                "correo=" + correo + "&" +
                                "password=" + password
                                ;
            
            String resultadoWS = ConexionServiciosweb.peticionServicioPUT(urlServicio, parametros);
            Gson gson = new Gson() ;
            Respuesta respuesta = gson.fromJson(resultadoWS, Respuesta.class);
            
            if (!respuesta.getError()) {                
                Utilidades.mostrarAlertaSimple("Administrador actualizado", 
                        " Administrador actualizado correctamente "
                        , Alert.AlertType.INFORMATION);
                Stage stage = (Stage) this.btnActualizar.getScene().getWindow();
                stage.close();
            }else{
                Utilidades.mostrarAlertaSimple("Error al editar el usuario", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }
            
            
            
        }catch(Exception e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);            
        }
                      
    }
    
    
    
}
