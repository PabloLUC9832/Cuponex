package empresa;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.ConexionServiciosweb;
import pojos.Empresa;
import pojos.Respuesta;
import util.Constantes;
import util.Utilidades;

public class FXMLGeneralEmpresaController implements Initializable {

    @FXML
    private TableView<Empresa> tbEmpresa;
    @FXML
    private TableColumn tcId;
    @FXML
    private TableColumn tcNombre;
    @FXML
    private TableColumn tcNombreComercial;
    @FXML
    private TableColumn tcRepresentanteLegal;
    @FXML
    private TableColumn tcCorreo;
    @FXML
    private TableColumn tcDireccion;
    @FXML
    private TableColumn tcCP;
    @FXML
    private TableColumn tcCiudad;
    @FXML
    private TableColumn tcTelefono;
    @FXML
    private TableColumn tcPaginaWeb;
    @FXML
    private TableColumn tcRFC;
    @FXML
    private TableColumn tcEstatus;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField tfBusqueda;
    
    private ObservableList<Empresa> listaEmpresas;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarColumnasTabla();
        cargarInformacionEmpresas();
        try {
            buscar();
        } catch (Exception ex) {
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de las sucursales" + ex
                    , Alert.AlertType.ERROR);        
        }        
    }    

    @FXML
    private void ventanaAdd(ActionEvent event) {
        
        try{
            /*FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLFormularioAltaEmpresa.fxml"));
            Parent ventana = (Parent) fxmlLoader.load();
            Stage stage = new Stage();            
            stage.setScene(new Scene(ventana));
            stage.setTitle("Añadir Empresa");
            stage.centerOnScreen();            
            stage.show();*/
            
            FXMLLoader loadController = new FXMLLoader(getClass().getResource("FXMLFormularioAltaEmpresa.fxml"));
            Parent vistaFormulario = loadController.load();
            FXMLFormularioAltaEmpresaController controllerFormulario = loadController.getController();
            
            controllerFormulario.recibir(listaEmpresas, tbEmpresa);
            
            Scene escenaFormulario = new Scene(vistaFormulario);
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(escenaFormulario);
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();               
            
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

        int filaSeleccionada = tbEmpresa.getSelectionModel().getSelectedIndex();

        if(filaSeleccionada >= 0){

            try{
                
                int idEmpresaSeleccionado = listaEmpresas.get(filaSeleccionada).getIdEmpresa();
                String nombre = listaEmpresas.get(filaSeleccionada).getNombre();
                String nombreComercial = listaEmpresas.get(filaSeleccionada).getNombreComercial() ;
                String representante = listaEmpresas.get(filaSeleccionada).getNombreRepresentanteLegal() ;
                String correo = listaEmpresas.get(filaSeleccionada).getCorreo() ;
                String direccion = listaEmpresas.get(filaSeleccionada).getDireccion() ;
                Integer cp = listaEmpresas.get(filaSeleccionada).getCodigoPostal() ;
                String ciudad = listaEmpresas.get(filaSeleccionada).getCiudad() ;
                Integer telefono = listaEmpresas.get(filaSeleccionada).getTelefono() ;
                String pagina = listaEmpresas.get(filaSeleccionada).getPaginaWeb() ;
                String rfc = listaEmpresas.get(filaSeleccionada).getRfc() ;
                Integer idEstatus = listaEmpresas.get(filaSeleccionada).getIdEstatus() ;


                FXMLLoader loadController = new FXMLLoader(getClass().getResource("FXMLFormularioEdicionEmpresa.fxml"));
                Parent vistaFormulario = loadController.load();
                FXMLFormularioEdicionEmpresaController controllerFormulario = loadController.getController();
                
                controllerFormulario.inicializarInformacionVentana(idEmpresaSeleccionado, nombre,  nombreComercial,
                                              representante,correo,direccion,
                                              cp, ciudad, telefono,
                                              pagina,  rfc,  idEstatus   
                                            );
                
                controllerFormulario.recibir(listaEmpresas, tbEmpresa);

                
                Scene escenaFormulario = new Scene(vistaFormulario);
                Stage escenarioFormulario = new Stage();
                escenarioFormulario.setScene(escenaFormulario);
                escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
                escenarioFormulario.showAndWait();
                
            }catch(IOException e){
                Utilidades.mostrarAlertaSimple("Error", "No se ha podido cargar la ventana principal -"+e, Alert.AlertType.ERROR);                
            }


        }else{
            Utilidades.mostrarAlertaSimple("Selecciona un registro", "Debes seleccionar una sucursal para su modificación"
                    , Alert.AlertType.WARNING);
        }         
               
    }

    @FXML
    private void eliminar(ActionEvent event) {
        
        int filaSeleccionada = tbEmpresa.getSelectionModel().getSelectedIndex();

        if(filaSeleccionada >= 0){

            try{
                
                int idEmpresaSeleccionado = listaEmpresas.get(filaSeleccionada).getIdEmpresa();
          
                if(Utilidades.mostrarAlertaEliminacion("Elminar", "empresa")==true){
                    consumirServicioEliminar(idEmpresaSeleccionado);
                    listaEmpresas.clear();
                    cargarInformacionEmpresas();
                }
                
            }catch(Exception e){
                Utilidades.mostrarAlertaSimple("Error", "No se ha podido cargar la ventana principal -"+e, Alert.AlertType.ERROR);                
            }


        }else{
            Utilidades.mostrarAlertaSimple("Selecciona un registro", "Debes seleccionar una empresa para su modificación"
                    , Alert.AlertType.WARNING);
        }           
        
    }
    
    private void buscar() throws Exception{
        
        tfBusqueda.setOnKeyReleased((e) -> {
            
            if(tfBusqueda.getText().equals("")){
                listaEmpresas.clear();
                cargarInformacionEmpresas();
            }else{
                listaEmpresas.clear();
                consumirServicioBusqueda(tfBusqueda.getText());                
            }
                    
        });
                
    }
    
    private void consumirServicioBusqueda(String nombre){
        String urlWS = Constantes.URL_BASE+"empresas/byNombre/"+nombre;
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaTipoEmpresa = new TypeToken<ArrayList <Empresa> >() {}.getType();
            ArrayList empresaWS = gson.fromJson(resultadoWS, listaTipoEmpresa);
            listaEmpresas.addAll(empresaWS);
            tbEmpresa.setItems(listaEmpresas);
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de las sucursales:|" +e 
                    , Alert.AlertType.ERROR);
        }
                        
    }
    
    private void inicializarColumnasTabla(){
        
        listaEmpresas = FXCollections.observableArrayList();
        tcId.setCellValueFactory(new PropertyValueFactory("idEmpresa"));
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcNombreComercial.setCellValueFactory(new PropertyValueFactory("nombreComercial"));
        tcRepresentanteLegal.setCellValueFactory(new PropertyValueFactory("nombreRepresentanteLegal"));
        tcCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        tcDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
        tcCP.setCellValueFactory(new PropertyValueFactory("codigoPostal"));
        tcCiudad.setCellValueFactory(new PropertyValueFactory("ciudad"));
        tcTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        tcPaginaWeb.setCellValueFactory(new PropertyValueFactory("paginaWeb"));
        tcRFC.setCellValueFactory(new PropertyValueFactory("rfc"));
        tcEstatus.setCellValueFactory(new PropertyValueFactory("idEstatus"));
        
    }
    
    private void cargarInformacionEmpresas(){
        String urlWS = Constantes.URL_BASE+"empresas/all";
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaTipoSucursal = new TypeToken<ArrayList <Empresa> >() {}.getType();
            ArrayList administradorWS = gson.fromJson(resultadoWS, listaTipoSucursal);
            listaEmpresas.addAll(administradorWS);
            tbEmpresa.setItems(listaEmpresas);
                    
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de los médicos"
                    , Alert.AlertType.ERROR);
        }
        
    }
    
    private void consumirServicioEliminar(int idEmpresa){
        
        try{
            
            String urlServicio = Constantes.URL_BASE+"empresas/eliminar";
            
            String parametros = "idEmpresa=" + idEmpresa;
            String resultadoWS = ConexionServiciosweb.peticionServicioDelete(urlServicio, parametros);
            Gson gson = new Gson() ;
            Respuesta respuesta = gson.fromJson(resultadoWS, Respuesta.class);
            
            if (!respuesta.getError()) {
                
                Utilidades.mostrarAlertaSimple("Empresa eliminada", 
                        "Empresa eliminada correctamente "
                        , Alert.AlertType.INFORMATION);
            }else{
                Utilidades.mostrarAlertaSimple("Error al eliminar la empresa", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }            
            
            
        }catch(Exception e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);            
        }
        
    }    
    
}
