# DrillingDataReceiver
Client-server application for receiving drilling data using the WITS and SibReceiver protocols and writing to the SQLite database
================================================================================================

The start of saving to the drilling database is carried out by connecting to the WITS server, which transmits the WITS Time Based package. 
Also starting the server and connecting to it the SIBReceiver client, which transmits Measurement While Drilling (MWD) data. 
The database will contain well depth data, bit depth, pressure in the discharge manifold and other data transmitted via the WITS protocol. 
The database will store survey, temperature, gamma ray and other data transmitted by the telemetry system.

Examples of console commands for managing the application can be found in the ClientServerLibrary repository.
