//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Authentication complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Authentication"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}TechnologyActionSecurityProperty"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="serverAuthenticationMechanism" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="clientAuthenticationMechanism" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="peerAuthenticationMechanism" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Authentication", propOrder = {
    "serverAuthenticationMechanism",
    "clientAuthenticationMechanism",
    "peerAuthenticationMechanism"
})
public class Authentication
    extends TechnologyActionSecurityProperty
{

    protected String serverAuthenticationMechanism;
    protected String clientAuthenticationMechanism;
    protected String peerAuthenticationMechanism;

    /**
     * Recupera il valore della proprietà serverAuthenticationMechanism.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerAuthenticationMechanism() {
        return serverAuthenticationMechanism;
    }

    /**
     * Imposta il valore della proprietà serverAuthenticationMechanism.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerAuthenticationMechanism(String value) {
        this.serverAuthenticationMechanism = value;
    }

    /**
     * Recupera il valore della proprietà clientAuthenticationMechanism.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientAuthenticationMechanism() {
        return clientAuthenticationMechanism;
    }

    /**
     * Imposta il valore della proprietà clientAuthenticationMechanism.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientAuthenticationMechanism(String value) {
        this.clientAuthenticationMechanism = value;
    }

    /**
     * Recupera il valore della proprietà peerAuthenticationMechanism.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeerAuthenticationMechanism() {
        return peerAuthenticationMechanism;
    }

    /**
     * Imposta il valore della proprietà peerAuthenticationMechanism.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeerAuthenticationMechanism(String value) {
        this.peerAuthenticationMechanism = value;
    }

}
