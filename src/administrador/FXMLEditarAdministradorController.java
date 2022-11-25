package administrador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.ConexionServiciosweb;
import pojos.Administrador;
import util.Constantes;
import java.lang.reflect.Type;
import util.Utilidades;

public class FXMLEditarAdministradorController implements Initializable {

    @FXML
    private TableView<Administrador> tbAdministrador;    
    @FXML
    private TableColumn tcId;
    @FXML
    private TableColumn tcNombre;
    @FXML
    private TableColumn tcPaterno;
    @FXML
    private TableColumn tcMaterno;
    @FXML
    private TableColumn tcCorreo;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfMaterno;
    @FXML
    private TextField tfPaterno;
    @FXML
    private TextField tfCorreo;

    private ObservableList<Administrador> listaAdministradores;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarColumnasTabla();
        cargarInformacionMedicos();
    }    

    @FXML
    private void clicCancelar(ActionEvent event) {
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
    }
    
    private void inicializarColumnasTabla(){
        
        listaAdministradores = FXCollections.observableArrayList();
        tcId.setCellValueFactory(new PropertyValueFactory("idAdministrador"));
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        tcMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        tcCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        
    }
    
    private void cargarInformacionMedicos(){
        String urlWS = Constantes.URL_BASE+"administradores/all";
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaTipoAdministrador = new TypeToken<ArrayList <Administrador> >() {}.getType();
            ArrayList administradorWS = gson.fromJson(resultadoWS, listaTipoAdministrador);
            listaAdministradores.addAll(administradorWS);
            tbAdministrador.setItems(listaAdministradores);
                    
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de los médicos"
                    , Alert.AlertType.ERROR);
        }
        
    }    
    
    
}
