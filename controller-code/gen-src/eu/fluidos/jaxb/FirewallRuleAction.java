//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per FirewallRuleAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="FirewallRuleAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="firewallActionType" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FirewallActionType" minOccurs="0"/&gt;
 *         &lt;element name="ruleParameters" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}FirewallConfigurationCondition" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirewallRuleAction", propOrder = {
    "firewallActionType",
    "ruleParameters"
})
public class FirewallRuleAction
    extends ConfigurationAction
{

    @XmlSchemaType(name = "string")
    protected FirewallActionType firewallActionType;
    protected FirewallConfigurationCondition ruleParameters;

    /**
     * Recupera il valore della proprietà firewallActionType.
     * 
     * @return
     *     possible object is
     *     {@link FirewallActionType }
     *     
     */
    public FirewallActionType getFirewallActionType() {
        return firewallActionType;
    }

    /**
     * Imposta il valore della proprietà firewallActionType.
     * 
     * @param value
     *     allowed object is
     *     {@link FirewallActionType }
     *     
     */
    public void setFirewallActionType(FirewallActionType value) {
        this.firewallActionType = value;
    }

    /**
     * Recupera il valore della proprietà ruleParameters.
     * 
     * @return
     *     possible object is
     *     {@link FirewallConfigurationCondition }
     *     
     */
    public FirewallConfigurationCondition getRuleParameters() {
        return ruleParameters;
    }

    /**
     * Imposta il valore della proprietà ruleParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link FirewallConfigurationCondition }
     *     
     */
    public void setRuleParameters(FirewallConfigurationCondition value) {
        this.ruleParameters = value;
    }

}
