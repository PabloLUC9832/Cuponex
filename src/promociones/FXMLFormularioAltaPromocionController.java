package promociones;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import modelo.ConexionServiciosweb;
import pojos.Catalogo;
import pojos.Promocion;
import pojos.Respuesta;
import pojos.Sucursal;
import util.Constantes;
import util.Utilidades;

public class FXMLFormularioAltaPromocionController implements Initializable {

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
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox cbSucursal;
    
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
    }    

    @FXML
    private void clicGuardarPromocion(ActionEvent event) {
        
        String nombre = tfNombre.getText();
        String descripcion = tfDescripcion.getText();
        
        LocalDate fechaInicioLD = dpFechaInicio.getValue();
        String fechaInicio = fechaInicioLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        LocalDate fechaTerminoLD = dpFechaTermino.getValue();
        String fechaTermino = fechaTerminoLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        String restricciones = tfRestricciones.getText();
                
        String idTipoPromo = cbTipoPromocion.getValue().toString();
        String[] partsTipoPromo = idTipoPromo.split("-");
        String partIDTipoPromo = partsTipoPromo[0];
        String partNombreTipoPromo = partsTipoPromo[1];
        
        String porcentaje = tfPorcentaje.getText();
        Float costoPromocion = Float.valueOf(tfCostoPromocion.getText());
        
        String idCatPromo = cbCategoria.getValue().toString();
        String[] partsCatPromo = idCatPromo.split("-");
        String partIDCatPromo = partsCatPromo[0];
        String partNombreCatPromo = partsCatPromo[1];
                
        String idEstPromo = cbEstatus.getValue().toString();
        String[] partsEstPromo = idEstPromo.split("-");
        String partIDEstPromo = partsEstPromo[0];
        String partNombreEstPromo = partsEstPromo[1];        
                
        String idSucPromo = cbSucursal.getValue().toString();
        String[] partSucPromo = idSucPromo.split("-");
        String partIDSucPromo = partSucPromo[0];
        String partNombreSucPromo = partSucPromo[1]; 
        
        Promocion promocion = new Promocion();
        promocion.setNombre(nombre);
        promocion.setDescripcion(descripcion);
        promocion.setFechaInicio(fechaInicio);
        promocion.setFechaTermino(fechaTermino);
        promocion.setRestricciones(restricciones);
        promocion.setTipoPromocion(Integer.parseInt(partIDTipoPromo));
        promocion.setPorcentaje(porcentaje);
        promocion.setCostoPromocion(costoPromocion);
        promocion.setCategoriaPromocion(Integer.parseInt(partIDCatPromo));
        promocion.setIdEstatus(Integer.parseInt(partIDEstPromo));        
        promocion.setIdSucursal(Integer.parseInt(partIDSucPromo));

        guardarInformacionPromocion(promocion);
        
    }
    
    private void guardarInformacionPromocion(Promocion promocion){
        
        try{
            String urlServicio = Constantes.URL_BASE+"promociones/registrar";
            String parametros = "nombre="+promocion.getNombre() + "&" +
                                "descripcion="+promocion.getDescripcion()+ "&" +
 
                                "fechaInicio="+promocion.getFechaInicio()+ "&" +
                                "fechaTermino="+promocion.getFechaTermino()+ "&" +
                                "restricciones="+promocion.getRestricciones()+ "&" +
                                "tipoPromocion="+promocion.getTipoPromocion()+ "&" + 
                                "porcentaje="+promocion.getPorcentaje()+ "&" +
                                "costoPromocion="+promocion.getCostoPromocion()+ "&" +
                                "categoriaPromocion="+promocion.getCategoriaPromocion()+ "&" +
                                "idEstatus="+promocion.getIdEstatus()+ "&" +
                                "idSucursal="+promocion.getIdSucursal()
                   
                                ;
            String resultado = ConexionServiciosweb.peticionServicioPOST(urlServicio, parametros);
            Gson gson = new Gson();
            Respuesta respuesta = gson.fromJson(resultado, Respuesta.class);
            
            if (!respuesta.getError()) {
                
                Utilidades.mostrarAlertaSimple("Promoción añadida", "Promoción añadida correctamente "
                        , Alert.AlertType.INFORMATION);  
                Stage stage = (Stage) this.btnGuardar.getScene().getWindow();
                stage.close();
                cargarInformacionPromociones();
            }else{
                Utilidades.mostrarAlertaSimple("Error al añadir promoción", respuesta.getMensaje(),
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
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de los tipos de promociones"
                    , Alert.AlertType.ERROR);
        }
        return listaSucursal;
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
