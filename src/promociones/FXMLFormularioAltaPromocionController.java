package promociones;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
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
    @FXML
    private Button btnSubirFoto;
    
    List<String> archivo;
    @FXML
    private Label lbImagenSeleccionada;

    String imagen = "";
    
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
        Integer tipoPromocion = Integer.parseInt(cbTipoPromocion.getValue().toString());
        String porcentaje = tfPorcentaje.getText();
        Float costoPromocion = Float.valueOf(tfCostoPromocion.getText());
        Integer categoriaPromocion = Integer.parseInt(cbCategoria.getValue().toString());
        Integer estatus = Integer.parseInt(cbEstatus.getValue().toString());
        Integer sucursal = Integer.parseInt(cbSucursal.getValue().toString());

        String fotoPr = imagen;
        
        Promocion promocion = new Promocion();
        promocion.setNombre(nombre);
        promocion.setDescripcion(descripcion);
        promocion.setFechaInicio(fechaInicio);
        promocion.setFechaTermino(fechaTermino);
        promocion.setRestricciones(restricciones);
        promocion.setTipoPromocion(tipoPromocion);
        promocion.setPorcentaje(porcentaje);
        promocion.setCostoPromocion(costoPromocion);
        promocion.setCategoriaPromocion(categoriaPromocion);
        promocion.setIdEstatus(estatus);
        promocion.setIdSucursal(sucursal);
        promocion.setFotoPromocion(fotoPr);
        
        System.out.println("fotoPr:::"+fotoPr);
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
                                "idSucursal="+promocion.getIdSucursal()  + "&" +
                   
                                "fotoPromocion="+promocion.getFotoPromocion()
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
    
    //private ObservableList<Catalogo> cargarInformacionTipoPromocion(String idCategoria){
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

    @FXML
    private void subirFoto(ActionEvent event) {
    //private String subirFoto() {
            /*
        FileChooser fcImagen = new FileChooser();
        fcImagen.setInitialDirectory(new File ("C:\\"));
        fcImagen.getExtensionFilters().addAll(new ExtensionFilter("PNG Files", "*.png","*jpg"));
        File seletedFile = fcImagen.showOpenDialog(null);
        System.out.println(seletedFile);
        lbImagenSeleccionada.setText(seletedFile.getAbsolutePath());
        */
        //String imagen = "";
        FileChooser fcImagen = new FileChooser();
        File file = fcImagen.showOpenDialog(btnSubirFoto.getScene().getWindow());
        lbImagenSeleccionada.setText(file.getAbsolutePath());        
        Path path = Paths.get(file.getAbsolutePath());
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            System.out.println("archivo|||"+file.length());            
            //imagen = file.toString();
            //imagen = "";
            //byte[] arr = new byte[(int)file.length()];
            byte[] arr = Files.readAllBytes(path);
            //byte[] array = method(file);
            //imagen = arr.toString();
            //System.out.println("read:   ||"+fileInputStream.read(arr));
            //imagen =""+fileInputStream.read(arr);
            imagen = Arrays.toString(arr);
            System.out.print(Arrays.toString(arr));
        }catch(IOException e){
            System.out.println(e);
        }
        
        //return imagen;
        
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
