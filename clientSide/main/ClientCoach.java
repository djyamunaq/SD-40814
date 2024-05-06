package clientSide.main;

import genclass.GenericIO;
import clientSide.entities.Coach;
import clientSide.entities.CoachStates;
import clientSide.stubs.ContestantsBenchStub;
import clientSide.stubs.GeneralReposStub;
import clientSide.stubs.PlaygroundStub;
import clientSide.stubs.RefereeSiteStub;

public class ClientCoach {
    public static void main(String[] args) {
        String refereeSiteServerHostName; // name of the platform where is located the barber shop server
        int refereeSiteServerPortNumb = -1; // port number for listening to service requests
        String contestantsBenchServerHostName; // name of the platform where is located the barber shop server
        int contestantsBenchServerPortNumb = -1; // port number for listening to service requests
        String playgroundServerHostName; // name of the platform where is located the barber shop server
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

        Coach[] coaches = new Coach[2];

        for (int i = 0; i < 2; i++) {
            coaches[i] = new Coach("Coach_" + (i + 1), i, playgroundStub, contestantsBenchStub, refereeSiteStub);
            coaches[i].setCoachId(i);
            coaches[i].setCoachState(CoachStates.WAIT_FOR_REFEREE_COMMAND);
        }

        /* start of the simulation */

        for (int i = 0; i < 2; i++) {
            coaches[i].start();
        }

        /* waiting for the end of the simulation */

        GenericIO.writelnString();
        for (int i = 0; i < 2; i++) {
            try {
                coaches[i].join();
            } catch (InterruptedException e) {
                GenericIO.writelnString("Error running coach " + (i + 1) + " thread");
            }
            GenericIO.writelnString("The coach " + (i + 1) + " has terminated.");
        }

        /* Shutdown servers */
        // refereeSiteStub.shutdown();
        // contestantsBenchStub.shutdown();
        // playgroundStub.shutdown();
        // genReposStub.shutdown();
    }
}
