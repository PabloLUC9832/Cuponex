package promociones;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import modelo.ConexionServiciosweb;
import pojos.Respuesta;
import util.Constantes;
import util.Utilidades;

public class FXMLSubirFotoController implements Initializable {

    @FXML
    private Label lbFoto;
    @FXML
    private Button btnSeleccionarFoto;
    @FXML
    private ImageView imgFoto;
    
    private int idPromocion;
    private boolean isEdicion = false;    
    @FXML
    private Button Guardar;

    String imgB;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void inicializarInformacionVentana(Integer idPromocion, String nombre){        
        this.idPromocion = idPromocion;
        isEdicion = true;
        lbFoto.setText("Subir foto de la promoción: "+nombre);
    }    

    //http://java-buddy.blogspot.com/2013/01/use-javafx-filechooser-to-open-image.html
    @FXML
    private void seleccionarFoto(ActionEvent event) throws FileNotFoundException, IOException {
        
            FileChooser fileChooser = new FileChooser();
            
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
             
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            FileInputStream fis = new FileInputStream(file);
            //ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] arr = new byte[(int)file.length()];
            //byte[] buf = new byte[1024];
            fis.read(arr);
            fis.close();
            //String imgB = Arrays.toString(bytes);
            imgB = Arrays.toString(arr);
            System.out.println("id:"+idPromocion+"\nBytes: "+imgB);
            //consumirServicioSubirFoto(idPromocion, imgB);

            
            
        
    }
    
    
    private void consumirServicioSubirFoto(Integer idPromocion,String fotoPromocion){
        
        try{
            
            String urlServicio = Constantes.URL_BASE+"promociones/subirImagen/"+idPromocion;
            
            String parametros = "idPromocion=" + idPromocion+ "&" +
                                "fotoPromocion=" + fotoPromocion
                                ;
            
            String resultadoWS = ConexionServiciosweb.peticionServicioPOSTImagen(urlServicio, parametros);
            Gson gson = new Gson() ;
            Respuesta respuesta = gson.fromJson(resultadoWS, Respuesta.class);
            
            if (!respuesta.getError()) {                
                Utilidades.mostrarAlertaSimple("Promoción actualizada", 
                        "Promoción actualizada correctamente "
                        , Alert.AlertType.INFORMATION);
                Stage stage = (Stage) this.btnSeleccionarFoto.getScene().getWindow();
                stage.close();
            }else{
                Utilidades.mostrarAlertaSimple("Error al editar la promoción", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }
                                    
        }catch(Exception e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);            
        }
                      
    }    

    @FXML
    private void btnGuardarFoto(ActionEvent event) {
        consumirServicioSubirFoto(idPromocion, imgB);
    }
    
}
