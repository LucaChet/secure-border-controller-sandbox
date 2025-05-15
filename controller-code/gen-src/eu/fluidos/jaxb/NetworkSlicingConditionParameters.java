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
 * <p>Classe Java per NetworkSlicingConditionParameters complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="NetworkSlicingConditionParameters"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationCondition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SliceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SliceName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="point" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}Point" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NetworkSlicingConditionParameters", propOrder = {
    "sliceID",
    "sliceName",
    "point"
})
public class NetworkSlicingConditionParameters
    extends ConfigurationCondition
{

    @XmlElement(name = "SliceID")
    protected String sliceID;
    @XmlElement(name = "SliceName", required = true)
    protected String sliceName;
    protected Point point;

    /**
     * Recupera il valore della proprietà sliceID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSliceID() {
        return sliceID;
    }

    /**
     * Imposta il valore della proprietà sliceID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSliceID(String value) {
        this.sliceID = value;
    }

    /**
     * Recupera il valore della proprietà sliceName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSliceName() {
        return sliceName;
    }

    /**
     * Imposta il valore della proprietà sliceName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSliceName(String value) {
        this.sliceName = value;
    }

    /**
     * Recupera il valore della proprietà point.
     * 
     * @return
     *     possible object is
     *     {@link Point }
     *     
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Imposta il valore della proprietà point.
     * 
     * @param value
     *     allowed object is
     *     {@link Point }
     *     
     */
    public void setPoint(Point value) {
        this.point = value;
    }

}
