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
 * <p>Classe Java per DataAggregationAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DataAggregationAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dataAggregationActionType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}DataAggregationActionType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="aditionalRuleParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}KeyValue" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataAggregationAction", propOrder = {
    "dataAggregationActionType",
    "aditionalRuleParameters"
})
public class DataAggregationAction
    extends ConfigurationAction
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected List<DataAggregationActionType> dataAggregationActionType;
    protected List<KeyValue> aditionalRuleParameters;

    /**
     * Gets the value of the dataAggregationActionType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the dataAggregationActionType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataAggregationActionType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataAggregationActionType }
     * 
     * 
     */
    public List<DataAggregationActionType> getDataAggregationActionType() {
        if (dataAggregationActionType == null) {
            dataAggregationActionType = new ArrayList<DataAggregationActionType>();
        }
        return this.dataAggregationActionType;
    }

    /**
     * Gets the value of the aditionalRuleParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the aditionalRuleParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAditionalRuleParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeyValue }
     * 
     * 
     */
    public List<KeyValue> getAditionalRuleParameters() {
        if (aditionalRuleParameters == null) {
            aditionalRuleParameters = new ArrayList<KeyValue>();
        }
        return this.aditionalRuleParameters;
    }

}
