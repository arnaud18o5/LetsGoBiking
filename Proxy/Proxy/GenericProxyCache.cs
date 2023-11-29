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
            if (!_cache.Contains(CacheItemName))
            {
                // faire le call api et mettre en cache 
                Task<string> result = requestAsync(CacheItemName);
                _cache.Add(CacheItemName, result.Result, DateTimeOffset.Now.AddSeconds(dt_default_secondes));
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
                Task<string> result = requestAsync(CacheItemName);
                _cache.Add(CacheItemName, result.Result, DateTimeOffset.Now.AddSeconds(dt_seconds));
            }
            return (T)_cache.Get(CacheItemName);
        }

        public T Get(string CacheItemName, DateTimeOffset dt)
        {
            if (!_cache.Contains(CacheItemName))
            {
                Task<string> result = requestAsync(CacheItemName);
                _cache.Add(CacheItemName, result.Result, dt);
            }
            return (T)_cache.Get(CacheItemName);
        }

        private async Task<string> requestAsync(string url)
        {
            HttpClient client = new HttpClient();
            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, url);
            request.Headers.Referrer = new Uri("https://facebook.com");
            Task<HttpResponseMessage> response = client.SendAsync(request);


            // Lire et afficher le contenu des réponses
            string contentStart = await response.Result.Content.ReadAsStringAsync();
            return contentStart;
        }
    }
}
