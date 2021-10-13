package mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class LeerArchivoTextoTOStringArrayBean {

    public static String[] leerArchivoTOArray(String archivo) throws FileNotFoundException, IOException {
        String[] retorno = null;
        List<String> lineaLeida = new ArrayList<>();

        File file = new File(archivo);

        FileInputStream fstream_school = new FileInputStream(archivo);
        DataInputStream data_input = new DataInputStream(fstream_school);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(data_input));
        String str_line;
        while ((str_line = buffer.readLine()) != null) {
            str_line = str_line.trim();
            lineaLeida.add(str_line);
        }
        
        // Pasamos del atrreglo 
        retorno = new String[lineaLeida.size()];
        for(int i=0; i<lineaLeida.size(); i++){
            retorno[i] = lineaLeida.get(i);
        }
        
        return retorno;
        

    }

}
