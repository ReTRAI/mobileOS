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
 * <p>Java class for ResellerBalance complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResellerBalance"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="resellerBalanceId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="debitCredit" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="movementValue" type="{http://www.w3.org/2001/XMLSchema}float"/&gt;
 *         &lt;element name="movementDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="movementType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="movementDetail" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResellerBalance", propOrder = {
    "resellerBalanceId",
    "debitCredit",
    "movementValue",
    "movementDate",
    "movementType",
    "movementDetail"
})
public class ResellerBalance {

    @XmlElement(required = true)
    protected String resellerBalanceId;
    @XmlElement(required = true)
    protected String debitCredit;
    protected float movementValue;
    @XmlElement(required = true)
    protected String movementDate;
    @XmlElement(required = true)
    protected String movementType;
    @XmlElement(required = true)
    protected String movementDetail;

    /**
     * Gets the value of the resellerBalanceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResellerBalanceId() {
        return resellerBalanceId;
    }

    /**
     * Sets the value of the resellerBalanceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResellerBalanceId(String value) {
        this.resellerBalanceId = value;
    }

    /**
     * Gets the value of the debitCredit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebitCredit() {
        return debitCredit;
    }

    /**
     * Sets the value of the debitCredit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebitCredit(String value) {
        this.debitCredit = value;
    }

    /**
     * Gets the value of the movementValue property.
     * 
     */
    public float getMovementValue() {
        return movementValue;
    }

    /**
     * Sets the value of the movementValue property.
     * 
     */
    public void setMovementValue(float value) {
        this.movementValue = value;
    }

    /**
     * Gets the value of the movementDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMovementDate() {
        return movementDate;
    }

    /**
     * Sets the value of the movementDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMovementDate(String value) {
        this.movementDate = value;
    }

    /**
     * Gets the value of the movementType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMovementType() {
        return movementType;
    }

    /**
     * Sets the value of the movementType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMovementType(String value) {
        this.movementType = value;
    }

    /**
     * Gets the value of the movementDetail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMovementDetail() {
        return movementDetail;
    }

    /**
     * Sets the value of the movementDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMovementDetail(String value) {
        this.movementDetail = value;
    }

}