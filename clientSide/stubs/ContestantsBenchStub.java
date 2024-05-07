package clientSide.stubs;

import genclass.GenericIO;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import clientSide.entities.*;

/**
 * Stub to the contestants bench stub.
 *
 * It instantiates a remote reference to the contestants stub.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class ContestantsBenchStub {
    /**
     * Name of the platform where is located the contestant bench server
     */
    private String serverHostName;

    /**
     * Port number for listening to service requests.
     */
    private int serverPortNumb;

    /**
     * Instantiation of a stub to the contestants bench stub
     *
     * @param serverHostName name of the platform where is located the contestants
     *                       bench
     *                       server
     * @param serverPortNumb port number for listening to service requests
     */
    public ContestantsBenchStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Send message to make contestant wait for call from coach
     * 
     * Called by contestant
     */
    public void waitForCoachCall() {
        /* Communication channel */
        ClientCom com;

        /* Output and input messages */
        Message outMessage, inMessage;

        /* Established connection */
        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        Contestant contestant = (Contestant) Thread.currentThread();

        outMessage = new Message(MessageType.WAITFORCOACHCALL, GameConstants.TYPE_CONTESTANT,
                contestant.getContestantId(), contestant.getContestantState(), contestant.getContestantStrength());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.WAITFORCOACHCALLDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify contestant id */
        if (inMessage.getContestantId() != ((Contestant) Thread.currentThread()).getContestantId()) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid contestant id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify contestant state */
        if ((inMessage.getContestantState() < ContestantStates.SEAT_AT_THE_BENCH)
                || (inMessage.getContestantState() > ContestantStates.DO_YOUR_BEST)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid contestant state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify contestant strentgh */
        if ((inMessage.getContestantStrength() < 0)
                || (inMessage.getContestantStrength() > GameConstants.MAX_STRENGTH)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid contestant strength!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        /* Update contestant state */
        ((Contestant) Thread.currentThread()).setContestantState(inMessage.getContestantState());
        ((Contestant) Thread.currentThread()).setContestantStrength(inMessage.getContestantStrength());
    }

    /**
     * Send message to make contestant stay in position if selected by coach
     * 
     * Called by contestant
     */
    public void followCoachAdvice() {
        /* Communication channel */
        ClientCom com;

        /* Output and input messages */
        Message outMessage, inMessage;

        /* Established connection */
        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        Contestant contestant = (Contestant) Thread.currentThread();

        outMessage = new Message(MessageType.FOLLOWCOACHADVICE, GameConstants.TYPE_CONTESTANT,
                contestant.getContestantId(), contestant.getContestantState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.FOLLOWCOACHADVICEDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify contestant id */
        if (inMessage.getContestantId() != ((Contestant) Thread.currentThread()).getContestantId()) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid contestant id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify contestant state */
        if ((inMessage.getContestantState() < ContestantStates.SEAT_AT_THE_BENCH)
                || (inMessage.getContestantState() > ContestantStates.DO_YOUR_BEST)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid contestant state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        /* Update contestant state */
        ((Contestant) Thread.currentThread()).setContestantState(inMessage.getContestantState());
    }

    /**
     * Send message to make contestant go back to bench
     * 
     * Called by contestant
     */
    public void seatDown() {
        /* Communication channel */
        ClientCom com;

        /* Output and input messages */
        Message outMessage, inMessage;

        /* Established connection */
        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        /* Get current contestant thread */
        Contestant contestant = (Contestant) Thread.currentThread();

        outMessage = new Message(MessageType.SEATDOWN, GameConstants.TYPE_CONTESTANT,
                contestant.getContestantId(), contestant.getContestantState(), contestant.getContestantStrength());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.SEATDOWNDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify contestant id */
        if (inMessage.getContestantId() != ((Contestant) Thread.currentThread()).getContestantId()) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid contestant id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify contestant state */
        if ((inMessage.getContestantState() < ContestantStates.SEAT_AT_THE_BENCH)
                || (inMessage.getContestantState() > ContestantStates.DO_YOUR_BEST)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid contestant state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify contestant strentgh */
        if ((inMessage.getContestantStrength() < 0)
                || (inMessage.getContestantStrength() > GameConstants.MAX_STRENGTH)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid contestant strength!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        /* Update contestant state */
        ((Contestant) Thread.currentThread()).setContestantState(inMessage.getContestantState());
        ((Contestant) Thread.currentThread()).setContestantStrength(inMessage.getContestantStrength());
    }

    /**
     * Send message to call contestants to trial
     * 
     * Called by coach
     */
    public void callContestants() {
        /* Communication channel */
        ClientCom com;

        /* Output and input messages */
        Message outMessage, inMessage;

        /* Established connection */
        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        Coach coach = (Coach) Thread.currentThread();

        outMessage = new Message(MessageType.CALLCONTESTANTS, GameConstants.TYPE_COACH,
                coach.getCoachId(), coach.getCoachState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.CALLCONTESTANTSDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify coach id */
        if (inMessage.getCoachId() != ((Coach) Thread.currentThread()).getCoachId()) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid coach id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify coach state */
        if ((inMessage.getCoachState() < CoachStates.WAIT_FOR_REFEREE_COMMAND)
                || (inMessage.getCoachState() > CoachStates.WATCH_TRIAL)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid coach state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        /* Update contestant state */
        ((Coach) Thread.currentThread()).setCoachState(inMessage.getCoachState());
    }

    /**
     * Send message to tell contestants bench that match ended
     * 
     * Called by referee
     */
    public void endMatch() {
        /* Communication channel */
        ClientCom com;

        /* Output and input messages */
        Message outMessage, inMessage;

        /* Established connection */
        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.ENDMATCH);

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.ENDMATCHDONE) {

            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Send message to shutdown contestants bench server
     */
    public void shutdown() {
        ClientCom com; // communication channel
        Message outMessage, // outgoing message
                inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.SHUT);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SHUTDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }
}
