package empresa;

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
import javafx.stage.Stage;
import modelo.ConexionServiciosweb;
import pojos.Catalogo;
import pojos.Empresa;
import pojos.Respuesta;
import util.Constantes;
import util.Utilidades;

public class FXMLFormularioEdicionEmpresaController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfNombreComercial;
    @FXML
    private TextField tfRepresentante;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfCP;
    @FXML
    private TextField tfCiudad;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfPagina;
    @FXML
    private TextField tfRFC;
    @FXML
    private ComboBox cbEstatus;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnCancelar;
    
    private int idEmpresa;
    private boolean isEdicion = false;
    
    ObservableList<Empresa> listaEmpresasR;
    TableView<Empresa> tbEmpresaR;     

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbEstatus.setItems(cargarInformacionCatalogo("3"));
        tfRFC.setDisable(true);
    }
    
    public void inicializarInformacionVentana( Integer idEmpresa,String nombre, String nombreComercial,
                                              String representante,String correo,String direccion,
                                              Integer cp, String ciudad, Integer telefono,
                                              String pagina, String rfc, Integer idEstatus                                        
                                            ){
        
        this.idEmpresa = idEmpresa;
        isEdicion = true;
        tfNombre.setText(nombre);
        tfNombreComercial.setText(nombreComercial);
        tfRepresentante.setText(representante);
        tfCorreo.setText(correo);
        tfDireccion.setText(direccion);
        tfCP.setText(Integer.toString(cp));
        tfCiudad.setText(ciudad);
        tfTelefono.setText(Integer.toString(telefono));
        tfPagina.setText(pagina);
        tfRFC.setText(rfc);
        cbEstatus.setValue(idEstatus);                
    }
    
    

    @FXML
    private void clicActualizarEmpresa(ActionEvent event) {
        
        String nombre = tfNombre.getText();
        String nombreComercial = tfNombreComercial.getText();
        String representante = tfRepresentante.getText();
        String correo = tfCorreo.getText();
        String direccion = tfDireccion.getText();
        Integer cp = Integer.parseInt(tfCP.getText());
        String ciudad = tfCiudad.getText();
        Integer telefono = Integer.parseInt(tfTelefono.getText());
        String pagina = tfPagina.getText();
        Integer estatus = Integer.parseInt(cbEstatus.getValue().toString());     
        
        if(nombre.isEmpty() || nombreComercial.isEmpty()||representante.isEmpty()||correo.isEmpty()||direccion.isEmpty() ||
                cp.toString().isEmpty() || ciudad.isEmpty() || telefono.toString().isEmpty() || pagina.isEmpty()
            ){
            Utilidades.mostrarAlertaSimple("Campos vacios", "Llena los campos vacios",Alert.AlertType.ERROR);            
        }else{
            consumirServicioModificar(idEmpresa, nombre,  nombreComercial,
                                              representante,correo,direccion,
                                              cp, ciudad, telefono,
                                              pagina,  estatus );            
        }        
        
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();        
        stage.close();
    } 
    
    private ObservableList<Catalogo> cargarInformacionCatalogo(String idCategoria){
        String urlWS = Constantes.URL_BASE+"catalogos/bycategoria/"+idCategoria;
        ObservableList<Catalogo> listaCatalogo;
        listaCatalogo = FXCollections.observableArrayList();        
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaCategorias = new TypeToken<ArrayList <Catalogo> >() {}.getType();
            ArrayList catalogoWS = gson.fromJson(resultadoWS, listaCategorias);

            listaCatalogo.addAll(catalogoWS);
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de las promociones"
                    , Alert.AlertType.ERROR);
        }
        return listaCatalogo;
    }
    
    private void consumirServicioModificar(Integer idEmpresa,String nombre, String nombreComercial,
                                              String representante,String correo,String direccion,
                                              Integer cp, String ciudad, Integer telefono,
                                              String pagina, Integer idEstatus  ){
        
        try{
            
            String urlServicio = Constantes.URL_BASE+"empresas/modificar";
            
            String parametros = "idEmpresa=" + idEmpresa+ "&" +
                                "nombre=" + nombre + "&" +
                                "nombreComercial=" + nombreComercial + "&" +
                                "nombreRepresentanteLegal=" + representante + "&" +
                                "correo=" + correo + "&" +
                                "direccion=" + direccion + "&" +
                                "codigoPostal=" + cp + "&" +
                                "ciudad=" + ciudad + "&" +
                                "telefono=" + telefono + "&" +
                                "paginaWeb=" + pagina + "&" +
                                "idEstatus=" + idEstatus 
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
                cargarInformacionEmpresas();
            }else{
                Utilidades.mostrarAlertaSimple("Error al editar la sucursal", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }
                                    
        }catch(Exception e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);            
        }
                      
    }
    
    void recibir(ObservableList<Empresa> listaEmpresas, TableView<Empresa> tbEmpresa){
        listaEmpresasR = listaEmpresas;
        tbEmpresaR = tbEmpresa;
    }
    
    private void cargarInformacionEmpresas(){
        String urlWS = Constantes.URL_BASE+"empresas/all";
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaTipoSucursal = new TypeToken<ArrayList <Empresa> >() {}.getType();
            ArrayList administradorWS = gson.fromJson(resultadoWS, listaTipoSucursal);
            recibir(listaEmpresasR, tbEmpresaR);
            listaEmpresasR.clear();
            listaEmpresasR.addAll(administradorWS);
            tbEmpresaR.setItems(listaEmpresasR);
                    
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de los médicos"
                    , Alert.AlertType.ERROR);
        }
        
    }    
    
    
    
}
