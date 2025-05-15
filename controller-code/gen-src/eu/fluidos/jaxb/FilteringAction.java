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
 * <p>Classe Java per FilteringAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="FilteringAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FilteringActionType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FilteringAction", propOrder = {
    "filteringActionType"
})
public class FilteringAction
    extends ConfigurationAction
{

    @XmlElement(name = "FilteringActionType", required = true)
    protected String filteringActionType;

    /**
     * Recupera il valore della proprietà filteringActionType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilteringActionType() {
        return filteringActionType;
    }

    /**
     * Imposta il valore della proprietà filteringActionType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilteringActionType(String value) {
        this.filteringActionType = value;
    }

}
