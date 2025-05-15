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
 * <p>Classe Java per EventDependency complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="EventDependency"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}Dependency"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="eventID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="configurationCondition" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationCondition"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventDependency", propOrder = {
    "eventID",
    "configurationCondition"
})
public class EventDependency
    extends Dependency
{

    @XmlElement(required = true)
    protected String eventID;
    @XmlElement(required = true)
    protected ConfigurationCondition configurationCondition;

    /**
     * Recupera il valore della proprietà eventID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Imposta il valore della proprietà eventID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventID(String value) {
        this.eventID = value;
    }

    /**
     * Recupera il valore della proprietà configurationCondition.
     * 
     * @return
     *     possible object is
     *     {@link ConfigurationCondition }
     *     
     */
    public ConfigurationCondition getConfigurationCondition() {
        return configurationCondition;
    }

    /**
     * Imposta il valore della proprietà configurationCondition.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfigurationCondition }
     *     
     */
    public void setConfigurationCondition(ConfigurationCondition value) {
        this.configurationCondition = value;
    }

}
