
package com.soap.ws.client.generated;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.3.2
 * Generated source version: 2.2
 * 
 */
@WebService(name = "IRootingServer", targetNamespace = "http://tempuri.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface IRootingServer {


    /**
     * 
     * @param startLatitude
     * @param endLongitude
     * @param startLongitude
     * @param endLatitude
     */
    @WebMethod(action = "http://tempuri.org/IRootingServer/getItinerary")
    @RequestWrapper(localName = "getItinerary", targetNamespace = "http://tempuri.org/", className = "com.soap.ws.client.generated.GetItinerary")
    @ResponseWrapper(localName = "getItineraryResponse", targetNamespace = "http://tempuri.org/", className = "com.soap.ws.client.generated.GetItineraryResponse")
    public void getItinerary(
        @WebParam(name = "startLatitude", targetNamespace = "http://tempuri.org/")
        Double startLatitude,
        @WebParam(name = "startLongitude", targetNamespace = "http://tempuri.org/")
        Double startLongitude,
        @WebParam(name = "endLatitude", targetNamespace = "http://tempuri.org/")
        Double endLatitude,
        @WebParam(name = "endLongitude", targetNamespace = "http://tempuri.org/")
        Double endLongitude);

}
