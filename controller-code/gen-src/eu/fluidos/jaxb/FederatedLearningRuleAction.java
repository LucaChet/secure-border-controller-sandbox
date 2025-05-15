//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per FederatedLearningRuleAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="FederatedLearningRuleAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="federationLearnigActionType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FederationLearnigActionType" minOccurs="0"/&gt;
 *         &lt;element name="aggregatorParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FLConfigurationCondition" minOccurs="0"/&gt;
 *         &lt;element name="localConnectionParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FLConfigurationCondition" minOccurs="0"/&gt;
 *         &lt;element name="integrationFabricParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FLConfigurationCondition" minOccurs="0"/&gt;
 *         &lt;element name="learningModelInfo" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FLConfigurationCondition" minOccurs="0"/&gt;
 *         &lt;element name="learningParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FLConfigurationCondition" minOccurs="0"/&gt;
 *         &lt;element name="obfuscationInfo" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FLConfigurationCondition" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FederatedLearningRuleAction", propOrder = {
    "federationLearnigActionType",
    "aggregatorParameters",
    "localConnectionParameters",
    "integrationFabricParameters",
    "learningModelInfo",
    "learningParameters",
    "obfuscationInfo"
})
public class FederatedLearningRuleAction
    extends ConfigurationAction
{

    @XmlSchemaType(name = "string")
    protected FederationLearnigActionType federationLearnigActionType;
    protected FLConfigurationCondition aggregatorParameters;
    protected FLConfigurationCondition localConnectionParameters;
    protected FLConfigurationCondition integrationFabricParameters;
    protected FLConfigurationCondition learningModelInfo;
    protected FLConfigurationCondition learningParameters;
    protected FLConfigurationCondition obfuscationInfo;

    /**
     * Recupera il valore della proprietà federationLearnigActionType.
     * 
     * @return
     *     possible object is
     *     {@link FederationLearnigActionType }
     *     
     */
    public FederationLearnigActionType getFederationLearnigActionType() {
        return federationLearnigActionType;
    }

    /**
     * Imposta il valore della proprietà federationLearnigActionType.
     * 
     * @param value
     *     allowed object is
     *     {@link FederationLearnigActionType }
     *     
     */
    public void setFederationLearnigActionType(FederationLearnigActionType value) {
        this.federationLearnigActionType = value;
    }

    /**
     * Recupera il valore della proprietà aggregatorParameters.
     * 
     * @return
     *     possible object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public FLConfigurationCondition getAggregatorParameters() {
        return aggregatorParameters;
    }

    /**
     * Imposta il valore della proprietà aggregatorParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public void setAggregatorParameters(FLConfigurationCondition value) {
        this.aggregatorParameters = value;
    }

    /**
     * Recupera il valore della proprietà localConnectionParameters.
     * 
     * @return
     *     possible object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public FLConfigurationCondition getLocalConnectionParameters() {
        return localConnectionParameters;
    }

    /**
     * Imposta il valore della proprietà localConnectionParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public void setLocalConnectionParameters(FLConfigurationCondition value) {
        this.localConnectionParameters = value;
    }

    /**
     * Recupera il valore della proprietà integrationFabricParameters.
     * 
     * @return
     *     possible object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public FLConfigurationCondition getIntegrationFabricParameters() {
        return integrationFabricParameters;
    }

    /**
     * Imposta il valore della proprietà integrationFabricParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public void setIntegrationFabricParameters(FLConfigurationCondition value) {
        this.integrationFabricParameters = value;
    }

    /**
     * Recupera il valore della proprietà learningModelInfo.
     * 
     * @return
     *     possible object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public FLConfigurationCondition getLearningModelInfo() {
        return learningModelInfo;
    }

    /**
     * Imposta il valore della proprietà learningModelInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public void setLearningModelInfo(FLConfigurationCondition value) {
        this.learningModelInfo = value;
    }

    /**
     * Recupera il valore della proprietà learningParameters.
     * 
     * @return
     *     possible object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public FLConfigurationCondition getLearningParameters() {
        return learningParameters;
    }

    /**
     * Imposta il valore della proprietà learningParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public void setLearningParameters(FLConfigurationCondition value) {
        this.learningParameters = value;
    }

    /**
     * Recupera il valore della proprietà obfuscationInfo.
     * 
     * @return
     *     possible object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public FLConfigurationCondition getObfuscationInfo() {
        return obfuscationInfo;
    }

    /**
     * Imposta il valore della proprietà obfuscationInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link FLConfigurationCondition }
     *     
     */
    public void setObfuscationInfo(FLConfigurationCondition value) {
        this.obfuscationInfo = value;
    }

}
