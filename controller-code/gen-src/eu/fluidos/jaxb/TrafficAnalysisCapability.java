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
 * <p>Classe Java per TrafficAnalysisCapability complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="TrafficAnalysisCapability"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}Capability"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="supportOnlineTraficAnalysis" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="supportOfflineTraficAnalysis" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrafficAnalysisCapability", propOrder = {
    "supportOnlineTraficAnalysis",
    "supportOfflineTraficAnalysis"
})
@XmlSeeAlso({
    IDSCapability.class,
    IPSCapability.class,
    MalwaresAnalysisCapability.class,
    LawfulInterceptionCapability.class
})
public class TrafficAnalysisCapability
    extends Capability
{

    protected boolean supportOnlineTraficAnalysis;
    protected boolean supportOfflineTraficAnalysis;

    /**
     * Recupera il valore della proprietà supportOnlineTraficAnalysis.
     * 
     */
    public boolean isSupportOnlineTraficAnalysis() {
        return supportOnlineTraficAnalysis;
    }

    /**
     * Imposta il valore della proprietà supportOnlineTraficAnalysis.
     * 
     */
    public void setSupportOnlineTraficAnalysis(boolean value) {
        this.supportOnlineTraficAnalysis = value;
    }

    /**
     * Recupera il valore della proprietà supportOfflineTraficAnalysis.
     * 
     */
    public boolean isSupportOfflineTraficAnalysis() {
        return supportOfflineTraficAnalysis;
    }

    /**
     * Imposta il valore della proprietà supportOfflineTraficAnalysis.
     * 
     */
    public void setSupportOfflineTraficAnalysis(boolean value) {
        this.supportOfflineTraficAnalysis = value;
    }

}
