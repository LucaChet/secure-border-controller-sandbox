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
 * <p>Classe Java per CheckAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="CheckAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CheckActionType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}CheckActionType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CheckAction", propOrder = {
    "checkActionType"
})
public class CheckAction
    extends ConfigurationAction
{

    @XmlElement(name = "CheckActionType", required = true)
    protected CheckActionType checkActionType;

    /**
     * Recupera il valore della proprietà checkActionType.
     * 
     * @return
     *     possible object is
     *     {@link CheckActionType }
     *     
     */
    public CheckActionType getCheckActionType() {
        return checkActionType;
    }

    /**
     * Imposta il valore della proprietà checkActionType.
     * 
     * @param value
     *     allowed object is
     *     {@link CheckActionType }
     *     
     */
    public void setCheckActionType(CheckActionType value) {
        this.checkActionType = value;
    }

}
