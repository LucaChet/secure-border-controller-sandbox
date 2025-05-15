//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per MonitoringAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MonitoringAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="monitoringActionType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}MonitoringActionType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="ProbeID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="reportPeriodicity" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="reportPerFlow" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="ruleID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="count" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aditionalMonitoringParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}KeyValue" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonitoringAction", propOrder = {
    "monitoringActionType",
    "probeID",
    "reportPeriodicity",
    "reportPerFlow",
    "ruleID",
    "count",
    "aditionalMonitoringParameters"
})
public class MonitoringAction
    extends ConfigurationAction
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected List<MonitoringActionType> monitoringActionType;
    @XmlElement(name = "ProbeID")
    protected String probeID;
    protected BigInteger reportPeriodicity;
    protected Boolean reportPerFlow;
    protected String ruleID;
    protected String count;
    protected List<KeyValue> aditionalMonitoringParameters;

    /**
     * Gets the value of the monitoringActionType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the monitoringActionType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMonitoringActionType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MonitoringActionType }
     * 
     * 
     */
    public List<MonitoringActionType> getMonitoringActionType() {
        if (monitoringActionType == null) {
            monitoringActionType = new ArrayList<MonitoringActionType>();
        }
        return this.monitoringActionType;
    }

    /**
     * Recupera il valore della proprietà probeID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeID() {
        return probeID;
    }

    /**
     * Imposta il valore della proprietà probeID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeID(String value) {
        this.probeID = value;
    }

    /**
     * Recupera il valore della proprietà reportPeriodicity.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getReportPeriodicity() {
        return reportPeriodicity;
    }

    /**
     * Imposta il valore della proprietà reportPeriodicity.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setReportPeriodicity(BigInteger value) {
        this.reportPeriodicity = value;
    }

    /**
     * Recupera il valore della proprietà reportPerFlow.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReportPerFlow() {
        return reportPerFlow;
    }

    /**
     * Imposta il valore della proprietà reportPerFlow.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReportPerFlow(Boolean value) {
        this.reportPerFlow = value;
    }

    /**
     * Recupera il valore della proprietà ruleID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleID() {
        return ruleID;
    }

    /**
     * Imposta il valore della proprietà ruleID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleID(String value) {
        this.ruleID = value;
    }

    /**
     * Recupera il valore della proprietà count.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCount() {
        return count;
    }

    /**
     * Imposta il valore della proprietà count.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCount(String value) {
        this.count = value;
    }

    /**
     * Gets the value of the aditionalMonitoringParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the aditionalMonitoringParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAditionalMonitoringParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeyValue }
     * 
     * 
     */
    public List<KeyValue> getAditionalMonitoringParameters() {
        if (aditionalMonitoringParameters == null) {
            aditionalMonitoringParameters = new ArrayList<KeyValue>();
        }
        return this.aditionalMonitoringParameters;
    }

}
