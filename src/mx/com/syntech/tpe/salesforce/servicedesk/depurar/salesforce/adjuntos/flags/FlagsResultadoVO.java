package mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.flags;

import mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.core.CoreResultadoVO;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class FlagsResultadoVO extends CoreResultadoVO{
    
    // Propiedades de la clase
    private Boolean enMantenimiento = null;    // true]: Existe ventana demantenimiento.

    /** METODOS GETTERS Y SETTERS **/
    public Boolean getEnMantenimiento() {
        return enMantenimiento;
    }

    public void setEnMantenimiento(Boolean enMantenimiento) {
        this.enMantenimiento = enMantenimiento;
    }
    
    
    
    
}
