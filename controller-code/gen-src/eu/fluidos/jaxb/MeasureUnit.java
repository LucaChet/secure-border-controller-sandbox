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
 * <p>Classe Java per MeasureUnit.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <pre>
 * &lt;simpleType name="MeasureUnit"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MESSAGE"/&gt;
 *     &lt;enumeration value="DATAGRAM"/&gt;
 *     &lt;enumeration value="CONNECTION"/&gt;
 *     &lt;enumeration value="CONNECTION_DURATION"/&gt;
 *     &lt;enumeration value="BYTE"/&gt;
 *     &lt;enumeration value="KBYTE"/&gt;
 *     &lt;enumeration value="MBYTE"/&gt;
 *     &lt;enumeration value="GBYTE"/&gt;
 *     &lt;enumeration value="TBYTE"/&gt;
 *     &lt;enumeration value="PBYTE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MeasureUnit")
@XmlEnum
public enum MeasureUnit {

    MESSAGE,
    DATAGRAM,
    CONNECTION,
    CONNECTION_DURATION,
    BYTE,
    KBYTE,
    MBYTE,
    GBYTE,
    TBYTE,
    PBYTE;

    public String value() {
        return name();
    }

    public static MeasureUnit fromValue(String v) {
        return valueOf(v);
    }

}
