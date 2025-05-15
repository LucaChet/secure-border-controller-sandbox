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
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per honeyPotType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="honeyPotType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="interaction_level" type="{http://www.example.org/IoTHoneynetSchema}interactionLevel"/&gt;
 *         &lt;element name="if" type="{http://www.example.org/IoTHoneynetSchema}ifType"/&gt;
 *         &lt;element name="operatingSystem" type="{http://www.example.org/IoTHoneynetSchema}operatingSystemType"/&gt;
 *         &lt;element name="software" type="{http://www.example.org/IoTHoneynetSchema}softwareType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}byte" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "honeyPotType", namespace = "http://www.example.org/IoTHoneynetSchema", propOrder = {
    "name",
    "interactionLevel",
    "_if",
    "operatingSystem",
    "software"
})
@XmlSeeAlso({
    IoTHoneyPotType.class
})
public class HoneyPotType {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(name = "interaction_level", required = true)
    @XmlSchemaType(name = "string")
    protected InteractionLevel interactionLevel;
    @XmlElement(name = "if", required = true)
    protected IfType _if;
    @XmlElement(required = true)
    protected OperatingSystemType operatingSystem;
    @XmlElement(required = true)
    protected SoftwareType software;
    @XmlAttribute(name = "id")
    protected Byte id;

    /**
     * Recupera il valore della proprietà name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il valore della proprietà name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Recupera il valore della proprietà interactionLevel.
     * 
     * @return
     *     possible object is
     *     {@link InteractionLevel }
     *     
     */
    public InteractionLevel getInteractionLevel() {
        return interactionLevel;
    }

    /**
     * Imposta il valore della proprietà interactionLevel.
     * 
     * @param value
     *     allowed object is
     *     {@link InteractionLevel }
     *     
     */
    public void setInteractionLevel(InteractionLevel value) {
        this.interactionLevel = value;
    }

    /**
     * Recupera il valore della proprietà if.
     * 
     * @return
     *     possible object is
     *     {@link IfType }
     *     
     */
    public IfType getIf() {
        return _if;
    }

    /**
     * Imposta il valore della proprietà if.
     * 
     * @param value
     *     allowed object is
     *     {@link IfType }
     *     
     */
    public void setIf(IfType value) {
        this._if = value;
    }

    /**
     * Recupera il valore della proprietà operatingSystem.
     * 
     * @return
     *     possible object is
     *     {@link OperatingSystemType }
     *     
     */
    public OperatingSystemType getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * Imposta il valore della proprietà operatingSystem.
     * 
     * @param value
     *     allowed object is
     *     {@link OperatingSystemType }
     *     
     */
    public void setOperatingSystem(OperatingSystemType value) {
        this.operatingSystem = value;
    }

    /**
     * Recupera il valore della proprietà software.
     * 
     * @return
     *     possible object is
     *     {@link SoftwareType }
     *     
     */
    public SoftwareType getSoftware() {
        return software;
    }

    /**
     * Imposta il valore della proprietà software.
     * 
     * @param value
     *     allowed object is
     *     {@link SoftwareType }
     *     
     */
    public void setSoftware(SoftwareType value) {
        this.software = value;
    }

    /**
     * Recupera il valore della proprietà id.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getId() {
        return id;
    }

    /**
     * Imposta il valore della proprietà id.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setId(Byte value) {
        this.id = value;
    }

}
