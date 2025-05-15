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
 * <p>Classe Java per DataProtectionAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DataProtectionAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="technology" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="technologyActionParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ActionParameters"/&gt;
 *         &lt;element name="technologyActionSecurityProperty" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}TechnologyActionSecurityProperty" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataProtectionAction", propOrder = {
    "technology",
    "technologyActionParameters",
    "technologyActionSecurityProperty"
})
public class DataProtectionAction
    extends ConfigurationAction
{

    @XmlElement(required = true)
    protected String technology;
    @XmlElement(required = true)
    protected ActionParameters technologyActionParameters;
    protected List<TechnologyActionSecurityProperty> technologyActionSecurityProperty;

    /**
     * Recupera il valore della proprietà technology.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTechnology() {
        return technology;
    }

    /**
     * Imposta il valore della proprietà technology.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTechnology(String value) {
        this.technology = value;
    }

    /**
     * Recupera il valore della proprietà technologyActionParameters.
     * 
     * @return
     *     possible object is
     *     {@link ActionParameters }
     *     
     */
    public ActionParameters getTechnologyActionParameters() {
        return technologyActionParameters;
    }

    /**
     * Imposta il valore della proprietà technologyActionParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link ActionParameters }
     *     
     */
    public void setTechnologyActionParameters(ActionParameters value) {
        this.technologyActionParameters = value;
    }

    /**
     * Gets the value of the technologyActionSecurityProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the technologyActionSecurityProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTechnologyActionSecurityProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TechnologyActionSecurityProperty }
     * 
     * 
     */
    public List<TechnologyActionSecurityProperty> getTechnologyActionSecurityProperty() {
        if (technologyActionSecurityProperty == null) {
            technologyActionSecurityProperty = new ArrayList<TechnologyActionSecurityProperty>();
        }
        return this.technologyActionSecurityProperty;
    }

}
