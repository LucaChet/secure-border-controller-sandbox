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
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per routerType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="routerType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="if" type="{http://www.example.org/IoTHoneynetSchema}ifType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="route" type="{http://www.example.org/IoTHoneynetSchema}routeType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="operatingSystem" type="{http://www.example.org/IoTHoneynetSchema}operatingSystemType"/&gt;
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
@XmlType(name = "routerType", namespace = "http://www.example.org/IoTHoneynetSchema", propOrder = {
    "name",
    "_if",
    "route",
    "operatingSystem"
})
public class RouterType {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(name = "if", required = true)
    protected List<IfType> _if;
    protected List<RouteType> route;
    @XmlElement(required = true)
    protected OperatingSystemType operatingSystem;
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
     * Gets the value of the if property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the if property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IfType }
     * 
     * 
     */
    public List<IfType> getIf() {
        if (_if == null) {
            _if = new ArrayList<IfType>();
        }
        return this._if;
    }

    /**
     * Gets the value of the route property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the route property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RouteType }
     * 
     * 
     */
    public List<RouteType> getRoute() {
        if (route == null) {
            route = new ArrayList<RouteType>();
        }
        return this.route;
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
