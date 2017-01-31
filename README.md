ASGconfiggen
------------

ASGconfiggen generates configuration file for the ASGtools.

### Installation ###

Download and unpack the ASGconfiggen package. You don't have to install anything or make changes to environment variables. To run it you will need a Java runtime environment (JRE) v1.7 (or later).

### Usage ###

For the following example command it is assumed that your current working directory is the ASGconfiggen main directory. If you want run ASGconfigge from another directory you have to add the path to the ASGconfigge main directory in front of the following commands (or you could add the `bin/` directory to your `PATH` variable).

    bin/ASGconfiggen_gui

### Build instructions ###

To build ASGtechmngr, Apache Maven v3 (or later) and the Java Development Kit (JDK) v1.7 (or later) are required.

1. Execute `mvn clean install -DskipTests`
