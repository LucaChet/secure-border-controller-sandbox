//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ReduceBandwidthActionType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ReduceBandwidthActionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="downlink_bandwidth_value" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="uplink_bandwidth_value" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReduceBandwidthActionType")
public class ReduceBandwidthActionType {

    @XmlAttribute(name = "downlink_bandwidth_value")
    protected Double downlinkBandwidthValue;
    @XmlAttribute(name = "uplink_bandwidth_value")
    protected Double uplinkBandwidthValue;

    /**
     * Recupera il valore della proprietà downlinkBandwidthValue.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDownlinkBandwidthValue() {
        return downlinkBandwidthValue;
    }

    /**
     * Imposta il valore della proprietà downlinkBandwidthValue.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDownlinkBandwidthValue(Double value) {
        this.downlinkBandwidthValue = value;
    }

    /**
     * Recupera il valore della proprietà uplinkBandwidthValue.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getUplinkBandwidthValue() {
        return uplinkBandwidthValue;
    }

    /**
     * Imposta il valore della proprietà uplinkBandwidthValue.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setUplinkBandwidthValue(Double value) {
        this.uplinkBandwidthValue = value;
    }

}
