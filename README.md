Requirement
-----------

1. Minimum JDK 8
1. Kaazing JMS Edition 4.0
1. Gradle version 2.3 or use the included gradle wrapper

Installation
------------

1. Setup development environment. [Setting up Kaazing WebSocket Gateway](http://developer.kaazing.com/documentation/jms/4.0/about/setup-guide.html)
1. Define Kaazing WebSocket home directory `kaazingHomeDir` in root project *build.gradle*. Example:
     ```
     ext {
          kaazingHomeDir = '/home/xuwam/kaazing-websocket-gateway-jms-4.0.6'
     }
     ```
1. Make changes to the *conf/gateway-config.xml* for the JMS Service:
    1. Add accepted URL:
        ```
        <accept>ws://${gateway.hostname}:${gateway.extras.port}/jms</accept>
        ```
    1. Allow all Cross Site Origin:
        ```
        <cross-site-constraint>
            <allow-origin>*</allow-origin>
        </cross-site-constraint>
        ```
1. Run ActiveMQ
1. Run Kaazing WebSocket
1. In project root directory run `gradle :jsclient:deploy :jclient:run`
1. Open page [http://localhost:8000/jsclient/](http://localhost:8000/jsclient/)


Notes
-----

1. In this project we are assuming we are using the default *conf/gateway-config.xml* come pre-installed with the gateway.