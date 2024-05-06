package clientSide.stubs;

import genclass.GenericIO;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import clientSide.entities.*;

public class PlaygroundStub {
    /**
     * Name of the platform where is located the barber shop server.
     */
    private String serverHostName;

    /**
     * Port number for listening to service requests.
     */
    private int serverPortNumb;

    /**
     * Instantiation of a stub to the playground
     *
     * @param serverHostName name of the platform where is located the playground
     *                       server
     * @param serverPortNumb port number for listening to service requests
     */
    public PlaygroundStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Check if match ended
     * 
     * @return true if match ended, false otherwise
     */
    public boolean matchEnded() {
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

        outMessage = new Message(MessageType.MATCHENDED);

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.MATCHENDEDDONE && inMessage.getMsgType() != MessageType.NONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return (inMessage.getMsgType() == MessageType.MATCHENDEDDONE);
    }

    /**
     * Check if game ended
     * 
     * @return true if game ended, false otherwise
     */
    public boolean gameEnded() {
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

        outMessage = new Message(MessageType.GAMEENDED);

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.GAMEENDEDDONE && inMessage.getMsgType() != MessageType.NONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return (inMessage.getMsgType() == MessageType.GAMEENDEDDONE);
    }

    /**
     * Contestant done pulling rope
     * 
     * Called by contestant: unblock referee to assert trial and then
     * wait for referee signal that trial is asserted
     */
    public void amDone() {
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

        outMessage = new Message(MessageType.AMDONE);

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.AMDONEDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Contestant get ready to trial
     * 
     * Called by contestant: wait for referee command to start trial,
     * then go to state DO_YOUR_BEST
     */
    public void getReady() {
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

        outMessage = new Message(MessageType.GETREADY, GameConstants.TYPE_CONTESTANT,
                contestant.getContestantId(), contestant.getContestantState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.GETREADYDONE) {
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
     * Pull the rope
     * 
     * Called by contestant: modify rope state (Add 1 if in team 1, sub 1 if in team
     * 2)
     */
    public synchronized void pullTheRope() {
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

        outMessage = new Message(MessageType.PULLTHEROPE, GameConstants.TYPE_CONTESTANT,
                contestant.getContestantId(), contestant.getContestantState(), contestant.getContestantStrength());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.PULLTHEROPEDONE) {
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
     * Start a new trial
     * 
     * Called by referee: wait for teams ready signal,
     * then go to WAIT_FOR_TRIAL_CONCLUSION state
     * and unblock participants to start trial
     */
    public void startTrial() {
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

        outMessage = new Message(MessageType.STARTTRIAL, GameConstants.TYPE_REFEREE,
                0, referee.getRefereeState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.STARTTRIALDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify referee state */
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
     * Assert trial decision
     * 
     * Called by referee: wait for contestant done signal,
     * then assert decision over trial result and unblock
     * coaches and contestants
     * 
     * @return true if still asserting trial, false otherwise
     */
    public boolean assertTrialDecision() {
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

        outMessage = new Message(MessageType.ASSERTTRIALDECISION);

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.ASSERTTRIALDECISIONDONE
                && inMessage.getMsgType() != MessageType.NONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return (inMessage.getMsgType() == MessageType.ASSERTTRIALDECISIONDONE);
    }

    /**
     * Announce new game
     * 
     * Called by referee: set game variables and go to
     * START_OF_A_GAME state
     */
    public void announceNewGame() {
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

        outMessage = new Message(MessageType.ANNOUNCENEWGAME, GameConstants.TYPE_REFEREE,
                0, referee.getRefereeState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.ANNOUNCENEWGAMEDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify referee state */
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
     * Declare game winner
     * 
     * Called by referee: go to END_OF_A_GAME state,
     * then decide and declare game winner
     */
    public synchronized void declareGameWinner() {
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

        outMessage = new Message(MessageType.DECLAREGAMEWINNER, GameConstants.TYPE_REFEREE,
                0, referee.getRefereeState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.DECLAREGAMEWINNERDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify referee state */
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
     * Declare match winner
     * 
     * Called by referee: go to END_OF_THE_MATCH state
     * and unblock possibly blocked contestants
     */
    public synchronized void declareMatchWinner() {
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

        outMessage = new Message(MessageType.DECLAREMATCHWINNER, GameConstants.TYPE_REFEREE,
                0, referee.getRefereeState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.DECLAREMATCHWINNERDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        /* Verify referee state */
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
     * Review notes (Coach sleep)
     * 
     * Called by coach to wait referee assert trial and go to
     * WAIT_FOR_REFEREE_COMMAND state
     */
    public synchronized void reviewNotes() {
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

        outMessage = new Message(MessageType.REVIEWNOTES, GameConstants.TYPE_COACH,
                coach.getCoachId(), coach.getCoachState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        /* Verify message type */
        if (inMessage.getMsgType() != MessageType.REVIEWNOTESDONE) {
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
