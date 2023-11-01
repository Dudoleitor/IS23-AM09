# Software Engineering project - _a.y. 2022-23_
![Box image](https://www.craniocreations.it/storage/media/products/54/112/My_Shelfie_box_ITA-ENG.png)  

## Project task
Implementation of [My Shelfie](https://www.craniocreations.it/prodotto/my-shelfie), a board game created by Cranio Creations ([Rulebook](https://github.com/Dudoleitor/IS23-AM09/blob/master/MyShelfie_Rulebook_ENG.pdf)).

The project task is to implement in Java a distributed system composed of a single server and multiple clients (one per player) that simulates the board game My Shelfie.  
Clients can interact with the server both via CLI and GUI, JavaFX is used for the latter. The network layer is implemented both using sockets and Java RMI.

### Documentation and tests coverage
Inside the [deliverables](https://github.com/Dudoleitor/IS23-AM09/tree/master/deliverables) folder there are UML diagrams and pdf documents to show the structure of the code and explain each design choice. Classes, interfaces and methods are well described using [javadoc](https://htmlpreview.github.io/?https://github.com/Dudoleitor/IS23-AM09/blob/master/deliverables/HTMLjavadoc/index.html).
Each aspect of the software was tested using the framework JUnit, the [test coverage report](https://htmlpreview.github.io/?https://github.com/Dudoleitor/IS23-AM09/blob/master/deliverables/HTMLCoverage/index.html) is also provided.

## Implemented features
| Feature | Status | Description |
| --- | :---: | --- |
| Complete game | :green_circle: ||
| RMI | :green_circle: | Network layer using RMI |
| Socket | :green_circle: | Network layer using sockets |
| CLI | :green_circle: | Command line interface, on clients |
| GUI | :green_circle: | JavaFX on clients |
| Multiple matches | :green_circle: | The server supports multiple matches at the same time |
| Persistence | :green_circle: | The status of each match is saved on the server and can be restored |
| Disconnection handling | :green_circle: | If a client crashes, the match continues skipping its turns; clients can reconnect |
| Chat | :green_circle: | Messages between players during the match |

## How to run
Download the jar archive _AM09-MyShelfie_ from assets related to the [latest release](https://github.com/Dudoleitor/IS23-AM09/releases), you need to have java 11 installed (or newer). The application has to be started in a terminal with the following command, an interactive cli menu will guide you through the configuration process:
```
java -jar AM09-MyShelfie.jar
```
If you prefer to specify (some or all) options as command line arguments, you can use the following syntax. 

To start the server:
```
java -jar AM09-MyShelfie.jar --server --ip <server_ip> --tcp <socket_port> --rmi <rmi_port>
```
To start the client:
```
java -jar AM09-MyShelfie.jar --client --ip <server_ip>
```
then you can choose between CLI and GUI with ```--cli``` and ```--gui``` options, between RMI and Socket with ```--rmi <port>``` and ```--tcp <port>```.

### Note about RMI interface selection, on client
RMI should be able to choose the correct network interface automatically with its relative IP, but if you encounter problems connecting to a server with an RMI client you can set the environment variable ```RMI_LOCAL_IP``` to the correct IP address.
Use ```export RMI_LOCAL_IP=<ip_address>``` on Linux and MacOS, ```set RMI_LOCAL_IP=<ip_address>``` in a Windows cmd terminal, ```$env:RMI_LOCAL_IP = '<ip_address>'``` in PowerShell.

### Note about game saves, on server
The server saves the status of each game running at the end of every turn, it uses a json file to store the information. The file is located and looked for in the current working directory, please be sure to start the server from a directory containing the files related to the games you intend to restore.

## Team members
- __Adorni Michele__ _@Mik-Ado_ michele.adorni@mail.polimi.it
- __Brunetta Giacomo__ _@giacomo-brunetta_ giacomo.brunetta@mail.polimi.it
- __Cantonetti Gianluca__ _@CantonettiGianluca27_ gianluca.cantonetti@mail.polimi.it
- __Carlotto Edoardo__ _@Dudoleitor_ edoardo.carlotto@mail.polimi.it

## Copyright disclaimer
My Shelfie is property of Cranio Creations and all of the copyrighted graphical assets used in this project were supplied by Politecnico di Milano in collaboration with their rights' holders.
