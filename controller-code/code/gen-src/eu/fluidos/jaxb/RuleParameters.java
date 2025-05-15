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
 * <p>Classe Java per RuleParameters complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RuleParameters"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationCondition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="destinationAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="action" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sourceAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RuleParameters", propOrder = {
    "destinationAddress",
    "action",
    "sourceAddress",
    "name"
})
public class RuleParameters
    extends ConfigurationCondition
{

    protected String destinationAddress;
    protected String action;
    protected String sourceAddress;
    protected String name;

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
     * Recupera il valore della proprietà action.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Imposta il valore della proprietà action.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
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

}
