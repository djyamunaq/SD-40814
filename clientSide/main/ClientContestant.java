package clientSide.main;

import java.util.Random;
import genclass.GenericIO;
import clientSide.entities.Contestant;
import clientSide.entities.ContestantStates;
import clientSide.entities.GameConstants;
import clientSide.stubs.ContestantsBenchStub;
import clientSide.stubs.GeneralReposStub;
import clientSide.stubs.PlaygroundStub;
import clientSide.stubs.RefereeSiteStub;

public class ClientContestant {
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
        ContestantsBenchStub contestantsBenchStub = new ContestantsBenchStub(contestantsBenchServerHostName, contestantsBenchServerPortNumb);       
        PlaygroundStub playgroundStub = new PlaygroundStub(playgroundServerHostName, playgroundServerPortNumb);       
        GeneralReposStub genReposStub = new GeneralReposStub(genReposServerHostName, genReposServerPortNumb);

        Contestant[] contestants = new Contestant[10];

        for (int i = 0; i < 10; i++) {
            /* Set random strength in range [1, 5] */
            Random rand = new Random();
            int strength = rand.nextInt(GameConstants.MAX_STRENGTH) + 1;

            contestants[i] = new Contestant("Cont_" + (i + 1), i, strength, playgroundStub, contestantsBenchStub);
            contestants[i].setContestantState(ContestantStates.SEAT_AT_THE_BENCH);

            /* Register contestant strength */
            genReposStub.setContestantStrength(i, strength);

            /* Set contestant id */
            contestants[i].setContestantId(i);

            /* Select contestant team */
            int team = (i < 5) ? 0 : 1;
            contestants[i].setcontestantTeam(team);
        }

        /* start of the simulation */
        
        for (int i = 0; i < 10; i++) {
            contestants[i].start();
        }

        /* waiting for the end of the simulation */

        GenericIO.writelnString();
        for (int i = 0; i < 10; i++) {
            try {
                contestants[i].join();
            } catch (InterruptedException e) {
                GenericIO.writelnString("Error running contestant " + (i + 1) + " thread");
            }
            GenericIO.writelnString("The contestant " + (i + 1) + " has terminated.");
        }

        /* Shutdown servers */
        // refereeSiteStub.shutdown();
        // contestantsBenchStub.shutdown();
        // playgroundStub.shutdown();
        // genReposStub.shutdown();
    }
}
