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
 * <p>Classe Java per ProtocolType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <pre>
 * &lt;simpleType name="ProtocolType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TCP"/&gt;
 *     &lt;enumeration value="UDP"/&gt;
 *     &lt;enumeration value="SCTP"/&gt;
 *     &lt;enumeration value="ALL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ProtocolType")
@XmlEnum
public enum ProtocolType {

    TCP,
    UDP,
    SCTP,
    ALL;

    public String value() {
        return name();
    }

    public static ProtocolType fromValue(String v) {
        return valueOf(v);
    }

}
