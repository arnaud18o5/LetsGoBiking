
using ConsoleApp_for_Self_Hosted_WS.ServiceReference1;
using Microsoft.JScript;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Globalization;
using System.Linq;
using System.Net.Http;
using System.Reflection.Emit;
using System.Runtime.Serialization;
using System.Security.Cryptography.X509Certificates;
using System.ServiceModel;
using System.ServiceModel.Channels;
using System.Text;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;
using System.Xml;
using JsonSerializer = System.Text.Json.JsonSerializer;

namespace ConsoleApp_for_Rooting_Server
{
    class RootingServer : IRootingServer

    {

        ProxyClient proxyClient = new ProxyClient();
        
        public async Task<String> GetItinerary(double startLatitude, double startLongitude, double endLatitude, double endLongitude)
        {
            // etape 1: regarder le trajet de start a end a pied pour s'en servir comme base
            // appel a l'api nominatim pour trouver start et end
            string nominatimApi = "https://nominatim.openstreetmap.org";
            string jcdecauxApi = "https://api.jcdecaux.com/vls/v3";

            //List<Itinerary> itineraries = new List<Itinerary>();
            String itineraries;
            //string getStartLocation = "/search?q=" + start + "&format=json&polygon_kml=1&addressdetails=1";
            //string getEndLocation = "/search?q=" + end + "&format=json&polygon_kml=1&addressdetails=1";
            //string responseStart = proxyClient.Get(nominatimApi + getStartLocation);
            //string responseEnd = proxyClient.Get(nominatimApi + getEndLocation);

            Console.WriteLine("Start point location:");
            Console.WriteLine(startLongitude.ToString(CultureInfo.InvariantCulture) + "," + startLatitude.ToString(CultureInfo.InvariantCulture));
            //Console.WriteLine(getLocation(responseStart));
            Console.WriteLine("End point location:");
            Console.WriteLine(endLongitude.ToString(CultureInfo.InvariantCulture) + "," + endLatitude.ToString(CultureInfo.InvariantCulture));
            //Console.WriteLine(getLocation(responseEnd));

            //LocationData locationStart = LocationData(getLatitudeNominatim(responseStart), getLongitudeNominatim(responseStart));
            //LocationData locationEnd = LocationData(getLatitudeNominatim(responseEnd), getLongitudeNominatim(responseEnd));
            LocationData locationStart = LocationData(startLatitude, startLongitude);
            LocationData locationEnd = LocationData(endLatitude, endLongitude);

            //string startLocation = getLocation(responseStart);
            //string endLocation = getLocation(responseEnd);

            string startLocation = locationStart.ToString();
            string endLocation = locationEnd.ToString();

            Console.WriteLine("Itinerary duration (foot-walking):");
            double footDuration = getItineraryDistance(startLocation, endLocation, "foot-walking");
            Console.WriteLine(footDuration);

            //on cherche tous les stations jcdecaux
            string uriStations = "/stations?apiKey=27db0677318fc1ed93bfed36ce7a4a2bd32f9f07";
            string responseStations = proxyClient.Get(jcdecauxApi + uriStations);
            
            // find the 5 closest stations to start

            var options = new JsonSerializerOptions
            {
                PropertyNameCaseInsensitive = true,
            };

            List<Station> stations = JsonSerializer.Deserialize<List<Station>>(responseStations, options);

            Station startStation = getClosestStation(locationStart, stations, 1, 0);
            Station endStation = getClosestStation(locationEnd, stations, 0, 1);

            if (!StationsAreInTheSameContract(startStation, endStation))
            {
                Console.WriteLine("That's going to be tough.");
                return "[" + getItinerary(startLocation, endLocation, "foot-walking") + "]";
            }

            double distanceStationToStation = getItineraryDistance(startStation.ToString(), endStation.ToString(), "cycling-regular");
            double distanceStartToStation = getItineraryDistance(startLocation, startStation.ToString(), "foot-walking");
            double distanceStationToEnd = getItineraryDistance(endStation.ToString(), endLocation, "foot-walking");

            Console.WriteLine("Start stations:");
            Console.WriteLine(startStation.ToString());
            Console.WriteLine("End stations:");
            Console.WriteLine(endStation.ToString());
            Console.WriteLine("Itinerary from start location to start station:");
            Console.WriteLine(distanceStartToStation);
            Console.WriteLine("Itinerary duration between stations(cycling-regular):");
            Console.WriteLine(distanceStationToStation);
            Console.WriteLine("Itinerary from end station to end location:");
            Console.WriteLine(distanceStationToEnd);
            Console.WriteLine("Total itinerary duration:");
            Console.WriteLine(distanceStartToStation + distanceStationToStation + distanceStationToEnd);

            Console.WriteLine("Should take a bike ?:");
            if (footDuration < distanceStartToStation + distanceStationToStation + distanceStationToEnd + 120) // we add 2 minutes to the itinerary to take the bike
            {
                Console.WriteLine("No");
                itineraries = "[" +getItinerary(startLocation, endLocation, "foot-walking") + "]";
            }
            else
            {
                Console.WriteLine("Yes");
                itineraries = "[" + getItinerary(startLocation, startStation.ToString(), "foot-walking") + ","
                    + getItinerary(startStation.ToString(), endStation.ToString(), "cycling-regular") + ","
                    + getItinerary(endStation.ToString(), endLocation, "foot-walking") + "]";
            }

            // parse itinaries to json
            return itineraries;

            /*
             * la on a les 2 stations les plus proches de start et end
             * maintenant il faut faire la logique pour trouver le meilleur trajet entre directement a pied ou en prenant un velo
             * il faut regarder que le velo soit disponible dans la station start et qu'il y ait une place dans la station end
             * il faut regarder que les deux stations soient dans le meme contrat
             * 
             */

            // etape 2: trouver la station de velo la plus proche de start et celle plus proche de end
            // dans toutes les stations du contrat trouver les x plus proches a vol d'oiseau de start et celles de end (appel api jcdecaux ou recherche dans cache)
            // dans les x stations trouvées, calculer le trajet a pied le plus proches de start et de end 
            // etape 3: trouver la somme des trajets start->station_start->station_end->end
            // trouver le trajet a velo station_start->station_end et ajouter les trajets a pieds
            // etape 4: comparer etape 1 et etape 3
            // retourner le meilleur trajet
        }

        public string getLocation(string content)
        {
            var locationData = JsonConvert.DeserializeObject<LocationNominatim[]>(content);
            string res = locationData[0].lon + "," + locationData[0].lat;
            return res;
        }

        public double getLatitudeNominatim(string content)
        {
            var locationData = JsonConvert.DeserializeObject<LocationNominatim[]>(content);
            return double.Parse( locationData[0].lat,  CultureInfo.InvariantCulture);
        }

        public double getLongitudeNominatim(string content)
        {
            var locationData = JsonConvert.DeserializeObject<LocationNominatim[]>(content);
            return double.Parse(locationData[0].lon, CultureInfo.InvariantCulture);
        }

        public LocationData LocationData(double latitude, double longitude)
        {
            LocationData locationData = new LocationData();
            locationData.latitude = latitude;
            locationData.longitude = longitude;
            return locationData;
        }

        public LocationData LocationData(string content)
        {
            var locationData = JsonConvert.DeserializeObject<LocationData[]>(content);
            return locationData[0];
        }

        public double GetDistance(double longitude, double latitude, double otherLongitude, double otherLatitude)
        {
            var d1 = latitude * (Math.PI / 180.0);
            var num1 = longitude * (Math.PI / 180.0);
            var d2 = otherLatitude * (Math.PI / 180.0);
            var num2 = otherLongitude * (Math.PI / 180.0) - num1;
            var d3 = Math.Pow(Math.Sin((d2 - d1) / 2.0), 2.0) + Math.Cos(d1) * Math.Cos(d2) * Math.Pow(Math.Sin(num2 / 2.0), 2.0);

            return 6376500.0 * (2.0 * Math.Atan2(Math.Sqrt(d3), Math.Sqrt(1.0 - d3)));
        }


        private double getItineraryDistance(string startLocation, string endLocation, string profile)
        {
            string openRouteServiceApi = "https://api.openrouteservice.org/v2";
            string uriItinerary = "/directions/" + profile + "?api_key=5b3ce3597851110001cf624833fd1c84c80546dbbd1400fe7eb552e3&start=" + startLocation + "&end=" + endLocation;

            string itinerary = proxyClient.Get(openRouteServiceApi + uriItinerary);
            Itinerary Itinerary = JsonConvert.DeserializeObject<Itinerary>(itinerary);

            JObject itineraryJson = JObject.Parse(itinerary);
            double? duration = (double?)itineraryJson["features"][0]["properties"]["summary"]["duration"];
            if (duration == null)
                return -1;
            return (double)duration;
        }

        private String getItinerary(string startLocation, string endLocation, string profile)
        {
            string openRouteServiceApi = "https://api.openrouteservice.org/v2";
            string uriItinerary = "/directions/" + profile + "?api_key=5b3ce3597851110001cf624833fd1c84c80546dbbd1400fe7eb552e3&start=" + startLocation + "&end=" + endLocation;

            return proxyClient.Get(openRouteServiceApi + uriItinerary);
        }

        private Station getClosestStation(LocationData location, List<Station> stations, int nbOfFreeBikes, int nbOfFreeStand)
        {

            // remove alll the stations with less free bikes than nbOfFreeBikes or less free stands than nbOfFreeStand
            stations.RemoveAll(x => x.totalStands.availabilities.bikes < nbOfFreeBikes || x.totalStands.availabilities.stands < nbOfFreeStand);
            stations.Sort((x, y) => GetDistance(location.longitude, location.latitude, x.position.longitude, x.position.latitude).CompareTo(GetDistance(location.longitude, location.latitude, y.position.longitude, y.position.latitude)));

            
            double[] durations = new double[5];
            for (int i = 0; i < 5; i++)
            {
                // pour les 5 stations les plus proches de start, on cherche le trajet a pied le plus proche de start
                durations[i] = getItineraryDistance(location.ToString(), stations[i].ToString(), "foot-walking");

            }
            // supprimer toutes les stations après les 5 premières
            List<Station> closestStations = stations.GetRange(0, 5);

            Station closestStation = closestStations[0];
            double minDuration = durations[0];
            for (int i = 0; i < 5; i++)
            {
                if (durations[i] < minDuration)
                {
                    minDuration = durations[i];
                    closestStation = closestStations[i];
                }
            }

            return closestStation;
        }

        private bool StationsAreInTheSameContract(Station startStation, Station endStation)
        {
            return (startStation.contractName == endStation.contractName);
            
        }

        public Task<List<Itinerary>> getItinerary(double startLatitude, double startLongitude, double endLatitude, double endLongitude)
        {
            throw new NotImplementedException();
        }
    }

    public class LocationNominatim
    {
        public string lon { get; set; }
        public string lat { get; set; }
    }
    public class LocationData
    {
        public double latitude { get; set; }
        public double longitude { get; set; }

        public override string ToString()
        {
            return longitude.ToString(CultureInfo.InvariantCulture) + "," + latitude.ToString(CultureInfo.InvariantCulture);
        }
    }

    [DataContract]
    public class Step
    {
        //public double Distance { get; set; }
        //public double Duration { get; set; }
        //public int Type { get; set; }
        //public string Instruction { get; set; }
        public string Name { get; set; }
        //public List<int> WayPoints { get; set; }
    }

    [DataContract]
    public class Segment
    {
        public double Distance { get; set; }
        public double Duration { get; set; }
        public List<Step> Steps { get; set; }
    }

    [DataContract]
    public class Summary
    {
        public double Distance { get; set; }
        public double Duration { get; set; }
    }

    [DataContract]

    public class Properties
    {
        public int Transfers { get; set; }
        public int Fare { get; set; }
        [DataMember]
        public List<Segment> Segments { get; set; }
        [DataMember]
        public List<int> WayPoints { get; set; }
        public Summary Summary { get; set; }
    }

    [DataContract]

    public class Geometry
    {
        [DataMember]
        public List<List<double>> Coordinates { get; set; }
        public string Type { get; set; }
    }

    [DataContract]
    public class Feature
    {
        public List<double> Bbox { get; set; }
        public string Type { get; set; }
        [DataMember]
        public Properties Properties { get; set; }
        [DataMember]
        public Geometry Geometry { get; set; }
    }

    [DataContract]
    public class RouteResponse
    {
        public string Type { get; set; }
        [DataMember]
        public Dictionary<string, object> Metadata { get; set; }
        public List<double> Bbox { get; set; }
        [DataMember]
        public List<Feature> Features { get; set; }
    }

    [DataContract]
    public class Itinerary
    {
        public string Type { get; set; }
        [DataMember]
        public Dictionary<string, object> Metadata { get; set; }
        public List<double> Bbox { get; set; }
        [DataMember]
        public List<Feature> Features { get; set; }
    }

    public class  Availabilities 
    {
        public int bikes { get; set; }
        public int stands { get; set; }
        public int mechanicalBikes { get; set; }
        public int electricalBikes { get; set; }
        public int electricalInternalBatteryBikes { get; set; }
        public int electricalRemovableBatteryBikes { get; set; }
    }

    public class  Stands 
    {
        public Availabilities availabilities { get; set; }
        public int capacity { get; set; }
    }

    public class Station
    {
        public int number { get; set; }
        public string contractName { get; set; }
        public string name { get; set; }
        public string address { get; set; }
        public LocationData position { get; set; }
        public Stands totalStands { get; set; }
        /*public bool banking { get; set; }
        public bool bonus { get; set; }
        public string status { get; set; }
        public Stands mainStands { get; set; }
        public string overflowStands { get; set; }*/

        // to string 
        public override string ToString()
        {
            return position.ToString() ;
        }

        

    }


}
