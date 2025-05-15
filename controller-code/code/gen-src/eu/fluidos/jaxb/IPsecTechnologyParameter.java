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
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per IPsecTechnologyParameter complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="IPsecTechnologyParameter"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}TechnologySpecificParameters"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IPsecProtocol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="isTunnel" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="localEndpoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="remoteEndpoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPsecTechnologyParameter", propOrder = {
    "iPsecProtocol",
    "isTunnel",
    "localEndpoint",
    "remoteEndpoint"
})
@XmlSeeAlso({
    GenericChannelProtectionTechnologyParameter.class,
    DTLSTechnologyParameter.class
})
public class IPsecTechnologyParameter
    extends TechnologySpecificParameters
{

    @XmlElement(name = "IPsecProtocol")
    protected String iPsecProtocol;
    protected Boolean isTunnel;
    protected String localEndpoint;
    protected String remoteEndpoint;

    /**
     * Recupera il valore della proprietà iPsecProtocol.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPsecProtocol() {
        return iPsecProtocol;
    }

    /**
     * Imposta il valore della proprietà iPsecProtocol.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPsecProtocol(String value) {
        this.iPsecProtocol = value;
    }

    /**
     * Recupera il valore della proprietà isTunnel.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTunnel() {
        return isTunnel;
    }

    /**
     * Imposta il valore della proprietà isTunnel.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTunnel(Boolean value) {
        this.isTunnel = value;
    }

    /**
     * Recupera il valore della proprietà localEndpoint.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalEndpoint() {
        return localEndpoint;
    }

    /**
     * Imposta il valore della proprietà localEndpoint.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalEndpoint(String value) {
        this.localEndpoint = value;
    }

    /**
     * Recupera il valore della proprietà remoteEndpoint.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteEndpoint() {
        return remoteEndpoint;
    }

    /**
     * Imposta il valore della proprietà remoteEndpoint.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteEndpoint(String value) {
        this.remoteEndpoint = value;
    }

}
