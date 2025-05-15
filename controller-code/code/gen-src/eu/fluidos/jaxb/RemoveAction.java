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
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RemoveAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RemoveAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RemoveActionType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}RemoveActionType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoveAction", propOrder = {
    "removeActionType"
})
@XmlSeeAlso({
    RemoveAdvertisementAction.class,
    RemoveTrackingTechniquesAction.class
})
public class RemoveAction
    extends ConfigurationAction
{

    @XmlElement(name = "RemoveActionType", required = true)
    protected RemoveActionType removeActionType;

    /**
     * Recupera il valore della proprietà removeActionType.
     * 
     * @return
     *     possible object is
     *     {@link RemoveActionType }
     *     
     */
    public RemoveActionType getRemoveActionType() {
        return removeActionType;
    }

    /**
     * Imposta il valore della proprietà removeActionType.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoveActionType }
     *     
     */
    public void setRemoveActionType(RemoveActionType value) {
        this.removeActionType = value;
    }

}
