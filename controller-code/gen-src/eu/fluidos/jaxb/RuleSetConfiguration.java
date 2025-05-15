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
 * <p>Classe Java per RuleSetConfiguration complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RuleSetConfiguration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}Configuration"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="defaultAction" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction" minOccurs="0"/&gt;
 *         &lt;element name="configurationRule" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationRule" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="NetworkSlicingConditionParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}NetworkSlicingConditionParameters" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="resolutionStrategy" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ResolutionStrategy" minOccurs="0"/&gt;
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RuleSetConfiguration", propOrder = {
    "defaultAction",
    "configurationRule",
    "networkSlicingConditionParameters",
    "resolutionStrategy",
    "name"
})
public class RuleSetConfiguration
    extends Configuration
{

    protected ConfigurationAction defaultAction;
    protected List<ConfigurationRule> configurationRule;
    @XmlElement(name = "NetworkSlicingConditionParameters")
    protected List<NetworkSlicingConditionParameters> networkSlicingConditionParameters;
    protected ResolutionStrategy resolutionStrategy;
    @XmlElement(name = "Name", required = true)
    protected String name;

    /**
     * Recupera il valore della proprietà defaultAction.
     * 
     * @return
     *     possible object is
     *     {@link ConfigurationAction }
     *     
     */
    public ConfigurationAction getDefaultAction() {
        return defaultAction;
    }

    /**
     * Imposta il valore della proprietà defaultAction.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfigurationAction }
     *     
     */
    public void setDefaultAction(ConfigurationAction value) {
        this.defaultAction = value;
    }

    /**
     * Gets the value of the configurationRule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the configurationRule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfigurationRule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationRule }
     * 
     * 
     */
    public List<ConfigurationRule> getConfigurationRule() {
        if (configurationRule == null) {
            configurationRule = new ArrayList<ConfigurationRule>();
        }
        return this.configurationRule;
    }

    /**
     * Gets the value of the networkSlicingConditionParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the networkSlicingConditionParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNetworkSlicingConditionParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NetworkSlicingConditionParameters }
     * 
     * 
     */
    public List<NetworkSlicingConditionParameters> getNetworkSlicingConditionParameters() {
        if (networkSlicingConditionParameters == null) {
            networkSlicingConditionParameters = new ArrayList<NetworkSlicingConditionParameters>();
        }
        return this.networkSlicingConditionParameters;
    }

    /**
     * Recupera il valore della proprietà resolutionStrategy.
     * 
     * @return
     *     possible object is
     *     {@link ResolutionStrategy }
     *     
     */
    public ResolutionStrategy getResolutionStrategy() {
        return resolutionStrategy;
    }

    /**
     * Imposta il valore della proprietà resolutionStrategy.
     * 
     * @param value
     *     allowed object is
     *     {@link ResolutionStrategy }
     *     
     */
    public void setResolutionStrategy(ResolutionStrategy value) {
        this.resolutionStrategy = value;
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
