package administrador;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import pojos.Administrador;
import util.Constantes;
import modelo.ConexionServiciosweb;
import pojos.Respuesta;
import util.Utilidades;

public class FXMLFormularioAltaAdministradorController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfPaterno;
    @FXML
    private TextField tfMaterno;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btnCancelar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       tfNombre.addEventHandler(KeyEvent.KEY_TYPED, event -> soloLetras(event));
       tfPaterno.addEventHandler(KeyEvent.KEY_TYPED, event -> soloLetras(event));        
       tfMaterno.addEventHandler(KeyEvent.KEY_TYPED, event -> soloLetras(event));        

    }    

    @FXML
    private void clicGuardarAdministrador(ActionEvent event) {
        
        String nombre  = tfNombre.getText();
        String correo  = tfCorreo.getText();
        String aPaterno = tfPaterno.getText();
        String aMaterno = tfMaterno.getText();
        String password = pfPassword.getText();
        
        Administrador admin = new Administrador();
        admin.setNombre(nombre);
        admin.setCorreo(correo);
        admin.setApellidoMaterno(aMaterno);
        admin.setApellidoPaterno(aPaterno);
        admin.setPassword(password);
        
        System.out.println(nombre+"||"+ correo+"||"+aPaterno+"||"+aMaterno+"||"+password);
        
        guardarInformacionAdministrador(admin);
        
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();        
        stage.close();        
    }
    
    public void soloLetras(KeyEvent keyEvent){
        try{
            char key = keyEvent.getCharacter().charAt(0);
            if(!Character.isLetter(key) && !Character.isSpaceChar(key) ){
                keyEvent.consume();
            }
        }catch(Exception e){
            System.out.println("Excepcion; "+e);
        }
            
    }
    
    private void guardarInformacionAdministrador(Administrador administrador){
        
        try{
            
            String urlServicio = Constantes.URL_BASE+"administradores/registrar";
            String parametros = "nombre=" + administrador.getNombre() + "&" +
                                "apellidoPaterno=" + administrador.getApellidoPaterno() + "&" +
                                "apellidoMaterno=" + administrador.getApellidoMaterno() + "&" +
                                "correo=" + administrador.getCorreo() + "&" +
                                "password=" + administrador.getPassword()
                                ;            
            String resultado = ConexionServiciosweb.peticionServicioPOST(urlServicio, parametros);
            
            Gson gson = new Gson();
            Respuesta respuesta = gson.fromJson(resultado, Respuesta.class);
            
            if (!respuesta.getError()) {
                
                Utilidades.mostrarAlertaSimple("Admiistrador añadido", " Administrador añadido correctamente "
                        , Alert.AlertType.INFORMATION);  
            }else{
                Utilidades.mostrarAlertaSimple("Error al añadir admiistrador", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }            
            
          
            
            
        }catch(IOException e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);            
        }
        
        
    }

    
        
    
}
