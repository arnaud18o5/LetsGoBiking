using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;

namespace Proxy
{
    [ServiceContract()]
    internal interface IProxy
    {
        [OperationContract]
        string Get(string CacheItemName);

        [OperationContract]
        string GetWithDTSeconds(string CacheItemName, double dt_seconds);

        [OperationContract]
        string GetWithDT(string CacheItemName, DateTimeOffset dt);


    }
}
