package mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.flags.FlagsBean;
import mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.props.AppPropsBean;
import org.apache.log4j.Category;

/**
 * Componente para eliminar los documentos provenientes de SalesForce que se
 * encuentran registrados en la base de ServiceDesk
 *
 * @author dell
 */
public class PrincipalMain {

    // Constantes de la clase
    private static Category log = Category.getInstance(PrincipalMain.class);

    /**
     * Inicio de la ejecución
     *
     * @param args the command line arguments Los parametros que recibe son los
     * siguientes: args[0] = folioTicket args[1] = usuario args[2] = password)
     */
    public static void main(String[] args) {

        // TODO code application logic here
        System.out.println("Ejecutando en " + AppPropsBean.getPropsVO().getPrincipalAmbiente());
        
        /** ASIGNANDO EL CHARSET **/
        log.info("Asignando Charset UTF-8");
        String charSet = "UTF-8";
        log.info("Configurando charset: " + charSet);

        try {
            asignarCharSet(charSet);
        } catch (Exception ex) {
            log.error("No se pudo asignar el charset " + charSet);
            log.error(ex);
            log.error("El programa puede presentar caracteres extraños en la información en lugar de letras con acentos, tildes, etc.");
        }
        
        /** Validar si el negocio se encuentra en ventaa **/
        if(FlagsBean.enVentanaMantenimiento().getEnMantenimiento()){
            log.warn("Se detecta una ventana de mantenimiento !!!. ");
            log.warn("La operación no se puede ejecutar porque actualmente se encuentra una ventana de mantenimiento en ejecución.");
            System.exit(0);
        }

        // Datos de prueba
        // args = new String[]{"1048172", "servicedesk", "DeskService01"};        
        // Desglosamos los parámetros, deben de ser obligatoriamente 3 parametros
        System.out.println("Class BDD: " + AppPropsBean.getPropsVO().getBddClassDriver());
        if (args != null && args.length == 3) {
            // Desglosamos los par{ametros 
            String numeroTicket = args[0];
            String usuario = args[1];
            String password = args[2];

            // Ejecutamos el componente con los parámetros recibidos
            try {
                PrincipalBean bean = new PrincipalBean();
                bean.eliminarAdjuntosPorTicket(numeroTicket, usuario, password);
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Se produjo un error al intentar ejecutar este componente");
                log.error("Mensaje del error: " + ex.getMessage());
                log.error("Traza....", ex);
            }

        } else {
            log.error("Los parámetros recibidos son incorrectos.");
            log.error("Se esperan tres parametros entre dobles comillas separador por espacio");
            log.error("Params: \"NumeroTicket\" \"usuario\" \"password\"");
        }

    }
    
    public static void asignarCharSet(String charset) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        log.info("   ::: Charset actual: " + Charset.defaultCharset().name());
        log.info("   ::: File encoding actual: " + System.getProperty("file.encoding"));
        log.info("   ::: Asignando el charset: " + charset);
        System.setProperty("file.encoding", charset);

        log.info("   ::: File encoding actual: " + System.getProperty("file.encoding"));

        Field fieldCharset = Charset.class.getDeclaredField("defaultCharset");
        fieldCharset.setAccessible(true);
        fieldCharset.set(null, null);
        log.info("   ::: Charset actual: " + Charset.defaultCharset().name());

    }
    

}
