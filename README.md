# Cassandra Sceheme


Table of Contents

* <a href="#currentStatus">Current Status</a>
* <a href="#motivations">Motivations</a>
* <a href="#configurations">Configurations</a>
    * <a href="#port">Port</a>
    * <a href="#zookeeper">Zookeeper</a>
    * <a href="#log">Log</a>
* <a href="#contact">Contact</a>

<a name="currentStatus"></a>
## Current Status
Kafa is a very good message platform. What is lacks is a simple monitoring tools to let me know what is going on.
There is [confluent](https://www.confluent.io/) but i costs money.
The other utils that I have used are:

### [kafka-manager](https://github.com/yahoo/kafka-manager) 
This utilitiy is very nice but only supports kafak up to version 0.9 

### [Trifecta](https://github.com/ldaniels528/trifecta)
This utility is very good, but mainly allows to view the zookeeper tree, and messages in broker.
There is no diagnostics utilites 

<a name="motivations"></a>
## Motivations
Currently there is no tool that will help with the versioning of cassandra scheme. I was looking for a tool that is simular to 
flyway or liquidbase

<a name="configurations"></a>
## Configurations
<a name="port"></a>
### Port
The application is a Spring boot application writen in scala on top of akka.
The default port is 7000, but change be changed by the command line --server.port=7000
<a name="zookeeper"></a>
### Zookeeper
The default value for the zookeeper is localhost:2181. This parameter can be changed with the command line for : --zookeeper.connections
In the case of multiple zookeepers, you add all of them together. For examples:
zookeeper.connections=hosta:2181,hostb:2181,hostc:2181
<a name="log"></a>
### Log
As part of spring boot the parameters for the log with the default values are:
logging.file=zookeeper-kafka-monitor.log
logging.path=./log


<a name="contact"></a>
## Contact
### Contributors
* [Chaim Turkel](chaimturkel.wordpress.com) (Email: cyturel@gmail.com)