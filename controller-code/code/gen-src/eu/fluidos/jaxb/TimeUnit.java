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
 * <p>Classe Java per TimeUnit.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <pre>
 * &lt;simpleType name="TimeUnit"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SECOND"/&gt;
 *     &lt;enumeration value="MINUTE"/&gt;
 *     &lt;enumeration value="HOUR"/&gt;
 *     &lt;enumeration value="DAY"/&gt;
 *     &lt;enumeration value="WEEK"/&gt;
 *     &lt;enumeration value="MONTH"/&gt;
 *     &lt;enumeration value="YEAR"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TimeUnit")
@XmlEnum
public enum TimeUnit {

    SECOND,
    MINUTE,
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public String value() {
        return name();
    }

    public static TimeUnit fromValue(String v) {
        return valueOf(v);
    }

}
