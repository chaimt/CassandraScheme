# Cassandra Scheme


Table of Contents

* <a href="#motivations">Motivations</a>
* <a href="#configurations">Configurations</a>
    * <a href="#usage">Usage</a>
    * <a href="#parameters">Parameters</a>
* <a href="#contact">Contact</a>

<a name="motivations"></a>
## Motivations
Currently there is no tool that will help with the versioning of cassandra scheme. I was looking for a tool that is similar to 
flyway or liquidbase


<a name="configurations"></a>
## Configurations

<a name="usage"></a>
## Usage
To use the tool you need to create a directory will all your script files (files must end in cql).
The tool will save in a table in Cassandra which scripts where already run. So all you need to do is add your new scripts to the 
same dir, and the tool will run only the new scripts.
In the case that you have changed a script that was already run, you will get a warning.

Since this is a spring boot application you can run the application from the command line like:

```commandline
java -jar cassandra-scheme.jar --cassandra.keyspace=myspace --scheme.dir=/mydir
```

Another option is to create a application.properties file under a config dir with all the parameters set.

<a name="parameters"></a>
## Parameters
Mandatory parameters that need to be configured:

```properties
cassandra.contactpoints = [ip]
cassandra.keyspace = [name]
scheme.dir = [location]
```

Optional parameters
```properties
cassandra.port = 9042
cassandra.version.table = scheme_version
```

<a name="log"></a>
### Log
As part of spring boot the parameters for the log with the default values are:
logging.file=cassandra-scheme.log
logging.path=./log


<a name="contact"></a>
## Contact
### Contributors
* [Chaim Turkel](chaimturkel.wordpress.com) (Email: cyturel@gmail.com)