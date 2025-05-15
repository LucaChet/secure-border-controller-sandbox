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
 * <p>Classe Java per KeyExchangeParameter complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="KeyExchangeParameter"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="keyExchangeAction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="hashAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="symmetricEncryptionAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="authenticationType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KeyExchangeParameter", propOrder = {
    "keyExchangeAction",
    "hashAlgorithm",
    "symmetricEncryptionAlgorithm",
    "authenticationType"
})
public class KeyExchangeParameter {

    protected String keyExchangeAction;
    protected String hashAlgorithm;
    protected String symmetricEncryptionAlgorithm;
    protected String authenticationType;

    /**
     * Recupera il valore della proprietà keyExchangeAction.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyExchangeAction() {
        return keyExchangeAction;
    }

    /**
     * Imposta il valore della proprietà keyExchangeAction.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyExchangeAction(String value) {
        this.keyExchangeAction = value;
    }

    /**
     * Recupera il valore della proprietà hashAlgorithm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    /**
     * Imposta il valore della proprietà hashAlgorithm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashAlgorithm(String value) {
        this.hashAlgorithm = value;
    }

    /**
     * Recupera il valore della proprietà symmetricEncryptionAlgorithm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSymmetricEncryptionAlgorithm() {
        return symmetricEncryptionAlgorithm;
    }

    /**
     * Imposta il valore della proprietà symmetricEncryptionAlgorithm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSymmetricEncryptionAlgorithm(String value) {
        this.symmetricEncryptionAlgorithm = value;
    }

    /**
     * Recupera il valore della proprietà authenticationType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationType() {
        return authenticationType;
    }

    /**
     * Imposta il valore della proprietà authenticationType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationType(String value) {
        this.authenticationType = value;
    }

}
