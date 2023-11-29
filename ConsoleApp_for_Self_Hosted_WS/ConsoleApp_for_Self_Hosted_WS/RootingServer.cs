
using ConsoleApp_for_Self_Hosted_WS.ServiceReference1;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Reflection.Emit;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ConsoleApp_for_Rooting_Server
{
    class RootingServer : IRootingServer
    {
        ProxyClient proxyClient = new ProxyClient();
        public async void getItinerary(string start, string end)
        {
            // etape 1: regarder le trajet de start a end a pied pour s'en servir comme base
            // appel a l'api nominatim pour trouver start et end
            string nominatimApi = "https://nominatim.openstreetmap.org";
            string openRouteServiceApi = "https://api.openrouteservice.org/v2";
            string getStartLocation = "/search?q=" + start + "&format=json&polygon_kml=1&addressdetails=1";
            string getEndLocation = "/search?q=" + end + "&format=json&polygon_kml=1&addressdetails=1";
            string response = proxyClient.Get(nominatimApi + getStartLocation);
            Console.WriteLine(response);
            /*
            HttpClient client = new HttpClient();
            HttpRequestMessage requestStart = new HttpRequestMessage(HttpMethod.Get, nominatimApi + getStartLocation);
            HttpRequestMessage requestEnd = new HttpRequestMessage(HttpMethod.Get, nominatimApi + getEndLocation);

            // Ajouter le referer dans les en-têtes
            requestStart.Headers.Referrer = new Uri("https://facebook.com");
            requestEnd.Headers.Referrer = new Uri("https://facebook.com");

            // Envoyer les requêtes et attendre les réponses
            Task<HttpResponseMessage> responseStart = client.SendAsync(requestStart);
            Task<HttpResponseMessage> responseEnd = client.SendAsync(requestEnd);


            // Lire et afficher le contenu des réponses
            string contentStart = await responseStart.Result.Content.ReadAsStringAsync();
            string contentEnd = await responseEnd.Result.Content.ReadAsStringAsync();

            Console.WriteLine("Start point location:");
            Console.WriteLine(getLocation(contentStart));
            Console.WriteLine("End point location:");
            Console.WriteLine(getLocation(contentEnd));

            string startLocation=getLocation(contentStart);
            string endLocation=getLocation(contentEnd);
            string profile = "foot-walking";

            string uriItinerary = "/directions/"+ profile +"?api_key=5b3ce3597851110001cf624833fd1c84c80546dbbd1400fe7eb552e3&start=" + startLocation + "&end=" + endLocation;

            HttpRequestMessage requestItinerary = new HttpRequestMessage(HttpMethod.Get, openRouteServiceApi + uriItinerary);

            Task<HttpResponseMessage> responseItinerary = client.SendAsync(requestItinerary);

            string contentItinerary = await responseItinerary.Result.Content.ReadAsStringAsync();
            Console.WriteLine(contentItinerary);*/

            // appel a l'api openrouteservice pour trouver le trajet

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
            var locationData = JsonConvert.DeserializeObject<LocationData[]>(content);
            string res = locationData[0].lon + "," + locationData[0].lat;
            return res;
        }
    }
    public class LocationData
    {
        public string lat { get; set; }
        public string lon { get; set; }
    }

    public class Itinerary
    {
        //a definir selon le type de carte dans le client
    }

}
