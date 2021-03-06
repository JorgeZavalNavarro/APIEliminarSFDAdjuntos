
package mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sid" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="buId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="rate" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sid",
    "buId",
    "rate"
})
@XmlRootElement(name = "updateRating")
public class UpdateRating {

    protected int sid;
    protected int buId;
    protected int rate;

    /**
     * Obtiene el valor de la propiedad sid.
     * 
     */
    public int getSid() {
        return sid;
    }

    /**
     * Define el valor de la propiedad sid.
     * 
     */
    public void setSid(int value) {
        this.sid = value;
    }

    /**
     * Obtiene el valor de la propiedad buId.
     * 
     */
    public int getBuId() {
        return buId;
    }

    /**
     * Define el valor de la propiedad buId.
     * 
     */
    public void setBuId(int value) {
        this.buId = value;
    }

    /**
     * Obtiene el valor de la propiedad rate.
     * 
     */
    public int getRate() {
        return rate;
    }

    /**
     * Define el valor de la propiedad rate.
     * 
     */
    public void setRate(int value) {
        this.rate = value;
    }

}
