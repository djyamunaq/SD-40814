package clientSide.stubs;

import genclass.GenericIO;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import clientSide.entities.*;

/**
 * Stub to the referee site.
 *
 * It instantiates a remote reference to the referee site.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class RefereeSiteStub {
    /**
     * Name of the platform where is located the referee site server
     */
    private String serverHostName;

    /**
     * Port number for listening to service requests.
     */
    private int serverPortNumb;

    /**
     * Instantiation of a stub to the referee site stub
     *
     * @param serverHostName name of the platform where is located the referee site
     *                       server
     * @param serverPortNumb port number for listening to service requests
     */

    public RefereeSiteStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Send message to end match
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

        Referee referee =  (Referee) Thread.currentThread();
        
        outMessage = new Message(MessageType.ENDMATCH, GameConstants.TYPE_REFEREE, 0, referee.getRefereeState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.ENDMATCHDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        /* Update referee state */
        ((Referee) Thread.currentThread()).setRefereeState(inMessage.getRefereeState());

        com.close();
    }

    /**
     * Send message to make coach wait referee call trial
     * 
     * Called by Coach
     */
    public void waitRefereeCallTrial() {
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

        outMessage = new Message(MessageType.WAITREFEREECALLTRIAL, GameConstants.TYPE_COACH,
                coach.getCoachId(), coach.getCoachState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.WAITREFEREECALLTRIALDONE) {
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
     * Send message to make referee call trial
     * 
     * Calle by referee
     */
    public void callTrial() {
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

        Referee referee = (Referee) Thread.currentThread();

        outMessage = new Message(MessageType.CALLTRIAL, GameConstants.TYPE_REFEREE,
                0, referee.getRefereeState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.CALLTRIALDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify contestant state */
        if ((inMessage.getRefereeState() < RefereeStates.START_OF_THE_MATCH)
                || (inMessage.getRefereeState() > RefereeStates.END_OF_THE_MATCH)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid referee state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        /* Update contestant state */
        ((Referee) Thread.currentThread()).setRefereeState(inMessage.getRefereeState());
    }

    /**
     * Send message to inform referee team is ready
     * 
     * Called by coach
     */
    public synchronized void informReferee() {
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

        outMessage = new Message(MessageType.INFORMREFEREE, GameConstants.TYPE_COACH,
                coach.getCoachId(), coach.getCoachState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.INFORMREFEREEDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify contestant id */
        if (inMessage.getCoachId() != ((Coach) Thread.currentThread()).getCoachId()) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid coach id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify contestant state */
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
     * Send message to shutdown referee site server
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
