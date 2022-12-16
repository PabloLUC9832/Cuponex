package empresa;

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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.ConexionServiciosweb;
import pojos.Catalogo;
import pojos.Empresa;
import pojos.Respuesta;
import util.Constantes;
import util.Utilidades;

public class FXMLFormularioAltaEmpresaController implements Initializable {

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
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    
    ObservableList<Empresa> listaEmpresasR;
    TableView<Empresa> tbEmpresaR;    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         cbEstatus.setItems(cargarInformacionCatalogo("3"));
    }    

    @FXML
    private void clicGuardarEmpresa(ActionEvent event) {
        
        String nombre = tfNombre.getText();
        String nombreComercial = tfNombreComercial.getText();
        String representante = tfRepresentante.getText();
        String correo = tfCorreo.getText();
        String direccion = tfDireccion.getText();
        Integer cp = Integer.parseInt(tfCP.getText());
        String ciudad = tfCiudad.getText();
        Integer telefono = Integer.parseInt(tfTelefono.getText());
        String pagina = tfPagina.getText();
        String rfc = tfRFC.getText();
        //Integer estatus = Integer.parseInt(cbEstatus.getValue().toString());
        Integer estatus = 0;
        if(cbEstatus.getValue().toString().equals("301-Activo")){
            estatus = 301;
        }else{
            estatus = 302;
        }
        
        Empresa empresa = new Empresa();
        empresa.setNombre(nombre);
        empresa.setNombreComercial(nombreComercial);
        empresa.setNombreRepresentanteLegal(representante);
        empresa.setCorreo(correo);
        empresa.setDireccion(direccion);
        empresa.setCodigoPostal(cp);
        empresa.setCiudad(ciudad);
        empresa.setTelefono(telefono);
        empresa.setPaginaWeb(pagina);
        empresa.setRfc(rfc);
        empresa.setIdEstatus(estatus);
        
        guardarInformacionEmpresa(empresa);
        
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
           
    private void guardarInformacionEmpresa(Empresa empresa){
        
        try{
            String urlServicio = Constantes.URL_BASE+"empresas/registrar";
            String parametros = "nombre="+empresa.getNombre() + "&" +
                                "nombreComercial="+empresa.getNombreComercial()+ "&" +
                                "nombreRepresentanteLegal="+empresa.getNombreRepresentanteLegal()+ "&" +
                                "correo="+empresa.getCorreo() + "&" +
                                "direccion="+empresa.getDireccion()+ "&" +
                                "codigoPostal="+empresa.getCodigoPostal()+ "&" +
                                "ciudad="+empresa.getCiudad()+ "&" +
                                "telefono="+empresa.getTelefono()+ "&" +
                                "paginaWeb="+empresa.getPaginaWeb()+ "&" +
                                "rfc="+empresa.getRfc() + "&" +
                                "idEstatus="+empresa.getIdEstatus()
                                ;
            String resultado = ConexionServiciosweb.peticionServicioPOST(urlServicio, parametros);
            Gson gson = new Gson();
            Respuesta respuesta = gson.fromJson(resultado, Respuesta.class);
            
            if (!respuesta.getError()) {
                
                Utilidades.mostrarAlertaSimple("Promoción añadida", "Promoción añadida correctamente "
                        , Alert.AlertType.INFORMATION);  
                Stage stage = (Stage) this.btnGuardar.getScene().getWindow();
                stage.close();
                cargarInformacionEmpresas();
            }else{
                Utilidades.mostrarAlertaSimple("Error al añadir promoción", respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
            }              

        }catch(IOException e){
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
