ws-axis2-project
===================

The goal of this project was to create an application that can perform content search from a specific set of sources (e.g.,YouTube, Twitter,Flickr etc.) based on user-specified keywords. The application conforms to the client-server architecture. On the server side, a standalone Axis2 web service receives user requests, performs the search and sends the reply to the client. This web service exposes a standard interface via WSDL which all clients need to use. For demonstration purposes, we also built a client web application that handles user interaction and communicates with the Axis2 web service over a secure connection (using features of WSSecurity).
