package serverSide.main;

import java.net.SocketTimeoutException;

import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.entities.*;
import serverSide.sharedRegions.*;
import clientSide.*;

public class ServerGeneralRepos {
    public static boolean waitConnection;

    public static void main(String[] args) {
        GeneralRepos repos; // stub to the general repository
        GeneralReposInterface reposInterface; // stub to the general repository
        ServerCom scon, sconi; // communication channels
        int portNumb = -1; // port number for listening to service requests

        if (args.length != 1) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }
        try {
            portNumb = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[0] is not a number!");
            System.exit(1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536)) {
            GenericIO.writelnString("args[0] is not a valid port number!");
            System.exit(1);
        }
        /* service is established */

        String fileName = "logger";
        repos = new GeneralRepos(fileName); // service is instantiated
        reposInterface = new GeneralReposInterface(repos); // interface to the service is instantiated
        scon = new ServerCom(portNumb); // listening channel at the public port is established
        scon.start();
        GenericIO.writelnString("Service is established!");
        GenericIO.writelnString("Server is listening for service requests.");

        /* service request processing */

        GeneralReposClientProxy cliProxy; // service provider agent

        waitConnection = true;
        while (waitConnection) {
            try {
                sconi = scon.accept(); // enter listening procedure
                cliProxy = new GeneralReposClientProxy(sconi, reposInterface); // start a service provider agent to
                                                                               // address
                cliProxy.start(); // the request of service
            } catch (SocketTimeoutException e) {
            }
        }
        scon.end(); // operations termination
        GenericIO.writelnString("Server was shutdown.");
    }
}
