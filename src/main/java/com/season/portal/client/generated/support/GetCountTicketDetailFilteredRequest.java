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
 *         &lt;element name="ticketId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="startDetailDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="endDetailDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="responseUserId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "ticketId",
    "startDetailDate",
    "endDetailDate",
    "responseUserId"
})
@XmlRootElement(name = "GetCountTicketDetailFilteredRequest")
public class GetCountTicketDetailFilteredRequest {

    @XmlElement(required = true)
    protected String ticketId;
    @XmlElement(required = true)
    protected String startDetailDate;
    @XmlElement(required = true)
    protected String endDetailDate;
    @XmlElement(required = true)
    protected String responseUserId;

    /**
     * Gets the value of the ticketId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicketId() {
        return ticketId;
    }

    /**
     * Sets the value of the ticketId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicketId(String value) {
        this.ticketId = value;
    }

    /**
     * Gets the value of the startDetailDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDetailDate() {
        return startDetailDate;
    }

    /**
     * Sets the value of the startDetailDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDetailDate(String value) {
        this.startDetailDate = value;
    }

    /**
     * Gets the value of the endDetailDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndDetailDate() {
        return endDetailDate;
    }

    /**
     * Sets the value of the endDetailDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndDetailDate(String value) {
        this.endDetailDate = value;
    }

    /**
     * Gets the value of the responseUserId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseUserId() {
        return responseUserId;
    }

    /**
     * Sets the value of the responseUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseUserId(String value) {
        this.responseUserId = value;
    }

}
