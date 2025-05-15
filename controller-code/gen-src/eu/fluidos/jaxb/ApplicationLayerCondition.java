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
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ApplicationLayerCondition complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ApplicationLayerCondition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="applicationProtocol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="httpCondition" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}HTTPCondition" minOccurs="0"/&gt;
 *         &lt;element name="fileExtension" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mimeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="maxconn" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="dst_domain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="src_domain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="URL_regex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationLayerCondition", propOrder = {
    "applicationProtocol",
    "url",
    "httpCondition",
    "fileExtension",
    "mimeType",
    "maxconn",
    "dstDomain",
    "srcDomain",
    "urlRegex"
})
@XmlSeeAlso({
    IoTApplicationLayerCondition.class
})
public class ApplicationLayerCondition {

    protected String applicationProtocol;
    @XmlElement(name = "URL")
    protected String url;
    protected HTTPCondition httpCondition;
    protected String fileExtension;
    protected String mimeType;
    protected Integer maxconn;
    @XmlElement(name = "dst_domain")
    protected String dstDomain;
    @XmlElement(name = "src_domain")
    protected String srcDomain;
    @XmlElement(name = "URL_regex")
    protected String urlRegex;

    /**
     * Recupera il valore della proprietà applicationProtocol.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationProtocol() {
        return applicationProtocol;
    }

    /**
     * Imposta il valore della proprietà applicationProtocol.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationProtocol(String value) {
        this.applicationProtocol = value;
    }

    /**
     * Recupera il valore della proprietà url.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURL() {
        return url;
    }

    /**
     * Imposta il valore della proprietà url.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURL(String value) {
        this.url = value;
    }

    /**
     * Recupera il valore della proprietà httpCondition.
     * 
     * @return
     *     possible object is
     *     {@link HTTPCondition }
     *     
     */
    public HTTPCondition getHttpCondition() {
        return httpCondition;
    }

    /**
     * Imposta il valore della proprietà httpCondition.
     * 
     * @param value
     *     allowed object is
     *     {@link HTTPCondition }
     *     
     */
    public void setHttpCondition(HTTPCondition value) {
        this.httpCondition = value;
    }

    /**
     * Recupera il valore della proprietà fileExtension.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * Imposta il valore della proprietà fileExtension.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileExtension(String value) {
        this.fileExtension = value;
    }

    /**
     * Recupera il valore della proprietà mimeType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Imposta il valore della proprietà mimeType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }

    /**
     * Recupera il valore della proprietà maxconn.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxconn() {
        return maxconn;
    }

    /**
     * Imposta il valore della proprietà maxconn.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxconn(Integer value) {
        this.maxconn = value;
    }

    /**
     * Recupera il valore della proprietà dstDomain.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDstDomain() {
        return dstDomain;
    }

    /**
     * Imposta il valore della proprietà dstDomain.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDstDomain(String value) {
        this.dstDomain = value;
    }

    /**
     * Recupera il valore della proprietà srcDomain.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrcDomain() {
        return srcDomain;
    }

    /**
     * Imposta il valore della proprietà srcDomain.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrcDomain(String value) {
        this.srcDomain = value;
    }

    /**
     * Recupera il valore della proprietà urlRegex.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURLRegex() {
        return urlRegex;
    }

    /**
     * Imposta il valore della proprietà urlRegex.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURLRegex(String value) {
        this.urlRegex = value;
    }

}
