/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package promociones;

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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modelo.ConexionServiciosweb;
import pojos.Promocion;
import pojos.Respuesta;
import pojos.Sucursal;
import util.Constantes;
import util.Utilidades;

public class FXMLAsignarPromocionSucursalController implements Initializable {

    @FXML
    private ComboBox cbSucursal;
    @FXML
    private Button btnAsignar;
    
    ObservableList<Promocion> listaPromocionesR;
    ObservableList<Sucursal> listaSucursalesR;
    @FXML
    private Label lbIdPromocion;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbDescripcion;
    @FXML
    private Label lbFechaInicio;
    @FXML
    private Label lbFechaTermino;
    @FXML
    private Label lbRestricciones;
    @FXML
    private Label lbTipoPromocion;
    @FXML
    private Label lbPorcentaje;
    @FXML
    private Label lbCostoPromocion;
    @FXML
    private Label lbCategoriaPromocion;
    @FXML
    private Label lbIdEstatus;
    @FXML
    private Label lbIdSucursal;
    
    private boolean isEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cbSucursal.setItems(cargarInformacionSucursales());
    }    
    
    public void inicializarInformacionVentana(Integer idPromocion, String nombre,String descripcion, String fechaInicio, 
                                              String fechaTermino, String restricciones, Integer tipoPromocion,String porcentaje,
                                              float costoPromocion, Integer categoriaPromocion,Integer idEstatus, Integer idSucursal
                                              ){
        
        lbIdPromocion.setText(String.valueOf(idPromocion));
        isEdicion = true;
        lbNombre.setText(nombre);
        lbDescripcion.setText(descripcion);
        lbFechaInicio.setText(fechaInicio);
        lbFechaTermino.setText(fechaTermino);
        lbRestricciones.setText(restricciones);
        lbTipoPromocion.setText(String.valueOf(tipoPromocion));
        lbPorcentaje.setText(porcentaje);
        lbCostoPromocion.setText(Float.toString(costoPromocion));
        lbCategoriaPromocion.setText(String.valueOf(categoriaPromocion));
        lbIdEstatus.setText(String.valueOf(idEstatus));
        lbIdSucursal.setText(String.valueOf(idSucursal));
        cbSucursal.setValue(idSucursal);
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
                Stage stage = (Stage) this.btnAsignar.getScene().getWindow();
                stage.close();
                
            }else{
                Utilidades.mostrarAlertaSimple("Error al añadir promoción", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }              

        }catch(IOException e){
            Utilidades.mostrarAlertaSimple("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);                        
        }
        
        
    }

    @FXML
    private void asignarPromocionSucursal(ActionEvent event) {
        Integer idPromocion = Integer.parseInt(lbIdPromocion.getText());
        String nombre = lbNombre.getText();
        String descripcion = lbDescripcion.getText();
        String fechaInicio = lbFechaInicio.getText();
        String fechaFin = lbFechaTermino.getText();
        String restricciones = lbRestricciones.getText();
        Integer tipoPromocion = Integer.parseInt(lbTipoPromocion.getText());
        String porcentaje = lbPorcentaje.getText();
        Float costoPromocion = Float.parseFloat(lbCostoPromocion.getText());
        Integer categoriaPromocion = Integer.parseInt(lbCategoriaPromocion.getText());
        Integer idEstatus = Integer.parseInt(lbIdEstatus.getText());

        String idSucPromo = cbSucursal.getValue().toString();
        String[] partsSucPromo = idSucPromo.split("-");
        String partIDSucPromo = partsSucPromo[0];
        String partNombreSucPromo = partsSucPromo[1];
        
        Promocion promocion = new Promocion();
        promocion.setNombre(nombre);
        promocion.setDescripcion(descripcion);
        promocion.setFechaInicio(fechaInicio);
        promocion.setFechaTermino(fechaFin);
        promocion.setRestricciones(restricciones);
        promocion.setTipoPromocion(tipoPromocion);
        promocion.setPorcentaje(porcentaje);
        promocion.setCostoPromocion(costoPromocion);
        promocion.setCategoriaPromocion(categoriaPromocion);
        promocion.setIdEstatus(idEstatus);
        promocion.setIdSucursal(Integer.parseInt(partIDSucPromo));
        
        if(nombre.isEmpty() || descripcion.isEmpty()||restricciones.isEmpty()||tipoPromocion.toString().isEmpty()||porcentaje.isEmpty() ||
                costoPromocion.toString().isEmpty() || categoriaPromocion.toString().isEmpty() || idEstatus.toString().isEmpty() || idSucPromo.toString().isEmpty()
            ){
            Utilidades.mostrarAlertaSimple("Campos vacios", "Llena los campos vacios",Alert.AlertType.ERROR);            
        }else{
            guardarInformacionPromocion(promocion);     
        }
    }
    
}
