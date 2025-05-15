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
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per TLS_SSL_TechnologyParameter complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="TLS_SSL_TechnologyParameter"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}TechnologySpecificParameters"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ciphers-client" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ssl-version-client" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ciphers-server" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ssl-version-server" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TLS_SSL_TechnologyParameter", propOrder = {
    "ciphersClient",
    "sslVersionClient",
    "ciphersServer",
    "sslVersionServer"
})
public class TLSSSLTechnologyParameter
    extends TechnologySpecificParameters
{

    @XmlElement(name = "ciphers-client", required = true)
    protected String ciphersClient;
    @XmlElement(name = "ssl-version-client", required = true)
    protected String sslVersionClient;
    @XmlElement(name = "ciphers-server", required = true)
    protected String ciphersServer;
    @XmlElement(name = "ssl-version-server", required = true)
    protected String sslVersionServer;

    /**
     * Recupera il valore della proprietà ciphersClient.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCiphersClient() {
        return ciphersClient;
    }

    /**
     * Imposta il valore della proprietà ciphersClient.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCiphersClient(String value) {
        this.ciphersClient = value;
    }

    /**
     * Recupera il valore della proprietà sslVersionClient.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSslVersionClient() {
        return sslVersionClient;
    }

    /**
     * Imposta il valore della proprietà sslVersionClient.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSslVersionClient(String value) {
        this.sslVersionClient = value;
    }

    /**
     * Recupera il valore della proprietà ciphersServer.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCiphersServer() {
        return ciphersServer;
    }

    /**
     * Imposta il valore della proprietà ciphersServer.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCiphersServer(String value) {
        this.ciphersServer = value;
    }

    /**
     * Recupera il valore della proprietà sslVersionServer.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSslVersionServer() {
        return sslVersionServer;
    }

    /**
     * Imposta il valore della proprietà sslVersionServer.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSslVersionServer(String value) {
        this.sslVersionServer = value;
    }

}
