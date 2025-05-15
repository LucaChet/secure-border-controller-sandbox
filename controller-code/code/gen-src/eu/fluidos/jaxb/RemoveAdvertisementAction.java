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
 * <p>Classe Java per RemoveAdvertisementAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RemoveAdvertisementAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}RemoveAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RemoveAdvertisementActionType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}RemoveAdvertisementActionType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoveAdvertisementAction", propOrder = {
    "removeAdvertisementActionType"
})
public class RemoveAdvertisementAction
    extends RemoveAction
{

    @XmlElement(name = "RemoveAdvertisementActionType", required = true)
    protected RemoveAdvertisementActionType removeAdvertisementActionType;

    /**
     * Recupera il valore della proprietà removeAdvertisementActionType.
     * 
     * @return
     *     possible object is
     *     {@link RemoveAdvertisementActionType }
     *     
     */
    public RemoveAdvertisementActionType getRemoveAdvertisementActionType() {
        return removeAdvertisementActionType;
    }

    /**
     * Imposta il valore della proprietà removeAdvertisementActionType.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoveAdvertisementActionType }
     *     
     */
    public void setRemoveAdvertisementActionType(RemoveAdvertisementActionType value) {
        this.removeAdvertisementActionType = value;
    }

}
