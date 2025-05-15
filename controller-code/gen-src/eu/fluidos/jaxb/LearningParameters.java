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
 * <p>Classe Java per LearningParameters complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="LearningParameters"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationCondition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="testSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="localEpochs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="batchSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="stepsPerEpoch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LearningParameters", propOrder = {
    "testSize",
    "localEpochs",
    "batchSize",
    "stepsPerEpoch"
})
public class LearningParameters
    extends ConfigurationCondition
{

    protected String testSize;
    protected String localEpochs;
    protected String batchSize;
    protected String stepsPerEpoch;

    /**
     * Recupera il valore della proprietà testSize.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestSize() {
        return testSize;
    }

    /**
     * Imposta il valore della proprietà testSize.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestSize(String value) {
        this.testSize = value;
    }

    /**
     * Recupera il valore della proprietà localEpochs.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalEpochs() {
        return localEpochs;
    }

    /**
     * Imposta il valore della proprietà localEpochs.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalEpochs(String value) {
        this.localEpochs = value;
    }

    /**
     * Recupera il valore della proprietà batchSize.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatchSize() {
        return batchSize;
    }

    /**
     * Imposta il valore della proprietà batchSize.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatchSize(String value) {
        this.batchSize = value;
    }

    /**
     * Recupera il valore della proprietà stepsPerEpoch.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStepsPerEpoch() {
        return stepsPerEpoch;
    }

    /**
     * Imposta il valore della proprietà stepsPerEpoch.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStepsPerEpoch(String value) {
        this.stepsPerEpoch = value;
    }

}
