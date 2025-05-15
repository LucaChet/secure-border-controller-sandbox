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
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ifType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ifType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="mac_addr" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ip" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}byte" /&gt;
 *       &lt;attribute name="net" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ifType", namespace = "http://www.example.org/IoTHoneynetSchema", propOrder = {
    "name",
    "macAddr",
    "ip"
})
public class IfType {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(name = "mac_addr", required = true)
    protected String macAddr;
    @XmlElement(required = true)
    protected String ip;
    @XmlAttribute(name = "id")
    protected Byte id;
    @XmlAttribute(name = "net")
    protected String net;

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
     * Recupera il valore della proprietà macAddr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMacAddr() {
        return macAddr;
    }

    /**
     * Imposta il valore della proprietà macAddr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMacAddr(String value) {
        this.macAddr = value;
    }

    /**
     * Recupera il valore della proprietà ip.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIp() {
        return ip;
    }

    /**
     * Imposta il valore della proprietà ip.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIp(String value) {
        this.ip = value;
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

    /**
     * Recupera il valore della proprietà net.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNet() {
        return net;
    }

    /**
     * Imposta il valore della proprietà net.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNet(String value) {
        this.net = value;
    }

}
