
package com.soap.ws.client.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Itinerary complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Itinerary"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Features" type="{http://schemas.datacontract.org/2004/07/ConsoleApp_for_Rooting_Server}ArrayOfFeature" minOccurs="0"/&gt;
 *         &lt;element name="Metadata" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfKeyValueOfstringanyType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Itinerary", propOrder = {
    "features",
    "metadata"
})
public class Itinerary {

    @XmlElementRef(name = "Features", namespace = "http://schemas.datacontract.org/2004/07/ConsoleApp_for_Rooting_Server", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfFeature> features;
    @XmlElementRef(name = "Metadata", namespace = "http://schemas.datacontract.org/2004/07/ConsoleApp_for_Rooting_Server", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfKeyValueOfstringanyType> metadata;

    /**
     * Obtient la valeur de la propriété features.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfFeature }{@code >}
     *     
     */
    public JAXBElement<ArrayOfFeature> getFeatures() {
        return features;
    }

    /**
     * Définit la valeur de la propriété features.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfFeature }{@code >}
     *     
     */
    public void setFeatures(JAXBElement<ArrayOfFeature> value) {
        this.features = value;
    }

    /**
     * Obtient la valeur de la propriété metadata.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfKeyValueOfstringanyType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfKeyValueOfstringanyType> getMetadata() {
        return metadata;
    }

    /**
     * Définit la valeur de la propriété metadata.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfKeyValueOfstringanyType }{@code >}
     *     
     */
    public void setMetadata(JAXBElement<ArrayOfKeyValueOfstringanyType> value) {
        this.metadata = value;
    }

}
