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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per NetworkSlicingAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="NetworkSlicingAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NetworkSlicingActionType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}NetworkSlicingActionType" minOccurs="0"/&gt;
 *         &lt;element name="networkSlicingConditionParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}NetworkSlicingConfigurationCondition" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NetworkSlicingAction", propOrder = {
    "networkSlicingActionType",
    "networkSlicingConditionParameters"
})
public class NetworkSlicingAction
    extends ConfigurationAction
{

    @XmlElement(name = "NetworkSlicingActionType")
    @XmlSchemaType(name = "string")
    protected NetworkSlicingActionType networkSlicingActionType;
    protected NetworkSlicingConfigurationCondition networkSlicingConditionParameters;

    /**
     * Recupera il valore della proprietà networkSlicingActionType.
     * 
     * @return
     *     possible object is
     *     {@link NetworkSlicingActionType }
     *     
     */
    public NetworkSlicingActionType getNetworkSlicingActionType() {
        return networkSlicingActionType;
    }

    /**
     * Imposta il valore della proprietà networkSlicingActionType.
     * 
     * @param value
     *     allowed object is
     *     {@link NetworkSlicingActionType }
     *     
     */
    public void setNetworkSlicingActionType(NetworkSlicingActionType value) {
        this.networkSlicingActionType = value;
    }

    /**
     * Recupera il valore della proprietà networkSlicingConditionParameters.
     * 
     * @return
     *     possible object is
     *     {@link NetworkSlicingConfigurationCondition }
     *     
     */
    public NetworkSlicingConfigurationCondition getNetworkSlicingConditionParameters() {
        return networkSlicingConditionParameters;
    }

    /**
     * Imposta il valore della proprietà networkSlicingConditionParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link NetworkSlicingConfigurationCondition }
     *     
     */
    public void setNetworkSlicingConditionParameters(NetworkSlicingConfigurationCondition value) {
        this.networkSlicingConditionParameters = value;
    }

}
