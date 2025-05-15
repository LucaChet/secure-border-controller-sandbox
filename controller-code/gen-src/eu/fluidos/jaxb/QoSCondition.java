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
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per QoSCondition complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="QoSCondition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationCondition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="profile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="throughput" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="transitDelay" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="errorRate" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="resilence" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="maxBandwidth" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="minBandwidth" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QoSCondition", propOrder = {
    "profile",
    "throughput",
    "transitDelay",
    "priority",
    "errorRate",
    "resilence",
    "maxBandwidth",
    "minBandwidth"
})
public class QoSCondition
    extends ConfigurationCondition
{

    protected String profile;
    protected BigInteger throughput;
    protected BigInteger transitDelay;
    protected BigInteger priority;
    protected BigInteger errorRate;
    protected BigInteger resilence;
    @XmlElement(required = true)
    protected BigInteger maxBandwidth;
    @XmlElement(required = true)
    protected BigInteger minBandwidth;

    /**
     * Recupera il valore della proprietà profile.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfile() {
        return profile;
    }

    /**
     * Imposta il valore della proprietà profile.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfile(String value) {
        this.profile = value;
    }

    /**
     * Recupera il valore della proprietà throughput.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getThroughput() {
        return throughput;
    }

    /**
     * Imposta il valore della proprietà throughput.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setThroughput(BigInteger value) {
        this.throughput = value;
    }

    /**
     * Recupera il valore della proprietà transitDelay.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTransitDelay() {
        return transitDelay;
    }

    /**
     * Imposta il valore della proprietà transitDelay.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTransitDelay(BigInteger value) {
        this.transitDelay = value;
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
     * Recupera il valore della proprietà errorRate.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getErrorRate() {
        return errorRate;
    }

    /**
     * Imposta il valore della proprietà errorRate.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setErrorRate(BigInteger value) {
        this.errorRate = value;
    }

    /**
     * Recupera il valore della proprietà resilence.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getResilence() {
        return resilence;
    }

    /**
     * Imposta il valore della proprietà resilence.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setResilence(BigInteger value) {
        this.resilence = value;
    }

    /**
     * Recupera il valore della proprietà maxBandwidth.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxBandwidth() {
        return maxBandwidth;
    }

    /**
     * Imposta il valore della proprietà maxBandwidth.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxBandwidth(BigInteger value) {
        this.maxBandwidth = value;
    }

    /**
     * Recupera il valore della proprietà minBandwidth.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMinBandwidth() {
        return minBandwidth;
    }

    /**
     * Imposta il valore della proprietà minBandwidth.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMinBandwidth(BigInteger value) {
        this.minBandwidth = value;
    }

}
