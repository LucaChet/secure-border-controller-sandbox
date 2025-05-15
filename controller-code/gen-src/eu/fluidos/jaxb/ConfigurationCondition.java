//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ConfigurationCondition complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ConfigurationCondition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isCNF" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConfigurationCondition", propOrder = {
    "isCNF"
})
@XmlSeeAlso({
    PolicyDependencyCondition.class,
    FiveGSecuritySliceCondition.class,
    PurposeCondition.class,
    DataAgreggationConfigurationConditions.class,
    MonitoringConfigurationConditions.class,
    NetworkSlicingCondition.class,
    NetworkSlicingConditionParameters.class,
    Point.class,
    ParametersFirewallConfigurationCondition.class,
    RuleParameters.class,
    FilterFLConfigurationCondition.class,
    AggregatorParameters.class,
    LocalConnectionParameters.class,
    LearningParameters.class,
    ObfuscationInfo.class,
    Parameters.class,
    LearningModelInfo.class,
    ModelParameters.class,
    AdditionalParameters.class,
    IntegrationFabricParameters.class,
    Topics.class,
    Topic.class,
    QoSCondition.class,
    AntiMalwareCondition.class,
    LoggingCondition.class,
    DataProtectionCondition.class,
    FilteringConfigurationCondition.class,
    KubernetesNetworkFilteringCondition.class
})
public class ConfigurationCondition {

    protected boolean isCNF;

    /**
     * Recupera il valore della proprietà isCNF.
     * 
     */
    public boolean isIsCNF() {
        return isCNF;
    }

    /**
     * Imposta il valore della proprietà isCNF.
     * 
     */
    public void setIsCNF(boolean value) {
        this.isCNF = value;
    }

}
