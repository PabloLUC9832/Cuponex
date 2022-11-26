package util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Utilidades {
     
    public static void mostrarAlertaSimple(String titulo, String mensaje, Alert.AlertType tipoAlerta){
        
        Alert alerta = new Alert(tipoAlerta);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
                
    }
    
    public static boolean mostrarAlertaEliminacion(String encabezado, String contenido){
        
        boolean respuesta = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(encabezado);
        alert.setTitle("Confirmar eliminación");
        alert.setContentText("¿Deseas realmente eliminar el "+ contenido +" seleccionado?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            respuesta = true;
        }
        
        return respuesta;
    }
      
}
