
package com.soap.ws.client.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="startLatitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="startLongitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="endLatitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="endLongitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
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
    "startLatitude",
    "startLongitude",
    "endLatitude",
    "endLongitude"
})
@XmlRootElement(name = "getItinerary")
public class GetItinerary {

    protected Double startLatitude;
    protected Double startLongitude;
    protected Double endLatitude;
    protected Double endLongitude;

    /**
     * Obtient la valeur de la propriété startLatitude.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getStartLatitude() {
        return startLatitude;
    }

    /**
     * Définit la valeur de la propriété startLatitude.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setStartLatitude(Double value) {
        this.startLatitude = value;
    }

    /**
     * Obtient la valeur de la propriété startLongitude.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getStartLongitude() {
        return startLongitude;
    }

    /**
     * Définit la valeur de la propriété startLongitude.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setStartLongitude(Double value) {
        this.startLongitude = value;
    }

    /**
     * Obtient la valeur de la propriété endLatitude.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getEndLatitude() {
        return endLatitude;
    }

    /**
     * Définit la valeur de la propriété endLatitude.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setEndLatitude(Double value) {
        this.endLatitude = value;
    }

    /**
     * Obtient la valeur de la propriété endLongitude.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getEndLongitude() {
        return endLongitude;
    }

    /**
     * Définit la valeur de la propriété endLongitude.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setEndLongitude(Double value) {
        this.endLongitude = value;
    }

}
