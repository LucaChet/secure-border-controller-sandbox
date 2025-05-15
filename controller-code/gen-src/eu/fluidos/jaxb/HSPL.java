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
 * <p>Classe Java per HSPL complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="HSPL"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="HSPL_id" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="HSPL_text" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HSPL")
public class HSPL {

    @XmlAttribute(name = "HSPL_id")
    protected String hsplId;
    @XmlAttribute(name = "HSPL_text")
    protected String hsplText;

    /**
     * Recupera il valore della proprietà hsplId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHSPLId() {
        return hsplId;
    }

    /**
     * Imposta il valore della proprietà hsplId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHSPLId(String value) {
        this.hsplId = value;
    }

    /**
     * Recupera il valore della proprietà hsplText.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHSPLText() {
        return hsplText;
    }

    /**
     * Imposta il valore della proprietà hsplText.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHSPLText(String value) {
        this.hsplText = value;
    }

}
