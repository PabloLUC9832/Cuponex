package promociones;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pojos.Promocion;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import modelo.ConexionServiciosweb;
import util.Constantes;
import util.Utilidades;

public class FXMLGeneralPromocionController implements Initializable {

    @FXML
    private TableView<Promocion> tbPromocion;
    @FXML
    private TableColumn tcId;
    @FXML
    private TableColumn tcNombre;
    @FXML
    private TableColumn tcDescripcion;
    @FXML
    private TableColumn tcFechaInicio;
    @FXML
    private TableColumn tcFechaTermino;
    @FXML
    private TableColumn tcRestricciones;
    @FXML
    private TableColumn tcTipoPromocion;
    @FXML
    private TableColumn tcPorcentaje;
    @FXML
    private TableColumn tcCostoPromocion;
    @FXML
    private TableColumn tcCategoriaPromocion;
    @FXML
    private TableColumn tcEstatus;
    @FXML
    private TableColumn tcSucursal;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField tfBusqueda;
    
    private ObservableList<Promocion> listaPromociones;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarColumnasTabla();
        cargarInformacionPromociones();
        try {
            buscar();
        } catch (Exception ex) {
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de las promociones" + ex
                    , Alert.AlertType.ERROR);        
        }        
    }    

    @FXML
    private void ventanaAdd(ActionEvent event) {
    }

    @FXML
    private void ventanaEdit(ActionEvent event) {
    }

    @FXML
    private void eliminar(ActionEvent event) {
    }
    
    private void inicializarColumnasTabla(){
        
        listaPromociones = FXCollections.observableArrayList();
        tcId.setCellValueFactory(new PropertyValueFactory("idPromocion"));
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
        tcFechaInicio.setCellValueFactory(new PropertyValueFactory("fechaInicio"));
        tcFechaTermino.setCellValueFactory(new PropertyValueFactory("fechaTermino"));        
        tcRestricciones.setCellValueFactory(new PropertyValueFactory("restricciones"));
        tcTipoPromocion.setCellValueFactory(new PropertyValueFactory("tipoPromocion"));
        tcPorcentaje.setCellValueFactory(new PropertyValueFactory("porcentaje"));
        tcCostoPromocion.setCellValueFactory(new PropertyValueFactory("costoPromocion"));
        tcCategoriaPromocion.setCellValueFactory(new PropertyValueFactory("categoriaPromocion"));
        tcEstatus.setCellValueFactory(new PropertyValueFactory("idEstatus"));
        tcSucursal.setCellValueFactory(new PropertyValueFactory("idSucursal"));
        
    }
    
    private void cargarInformacionPromociones(){
        
        String urlWS = Constantes.URL_BASE+"promociones/all";
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type listaTipoPromocion = new TypeToken<ArrayList <Promocion>>() {}.getType();
            ArrayList promocionWS = gson.fromJson(resultadoWS, listaTipoPromocion);
            listaPromociones.addAll(promocionWS);
            tbPromocion.setItems(listaPromociones);
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", 
                    "Por el momento no se puede obtener la información de las promociones"
                    , Alert.AlertType.ERROR);
        }
                
    }
    
    private void buscar() throws Exception{
                
        tfBusqueda.setOnKeyReleased((e) -> {
            
            if(tfBusqueda.getText().equals("")){
                listaPromociones.clear();
                cargarInformacionPromociones();
            }else{
                listaPromociones.clear();
                consumirServicioBusqueda(tfBusqueda.getText());
            }
                    
        });
                
    }    
    
    
    private void consumirServicioBusqueda(String nombre){
        String urlWS = Constantes.URL_BASE+"promociones/byNombre/"+nombre;
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaTipoPromocion = new TypeToken<ArrayList <Promocion> >() {}.getType();
            ArrayList sucursalWS = gson.fromJson(resultadoWS, listaTipoPromocion);
            //listaSucursales.clear();
            listaPromociones.addAll(sucursalWS);
            tbPromocion.setItems(listaPromociones);
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de las promociones:|" +e 
                    , Alert.AlertType.ERROR);
        }
                        
    }

    

    
    
}
