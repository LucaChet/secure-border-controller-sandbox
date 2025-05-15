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
 * <p>Classe Java per LearningModelInfo complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="LearningModelInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationCondition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="learningType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="modelParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ModelParameters" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LearningModelInfo", propOrder = {
    "learningType",
    "modelParameters"
})
public class LearningModelInfo
    extends ConfigurationCondition
{

    protected String learningType;
    protected ModelParameters modelParameters;

    /**
     * Recupera il valore della proprietà learningType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLearningType() {
        return learningType;
    }

    /**
     * Imposta il valore della proprietà learningType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLearningType(String value) {
        this.learningType = value;
    }

    /**
     * Recupera il valore della proprietà modelParameters.
     * 
     * @return
     *     possible object is
     *     {@link ModelParameters }
     *     
     */
    public ModelParameters getModelParameters() {
        return modelParameters;
    }

    /**
     * Imposta il valore della proprietà modelParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link ModelParameters }
     *     
     */
    public void setModelParameters(ModelParameters value) {
        this.modelParameters = value;
    }

}
