using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace ProxyCache
{
    // REMARQUE : vous pouvez utiliser la commande Renommer du menu Refactoriser pour changer le nom de classe "Service1" à la fois dans le code et le fichier de configuration.
    public class ProxyCache : IProxyCache
    {
        GenericProxyCache<string> _proxyCache = new GenericProxyCache<string>();
        public string Get(string CacheItemName)
        {
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
