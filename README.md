# Bitrepository client to get salted checksums

This project contains the code for a [Bitrepository client](https://github.com/bitrepository/reference/tree/master/bitrepository-client) to 
retrieve salted checksums from a bitrepository and compare these to checksums stored locally. The client also contains a GUI to perform the 
necessary operations. The program facilitates functionality to:

* Specify the necessary repository settings (and load and save these)
* Provide a list of files with known checksums
* Retrieve multiple salted checksums (for the files in the above list) and compare these to the expected values
* Save the resulting checksum list and comparisons of these

## Design and development

The backend code was developed using a compositional design. Test-driven development (TDD) was use throughout the project to develop the code. 
A great reference addressing these techniques can be found [here](https://www.crcpress.com/Flexible-Reliable-Software-Using-Patterns-and-Agile-Development/Christensen/p/book/9781420093629)

The GUI part of the code was created in Swing using the WindowsBuilder plugin for Eclipse, i.e. a lot of the code in [Main.java](https://github.com/andreaskring/bitmagasinet-gui/blob/master/src/main/java/dk/magenta/bitmagasinet/gui/Main.java) was auto-generated by WindowsBuilder.

## Installation

In order to build the project you will need to install Java (version 1.8) and [Maven](http://maven.apache.org/) (version 3.3.9 or above). 
Git will of course also be needed. Do the following to install the project:

* Clone this project and change directory to the folder containing the project
* (Run the tests with `$ mvn clean test`)
* Build the project with `$ mvn clean package`

This will build an executable jar and a library folder containing all necessary dependencies in the `target/` folder. The program can be run by just double-clicking the jar file or by running the command `$ java -jar target/bitmagasin-gui.jar` (doing it the latter way will enable you to see the Java log in the terminal).

## Releases

A zip file containing the bitmagasin-gui.jar file and the necessary dependencies (in the library folder called `lib`) can be downloaded 
by clicking the "Release" tab in the menu above.

## Contact
[Andreas Kring](https://github.com/andreaskring)@[Magenta Aps](http://www.magenta.dk)
