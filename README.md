# __Progetto__ di __Ingegneria__ del __Software__ _aa 2022-23_
![Immagine Scatola](https://www.craniocreations.it/storage/media/products/54/112/My_Shelfie_box_ITA-ENG.png)  
## Project Task
Implementation of [My Shelfie](https://www.craniocreations.it/prodotto/my-shelfie), a board game created by Cranio Creations.

The project task is to create a distributed system composed by a single server and multiple clients (one per player) that simulates the board game My Shelfies.  
Clients can interact with the server both via CLI and GUI.

## Features status
| Feature | Status |
| --- | :---: |
| Complete game | :green_circle: |
| RMI | :green_circle: |
| Socket | :green_circle: |
| TUI | :green_circle: |
| GUI | :green_circle: |
| Multiple matches | :green_circle: |
| Persistence | :green_circle: |
| Disconnection handling | :green_circle: |
| Chat | :green_circle: |

## How to run
Download the jar archive _AM09-MyShelfie_ from assets related to the [latest release](https://github.com/Dudoleitor/IS23-AM09/releases), the application has to be started in a terminal with the following command, an interactive cli menu will guide you through the configuration process:
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
