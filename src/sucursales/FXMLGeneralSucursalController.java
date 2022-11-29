package sucursales;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import modelo.ConexionServiciosweb;
import pojos.Respuesta;
import pojos.Sucursal;
import util.Constantes;
import util.Utilidades;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class FXMLGeneralSucursalController implements Initializable {

    @FXML
    private TableView<Sucursal> tbSucursal;
    @FXML
    private TableColumn tcId;
    @FXML
    private TableColumn tcNombre;
    @FXML
    private TableColumn tcDireccion;
    @FXML
    private TableColumn tcCP;
    @FXML
    private TableColumn tcColonia;
    @FXML
    private TableColumn tcCiudad;
    @FXML
    private TableColumn tcTelefono;
    @FXML
    private TableColumn tcLatitud;
    @FXML
    private TableColumn tcLongitud;
    @FXML
    private TableColumn tcEncargado;
    @FXML
    private TableColumn tcIdEmpresa;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField tfBusqueda;
    
    private ObservableList<Sucursal> listaSucursales;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarColumnasTabla();
        cargarInformacionSucursales();
        try {
            busqueda();
        } catch (Exception ex) {
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de las sucursales" + ex
                    , Alert.AlertType.ERROR);        
        }
    }    

    @FXML
    private void ventanaAdd(ActionEvent event) {
        
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLFormularioAltaSucursal.fxml"));
            Parent ventana = (Parent) fxmlLoader.load();
            Stage stage = new Stage();            
            stage.setScene(new Scene(ventana));
            stage.setTitle("Añadir administrador");
            stage.centerOnScreen();            
            stage.show();
            
        }catch(IOException e){
            String errorMessage = "El tiempo de espera se ha agotado o se perdío la conexión\n" +"con la Base Datos.";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error, No hay conexión con la Base de Datos");
            alert.setHeaderText(" ¡Por favor! intentelo nuevamente");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }          
        
    }

    @FXML
    private void ventanaEdit(ActionEvent event) {
    }

    @FXML
    private void eliminar(ActionEvent event) {
    }
 
    private void busqueda() throws Exception{
        
        tfBusqueda.setOnKeyReleased((e) -> {

            consumirServicioBusqueda(tfBusqueda.getText());
        
        });
        
        
        
    }
    
    
    private void consumirServicioBusqueda(String nombre){
        String urlWS = Constantes.URL_BASE+"sucursales/byNombre/"+nombre;
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaTipoSucursal = new TypeToken<ArrayList <Sucursal> >() {}.getType();
            ArrayList sucursalWS = gson.fromJson(resultadoWS, listaTipoSucursal);
            listaSucursales.addAll(sucursalWS);
            tbSucursal.setItems(listaSucursales);
                    
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de las sucursales:|" +e 
                    , Alert.AlertType.ERROR);
        }
        
        
        
    }      
    
    
    
    private void inicializarColumnasTabla(){
        
        listaSucursales = FXCollections.observableArrayList();
        tcId.setCellValueFactory(new PropertyValueFactory("idSucursal"));
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
        tcCP.setCellValueFactory(new PropertyValueFactory("codigoPostal"));
        tcColonia.setCellValueFactory(new PropertyValueFactory("colonia"));        
        tcCiudad.setCellValueFactory(new PropertyValueFactory("ciudad"));
        tcTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        tcLatitud.setCellValueFactory(new PropertyValueFactory("latitud"));
        tcLongitud.setCellValueFactory(new PropertyValueFactory("longitud"));
        tcEncargado.setCellValueFactory(new PropertyValueFactory("encargado"));
        tcIdEmpresa.setCellValueFactory(new PropertyValueFactory("idEmpresa"));
        
    }    
    
    private void cargarInformacionSucursales(){
        String urlWS = Constantes.URL_BASE+"sucursales/all";
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaTipoSucursal = new TypeToken<ArrayList <Sucursal> >() {}.getType();
            ArrayList administradorWS = gson.fromJson(resultadoWS, listaTipoSucursal);
            listaSucursales.addAll(administradorWS);
            tbSucursal.setItems(listaSucursales);
                    
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de los médicos"
                    , Alert.AlertType.ERROR);
        }
        
    }    
    
    
    
}
