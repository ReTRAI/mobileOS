//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.04.06 at 02:13:35 PM UTC 
//


package com.season.portal.client.generated.reseller;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="parentResellerId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="childResellerId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "parentResellerId",
    "childResellerId"
})
@XmlRootElement(name = "GetResellerAssociationRequest")
public class GetResellerAssociationRequest {

    @XmlElement(required = true)
    protected String parentResellerId;
    @XmlElement(required = true)
    protected String childResellerId;

    /**
     * Gets the value of the parentResellerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentResellerId() {
        return parentResellerId;
    }

    /**
     * Sets the value of the parentResellerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentResellerId(String value) {
        this.parentResellerId = value;
    }

    /**
     * Gets the value of the childResellerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChildResellerId() {
        return childResellerId;
    }

    /**
     * Sets the value of the childResellerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChildResellerId(String value) {
        this.childResellerId = value;
    }

}
