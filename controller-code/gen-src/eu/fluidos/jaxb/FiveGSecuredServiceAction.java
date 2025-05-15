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
 * <p>Classe Java per FiveGSecuredServiceAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="FiveGSecuredServiceAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fiveGSecuredServiceActionType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FiveGSecuritySliceActionType"/&gt;
 *         &lt;element name="securedService" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}SecuredService"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FiveGSecuredServiceAction", propOrder = {
    "fiveGSecuredServiceActionType",
    "securedService"
})
public class FiveGSecuredServiceAction
    extends ConfigurationAction
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected FiveGSecuritySliceActionType fiveGSecuredServiceActionType;
    @XmlElement(required = true)
    protected SecuredService securedService;

    /**
     * Recupera il valore della proprietà fiveGSecuredServiceActionType.
     * 
     * @return
     *     possible object is
     *     {@link FiveGSecuritySliceActionType }
     *     
     */
    public FiveGSecuritySliceActionType getFiveGSecuredServiceActionType() {
        return fiveGSecuredServiceActionType;
    }

    /**
     * Imposta il valore della proprietà fiveGSecuredServiceActionType.
     * 
     * @param value
     *     allowed object is
     *     {@link FiveGSecuritySliceActionType }
     *     
     */
    public void setFiveGSecuredServiceActionType(FiveGSecuritySliceActionType value) {
        this.fiveGSecuredServiceActionType = value;
    }

    /**
     * Recupera il valore della proprietà securedService.
     * 
     * @return
     *     possible object is
     *     {@link SecuredService }
     *     
     */
    public SecuredService getSecuredService() {
        return securedService;
    }

    /**
     * Imposta il valore della proprietà securedService.
     * 
     * @param value
     *     allowed object is
     *     {@link SecuredService }
     *     
     */
    public void setSecuredService(SecuredService value) {
        this.securedService = value;
    }

}
