//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per FilterFLConfigurationCondition complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="FilterFLConfigurationCondition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationCondition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="instanceThreshold" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="aggregatorParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}AggregatorParameters" minOccurs="0"/&gt;
 *         &lt;element name="localConnectionParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}LocalConnectionParameters" minOccurs="0"/&gt;
 *         &lt;element name="integrationFabricParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}IntegrationFabricParameters" minOccurs="0"/&gt;
 *         &lt;element name="learningModelInfo" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}LearningModelInfo" minOccurs="0"/&gt;
 *         &lt;element name="learningParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}LearningParameters" minOccurs="0"/&gt;
 *         &lt;element name="obfuscationInfo" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ObfuscationInfo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FilterFLConfigurationCondition", propOrder = {
    "instanceThreshold",
    "aggregatorParameters",
    "localConnectionParameters",
    "integrationFabricParameters",
    "learningModelInfo",
    "learningParameters",
    "obfuscationInfo"
})
@XmlSeeAlso({
    FLConfigurationCondition.class
})
public class FilterFLConfigurationCondition
    extends ConfigurationCondition
{

    protected BigInteger instanceThreshold;
    protected AggregatorParameters aggregatorParameters;
    protected LocalConnectionParameters localConnectionParameters;
    protected IntegrationFabricParameters integrationFabricParameters;
    protected LearningModelInfo learningModelInfo;
    protected LearningParameters learningParameters;
    protected ObfuscationInfo obfuscationInfo;

    /**
     * Recupera il valore della proprietà instanceThreshold.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getInstanceThreshold() {
        return instanceThreshold;
    }

    /**
     * Imposta il valore della proprietà instanceThreshold.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setInstanceThreshold(BigInteger value) {
        this.instanceThreshold = value;
    }

    /**
     * Recupera il valore della proprietà aggregatorParameters.
     * 
     * @return
     *     possible object is
     *     {@link AggregatorParameters }
     *     
     */
    public AggregatorParameters getAggregatorParameters() {
        return aggregatorParameters;
    }

    /**
     * Imposta il valore della proprietà aggregatorParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link AggregatorParameters }
     *     
     */
    public void setAggregatorParameters(AggregatorParameters value) {
        this.aggregatorParameters = value;
    }

    /**
     * Recupera il valore della proprietà localConnectionParameters.
     * 
     * @return
     *     possible object is
     *     {@link LocalConnectionParameters }
     *     
     */
    public LocalConnectionParameters getLocalConnectionParameters() {
        return localConnectionParameters;
    }

    /**
     * Imposta il valore della proprietà localConnectionParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalConnectionParameters }
     *     
     */
    public void setLocalConnectionParameters(LocalConnectionParameters value) {
        this.localConnectionParameters = value;
    }

    /**
     * Recupera il valore della proprietà integrationFabricParameters.
     * 
     * @return
     *     possible object is
     *     {@link IntegrationFabricParameters }
     *     
     */
    public IntegrationFabricParameters getIntegrationFabricParameters() {
        return integrationFabricParameters;
    }

    /**
     * Imposta il valore della proprietà integrationFabricParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link IntegrationFabricParameters }
     *     
     */
    public void setIntegrationFabricParameters(IntegrationFabricParameters value) {
        this.integrationFabricParameters = value;
    }

    /**
     * Recupera il valore della proprietà learningModelInfo.
     * 
     * @return
     *     possible object is
     *     {@link LearningModelInfo }
     *     
     */
    public LearningModelInfo getLearningModelInfo() {
        return learningModelInfo;
    }

    /**
     * Imposta il valore della proprietà learningModelInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link LearningModelInfo }
     *     
     */
    public void setLearningModelInfo(LearningModelInfo value) {
        this.learningModelInfo = value;
    }

    /**
     * Recupera il valore della proprietà learningParameters.
     * 
     * @return
     *     possible object is
     *     {@link LearningParameters }
     *     
     */
    public LearningParameters getLearningParameters() {
        return learningParameters;
    }

    /**
     * Imposta il valore della proprietà learningParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link LearningParameters }
     *     
     */
    public void setLearningParameters(LearningParameters value) {
        this.learningParameters = value;
    }

    /**
     * Recupera il valore della proprietà obfuscationInfo.
     * 
     * @return
     *     possible object is
     *     {@link ObfuscationInfo }
     *     
     */
    public ObfuscationInfo getObfuscationInfo() {
        return obfuscationInfo;
    }

    /**
     * Imposta il valore della proprietà obfuscationInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link ObfuscationInfo }
     *     
     */
    public void setObfuscationInfo(ObfuscationInfo value) {
        this.obfuscationInfo = value;
    }

}
