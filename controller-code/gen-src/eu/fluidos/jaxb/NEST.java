//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per NEST complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="NEST"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="availability" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="mmtel_support" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="session_and_service_continuity_support" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="three_gpp_5g_qos" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NEST", propOrder = {
    "availability",
    "mmtelSupport",
    "sessionAndServiceContinuitySupport",
    "threeGpp5GQos"
})
public class NEST {

    protected Float availability;
    @XmlElement(name = "mmtel_support")
    protected Integer mmtelSupport;
    @XmlElement(name = "session_and_service_continuity_support")
    protected Integer sessionAndServiceContinuitySupport;
    @XmlElement(name = "three_gpp_5g_qos")
    protected Integer threeGpp5GQos;

    /**
     * Recupera il valore della proprietà availability.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getAvailability() {
        return availability;
    }

    /**
     * Imposta il valore della proprietà availability.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setAvailability(Float value) {
        this.availability = value;
    }

    /**
     * Recupera il valore della proprietà mmtelSupport.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMmtelSupport() {
        return mmtelSupport;
    }

    /**
     * Imposta il valore della proprietà mmtelSupport.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMmtelSupport(Integer value) {
        this.mmtelSupport = value;
    }

    /**
     * Recupera il valore della proprietà sessionAndServiceContinuitySupport.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSessionAndServiceContinuitySupport() {
        return sessionAndServiceContinuitySupport;
    }

    /**
     * Imposta il valore della proprietà sessionAndServiceContinuitySupport.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSessionAndServiceContinuitySupport(Integer value) {
        this.sessionAndServiceContinuitySupport = value;
    }

    /**
     * Recupera il valore della proprietà threeGpp5GQos.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getThreeGpp5GQos() {
        return threeGpp5GQos;
    }

    /**
     * Imposta il valore della proprietà threeGpp5GQos.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setThreeGpp5GQos(Integer value) {
        this.threeGpp5GQos = value;
    }

}
