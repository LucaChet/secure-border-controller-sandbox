//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per FiveGSecuritySliceAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="FiveGSecuritySliceAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fiveGSecuritySliceActionType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FiveGSecuritySliceActionType"/&gt;
 *         &lt;element name="securedService" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}SecuredService" maxOccurs="unbounded"/&gt;
 *         &lt;element name="nest" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}NEST" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FiveGSecuritySliceAction", propOrder = {
    "fiveGSecuritySliceActionType",
    "securedService",
    "nest"
})
public class FiveGSecuritySliceAction
    extends ConfigurationAction
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected FiveGSecuritySliceActionType fiveGSecuritySliceActionType;
    @XmlElement(required = true)
    protected List<SecuredService> securedService;
    protected NEST nest;

    /**
     * Recupera il valore della proprietà fiveGSecuritySliceActionType.
     * 
     * @return
     *     possible object is
     *     {@link FiveGSecuritySliceActionType }
     *     
     */
    public FiveGSecuritySliceActionType getFiveGSecuritySliceActionType() {
        return fiveGSecuritySliceActionType;
    }

    /**
     * Imposta il valore della proprietà fiveGSecuritySliceActionType.
     * 
     * @param value
     *     allowed object is
     *     {@link FiveGSecuritySliceActionType }
     *     
     */
    public void setFiveGSecuritySliceActionType(FiveGSecuritySliceActionType value) {
        this.fiveGSecuritySliceActionType = value;
    }

    /**
     * Gets the value of the securedService property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the securedService property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecuredService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SecuredService }
     * 
     * 
     */
    public List<SecuredService> getSecuredService() {
        if (securedService == null) {
            securedService = new ArrayList<SecuredService>();
        }
        return this.securedService;
    }

    /**
     * Recupera il valore della proprietà nest.
     * 
     * @return
     *     possible object is
     *     {@link NEST }
     *     
     */
    public NEST getNest() {
        return nest;
    }

    /**
     * Imposta il valore della proprietà nest.
     * 
     * @param value
     *     allowed object is
     *     {@link NEST }
     *     
     */
    public void setNest(NEST value) {
        this.nest = value;
    }

}
