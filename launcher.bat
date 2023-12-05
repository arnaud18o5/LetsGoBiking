cd "Proxy\Proxy\bin\Debug"

powershell Start-Process -FilePath "Proxy.exe" -Verb RunAs

cd "..\..\..\..\ConsoleApp_for_Self_Hosted_WS\ConsoleApp_for_Self_Hosted_WS\bin\Debug"

powershell Start-Process -FilePath "ConsoleApp_for_Self_Hosted_WS.exe" -Verb RunAs
