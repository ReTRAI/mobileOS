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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Reseller complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Reseller"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="resellerId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="resellerName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="currentBalance" type="{http://www.w3.org/2001/XMLSchema}float"/&gt;
 *         &lt;element name="totalDevices" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Reseller", propOrder = {
    "resellerId",
    "userId",
    "resellerName",
    "currentBalance",
    "totalDevices"
})
public class Reseller {

    @XmlElement(required = true)
    protected String resellerId;
    @XmlElement(required = true)
    protected String userId;
    @XmlElement(required = true)
    protected String resellerName;
    protected float currentBalance;
    protected long totalDevices;

    /**
     * Gets the value of the resellerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResellerId() {
        return resellerId;
    }

    /**
     * Sets the value of the resellerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResellerId(String value) {
        this.resellerId = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the resellerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResellerName() {
        return resellerName;
    }

    /**
     * Sets the value of the resellerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResellerName(String value) {
        this.resellerName = value;
    }

    /**
     * Gets the value of the currentBalance property.
     * 
     */
    public float getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Sets the value of the currentBalance property.
     * 
     */
    public void setCurrentBalance(float value) {
        this.currentBalance = value;
    }

    /**
     * Gets the value of the totalDevices property.
     * 
     */
    public long getTotalDevices() {
        return totalDevices;
    }

    /**
     * Sets the value of the totalDevices property.
     * 
     */
    public void setTotalDevices(long value) {
        this.totalDevices = value;
    }

}