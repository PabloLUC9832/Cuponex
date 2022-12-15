package promociones;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
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
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.DatatypeConverter;
import modelo.ConexionServiciosweb;
import pojos.Respuesta;
import sun.misc.BASE64Encoder;
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
    private byte [] byteImage = null;
    private String imageType = "";
    
    
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
        
        FileChooser j = new FileChooser();
        File file = j.showOpenDialog(new Stage());
        if (file!=null){
            try{
                
            
            String imageName = file.getName();
            String[] imageNameArr = imageName.split("\\.");
            
            this.imageType = imageNameArr[imageNameArr.length-1].toLowerCase();
            if (!this.imageType.equals("png") && !this.imageType.equals("jpg")){
                Utilidades.mostrarAlertaSimple("Error", "Solo archivos jpg y png", Alert.AlertType.NONE);
                return;
            }
            
            if(file.length()>1000000){
                Utilidades.mostrarAlertaSimple("Error", "La imagen debe pesar almenos 1mb", Alert.AlertType.NONE);
                return;
            }
            
            //Image image1 = new Image(file.toURI().toString());
            //this.imagen.setImage(imagen1);
            
            BufferedImage bImage = ImageIO.read(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, imageType, bos);
            this.byteImage = bos.toByteArray();
                //consumirServicioSubirFoto(idPromocion, byteImage);
         }catch(Exception e){
             
         }   
        }
   
    }
    
    
    private void consumirServicioSubirFoto(Integer idPromocion,byte[] fotoPromocion){
        
        try{
            
            String urlServicio = Constantes.URL_BASE+"promociones/subirImagen/"+idPromocion;
            
            //System.out.println(idPromocion);
            //System.out.println(fotoPromocion);
            
            String parametros = "idPromocion=" + idPromocion+ "&" +
                                "fotoPromocion=" + fotoPromocion
                                ;
            
            String resultadoWS = ConexionServiciosweb.peticionServicioPOSTImagen(urlServicio, this.byteImage);
            Gson gson = new Gson() ;
            Respuesta respuesta = gson.fromJson(resultadoWS, Respuesta.class);
            
            if (!respuesta.getError()) {                
                Utilidades.mostrarAlertaSimple("Promoción actualizada", 
                        "Promoción actualizada correctamente "
                        , Alert.AlertType.INFORMATION);
                //System.out.println("PARAMETROS: "+parametros);
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
        consumirServicioSubirFoto(idPromocion, this.byteImage);
    }
    
}
