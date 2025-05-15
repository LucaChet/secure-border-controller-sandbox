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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ioTHoneyPotType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ioTHoneyPotType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.example.org/IoTHoneynetSchema}honeyPotType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="model" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="location" type="{http://www.example.org/IoTHoneynetSchema}physicalLocation" minOccurs="0"/&gt;
 *         &lt;element name="resource" type="{http://www.example.org/IoTHoneynetSchema}ioTResourceType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ioTHoneyPotType", namespace = "http://www.example.org/IoTHoneynetSchema", propOrder = {
    "model",
    "location",
    "resource"
})
@XmlSeeAlso({
    IoTRouterType.class
})
public class IoTHoneyPotType
    extends HoneyPotType
{

    protected String model;
    protected PhysicalLocation location;
    @XmlSchemaType(name = "string")
    protected List<IoTResourceType> resource;

    /**
     * Recupera il valore della proprietà model.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * Imposta il valore della proprietà model.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Recupera il valore della proprietà location.
     * 
     * @return
     *     possible object is
     *     {@link PhysicalLocation }
     *     
     */
    public PhysicalLocation getLocation() {
        return location;
    }

    /**
     * Imposta il valore della proprietà location.
     * 
     * @param value
     *     allowed object is
     *     {@link PhysicalLocation }
     *     
     */
    public void setLocation(PhysicalLocation value) {
        this.location = value;
    }

    /**
     * Gets the value of the resource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the resource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IoTResourceType }
     * 
     * 
     */
    public List<IoTResourceType> getResource() {
        if (resource == null) {
            resource = new ArrayList<IoTResourceType>();
        }
        return this.resource;
    }

}
