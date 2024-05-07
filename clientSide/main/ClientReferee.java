package clientSide.main;

import genclass.GenericIO;
import clientSide.entities.*;
import clientSide.stubs.*;

/**
 * Client referee class
 * 
 * Communicate with stubs to do referee stuff
 */
public class ClientReferee {
    public static void main(String[] args) {
        String refereeSiteServerHostName; // name of the platform where is located the referee site server
        int refereeSiteServerPortNumb = -1; // port number for listening to service requests
        String contestantsBenchServerHostName; // name of the platform where is located the contestants bench server
        int contestantsBenchServerPortNumb = -1; // port number for listening to service requests
        String playgroundServerHostName; // name of the platform where is located the playground server
        int playgroundServerPortNumb = -1; // port number for listening to service requests
        String genReposServerHostName; // name of the platform where is located the general repository server
        int genReposServerPortNumb = -1; // port number for listening to service requests

        /* getting problem runtime parameters */

        if (args.length != 8) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }
        refereeSiteServerHostName = args[0];
        try {
            refereeSiteServerPortNumb = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[1] is not a number!");
            System.exit(1);
        }
        if ((refereeSiteServerPortNumb < 4000) || (refereeSiteServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[1] is not a valid port number!");
            System.exit(1);
        }

        contestantsBenchServerHostName = args[2];
        try {
            contestantsBenchServerPortNumb = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[5] is not a number!");
            System.exit(1);
        }
        if ((contestantsBenchServerPortNumb < 4000) || (contestantsBenchServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[5] is not a valid port number!");
            System.exit(1);
        }

        playgroundServerHostName = args[4];
        try {
            playgroundServerPortNumb = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[7] is not a number!");
            System.exit(1);
        }
        if ((playgroundServerPortNumb < 4000) || (playgroundServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[7] is not a valid port number!");
            System.exit(1);
        }

        genReposServerHostName = args[6];
        try {
            genReposServerPortNumb = Integer.parseInt(args[7]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[9] is not a number!");
            System.exit(1);
        }
        if ((genReposServerPortNumb < 4000) || (genReposServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[9] is not a valid port number!");
            System.exit(1);
        }

        /* problem initialization */

        RefereeSiteStub refereeSiteStub = new RefereeSiteStub(refereeSiteServerHostName, refereeSiteServerPortNumb);
        ContestantsBenchStub contestantsBenchStub = new ContestantsBenchStub(contestantsBenchServerHostName,
                contestantsBenchServerPortNumb);
        PlaygroundStub playgroundStub = new PlaygroundStub(playgroundServerHostName, playgroundServerPortNumb);
        GeneralReposStub genReposStub = new GeneralReposStub(genReposServerHostName, genReposServerPortNumb);

        Referee referee = new Referee("Ref", playgroundStub, contestantsBenchStub, refereeSiteStub);

        /* start of the simulation */

        referee.start();

        /* waiting for the end of the simulation */

        GenericIO.writelnString();
        try {
            referee.join();
        } catch (InterruptedException e) {
        }
        GenericIO.writelnString("The referee has terminated.");
        GenericIO.writelnString();

    }
}
