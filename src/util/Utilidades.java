package util;

import javafx.scene.control.Alert;

public class Utilidades {

    public static void mostrarAlertaSimple(String titulo, String mensaje, Alert.AlertType tipoAlerta){
        
        Alert alerta = new Alert(tipoAlerta);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
                
    }    

}
