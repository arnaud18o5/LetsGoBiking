
package com.soap.ws.client.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Properties complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Properties"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Segments" type="{http://schemas.datacontract.org/2004/07/ConsoleApp_for_Rooting_Server}ArrayOfSegment" minOccurs="0"/&gt;
 *         &lt;element name="WayPoints" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfint" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Properties", propOrder = {
    "segments",
    "wayPoints"
})
public class Properties {

    @XmlElementRef(name = "Segments", namespace = "http://schemas.datacontract.org/2004/07/ConsoleApp_for_Rooting_Server", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfSegment> segments;
    @XmlElementRef(name = "WayPoints", namespace = "http://schemas.datacontract.org/2004/07/ConsoleApp_for_Rooting_Server", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfint> wayPoints;

    /**
     * Obtient la valeur de la propriété segments.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSegment }{@code >}
     *     
     */
    public JAXBElement<ArrayOfSegment> getSegments() {
        return segments;
    }

    /**
     * Définit la valeur de la propriété segments.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSegment }{@code >}
     *     
     */
    public void setSegments(JAXBElement<ArrayOfSegment> value) {
        this.segments = value;
    }

    /**
     * Obtient la valeur de la propriété wayPoints.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *     
     */
    public JAXBElement<ArrayOfint> getWayPoints() {
        return wayPoints;
    }

    /**
     * Définit la valeur de la propriété wayPoints.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *     
     */
    public void setWayPoints(JAXBElement<ArrayOfint> value) {
        this.wayPoints = value;
    }

}
