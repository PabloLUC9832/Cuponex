package promociones;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import modelo.ConexionServiciosweb;
import pojos.Catalogo;
import pojos.Promocion;
import pojos.Respuesta;
import pojos.Sucursal;
import util.Constantes;
import util.Utilidades;
public class FXMLEditarPromocionController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaTermino;
    @FXML
    private TextField tfRestricciones;
    @FXML
    private ComboBox cbTipoPromocion;
    @FXML
    private TextField tfPorcentaje;
    @FXML
    private TextField tfCostoPromocion;
    @FXML
    private ComboBox cbCategoria;
    @FXML
    private ComboBox cbEstatus;
    @FXML
    private ComboBox cbSucursal;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnCancelar;
    
    private int idPromocion;
    private boolean isEdicion = false;
    
    ObservableList<Promocion> listaPromocionesR;
    TableView<Promocion> tbPromocionR;       
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfNombre.addEventHandler(KeyEvent.KEY_TYPED, event -> soloLetras(event));
        tfCostoPromocion.addEventHandler(KeyEvent.KEY_TYPED, event -> soloNumeros(event));
        
        cbTipoPromocion.setItems(cargarInformacionCatalogo("1"));
        cbCategoria.setItems(cargarInformacionCatalogo("2"));
        cbEstatus.setItems(cargarInformacionCatalogo("3"));
        cbSucursal.setItems(cargarInformacionSucursales());
        
        dpFechaInicio.getEditor().setDisable(true);
        dpFechaTermino.getEditor().setDisable(true);
    }    
    
    public void inicializarInformacionVentana(Integer idPromocion, String nombre,String descripcion, String fechaInicio, 
                                              String fechaTermino, String restricciones, Integer tipoPromocion,String porcentaje,
                                              float costoPromocion, Integer categoriaPromocion,Integer idEstatus, Integer idSucursal
                                              ){
        
        this.idPromocion = idPromocion;
        isEdicion = true;
        tfNombre.setText(nombre);
        tfDescripcion.setText(descripcion);
        dpFechaInicio.getEditor().setText(fechaInicio);
        dpFechaTermino.getEditor().setText(fechaTermino);
        tfRestricciones.setText(restricciones);
        cbTipoPromocion.setValue(tipoPromocion);
        tfPorcentaje.setText(porcentaje);
        tfCostoPromocion.setText(Float.toString(costoPromocion));
        cbCategoria.setValue(categoriaPromocion);
        cbEstatus.setValue(idEstatus);
        cbSucursal.setValue(idSucursal);
    }
        
    @FXML
    private void clicActualizarPromocion(ActionEvent event) {
        
        String nombre = tfNombre.getText();
        String descripcion = tfDescripcion.getText();               
        String restricciones = tfRestricciones.getText();
        
        String idTipPromo = cbTipoPromocion.getValue().toString();
        String[] partsTipPromo = idTipPromo.split("-");
        String partIDTipPromo = partsTipPromo[0];       
        
        String porcentaje = tfPorcentaje.getText();
        Float costoPromocion = Float.valueOf(tfCostoPromocion.getText());
        
        String idCatPromo = cbCategoria.getValue().toString();
        String[] partsCatPromo = idCatPromo.split("-");
        String partIDCatPromo = partsCatPromo[0];         
        
        String idEstPromo = cbEstatus.getValue().toString();
        String[] partsEstPromo = idEstPromo.split("-");
        String partIDEstPromo = partsEstPromo[0];     
        
        String idSucPromo = cbSucursal.getValue().toString();
        String[] partsSucPromo = idSucPromo.split("-");
        String partIDSucPromo = partsSucPromo[0];   
        
        if(nombre.isEmpty() || descripcion.isEmpty()||restricciones.isEmpty()||idTipPromo.toString().isEmpty()||porcentaje.isEmpty() ||
                costoPromocion.toString().isEmpty() || idCatPromo.toString().isEmpty() || idEstPromo.toString().isEmpty() || idSucPromo.toString().isEmpty()
            ){
            Utilidades.mostrarAlertaSimple("Campos vacios", "Llena los campos vacios",Alert.AlertType.ERROR);            
        }else{
            consumirServicioModificar(idPromocion,nombre, descripcion, restricciones,          
                    Integer.parseInt(partIDTipPromo),porcentaje,costoPromocion,Integer.parseInt(partIDCatPromo)
                    ,Integer.parseInt(partIDEstPromo),Integer.parseInt(partIDSucPromo));            
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
    
    private ObservableList<Sucursal> cargarInformacionSucursales(){
        String urlWS = Constantes.URL_BASE+"sucursales/all";
        ObservableList<Sucursal> listaSucursal;
        listaSucursal = FXCollections.observableArrayList();        
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaSucursales = new TypeToken<ArrayList <Sucursal> >() {}.getType();
            ArrayList catalogoWS = gson.fromJson(resultadoWS, listaSucursales);

            listaSucursal.addAll(catalogoWS);
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de las sucursales"
                    , Alert.AlertType.ERROR);
        }
        return listaSucursal;
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
    
    private void consumirServicioModificar(Integer idPromocion,String nombre,String descripcion, String restricciones, 
                    Integer tipoPromocion, String porcentaje, float costoPromocion, Integer categoriaPromocion, Integer idEstatus, Integer idSucursal){
        
        try{
            
            String urlServicio = Constantes.URL_BASE+"promociones/modificar";
            
            String parametros = "idPromocion=" + idPromocion+ "&" +
                                "nombre=" + nombre + "&" +
                                "descripcion=" + descripcion + "&" +  
                                "restricciones=" + restricciones + "&" +
                                "tipoPromocion=" + tipoPromocion + "&" +
                                "porcentaje=" + porcentaje + "&" +
                                "costoPromocion=" + costoPromocion + "&" +
                                "categoriaPromocion=" + categoriaPromocion + "&" +
                                "idEstatus=" + idEstatus + "&" +
                                "idSucursal=" + idSucursal
                                ;
            
            String resultadoWS = ConexionServiciosweb.peticionServicioPUT(urlServicio, parametros);
            Gson gson = new Gson() ;
            Respuesta respuesta = gson.fromJson(resultadoWS, Respuesta.class);
            
            if (!respuesta.getError()) {                
                Utilidades.mostrarAlertaSimple("Promoción actualizada", 
                        "Promoción actualizada correctamente "
                        , Alert.AlertType.INFORMATION);
                Stage stage = (Stage) this.btnActualizar.getScene().getWindow();
                stage.close();
                cargarInformacionPromociones();
            }else{
                Utilidades.mostrarAlertaSimple("Error al editar la promoción", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }
                                    
        }catch(Exception e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);            
        }
                      
    }
    
    void recibir(ObservableList<Promocion> listaPromociones, TableView<Promocion> tbPromocion){
        listaPromocionesR = listaPromociones;
        tbPromocionR = tbPromocion;
    }    
    
    private void cargarInformacionPromociones(){
        
        String urlWS = Constantes.URL_BASE+"promociones/all";
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type listaTipoPromocion = new TypeToken<ArrayList <Promocion>>() {}.getType();
            ArrayList promocionWS = gson.fromJson(resultadoWS, listaTipoPromocion);
            recibir(listaPromocionesR, tbPromocionR);
            listaPromocionesR.clear();
            listaPromocionesR.addAll(promocionWS);
            tbPromocionR.setItems(listaPromocionesR);
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", 
                    "Por el momento no se puede obtener la información de las promociones"
                    , Alert.AlertType.ERROR);
        }
                
    }    
    
    
}
