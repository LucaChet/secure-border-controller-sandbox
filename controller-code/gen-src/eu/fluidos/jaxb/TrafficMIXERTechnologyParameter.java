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
 * <p>Classe Java per TrafficMIXERTechnologyParameter complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="TrafficMIXERTechnologyParameter"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}TechnologySpecificParameters"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GW" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="peerID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="cryptoBackend" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrafficMIXERTechnologyParameter", propOrder = {
    "gw",
    "peerID",
    "key",
    "cryptoBackend"
})
public class TrafficMIXERTechnologyParameter
    extends TechnologySpecificParameters
{

    @XmlElement(name = "GW", required = true)
    protected String gw;
    @XmlElement(required = true)
    protected String peerID;
    @XmlElement(required = true)
    protected String key;
    @XmlElement(required = true)
    protected String cryptoBackend;

    /**
     * Recupera il valore della proprietà gw.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGW() {
        return gw;
    }

    /**
     * Imposta il valore della proprietà gw.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGW(String value) {
        this.gw = value;
    }

    /**
     * Recupera il valore della proprietà peerID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeerID() {
        return peerID;
    }

    /**
     * Imposta il valore della proprietà peerID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeerID(String value) {
        this.peerID = value;
    }

    /**
     * Recupera il valore della proprietà key.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Imposta il valore della proprietà key.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * Recupera il valore della proprietà cryptoBackend.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCryptoBackend() {
        return cryptoBackend;
    }

    /**
     * Imposta il valore della proprietà cryptoBackend.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCryptoBackend(String value) {
        this.cryptoBackend = value;
    }

}
