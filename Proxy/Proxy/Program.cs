using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.ServiceModel.Description;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace Proxy
{
    internal class Program
    {
        static void Main(string[] args)
        {
            // Create a URI to serve as the base address
            Uri httpUrl = new Uri("http://localhost:8091/ProxyCache");

            // Create ServiceHost
            ServiceHost host = new ServiceHost(typeof(Proxy), httpUrl);

            // Add a service endpoint
            WSHttpBinding binding = new WSHttpBinding()
            {
                MaxBufferPoolSize = Int32.MaxValue,
                MaxReceivedMessageSize = Int32.MaxValue,
                ReaderQuotas = new XmlDictionaryReaderQuotas()
                {
                    MaxArrayLength = 200000000,
                    MaxDepth = 32,
                    MaxStringContentLength = 200000000
                }
            };


            host.AddServiceEndpoint(typeof(IProxy), binding, "");

            // Enable metadata exchange
            ServiceMetadataBehavior smb = new ServiceMetadataBehavior();
            smb.HttpGetEnabled = true;
            host.Description.Behaviors.Add(smb);

            // Start the Service
            host.Open();

            Console.WriteLine("Service is host at " + DateTime.Now.ToString());
            Console.WriteLine("Host is running... Press <Enter> key to stop");
            Console.ReadLine();
        }
    }
}
