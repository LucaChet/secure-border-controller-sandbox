//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RSAC complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RSAC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="RSACviolence"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *             &lt;minInclusive value="0"/&gt;
 *             &lt;maxInclusive value="4"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="RSACsex"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *             &lt;minInclusive value="0"/&gt;
 *             &lt;maxInclusive value="4"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="RSACnudity"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *             &lt;minInclusive value="0"/&gt;
 *             &lt;maxInclusive value="4"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="RSAClanguage"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *             &lt;minInclusive value="0"/&gt;
 *             &lt;maxInclusive value="4"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RSAC")
public class RSAC {

    @XmlAttribute(name = "RSACviolence")
    protected Integer rsaCviolence;
    @XmlAttribute(name = "RSACsex")
    protected Integer rsaCsex;
    @XmlAttribute(name = "RSACnudity")
    protected Integer rsaCnudity;
    @XmlAttribute(name = "RSAClanguage")
    protected Integer rsaClanguage;

    /**
     * Recupera il valore della proprietà rsaCviolence.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRSACviolence() {
        return rsaCviolence;
    }

    /**
     * Imposta il valore della proprietà rsaCviolence.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRSACviolence(Integer value) {
        this.rsaCviolence = value;
    }

    /**
     * Recupera il valore della proprietà rsaCsex.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRSACsex() {
        return rsaCsex;
    }

    /**
     * Imposta il valore della proprietà rsaCsex.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRSACsex(Integer value) {
        this.rsaCsex = value;
    }

    /**
     * Recupera il valore della proprietà rsaCnudity.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRSACnudity() {
        return rsaCnudity;
    }

    /**
     * Imposta il valore della proprietà rsaCnudity.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRSACnudity(Integer value) {
        this.rsaCnudity = value;
    }

    /**
     * Recupera il valore della proprietà rsaClanguage.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRSAClanguage() {
        return rsaClanguage;
    }

    /**
     * Imposta il valore della proprietà rsaClanguage.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRSAClanguage(Integer value) {
        this.rsaClanguage = value;
    }

}
