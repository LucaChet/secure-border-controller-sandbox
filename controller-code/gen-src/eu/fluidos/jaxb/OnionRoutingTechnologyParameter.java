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
 * <p>Classe Java per OnionRoutingTechnologyParameter complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="OnionRoutingTechnologyParameter"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}AnonymityTechnologyParameter"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="exitRelay" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="IPv6Exit" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="publicRelay" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="hiddenServiceDir" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="hiddenServicePort" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="relayBandwidthRate" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="relayBandwidthBurst" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OnionRoutingTechnologyParameter", propOrder = {
    "exitRelay",
    "iPv6Exit",
    "publicRelay",
    "hiddenServiceDir",
    "hiddenServicePort",
    "relayBandwidthRate",
    "relayBandwidthBurst"
})
public class OnionRoutingTechnologyParameter
    extends AnonymityTechnologyParameter
{

    protected boolean exitRelay;
    @XmlElement(name = "IPv6Exit")
    protected boolean iPv6Exit;
    protected boolean publicRelay;
    @XmlElement(required = true)
    protected String hiddenServiceDir;
    @XmlElement(required = true)
    protected String hiddenServicePort;
    @XmlElement(required = true)
    protected BigInteger relayBandwidthRate;
    @XmlElement(required = true)
    protected BigInteger relayBandwidthBurst;

    /**
     * Recupera il valore della proprietà exitRelay.
     * 
     */
    public boolean isExitRelay() {
        return exitRelay;
    }

    /**
     * Imposta il valore della proprietà exitRelay.
     * 
     */
    public void setExitRelay(boolean value) {
        this.exitRelay = value;
    }

    /**
     * Recupera il valore della proprietà iPv6Exit.
     * 
     */
    public boolean isIPv6Exit() {
        return iPv6Exit;
    }

    /**
     * Imposta il valore della proprietà iPv6Exit.
     * 
     */
    public void setIPv6Exit(boolean value) {
        this.iPv6Exit = value;
    }

    /**
     * Recupera il valore della proprietà publicRelay.
     * 
     */
    public boolean isPublicRelay() {
        return publicRelay;
    }

    /**
     * Imposta il valore della proprietà publicRelay.
     * 
     */
    public void setPublicRelay(boolean value) {
        this.publicRelay = value;
    }

    /**
     * Recupera il valore della proprietà hiddenServiceDir.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHiddenServiceDir() {
        return hiddenServiceDir;
    }

    /**
     * Imposta il valore della proprietà hiddenServiceDir.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHiddenServiceDir(String value) {
        this.hiddenServiceDir = value;
    }

    /**
     * Recupera il valore della proprietà hiddenServicePort.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHiddenServicePort() {
        return hiddenServicePort;
    }

    /**
     * Imposta il valore della proprietà hiddenServicePort.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHiddenServicePort(String value) {
        this.hiddenServicePort = value;
    }

    /**
     * Recupera il valore della proprietà relayBandwidthRate.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRelayBandwidthRate() {
        return relayBandwidthRate;
    }

    /**
     * Imposta il valore della proprietà relayBandwidthRate.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRelayBandwidthRate(BigInteger value) {
        this.relayBandwidthRate = value;
    }

    /**
     * Recupera il valore della proprietà relayBandwidthBurst.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRelayBandwidthBurst() {
        return relayBandwidthBurst;
    }

    /**
     * Imposta il valore della proprietà relayBandwidthBurst.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRelayBandwidthBurst(BigInteger value) {
        this.relayBandwidthBurst = value;
    }

}
