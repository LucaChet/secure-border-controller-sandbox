//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Site2SiteNetworkConfiguration complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Site2SiteNetworkConfiguration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}AdditionalNetworkConfigurationParameters"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="localNetwork" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="remoteNetwork" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="localNetworkNetmask" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="remoteNetworkNetmask" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Site2SiteNetworkConfiguration", propOrder = {
    "localNetwork",
    "remoteNetwork",
    "localNetworkNetmask",
    "remoteNetworkNetmask"
})
public class Site2SiteNetworkConfiguration
    extends AdditionalNetworkConfigurationParameters
{

    protected String localNetwork;
    protected String remoteNetwork;
    protected String localNetworkNetmask;
    protected String remoteNetworkNetmask;

    /**
     * Recupera il valore della proprietà localNetwork.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalNetwork() {
        return localNetwork;
    }

    /**
     * Imposta il valore della proprietà localNetwork.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalNetwork(String value) {
        this.localNetwork = value;
    }

    /**
     * Recupera il valore della proprietà remoteNetwork.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteNetwork() {
        return remoteNetwork;
    }

    /**
     * Imposta il valore della proprietà remoteNetwork.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteNetwork(String value) {
        this.remoteNetwork = value;
    }

    /**
     * Recupera il valore della proprietà localNetworkNetmask.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalNetworkNetmask() {
        return localNetworkNetmask;
    }

    /**
     * Imposta il valore della proprietà localNetworkNetmask.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalNetworkNetmask(String value) {
        this.localNetworkNetmask = value;
    }

    /**
     * Recupera il valore della proprietà remoteNetworkNetmask.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteNetworkNetmask() {
        return remoteNetworkNetmask;
    }

    /**
     * Imposta il valore della proprietà remoteNetworkNetmask.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteNetworkNetmask(String value) {
        this.remoteNetworkNetmask = value;
    }

}
