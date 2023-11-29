using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Proxy
{
    internal class Proxy : IProxy
    {
        GenericProxyCache<string> _proxyCache = new GenericProxyCache<string>();
        public string Get(string CacheItemName)
        {
            Console.WriteLine("Get " + CacheItemName);
            return _proxyCache.Get(CacheItemName);
        }

        public string GetWithDTSeconds(string CacheItemName, double dt_seconds)
        {
            return _proxyCache.Get(CacheItemName, dt_seconds);
        }

        public string GetWithDT(string CacheItemName, DateTimeOffset dt)
        {
            return _proxyCache.Get(CacheItemName, dt);
        }
    }
}
