//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per LevelType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <pre>
 * &lt;simpleType name="LevelType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="child"/&gt;
 *     &lt;enumeration value="adolescent"/&gt;
 *     &lt;enumeration value="pgr"/&gt;
 *     &lt;enumeration value="universal"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LevelType")
@XmlEnum
public enum LevelType {

    @XmlEnumValue("child")
    CHILD("child"),
    @XmlEnumValue("adolescent")
    ADOLESCENT("adolescent"),
    @XmlEnumValue("pgr")
    PGR("pgr"),
    @XmlEnumValue("universal")
    UNIVERSAL("universal");
    private final String value;

    LevelType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LevelType fromValue(String v) {
        for (LevelType c: LevelType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
