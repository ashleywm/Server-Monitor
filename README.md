# Server-Monitor
Java server side application for the Server Monitoring project 

To get sigar.jar to work on a server you must put .dll and .so files into the the lib/System32 folders.

These files are located inside of the /lib folder

This allows for the sigar.jar to contact low level system info. These files were not written as part of the project and are infact part of the SIGAR API.
Please see SIGAR API docs for the usage of these files. 