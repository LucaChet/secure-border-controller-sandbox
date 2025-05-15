//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DataProtectionCapability complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DataProtectionCapability"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}Capability"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="supportsDataAuthenticationAndIntegrity" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="supportsDigitalSignature" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="supportsEncryption" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="supportsKeyExchange" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataProtectionCapability", propOrder = {
    "supportsDataAuthenticationAndIntegrity",
    "supportsDigitalSignature",
    "supportsEncryption",
    "supportsKeyExchange"
})
@XmlSeeAlso({
    EncryptionCapability.class
})
public class DataProtectionCapability
    extends Capability
{

    protected boolean supportsDataAuthenticationAndIntegrity;
    protected boolean supportsDigitalSignature;
    protected boolean supportsEncryption;
    protected boolean supportsKeyExchange;

    /**
     * Recupera il valore della proprietà supportsDataAuthenticationAndIntegrity.
     * 
     */
    public boolean isSupportsDataAuthenticationAndIntegrity() {
        return supportsDataAuthenticationAndIntegrity;
    }

    /**
     * Imposta il valore della proprietà supportsDataAuthenticationAndIntegrity.
     * 
     */
    public void setSupportsDataAuthenticationAndIntegrity(boolean value) {
        this.supportsDataAuthenticationAndIntegrity = value;
    }

    /**
     * Recupera il valore della proprietà supportsDigitalSignature.
     * 
     */
    public boolean isSupportsDigitalSignature() {
        return supportsDigitalSignature;
    }

    /**
     * Imposta il valore della proprietà supportsDigitalSignature.
     * 
     */
    public void setSupportsDigitalSignature(boolean value) {
        this.supportsDigitalSignature = value;
    }

    /**
     * Recupera il valore della proprietà supportsEncryption.
     * 
     */
    public boolean isSupportsEncryption() {
        return supportsEncryption;
    }

    /**
     * Imposta il valore della proprietà supportsEncryption.
     * 
     */
    public void setSupportsEncryption(boolean value) {
        this.supportsEncryption = value;
    }

    /**
     * Recupera il valore della proprietà supportsKeyExchange.
     * 
     */
    public boolean isSupportsKeyExchange() {
        return supportsKeyExchange;
    }

    /**
     * Imposta il valore della proprietà supportsKeyExchange.
     * 
     */
    public void setSupportsKeyExchange(boolean value) {
        this.supportsKeyExchange = value;
    }

}
