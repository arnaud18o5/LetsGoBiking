using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Runtime.Caching;
using System.Text;
using System.Threading.Tasks;

namespace ProxyCache
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
                string result = request(CacheItemName);
                _cache.Add(CacheItemName, result, DateTimeOffset.Now.AddSeconds(dt_default_secondes));   
            }
            return (T)_cache.Get(CacheItemName);
        }

        public T Get(string CacheItemName, double dt_seconds)
        {
            if (!_cache.Contains(CacheItemName))
            {
                // faire le call api et mettre en cache 
                string result = request(CacheItemName);
                _cache.Add(CacheItemName, result, DateTimeOffset.Now.AddSeconds(dt_seconds));
            }
            return (T)_cache.Get(CacheItemName);
        }

        public T Get(string CacheItemName, DateTimeOffset dt)
        {
            if (!_cache.Contains(CacheItemName)) {
                string result = request(CacheItemName);
                _cache.Add(CacheItemName, result, dt);
            }
            return (T)_cache.Get(CacheItemName);
        }

        private string request(string url)
        {
            string result = string.Empty;
            using (var client = new HttpClient())
            {
                var response = client.GetAsync(url).Result;
                if (response.IsSuccessStatusCode)
                {
                    result = response.Content.ReadAsStringAsync().Result;
                }
            }
            return result;
        }
    }
}
