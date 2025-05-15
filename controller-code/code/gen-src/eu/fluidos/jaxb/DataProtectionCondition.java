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
 * <p>Classe Java per DataProtectionCondition complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DataProtectionCondition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationCondition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="packetFilterCondition" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}PacketFilterCondition" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataProtectionCondition", propOrder = {
    "packetFilterCondition"
})
@XmlSeeAlso({
    InterfaceSelectionCondition.class,
    DataSelectionCondition.class
})
public class DataProtectionCondition
    extends ConfigurationCondition
{

    protected PacketFilterCondition packetFilterCondition;

    /**
     * Recupera il valore della proprietà packetFilterCondition.
     * 
     * @return
     *     possible object is
     *     {@link PacketFilterCondition }
     *     
     */
    public PacketFilterCondition getPacketFilterCondition() {
        return packetFilterCondition;
    }

    /**
     * Imposta il valore della proprietà packetFilterCondition.
     * 
     * @param value
     *     allowed object is
     *     {@link PacketFilterCondition }
     *     
     */
    public void setPacketFilterCondition(PacketFilterCondition value) {
        this.packetFilterCondition = value;
    }

}
