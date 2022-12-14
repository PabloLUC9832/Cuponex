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
import javafx.stage.Modality;
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
            buscar();
        } catch (Exception ex) {
            Utilidades.mostrarAlertaSimple("Error de conexi??n", "Por el momento no se puede obtener la informaci??n de las sucursales" + ex
                    , Alert.AlertType.ERROR);        
        }
        
    }    

    @FXML
    private void ventanaAdd(ActionEvent event) {
        
        try{
            FXMLLoader loadController = new FXMLLoader(getClass().getResource("FXMLFormularioAltaSucursal.fxml"));
            Parent vistaFormulario = loadController.load();
            FXMLFormularioAltaSucursalController controllerFormulario = loadController.getController();
            
            controllerFormulario.recibir(listaSucursales, tbSucursal);
            
            Scene escenaFormulario = new Scene(vistaFormulario);
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(escenaFormulario);
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
            
        }catch(IOException e){
            String errorMessage = "El tiempo de espera se ha agotado o se perd??o la conexi??n\n" +"con la Base Datos.";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error, No hay conexi??n con la Base de Datos");
            alert.setHeaderText(" ??Por favor! intentelo nuevamente");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }          
        
    }

    @FXML
    private void ventanaEdit(ActionEvent event) {
        
        int filaSeleccionada = tbSucursal.getSelectionModel().getSelectedIndex();

        if(filaSeleccionada >= 0){

            try{
                
                int idSucursalSeleccionado = listaSucursales.get(filaSeleccionada).getIdSucursal();
                String nombre = listaSucursales.get(filaSeleccionada).getNombre();
                String direccion = listaSucursales.get(filaSeleccionada).getDireccion();
                Integer cp = listaSucursales.get(filaSeleccionada).getCodigoPostal();
                String colonia = listaSucursales.get(filaSeleccionada).getColonia();
                String ciudad = listaSucursales.get(filaSeleccionada).getCiudad();
                Integer telefono  = listaSucursales.get(filaSeleccionada).getTelefono();
                String latitud = listaSucursales.get(filaSeleccionada).getLatitud();
                String longitud = listaSucursales.get(filaSeleccionada).getLongitud();
                String encargado = listaSucursales.get(filaSeleccionada).getEncargado();
                Integer idEmpresa = listaSucursales.get(filaSeleccionada).getIdEmpresa();

                FXMLLoader loadController = new FXMLLoader(getClass().getResource("FXMLFormularioEdicionSucursal.fxml"));
                Parent vistaFormulario = loadController.load();
                FXMLFormularioEdicionSucursalController controllerFormulario = loadController.getController();
                
                controllerFormulario.inicializarInformacionVentana(idSucursalSeleccionado, nombre, direccion,cp, 
                                                                   colonia, ciudad,telefono,latitud,
                                                                   longitud,encargado,idEmpresa
                                                                   );
                
                controllerFormulario.recibir(listaSucursales, tbSucursal);
                                
                Scene escenaFormulario = new Scene(vistaFormulario);
                Stage escenarioFormulario = new Stage();
                escenarioFormulario.setScene(escenaFormulario);
                escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
                escenarioFormulario.showAndWait();
                
            }catch(IOException e){
                Utilidades.mostrarAlertaSimple("Error", "No se ha podido cargar la ventana principal -"+e, Alert.AlertType.ERROR);                
            }


        }else{
            Utilidades.mostrarAlertaSimple("Selecciona un registro", "Debes seleccionar una sucursal para su modificaci??n"
                    , Alert.AlertType.WARNING);
        }         
        
        
    }

    @FXML
    private void eliminar(ActionEvent event) {
        
        int filaSeleccionada = tbSucursal.getSelectionModel().getSelectedIndex();

        if(filaSeleccionada >= 0){

            try{
                
                int idSucursalSeleccionado = listaSucursales.get(filaSeleccionada).getIdSucursal();
          
                if(Utilidades.mostrarAlertaEliminacion("Eliminar", "sucursal")==true){
                    consumirServicioEliminar(idSucursalSeleccionado);
                    listaSucursales.clear();
                    cargarInformacionSucursales();                    
                }
                
            }catch(Exception e){
                Utilidades.mostrarAlertaSimple("Error", "No se ha podido cargar la ventana principal -"+e, Alert.AlertType.ERROR);                
            }


        }else{
            Utilidades.mostrarAlertaSimple("Selecciona un registro", "Debes seleccionar una sucursal para su modificaci??n"
                    , Alert.AlertType.WARNING);
        }        
                
        
        
        
    }
     
    private void buscar() throws Exception{
        
        
        tfBusqueda.setOnKeyReleased((e) -> {

            
            if(tfBusqueda.getText().equals("")){
                listaSucursales.clear();
                cargarInformacionSucursales();
            }else{
                listaSucursales.clear();
                consumirServicioBusqueda(tfBusqueda.getText());                
            }
                    
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
            Utilidades.mostrarAlertaSimple("Error de conexi??n", "Por el momento no se puede obtener la informaci??n de las sucursales:|" +e 
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
            Utilidades.mostrarAlertaSimple("Error de conexi??n", "Por el momento no se puede obtener la informaci??n de los m??dicos"
                    , Alert.AlertType.ERROR);
        }
        
    }
    
    private void consumirServicioEliminar(int idSucursal){
        
        try{
            
            String urlServicio = Constantes.URL_BASE+"sucursales/eliminar";
            
            String parametros = "idSucursal=" + idSucursal;
            String resultadoWS = ConexionServiciosweb.peticionServicioDelete(urlServicio, parametros);
            Gson gson = new Gson() ;
            Respuesta respuesta = gson.fromJson(resultadoWS, Respuesta.class);
            
            if (!respuesta.getError()) {
                
                Utilidades.mostrarAlertaSimple("Sucursal eliminada", 
                        "Sucursal eliminado correctamente "
                        , Alert.AlertType.INFORMATION);
            }else{
                Utilidades.mostrarAlertaSimple("Error al eliminar al administrador", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }            
            
            
        }catch(Exception e){
            Utilidades.mostrarAlertaSimple("Error de conexi??n", e.getMessage(), Alert.AlertType.ERROR);            
        }
        
    }
    
    
    
    
}
