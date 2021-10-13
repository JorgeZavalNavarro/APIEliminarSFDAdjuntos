package mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.utils;

/**
 *
 * @author jzavala
 */
public class SerialClaveException extends Exception{

    public SerialClaveException(Exception ex){
        super(ex);
    }

    public SerialClaveException(String mensaje){
        super(mensaje);
    }
}
