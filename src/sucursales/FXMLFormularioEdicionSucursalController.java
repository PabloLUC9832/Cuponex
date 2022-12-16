package sucursales;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import modelo.ConexionServiciosweb;
import pojos.Empresa;
import pojos.Respuesta;
import pojos.Sucursal;
import util.Constantes;
import util.Utilidades;


public class FXMLFormularioEdicionSucursalController implements Initializable {

    private int idSucursal;
    private boolean isEdicion = false;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfCP;
    @FXML
    private TextField tfColonia;
    @FXML
    private TextField tfCiudad;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfLatitud;
    @FXML
    private TextField tfLongitud;
    @FXML
    private TextField tfEncargado;
    @FXML
    private ComboBox cbEmpresa;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnCancelar;

    ObservableList<Sucursal> listaSucursalesR;
    TableView<Sucursal> tbSucursalR;       
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tfNombre.addEventHandler(KeyEvent.KEY_TYPED, event -> soloLetras(event));
        tfCP.addEventHandler(KeyEvent.KEY_TYPED, event -> soloNumeros(event));
        tfTelefono.addEventHandler(KeyEvent.KEY_TYPED, event -> soloNumeros(event));  

        cbEmpresa.setItems(cargarInformacionEmpresas());
                        
    }    
    
    public void inicializarInformacionVentana(Integer idSucursalSeleccionado, String nombre,String direccion, Integer cp, 
                                                                   String colonia, String ciudad, Integer telefono,String latitud,
                                                                   String longitud, String encargado, Integer idEmpresa
                                                                   ){
        
        this.idSucursal = idSucursalSeleccionado;
        isEdicion = true;
        tfNombre.setText(nombre);
        tfDireccion.setText(direccion);
        tfCP.setText(cp.toString());
        tfColonia.setText(colonia);
        tfCiudad.setText(ciudad);
        tfTelefono.setText(telefono.toString());
        tfLatitud.setText(latitud);
        tfLongitud.setText(longitud);
        tfEncargado.setText(encargado);
        cbEmpresa.setValue(idEmpresa);
        
    }

    @FXML
    private void clicActualizarSucursal(ActionEvent event) {
        
        String nombre = tfNombre.getText();
        String direccion = tfDireccion.getText();
        Integer cp = Integer.parseInt(tfCP.getText().toString());
        String colonia = tfColonia.getText();
        String ciudad = tfCiudad.getText();
        Integer telefono = Integer.parseInt(tfTelefono.getText());
        String latitud = tfLatitud.getText();
        String longitud = tfLongitud.getText();
        String encargado = tfEncargado.getText();
        Integer empresa = 0;
        String idEmpre = cbEmpresa.getValue().toString();
        String[] parts = idEmpre.split("-");
        String partID = parts[0];
        String partNombre = parts[1];        

        if(nombre.isEmpty() || direccion.isEmpty()||tfCP.getText().isEmpty()||colonia.isEmpty()||ciudad.isEmpty() ||
                tfTelefono.getText().isEmpty() || latitud.isEmpty() || longitud.isEmpty() || encargado.isEmpty()
            ){
            Utilidades.mostrarAlertaSimple("Campos vacios", "Llena los campos vacios",Alert.AlertType.ERROR);            
        }else{
            consumirServicioModificar(idSucursal,nombre, direccion, cp, colonia, ciudad, telefono,latitud,longitud,encargado,Integer.parseInt(partID));
        }
        
        
        
        
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();        
        stage.close();     
    }
    
    
    private void consumirServicioModificar(Integer idSucursalSeleccionado, String nombre,String direccion, Integer cp, 
                                                                   String colonia, String ciudad, Integer telefono,String latitud,
                                                                   String longitud, String encargado, Integer idEmpresa){
        
        try{
            
            String urlServicio = Constantes.URL_BASE+"sucursales/modificar";
            
            String parametros = "idSucursal=" + idSucursalSeleccionado+ "&" +
                                "nombre=" + nombre + "&" +
                                "direccion=" + direccion + "&" +
                                "codigoPostal=" + cp + "&" +
                                "colonia=" + colonia + "&" +
                                "ciudad=" + ciudad + "&" +
                                "telefono=" + telefono + "&" +
                                "latitud=" + latitud + "&" +
                                "longitud=" + longitud + "&" +
                                "encargado=" + encargado + "&" +
                                "idEmpresa=" + idEmpresa
                                ;
            
            String resultadoWS = ConexionServiciosweb.peticionServicioPUT(urlServicio, parametros);
            Gson gson = new Gson() ;
            Respuesta respuesta = gson.fromJson(resultadoWS, Respuesta.class);
            
            if (!respuesta.getError()) {                
                Utilidades.mostrarAlertaSimple("Sucursal actualizada", 
                        "Sucursal actualizada correctamente "
                        , Alert.AlertType.INFORMATION);
                Stage stage = (Stage) this.btnActualizar.getScene().getWindow();
                stage.close();
                cargarInformacionSucursales();
            }else{
                Utilidades.mostrarAlertaSimple("Error al editar la sucursal", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }
                                    
        }catch(Exception e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);            
        }
                      
    }
    
    private ObservableList<Empresa> cargarInformacionEmpresas(){
        String urlWS = Constantes.URL_BASE+"empresas/all";
        ObservableList<Empresa> listaEmpresa;
        listaEmpresa = FXCollections.observableArrayList();        
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaEmpresas = new TypeToken<ArrayList <Empresa> >() {}.getType();
            ArrayList catalogoWS = gson.fromJson(resultadoWS, listaEmpresas);

            listaEmpresa.addAll(catalogoWS);
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de las empresas"
                    , Alert.AlertType.ERROR);
        }
        return listaEmpresa;
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
    
    public void soloNumeros(KeyEvent keyEvent){
        try{
            char key = keyEvent.getCharacter().charAt(0);
            if(!Character.isDigit(key)){
                keyEvent.consume();
            }
        }catch(Exception e){
          
        }
    }
    
    void recibir(ObservableList<Sucursal> listaSucursales, TableView<Sucursal> tbSucursal){
        listaSucursalesR = listaSucursales;
        tbSucursalR = tbSucursal;
    }    
    
    private void cargarInformacionSucursales(){
        String urlWS = Constantes.URL_BASE+"sucursales/all";
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaTipoSucursal = new TypeToken<ArrayList <Sucursal> >() {}.getType();
            ArrayList administradorWS = gson.fromJson(resultadoWS, listaTipoSucursal);
            recibir(listaSucursalesR, tbSucursalR);
            listaSucursalesR.clear();
            listaSucursalesR.addAll(administradorWS);
            tbSucursalR.setItems(listaSucursalesR);
                    
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de los médicos"
                    , Alert.AlertType.ERROR);
        }
        
    }    
    
    
}
