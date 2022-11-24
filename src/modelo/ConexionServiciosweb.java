package modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexionServiciosweb {

    public static String peticionServicioPOST(String url,String parametros) throws IOException{       
        
        String resultado = "";
        URL urlAcceso = new URL(url);
        HttpURLConnection conexionHTTP = (HttpURLConnection) urlAcceso.openConnection();
        conexionHTTP.setRequestMethod("POST");
        conexionHTTP.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        conexionHTTP.setDoOutput(true);
        
        OutputStream outputSalida = conexionHTTP.getOutputStream() ;
        outputSalida.write(parametros.getBytes());
        outputSalida.flush();
        outputSalida.close();

        int codigoRespuesta = conexionHTTP.getResponseCode();
        System.out.println("El código de respuesta es: "+ codigoRespuesta);
        
        if (codigoRespuesta == HttpURLConnection.HTTP_OK) {
            resultado = convierteStreamCadena(conexionHTTP.getInputStream());
        }else{
            resultado = "Error en la petición POST con código: "+codigoRespuesta;
        }
        
        return resultado;
    }
    
    
    private static String convierteStreamCadena(InputStream streamServicio) throws IOException{
        
        InputStreamReader isr = new InputStreamReader(streamServicio)  ; 
        BufferedReader in = new BufferedReader(isr);
        String inputLine;
        StringBuilder response = new StringBuilder();

        while( (inputLine = in.readLine()) != null ){
            response.append(inputLine);
        }
        in.close();
        
        return response.toString();
    }    

    
}
