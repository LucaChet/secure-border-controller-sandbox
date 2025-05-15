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
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ITResourceType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ITResourceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="configuration" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}Configuration"/&gt;
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="dependencies" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}Dependencies" minOccurs="0"/&gt;
 *         &lt;element name="enablerCandidates" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}EnablerCandidates" minOccurs="0"/&gt;
 *         &lt;element name="orchestrationRequirements" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}OrchestrationRequirements" minOccurs="0"/&gt;
 *         &lt;element name="enforcementRequirements" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}EnforcementRequirements" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="orchestrationID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="tenantID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="sliceID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ITResourceType", propOrder = {
    "configuration",
    "priority",
    "dependencies",
    "enablerCandidates",
    "orchestrationRequirements",
    "enforcementRequirements"
})
public class ITResourceType {

    @XmlElement(required = true)
    protected Configuration configuration;
    protected BigInteger priority;
    protected Dependencies dependencies;
    protected EnablerCandidates enablerCandidates;
    protected OrchestrationRequirements orchestrationRequirements;
    protected EnforcementRequirements enforcementRequirements;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "orchestrationID")
    protected String orchestrationID;
    @XmlAttribute(name = "tenantID")
    protected String tenantID;
    @XmlAttribute(name = "sliceID")
    protected String sliceID;

    /**
     * Recupera il valore della proprietà configuration.
     * 
     * @return
     *     possible object is
     *     {@link Configuration }
     *     
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Imposta il valore della proprietà configuration.
     * 
     * @param value
     *     allowed object is
     *     {@link Configuration }
     *     
     */
    public void setConfiguration(Configuration value) {
        this.configuration = value;
    }

    /**
     * Recupera il valore della proprietà priority.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPriority() {
        return priority;
    }

    /**
     * Imposta il valore della proprietà priority.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPriority(BigInteger value) {
        this.priority = value;
    }

    /**
     * Recupera il valore della proprietà dependencies.
     * 
     * @return
     *     possible object is
     *     {@link Dependencies }
     *     
     */
    public Dependencies getDependencies() {
        return dependencies;
    }

    /**
     * Imposta il valore della proprietà dependencies.
     * 
     * @param value
     *     allowed object is
     *     {@link Dependencies }
     *     
     */
    public void setDependencies(Dependencies value) {
        this.dependencies = value;
    }

    /**
     * Recupera il valore della proprietà enablerCandidates.
     * 
     * @return
     *     possible object is
     *     {@link EnablerCandidates }
     *     
     */
    public EnablerCandidates getEnablerCandidates() {
        return enablerCandidates;
    }

    /**
     * Imposta il valore della proprietà enablerCandidates.
     * 
     * @param value
     *     allowed object is
     *     {@link EnablerCandidates }
     *     
     */
    public void setEnablerCandidates(EnablerCandidates value) {
        this.enablerCandidates = value;
    }

    /**
     * Recupera il valore della proprietà orchestrationRequirements.
     * 
     * @return
     *     possible object is
     *     {@link OrchestrationRequirements }
     *     
     */
    public OrchestrationRequirements getOrchestrationRequirements() {
        return orchestrationRequirements;
    }

    /**
     * Imposta il valore della proprietà orchestrationRequirements.
     * 
     * @param value
     *     allowed object is
     *     {@link OrchestrationRequirements }
     *     
     */
    public void setOrchestrationRequirements(OrchestrationRequirements value) {
        this.orchestrationRequirements = value;
    }

    /**
     * Recupera il valore della proprietà enforcementRequirements.
     * 
     * @return
     *     possible object is
     *     {@link EnforcementRequirements }
     *     
     */
    public EnforcementRequirements getEnforcementRequirements() {
        return enforcementRequirements;
    }

    /**
     * Imposta il valore della proprietà enforcementRequirements.
     * 
     * @param value
     *     allowed object is
     *     {@link EnforcementRequirements }
     *     
     */
    public void setEnforcementRequirements(EnforcementRequirements value) {
        this.enforcementRequirements = value;
    }

    /**
     * Recupera il valore della proprietà id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Imposta il valore della proprietà id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Recupera il valore della proprietà orchestrationID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrchestrationID() {
        return orchestrationID;
    }

    /**
     * Imposta il valore della proprietà orchestrationID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrchestrationID(String value) {
        this.orchestrationID = value;
    }

    /**
     * Recupera il valore della proprietà tenantID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTenantID() {
        return tenantID;
    }

    /**
     * Imposta il valore della proprietà tenantID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTenantID(String value) {
        this.tenantID = value;
    }

    /**
     * Recupera il valore della proprietà sliceID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSliceID() {
        return sliceID;
    }

    /**
     * Imposta il valore della proprietà sliceID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSliceID(String value) {
        this.sliceID = value;
    }

}
