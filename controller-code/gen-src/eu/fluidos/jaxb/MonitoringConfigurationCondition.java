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
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per MonitoringConfigurationCondition complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MonitoringConfigurationCondition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FilteringConfigurationCondition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="detectionFilter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="channelProtected" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="signatureList" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}SignatureList" minOccurs="0"/&gt;
 *         &lt;element name="maxCount" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}Counts" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="minCount" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}Counts" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonitoringConfigurationCondition", propOrder = {
    "detectionFilter",
    "channelProtected",
    "signatureList",
    "maxCount",
    "minCount"
})
public class MonitoringConfigurationCondition
    extends FilteringConfigurationCondition
{

    protected String detectionFilter;
    protected Boolean channelProtected;
    protected SignatureList signatureList;
    protected List<Counts> maxCount;
    protected List<Counts> minCount;

    /**
     * Recupera il valore della proprietà detectionFilter.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetectionFilter() {
        return detectionFilter;
    }

    /**
     * Imposta il valore della proprietà detectionFilter.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetectionFilter(String value) {
        this.detectionFilter = value;
    }

    /**
     * Recupera il valore della proprietà channelProtected.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isChannelProtected() {
        return channelProtected;
    }

    /**
     * Imposta il valore della proprietà channelProtected.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setChannelProtected(Boolean value) {
        this.channelProtected = value;
    }

    /**
     * Recupera il valore della proprietà signatureList.
     * 
     * @return
     *     possible object is
     *     {@link SignatureList }
     *     
     */
    public SignatureList getSignatureList() {
        return signatureList;
    }

    /**
     * Imposta il valore della proprietà signatureList.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureList }
     *     
     */
    public void setSignatureList(SignatureList value) {
        this.signatureList = value;
    }

    /**
     * Gets the value of the maxCount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the maxCount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMaxCount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Counts }
     * 
     * 
     */
    public List<Counts> getMaxCount() {
        if (maxCount == null) {
            maxCount = new ArrayList<Counts>();
        }
        return this.maxCount;
    }

    /**
     * Gets the value of the minCount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the minCount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMinCount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Counts }
     * 
     * 
     */
    public List<Counts> getMinCount() {
        if (minCount == null) {
            minCount = new ArrayList<Counts>();
        }
        return this.minCount;
    }

}
