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
 * <p>Classe Java per AggregatorParameters complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="AggregatorParameters"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationCondition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rounds" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="fusionAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="apiPort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="minClients" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AggregatorParameters", propOrder = {
    "url",
    "rounds",
    "fusionAlgorithm",
    "apiPort",
    "minClients"
})
public class AggregatorParameters
    extends ConfigurationCondition
{

    @XmlElement(name = "URL")
    protected String url;
    protected BigInteger rounds;
    protected String fusionAlgorithm;
    protected String apiPort;
    protected String minClients;

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
     * Recupera il valore della proprietà rounds.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRounds() {
        return rounds;
    }

    /**
     * Imposta il valore della proprietà rounds.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRounds(BigInteger value) {
        this.rounds = value;
    }

    /**
     * Recupera il valore della proprietà fusionAlgorithm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFusionAlgorithm() {
        return fusionAlgorithm;
    }

    /**
     * Imposta il valore della proprietà fusionAlgorithm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFusionAlgorithm(String value) {
        this.fusionAlgorithm = value;
    }

    /**
     * Recupera il valore della proprietà apiPort.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApiPort() {
        return apiPort;
    }

    /**
     * Imposta il valore della proprietà apiPort.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApiPort(String value) {
        this.apiPort = value;
    }

    /**
     * Recupera il valore della proprietà minClients.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinClients() {
        return minClients;
    }

    /**
     * Imposta il valore della proprietà minClients.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinClients(String value) {
        this.minClients = value;
    }

}
