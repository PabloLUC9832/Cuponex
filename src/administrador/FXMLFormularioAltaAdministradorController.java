package administrador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
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
    @FXML
    private Button btnGuardar;
    
    ObservableList<Administrador> listaAdministradoresR;
    TableView<Administrador> tbAdministradorR;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       tfNombre.addEventHandler(KeyEvent.KEY_TYPED, event -> soloLetras(event));
       tfPaterno.addEventHandler(KeyEvent.KEY_TYPED, event -> soloLetras(event));        
       tfMaterno.addEventHandler(KeyEvent.KEY_TYPED, event -> soloLetras(event));        

    }    
    
    private void cargarInformacionAdministradores(){
        String urlWS = Constantes.URL_BASE+"administradores/all";
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaTipoAdministrador = new TypeToken<ArrayList <Administrador> >() {}.getType();
            ArrayList administradorWS = gson.fromJson(resultadoWS, listaTipoAdministrador);
            recibir(listaAdministradoresR, tbAdministradorR);
            listaAdministradoresR.clear();
            listaAdministradoresR.addAll(administradorWS);
            tbAdministradorR.setItems(listaAdministradoresR);
                    
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de los administradores"
                    , Alert.AlertType.ERROR);
        }
        
    }    
    
    void recibir(ObservableList<Administrador> listaAdministradores, TableView<Administrador> tbAdministrador){
        listaAdministradoresR = listaAdministradores;
        tbAdministradorR = tbAdministrador;
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
                Stage stage = (Stage) this.btnGuardar.getScene().getWindow();
                stage.close();
                cargarInformacionAdministradores();
            }else{
                Utilidades.mostrarAlertaSimple("Error al añadir admiistrador", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }            
            
          
            
            
        }catch(IOException e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);            
        }
        
        
    }

    
        
    
}
