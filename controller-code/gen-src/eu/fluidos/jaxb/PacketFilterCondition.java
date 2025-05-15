//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per PacketFilterCondition complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="PacketFilterCondition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SourceMAC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DestinationMAC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SourceAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DestinationAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SourcePort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DestinationPort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="direction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Size" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Interface" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProtocolType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="encapsulationType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="encapsulationID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="bidirectional" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="encapsulation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="innerPacket" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}PacketFilterCondition" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PacketFilterCondition", propOrder = {
    "sourceMAC",
    "destinationMAC",
    "sourceAddress",
    "destinationAddress",
    "sourcePort",
    "destinationPort",
    "direction",
    "size",
    "_interface",
    "protocolType",
    "encapsulationType",
    "encapsulationID",
    "state",
    "bidirectional",
    "encapsulation",
    "innerPacket"
})
public class PacketFilterCondition {

    @XmlElement(name = "SourceMAC")
    protected String sourceMAC;
    @XmlElement(name = "DestinationMAC")
    protected String destinationMAC;
    @XmlElement(name = "SourceAddress")
    protected String sourceAddress;
    @XmlElement(name = "DestinationAddress")
    protected String destinationAddress;
    @XmlElement(name = "SourcePort")
    protected String sourcePort;
    @XmlElement(name = "DestinationPort")
    protected String destinationPort;
    protected String direction;
    @XmlElement(name = "Size")
    protected String size;
    @XmlElement(name = "Interface")
    protected String _interface;
    @XmlElement(name = "ProtocolType")
    protected String protocolType;
    protected String encapsulationType;
    protected String encapsulationID;
    @XmlElement(name = "State")
    protected List<String> state;
    protected Boolean bidirectional;
    protected Boolean encapsulation;
    protected PacketFilterCondition innerPacket;

    /**
     * Recupera il valore della proprietà sourceMAC.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceMAC() {
        return sourceMAC;
    }

    /**
     * Imposta il valore della proprietà sourceMAC.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceMAC(String value) {
        this.sourceMAC = value;
    }

    /**
     * Recupera il valore della proprietà destinationMAC.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationMAC() {
        return destinationMAC;
    }

    /**
     * Imposta il valore della proprietà destinationMAC.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationMAC(String value) {
        this.destinationMAC = value;
    }

    /**
     * Recupera il valore della proprietà sourceAddress.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceAddress() {
        return sourceAddress;
    }

    /**
     * Imposta il valore della proprietà sourceAddress.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceAddress(String value) {
        this.sourceAddress = value;
    }

    /**
     * Recupera il valore della proprietà destinationAddress.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * Imposta il valore della proprietà destinationAddress.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationAddress(String value) {
        this.destinationAddress = value;
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
     * Recupera il valore della proprietà direction.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Imposta il valore della proprietà direction.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirection(String value) {
        this.direction = value;
    }

    /**
     * Recupera il valore della proprietà size.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSize() {
        return size;
    }

    /**
     * Imposta il valore della proprietà size.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSize(String value) {
        this.size = value;
    }

    /**
     * Recupera il valore della proprietà interface.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterface() {
        return _interface;
    }

    /**
     * Imposta il valore della proprietà interface.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterface(String value) {
        this._interface = value;
    }

    /**
     * Recupera il valore della proprietà protocolType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocolType() {
        return protocolType;
    }

    /**
     * Imposta il valore della proprietà protocolType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocolType(String value) {
        this.protocolType = value;
    }

    /**
     * Recupera il valore della proprietà encapsulationType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncapsulationType() {
        return encapsulationType;
    }

    /**
     * Imposta il valore della proprietà encapsulationType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncapsulationType(String value) {
        this.encapsulationType = value;
    }

    /**
     * Recupera il valore della proprietà encapsulationID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncapsulationID() {
        return encapsulationID;
    }

    /**
     * Imposta il valore della proprietà encapsulationID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncapsulationID(String value) {
        this.encapsulationID = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the state property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getState().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getState() {
        if (state == null) {
            state = new ArrayList<String>();
        }
        return this.state;
    }

    /**
     * Recupera il valore della proprietà bidirectional.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBidirectional() {
        return bidirectional;
    }

    /**
     * Imposta il valore della proprietà bidirectional.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBidirectional(Boolean value) {
        this.bidirectional = value;
    }

    /**
     * Recupera il valore della proprietà encapsulation.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEncapsulation() {
        return encapsulation;
    }

    /**
     * Imposta il valore della proprietà encapsulation.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEncapsulation(Boolean value) {
        this.encapsulation = value;
    }

    /**
     * Recupera il valore della proprietà innerPacket.
     * 
     * @return
     *     possible object is
     *     {@link PacketFilterCondition }
     *     
     */
    public PacketFilterCondition getInnerPacket() {
        return innerPacket;
    }

    /**
     * Imposta il valore della proprietà innerPacket.
     * 
     * @param value
     *     allowed object is
     *     {@link PacketFilterCondition }
     *     
     */
    public void setInnerPacket(PacketFilterCondition value) {
        this.innerPacket = value;
    }

}
