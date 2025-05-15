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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per AnonymityAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="AnonymityAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="anonymityActionType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}AnonymityActionType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="anonymityTarget" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}AnonymityConfigurationCondition" minOccurs="0"/&gt;
 *         &lt;element name="anonymityTechnologyParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}AnonymityTechnologyParameter" minOccurs="0"/&gt;
 *         &lt;element name="aditionalAnonymityParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}KeyValue" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnonymityAction", propOrder = {
    "anonymityActionType",
    "anonymityTarget",
    "anonymityTechnologyParameters",
    "aditionalAnonymityParameters"
})
public class AnonymityAction
    extends ConfigurationAction
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected List<AnonymityActionType> anonymityActionType;
    protected AnonymityConfigurationCondition anonymityTarget;
    protected AnonymityTechnologyParameter anonymityTechnologyParameters;
    protected List<KeyValue> aditionalAnonymityParameters;

    /**
     * Gets the value of the anonymityActionType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the anonymityActionType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnonymityActionType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AnonymityActionType }
     * 
     * 
     */
    public List<AnonymityActionType> getAnonymityActionType() {
        if (anonymityActionType == null) {
            anonymityActionType = new ArrayList<AnonymityActionType>();
        }
        return this.anonymityActionType;
    }

    /**
     * Recupera il valore della proprietà anonymityTarget.
     * 
     * @return
     *     possible object is
     *     {@link AnonymityConfigurationCondition }
     *     
     */
    public AnonymityConfigurationCondition getAnonymityTarget() {
        return anonymityTarget;
    }

    /**
     * Imposta il valore della proprietà anonymityTarget.
     * 
     * @param value
     *     allowed object is
     *     {@link AnonymityConfigurationCondition }
     *     
     */
    public void setAnonymityTarget(AnonymityConfigurationCondition value) {
        this.anonymityTarget = value;
    }

    /**
     * Recupera il valore della proprietà anonymityTechnologyParameters.
     * 
     * @return
     *     possible object is
     *     {@link AnonymityTechnologyParameter }
     *     
     */
    public AnonymityTechnologyParameter getAnonymityTechnologyParameters() {
        return anonymityTechnologyParameters;
    }

    /**
     * Imposta il valore della proprietà anonymityTechnologyParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link AnonymityTechnologyParameter }
     *     
     */
    public void setAnonymityTechnologyParameters(AnonymityTechnologyParameter value) {
        this.anonymityTechnologyParameters = value;
    }

    /**
     * Gets the value of the aditionalAnonymityParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the aditionalAnonymityParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAditionalAnonymityParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeyValue }
     * 
     * 
     */
    public List<KeyValue> getAditionalAnonymityParameters() {
        if (aditionalAnonymityParameters == null) {
            aditionalAnonymityParameters = new ArrayList<KeyValue>();
        }
        return this.aditionalAnonymityParameters;
    }

}
