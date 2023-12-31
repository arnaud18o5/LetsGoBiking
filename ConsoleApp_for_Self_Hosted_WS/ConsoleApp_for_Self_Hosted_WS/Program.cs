﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;



// add the WCF ServiceModel namespace 
using System.ServiceModel;
using System.ServiceModel.Description;
using Newtonsoft.Json.Linq;
using System.Net.Http;

namespace ConsoleApp_for_Rooting_Server 
{ 
    class Program
    {
        static void Main(string[] args)
        {
            //Create a URI to serve as the base address
            //Be careful to run Visual Studio as Admistrator or to allow VS to open new port netsh command. 
            // Example : netsh http add urlacl url=http://+:80/MyUri user=DOMAIN\user
            Uri httpUrl = new Uri("http://localhost:8090/RootingServer");

            //Create ServiceHost
            ServiceHost host = new ServiceHost(typeof(RootingServer), httpUrl);

            // Multiple end points can be added to the Service using AddServiceEndpoint() method.
            // Host.Open() will run the service, so that it can be used by any client.

            // Example adding :
            // Uri tcpUrl = new Uri("net.tcp://localhost:8090/MyService/SimpleCalculator");
            // ServiceHost host = new ServiceHost(typeof(MyCalculatorService.SimpleCalculator), httpUrl, tcpUrl);
            //WSHttpBinding binding = new WSHttpBinding();
            
            // Set the MaxReceivedMessageSize to a desired value (e.g., 10 MB)
            //Add a service endpoint
            //host.AddServiceEndpoint(typeof(IRootingServer), binding, ""); 

            //Enable metadata exchange
            ServiceMetadataBehavior smb = new ServiceMetadataBehavior();
            smb.HttpGetEnabled = true;
            host.Description.Behaviors.Add(smb);
            
            //Start the Service
            host.Open();

            Console.WriteLine("Service is host at " + DateTime.Now.ToString());
            Console.WriteLine("Host is running... Press <Enter> key to stop");
            Console.ReadLine();




        }
        
        
    }
}
