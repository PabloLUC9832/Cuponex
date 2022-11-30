package promociones;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import modelo.ConexionServiciosweb;
import pojos.Respuesta;
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
        
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLFormularioAltaPromocion.fxml"));
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
        
        int filaSeleccionada = tbPromocion.getSelectionModel().getSelectedIndex();

        if(filaSeleccionada >= 0){
            try{                
                int idPromocionSeleccionado = listaPromociones.get(filaSeleccionada).getIdPromocion();
          
                if(Utilidades.mostrarAlertaEliminacion("Elminar", "promocion")==true){
                    consumirServicioEliminar(idPromocionSeleccionado);
                }
                
            }catch(Exception e){
                Utilidades.mostrarAlertaSimple("Error", "No se ha podido cargar la ventana principal -"+e, Alert.AlertType.ERROR);                
            }
        }else{
            Utilidades.mostrarAlertaSimple("Selecciona un registro", "Debes seleccionar una promoción para su eliminación"
                    , Alert.AlertType.WARNING);
        }   
        
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
    
    private void consumirServicioEliminar(int idPromocion){
        
        try{
            
            String urlServicio = Constantes.URL_BASE+"promociones/eliminar";
            
            String parametros = "idPromocion=" + idPromocion;
            String resultadoWS = ConexionServiciosweb.peticionServicioDelete(urlServicio, parametros);
            Gson gson = new Gson() ;
            Respuesta respuesta = gson.fromJson(resultadoWS, Respuesta.class);
            
            if (!respuesta.getError()) {
                
                Utilidades.mostrarAlertaSimple("Promoción eliminada", 
                        "Promoción eliminado correctamente "
                        , Alert.AlertType.INFORMATION);
            }else{
                Utilidades.mostrarAlertaSimple("Error al eliminar promoción", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }            
            
            
        }catch(Exception e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);            
        }
        
    }    

    

    
    
}