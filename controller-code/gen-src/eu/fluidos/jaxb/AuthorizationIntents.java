//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per AuthorizationIntents complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="AuthorizationIntents"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}Configuration"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="forbiddenConnectionList" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationRule" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="mandatoryConnectionList" type="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}ConfigurationRule" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthorizationIntents", propOrder = {
    "forbiddenConnectionList",
    "mandatoryConnectionList"
})
public class AuthorizationIntents
    extends Configuration
{

    protected List<ConfigurationRule> forbiddenConnectionList;
    protected List<ConfigurationRule> mandatoryConnectionList;

    /**
     * Gets the value of the forbiddenConnectionList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the forbiddenConnectionList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getForbiddenConnectionList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationRule }
     * 
     * 
     */
    public List<ConfigurationRule> getForbiddenConnectionList() {
        if (forbiddenConnectionList == null) {
            forbiddenConnectionList = new ArrayList<ConfigurationRule>();
        }
        return this.forbiddenConnectionList;
    }

    /**
     * Gets the value of the mandatoryConnectionList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the mandatoryConnectionList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMandatoryConnectionList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationRule }
     * 
     * 
     */
    public List<ConfigurationRule> getMandatoryConnectionList() {
        if (mandatoryConnectionList == null) {
            mandatoryConnectionList = new ArrayList<ConfigurationRule>();
        }
        return this.mandatoryConnectionList;
    }

}
