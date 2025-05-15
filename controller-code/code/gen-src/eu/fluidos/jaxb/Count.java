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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Count complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Count"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="measureUnit" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}MeasureUnit"/&gt;
 *         &lt;element name="timeUnit" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}TimeUnit" minOccurs="0"/&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="per" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}TimeUnit" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Count", propOrder = {
    "measureUnit",
    "timeUnit",
    "value",
    "per"
})
public class Count {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected MeasureUnit measureUnit;
    @XmlSchemaType(name = "string")
    protected TimeUnit timeUnit;
    @XmlElement(required = true)
    protected String value;
    @XmlSchemaType(name = "string")
    protected TimeUnit per;

    /**
     * Recupera il valore della proprietà measureUnit.
     * 
     * @return
     *     possible object is
     *     {@link MeasureUnit }
     *     
     */
    public MeasureUnit getMeasureUnit() {
        return measureUnit;
    }

    /**
     * Imposta il valore della proprietà measureUnit.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasureUnit }
     *     
     */
    public void setMeasureUnit(MeasureUnit value) {
        this.measureUnit = value;
    }

    /**
     * Recupera il valore della proprietà timeUnit.
     * 
     * @return
     *     possible object is
     *     {@link TimeUnit }
     *     
     */
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * Imposta il valore della proprietà timeUnit.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeUnit }
     *     
     */
    public void setTimeUnit(TimeUnit value) {
        this.timeUnit = value;
    }

    /**
     * Recupera il valore della proprietà value.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Imposta il valore della proprietà value.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Recupera il valore della proprietà per.
     * 
     * @return
     *     possible object is
     *     {@link TimeUnit }
     *     
     */
    public TimeUnit getPer() {
        return per;
    }

    /**
     * Imposta il valore della proprietà per.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeUnit }
     *     
     */
    public void setPer(TimeUnit value) {
        this.per = value;
    }

}
