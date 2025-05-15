//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ParentalControlAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ParentalControlAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}EnableAction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pics" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}Pics" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParentalControlAction", propOrder = {
    "pics"
})
public class ParentalControlAction
    extends EnableAction
{

    protected Pics pics;

    /**
     * Recupera il valore della proprietà pics.
     * 
     * @return
     *     possible object is
     *     {@link Pics }
     *     
     */
    public Pics getPics() {
        return pics;
    }

    /**
     * Imposta il valore della proprietà pics.
     * 
     * @param value
     *     allowed object is
     *     {@link Pics }
     *     
     */
    public void setPics(Pics value) {
        this.pics = value;
    }

}
