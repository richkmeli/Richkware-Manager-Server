# Richkware-Manager-Server
[![Build Status](https://travis-ci.org/richkmeli/Richkware-Manager-Server.svg?branch=master)](https://travis-ci.org/richkmeli/Richkware-Manager-Server)
[![](https://jitpack.io/v/richkmeli/Richkware-Manager-Server.svg)](https://jitpack.io/#richkmeli/Richkware-Manager-Server)


Service for the management of hosts in which is present an instance of malware developed using **Richkware** framework.

![](https://raw.githubusercontent.com/richkmeli/richkmeli.github.io/master/Richkware/GUI/RMS/RMS.png)

## Implementation

RMS has been developed following the REST principles; the following table shows which HTTP methods have been used for each servlet.

|  HTTP methods  | GET | POST | PUT | DELETE |
|--------------|:----:|:---:|:---:|:------:|
| device | x | | x | x |
| user | x | | | x |
| devices | x | | | x |
| users | x | | | |
| encryptionKey | x | | | |


## Related Projects

[Richkware](https://github.com/richkmeli/Richkware): Framework for building Windows malware.

[Richkware-Manager-Client](https://github.com/richkmeli/Richkware-Manager-Client): Client of **Richkware-Manager-Server**, that it obtains the list of all hosts from the server and it's able to send any kind of commands to them.

![](https://raw.githubusercontent.com/richkmeli/richkmeli.github.io/master/Richkware/Diagram/RichkwareDiagram1.2.png)

## Requirements
These are the base requirements to build and use Richkware:

-   Java 1.8 or higher
-   MySQL

## Get Started

Open the configuration file (/src/main/resources/configuration.properties) and set the parameters inside it. In particular:

- __database.url__: address of the database, RMS supports MySQL, if you want to use another one, it may not work. (default: jdbc:mysql://db:3306/)
- __database.username__: username used to access to the database (default: root)
- __database.password__: password used to access to the database (default: richk)
- __encryptionkey__: encryption key used to exchange message to Richkware and RMC. if you change this parameter, remember to change also the configurations in Richkware and RMC (default: richktest)

now we can build the "war" file, executing the following command:
    
    mvn package

then you can deploy RMS using __docker-compose__

    docker-compose up

finally, you can open [RMS](http://0.0.0.0:8080/Richkware-Manager-Server/).