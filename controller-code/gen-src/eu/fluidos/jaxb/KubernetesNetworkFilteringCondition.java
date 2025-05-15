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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per KubernetesNetworkFilteringCondition complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="KubernetesNetworkFilteringCondition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationCondition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="source" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ResourceSelector"/&gt;
 *         &lt;element name="sourcePort" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="destination" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ResourceSelector"/&gt;
 *         &lt;element name="destinationPort" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="protocolType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ProtocolType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KubernetesNetworkFilteringCondition", propOrder = {
    "source",
    "sourcePort",
    "destination",
    "destinationPort",
    "protocolType"
})
public class KubernetesNetworkFilteringCondition
    extends ConfigurationCondition
{

    @XmlElement(required = true)
    protected ResourceSelector source;
    @XmlElement(required = true)
    protected String sourcePort;
    @XmlElement(required = true)
    protected ResourceSelector destination;
    @XmlElement(required = true)
    protected String destinationPort;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ProtocolType protocolType;

    /**
     * Recupera il valore della proprietà source.
     * 
     * @return
     *     possible object is
     *     {@link ResourceSelector }
     *     
     */
    public ResourceSelector getSource() {
        return source;
    }

    /**
     * Imposta il valore della proprietà source.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceSelector }
     *     
     */
    public void setSource(ResourceSelector value) {
        this.source = value;
    }

    /**
     * Recupera il valore della proprietà sourcePort.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcePort() {
        return sourcePort;
    }

    /**
     * Imposta il valore della proprietà sourcePort.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcePort(String value) {
        this.sourcePort = value;
    }

    /**
     * Recupera il valore della proprietà destination.
     * 
     * @return
     *     possible object is
     *     {@link ResourceSelector }
     *     
     */
    public ResourceSelector getDestination() {
        return destination;
    }

    /**
     * Imposta il valore della proprietà destination.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceSelector }
     *     
     */
    public void setDestination(ResourceSelector value) {
        this.destination = value;
    }

    /**
     * Recupera il valore della proprietà destinationPort.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationPort() {
        return destinationPort;
    }

    /**
     * Imposta il valore della proprietà destinationPort.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationPort(String value) {
        this.destinationPort = value;
    }

    /**
     * Recupera il valore della proprietà protocolType.
     * 
     * @return
     *     possible object is
     *     {@link ProtocolType }
     *     
     */
    public ProtocolType getProtocolType() {
        return protocolType;
    }

    /**
     * Imposta il valore della proprietà protocolType.
     * 
     * @param value
     *     allowed object is
     *     {@link ProtocolType }
     *     
     */
    public void setProtocolType(ProtocolType value) {
        this.protocolType = value;
    }

}
