package mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.pruebas;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.utils.SerialClaveBean;
import org.apache.log4j.Category;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class PruebaMasivaCommand {
    
    // Propiedades de laclase
    private static Category log = Category.getInstance(PruebaMasivaCommand.class);
    

    public void ejecutarComando(String comando, String idEjecucion){
        // Obtener el nombre del comando a ejecutar
        
        String logInicial = "   <" + idEjecucion + "> ::: ";
        String comandoEjecutar = null;
        log.info(logInicial + "Ejecución del comando ...");

        try {

            comandoEjecutar = comando;
            // comandoEjecutar = "cmd /c dir";
            log.info(logInicial + "Comando a ejecutar: " + comandoEjecutar);

            // Lanzar programa (se usa "cmd /c dir" para lanzar un comando del sistema operativo)
            log.info(logInicial + "Ejecutando...");
            Process p = Runtime.getRuntime().exec(comandoEjecutar);

            /**
             * Obtenemos la salida del comando *
             */
            log.info(logInicial + "Recibieno respuesta...");
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Se lee la primera linea
            String aux = br.readLine();
            // log.info("     -->" + aux);

            // Mientras se haya leido alguna linea
            while (aux != null) {
                // Se escribe la linea en pantalla
                System.out.println(aux);
                log.info(logInicial + "" + aux);

                // y se lee la siguiente.
                aux = br.readLine();
            }
            log.info(logInicial + "Ejecución terminada");

        } catch (Exception ex) {
            // Excepciones si hay algún problema al arrancar el ejecutable o al leer su salida.*/
            log.error(logInicial + "No se pudo ejecutar el programa: " + comandoEjecutar);
            log.error(logInicial + "" + ex.getLocalizedMessage());
            log.error(logInicial + "" + ex.getMessage());
            log.error(logInicial + "Trasa del error....", ex);
        }
    }
}
