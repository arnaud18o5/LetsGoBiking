using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
// add assembly System.ServiceModel  and using for the corresponding model
using System.ServiceModel;

namespace ConsoleApp_for_Rooting_Server
{

    [ServiceContract()]
    public interface IRootingServer
    {
        [OperationContract()]
        void getItinerary(string start, string end);
    }


}
