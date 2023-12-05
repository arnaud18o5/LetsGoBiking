using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Runtime.Caching;
using System.Text;
using System.Threading.Tasks;

namespace Proxy
{
    internal class GenericProxyCache<T>
    {
        double dt_default_secondes = 6000;
        ObjectCache _cache = MemoryCache.Default;
        public T Get(string CacheItemName)
        {
            // we assume that the cacheItemName is the request
            if (!_cache.Contains(CacheItemName) || _cache.Get(CacheItemName) == null)
            {
                // faire le call api et mettre en cache
                Console.WriteLine("Get " + CacheItemName);
                string result = requestAsync(CacheItemName).Result;
                if (result == null)
                {
                    return default(T);
                }
                _cache.Add(CacheItemName, result, DateTimeOffset.MaxValue);
            }
            else
            {
                Console.WriteLine("Get " + CacheItemName + " from cache");
            }
            return (T)_cache.Get(CacheItemName);
        }

        public T Get(string CacheItemName, double dt_seconds)
        {
            if (!_cache.Contains(CacheItemName))
            {
                // faire le call api et mettre en cache 
                string result = requestAsync(CacheItemName).Result;
                if (result == null)
                {
                    return default(T);
                }
                _cache.Add(CacheItemName, result, DateTimeOffset.Now.AddSeconds(dt_seconds));
            }
            return (T)_cache.Get(CacheItemName);
        }

        public T Get(string CacheItemName, DateTimeOffset dt)
        {
            if (!_cache.Contains(CacheItemName))
            {
                string result = requestAsync(CacheItemName).Result;
                if (result == null)
                {
                    return default(T);
                }
                _cache.Add(CacheItemName, result, dt);
            }
            return (T)_cache.Get(CacheItemName);
        }

        private async Task<string> requestAsync(string url)
        {
            using (HttpClient client = new HttpClient())
            {
                // Augmentez la taille maximale du contenu de la réponse selon vos besoins
                client.MaxResponseContentBufferSize = int.MaxValue;
                HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, url);
                request.Headers.Referrer = new Uri("https://facebook.com");

                try
                {
                    HttpResponseMessage response = await client.SendAsync(request);

                    // Vérifiez si la réponse est réussie (200 OK) avant de lire le contenu
                    response.EnsureSuccessStatusCode();

                    // Lire et retourner le contenu de la réponse
                    return await response.Content.ReadAsStringAsync();
                }
                catch (HttpRequestException ex)
                {
                    // Gérez les erreurs de requête HTTP ici
                    Console.WriteLine($"Une erreur HTTP s'est produite : {ex.Message}");
                    return null; // Ou lancez une exception appropriée selon vos besoins
                }
            }
        }
    }
}
