//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.04.06 at 02:13:36 PM UTC 
//


package com.season.portal.client.generated.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Expiring complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Expiring"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="renewed" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="in30Days" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="in15Days" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="in7Days" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Expiring", propOrder = {
    "renewed",
    "in30Days",
    "in15Days",
    "in7Days"
})
public class Expiring {

    protected long renewed;
    protected long in30Days;
    protected long in15Days;
    protected long in7Days;

    /**
     * Gets the value of the renewed property.
     * 
     */
    public long getRenewed() {
        return renewed;
    }

    /**
     * Sets the value of the renewed property.
     * 
     */
    public void setRenewed(long value) {
        this.renewed = value;
    }

    /**
     * Gets the value of the in30Days property.
     * 
     */
    public long getIn30Days() {
        return in30Days;
    }

    /**
     * Sets the value of the in30Days property.
     * 
     */
    public void setIn30Days(long value) {
        this.in30Days = value;
    }

    /**
     * Gets the value of the in15Days property.
     * 
     */
    public long getIn15Days() {
        return in15Days;
    }

    /**
     * Sets the value of the in15Days property.
     * 
     */
    public void setIn15Days(long value) {
        this.in15Days = value;
    }

    /**
     * Gets the value of the in7Days property.
     * 
     */
    public long getIn7Days() {
        return in7Days;
    }

    /**
     * Sets the value of the in7Days property.
     * 
     */
    public void setIn7Days(long value) {
        this.in7Days = value;
    }

}
