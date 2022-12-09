package administrador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.Catalogo;
import pojos.Respuesta;
import util.Utilidades;

public class FXMLGeneralAdministradorController implements Initializable {

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

    private ObservableList<Administrador> listaAdministradores;    
    @FXML
    private Button btEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarColumnasTabla();
        cargarInformacionAdministradores();
    }    
    
    private void inicializarColumnasTabla(){
        
        listaAdministradores = FXCollections.observableArrayList();        
        tcId.setCellValueFactory(new PropertyValueFactory("idAdministrador"));
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        tcMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        tcCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        
    }
    
    private void cargarInformacionAdministradores(){
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
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de los administradores"
                    , Alert.AlertType.ERROR);
        }
        
    }
        
    @FXML
    private void ventanaAdd(ActionEvent event) {
        
        try{
            /*
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLFormularioAltaAdministrador.fxml"));
            Parent ventana = (Parent) fxmlLoader.load();
            Stage stage = new Stage();            
            stage.setScene(new Scene(ventana));
            stage.setTitle("Añadir administrador");
            stage.centerOnScreen();            
            stage.show();
            */
            FXMLLoader loadController = new FXMLLoader(getClass().getResource("FXMLFormularioAltaAdministrador.fxml"));
            Parent vistaFormulario = loadController.load();
            FXMLFormularioAltaAdministradorController controllerFormulario = loadController.getController();
            
            controllerFormulario.recibir(listaAdministradores, tbAdministrador);
            
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
       
        int filaSeleccionada = tbAdministrador.getSelectionModel().getSelectedIndex();

        if(filaSeleccionada >= 0){

            try{
                
                int idAdministradorSeleccionado = listaAdministradores.get(filaSeleccionada).getIdAdministrador();
                String nombre = listaAdministradores.get(filaSeleccionada).getNombre();
                String apellidoPaterno = listaAdministradores.get(filaSeleccionada).getApellidoPaterno();
                String apellidoMaterno = listaAdministradores.get(filaSeleccionada).getApellidoMaterno();
                String correo = listaAdministradores.get(filaSeleccionada).getCorreo();
                String password = listaAdministradores.get(filaSeleccionada).getPassword();            

                FXMLLoader loadController = new FXMLLoader(getClass().getResource("FXMLFormularioEdicionAdministrador.fxml"));
                Parent vistaFormulario = loadController.load();
                FXMLFormularioEdicionAdministradorController controllerFormulario = loadController.getController();
                
                controllerFormulario.inicializarInformacionVentana(idAdministradorSeleccionado, nombre, apellidoPaterno, apellidoMaterno, correo, password);
                
                controllerFormulario.recibir(listaAdministradores, tbAdministrador);
                
                Scene escenaFormulario = new Scene(vistaFormulario);
                Stage escenarioFormulario = new Stage();
                escenarioFormulario.setScene(escenaFormulario);
                escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
                escenarioFormulario.showAndWait();
                
            }catch(IOException e){
                Utilidades.mostrarAlertaSimple("Error", "No se ha podido cargar la ventana principal -"+e, Alert.AlertType.ERROR);                
            }


        }else{
            Utilidades.mostrarAlertaSimple("Selecciona un registro", "Debes seleccionar un administrador para su modificación"
                    , Alert.AlertType.WARNING);
        }
         
        
    }

    @FXML
    private void eliminar(ActionEvent event) {
        
        int filaSeleccionada = tbAdministrador.getSelectionModel().getSelectedIndex();

        if(filaSeleccionada >= 0){

            try{
                
                int idAdministradorSeleccionado = listaAdministradores.get(filaSeleccionada).getIdAdministrador();
          
                if(Utilidades.mostrarAlertaEliminacion("Elminar", "administrador")==true){
                    consumirServicioEliminar(idAdministradorSeleccionado);
                    listaAdministradores.clear();
                    cargarInformacionAdministradores();
                }
                
            }catch(Exception e){
                Utilidades.mostrarAlertaSimple("Error", "No se ha podido cargar la ventana principal -"+e, Alert.AlertType.ERROR);                
            }


        }else{
            Utilidades.mostrarAlertaSimple("Selecciona un registro", "Debes seleccionar un administrador para su modificación"
                    , Alert.AlertType.WARNING);
        }        
        
        
    }
    
    private void consumirServicioEliminar(int idAdministrador){
        
        try{
            
            String urlServicio = Constantes.URL_BASE+"administradores/eliminar";
            
            String parametros = "idAdministrador=" + idAdministrador;
            String resultadoWS = ConexionServiciosweb.peticionServicioDelete(urlServicio, parametros);
            Gson gson = new Gson() ;
            Respuesta respuesta = gson.fromJson(resultadoWS, Respuesta.class);
            
            if (!respuesta.getError()) {
                
                Utilidades.mostrarAlertaSimple("Administrador eliminado", 
                        " Administrador eliminado correctamente "
                        , Alert.AlertType.INFORMATION);
            }else{
                Utilidades.mostrarAlertaSimple("Error al eliminar el administrador", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }            
            
            
        }catch(Exception e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);            
        }
        
    }
    
    
}
