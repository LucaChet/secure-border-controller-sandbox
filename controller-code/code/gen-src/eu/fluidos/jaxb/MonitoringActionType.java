//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per MonitoringActionType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <pre>
 * &lt;simpleType name="MonitoringActionType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ALERT"/&gt;
 *     &lt;enumeration value="SECURITY_ANALYSIS"/&gt;
 *     &lt;enumeration value="ENABLE_SESS_STATS"/&gt;
 *     &lt;enumeration value="ENABLE_NO_SESS_STATS"/&gt;
 *     &lt;enumeration value="BEHAVIORAL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MonitoringActionType")
@XmlEnum
public enum MonitoringActionType {

    ALERT,
    SECURITY_ANALYSIS,
    ENABLE_SESS_STATS,
    ENABLE_NO_SESS_STATS,
    BEHAVIORAL;

    public String value() {
        return name();
    }

    public static MonitoringActionType fromValue(String v) {
        return valueOf(v);
    }

}
