/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package promociones;

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
import modelo.ConexionServiciosweb;
import pojos.Promocion;
import pojos.Sucursal;
import util.Constantes;
import util.Utilidades;

public class FXMLAsignarPromocionSucursalController implements Initializable {

    @FXML
    private ComboBox cbPromocion;
    @FXML
    private ComboBox cbSucursal;
    @FXML
    private Button btnAsignar;
    
    ObservableList<Promocion> listaPromocionesR;
    ObservableList<Sucursal> listaSucursalesR;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cbSucursal.setItems(cargarInformacionSucursales());
        cbPromocion.setItems(cargarInformacionPromociones());
    }    

    @FXML
    private void AsignarPromocionSucursal(ActionEvent event) {
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
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de los tipos de sucursales"
                    , Alert.AlertType.ERROR);
        }
        return listaSucursal;
    }
    
    private ObservableList<Promocion> cargarInformacionPromociones(){
        String urlWS = Constantes.URL_BASE+"promociones/allIds";
        ObservableList<Promocion> listaPromociones;
        listaPromociones = FXCollections.observableArrayList();        
        try{
            String resultadoWS = ConexionServiciosweb.peticionServicioGET(urlWS);
            Gson gson = new Gson();
            Type  listaSucursales = new TypeToken<ArrayList <Promocion> >() {}.getType();
            ArrayList catalogoWS = gson.fromJson(resultadoWS, listaSucursales);
            listaPromociones.addAll(catalogoWS);
        }catch(Exception e){
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de conexión", "Por el momento no se puede obtener la información de los tipos de promociones"
                    , Alert.AlertType.ERROR);
        }
        return listaPromociones;
    }
    
}
