//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.04.06 at 02:13:35 PM UTC 
//


package com.season.portal.client.generated.support;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SupportAssociation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SupportAssociation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="supportAssociationId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="parentSupportId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="childSupportId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SupportAssociation", propOrder = {
    "supportAssociationId",
    "parentSupportId",
    "childSupportId"
})
public class SupportAssociation {

    @XmlElement(required = true)
    protected String supportAssociationId;
    @XmlElement(required = true)
    protected String parentSupportId;
    @XmlElement(required = true)
    protected String childSupportId;

    /**
     * Gets the value of the supportAssociationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupportAssociationId() {
        return supportAssociationId;
    }

    /**
     * Sets the value of the supportAssociationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupportAssociationId(String value) {
        this.supportAssociationId = value;
    }

    /**
     * Gets the value of the parentSupportId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentSupportId() {
        return parentSupportId;
    }

    /**
     * Sets the value of the parentSupportId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentSupportId(String value) {
        this.parentSupportId = value;
    }

    /**
     * Gets the value of the childSupportId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChildSupportId() {
        return childSupportId;
    }

    /**
     * Sets the value of the childSupportId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChildSupportId(String value) {
        this.childSupportId = value;
    }

}
