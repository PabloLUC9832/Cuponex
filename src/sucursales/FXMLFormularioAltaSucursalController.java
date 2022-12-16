package sucursales;

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

public class FXMLFormularioAltaSucursalController implements Initializable {

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
    private Button btnGuardar;
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

    @FXML
    private void clicGuardarSucursal(ActionEvent event) {
        
        String nombre = tfNombre.getText();
        String direccion = tfDireccion.getText();
        Integer cp = Integer.parseInt(tfCP.getText().toString());
        String colonia = tfColonia.getText();
        String ciudad = tfCiudad.getText();
        Integer telefono = Integer.parseInt(tfTelefono.getText());
        String latitud = tfLatitud.getText();
        String longitud = tfLongitud.getText();
        String encargado = tfEncargado.getText();
        //Integer empresa = Integer.parseInt(cbEmpresa.getValue().toString());
        String idEmpre = cbEmpresa.getValue().toString();
        String[] parts = idEmpre.split("-");
        String partID = parts[0];
        String partNombre = parts[1];
                
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(nombre);
        sucursal.setDireccion(direccion);
        sucursal.setCodigoPostal(cp);
        sucursal.setColonia(colonia);
        sucursal.setCiudad(ciudad);
        sucursal.setTelefono(telefono);
        sucursal.setLatitud(latitud);
        sucursal.setLongitud(longitud);
        sucursal.setEncargado(encargado);
        sucursal.setIdEmpresa(Integer.parseInt(partID));
        
        guardarInformacionSucursal(sucursal);
        
    }

    private void guardarInformacionSucursal(Sucursal sucursal){
        
        try{
            String urlServicio = Constantes.URL_BASE+"sucursales/registrar";
            String parametros = "nombre="+sucursal.getNombre() + "&" +
                                "direccion="+sucursal.getDireccion()+ "&" +
                                "codigoPostal="+sucursal.getCodigoPostal()+ "&" +
                                "colonia="+sucursal.getColonia()+ "&" +
                                "ciudad="+sucursal.getCiudad()+ "&" +
                                "telefono="+sucursal.getTelefono()+ "&" +
                                "latitud="+sucursal.getLatitud()+ "&" +                    
                                "longitud="+sucursal.getLongitud()+ "&" +                    
                                "encargado="+sucursal.getEncargado()+ "&" +
                                "idEmpresa="+sucursal.getIdEmpresa()
                                ;
            String resultado = ConexionServiciosweb.peticionServicioPOST(urlServicio, parametros);
            Gson gson = new Gson();
            Respuesta respuesta = gson.fromJson(resultado, Respuesta.class);
            
            if (!respuesta.getError()) {
                
                Utilidades.mostrarAlertaSimple("Sucursal añadida", "Sucursal añadido correctamente "
                        , Alert.AlertType.INFORMATION);  
                Stage stage = (Stage) this.btnGuardar.getScene().getWindow();
                stage.close();
                cargarInformacionSucursales();
            }else{
                Utilidades.mostrarAlertaSimple("Error al añadir sucursal", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }              

        }catch(IOException e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);                        
        }
        
        
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
    
    public void soloNumeros(KeyEvent keyEvent){
        try{
            char key = keyEvent.getCharacter().charAt(0);
            if(!Character.isDigit(key)){
                keyEvent.consume();
            }
        }catch(Exception e){
          
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
            System.out.println("||"+catalogoWS+"||");
            listaEmpresa.addAll(catalogoWS);
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de las empresas"
                    , Alert.AlertType.ERROR);
        }
        return listaEmpresa;
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
