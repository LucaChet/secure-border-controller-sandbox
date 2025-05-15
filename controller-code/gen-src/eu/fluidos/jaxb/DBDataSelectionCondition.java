//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.09.16 alle 02:11:49 PM CEST 
//


package eu.fluidos.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DBDataSelectionCondition complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DBDataSelectionCondition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://modeliosoft/xsddesigner/a22bd60b-ee3d-425c-8618-beb6a854051a/ITResource.xsd}DataSelectionCondition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tableName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sqlQuery" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="viewName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DBDataSelectionCondition", propOrder = {
    "tableName",
    "sqlQuery",
    "viewName"
})
public class DBDataSelectionCondition
    extends DataSelectionCondition
{

    @XmlElement(required = true)
    protected String tableName;
    @XmlElement(required = true)
    protected String sqlQuery;
    @XmlElement(required = true)
    protected String viewName;

    /**
     * Recupera il valore della proprietà tableName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Imposta il valore della proprietà tableName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTableName(String value) {
        this.tableName = value;
    }

    /**
     * Recupera il valore della proprietà sqlQuery.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSqlQuery() {
        return sqlQuery;
    }

    /**
     * Imposta il valore della proprietà sqlQuery.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSqlQuery(String value) {
        this.sqlQuery = value;
    }

    /**
     * Recupera il valore della proprietà viewName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Imposta il valore della proprietà viewName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setViewName(String value) {
        this.viewName = value;
    }

}
