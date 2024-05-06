package serverSide.entities;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.sharedRegions.ContestantsBenchInterface;

public class ContestantsBenchClientProxy extends Thread {
    /**
     * Number of instantiayed threads.
     */

    private static int nProxy = 0;

    /**
     * Communication channel.
     */

    private ServerCom sconi;

    /**
     * Interface to the coach Shop.
     */

    private ContestantsBenchInterface contestantsBenchInterface;

    /**
     * Coach identification.
     */

    private int coachId;

    /**
     * Coach state.
     */

    private int coachState;

    /**
     * contestant identification.
     */

    private int contestantId;

    /**
     * contestant state.
     */
    private int contestantState;

    private int contestantStrength;

    private int contestantRegister[];

    /**
     * Instantiation of a client proxy.
     *
     * @param sconi                     communication channel
     * @param ContestantsBenchInterface interface to the coach shop
     */

    public ContestantsBenchClientProxy(ServerCom sconi, ContestantsBenchInterface contestantsBenchInterface) {
        super("ContestantsBenchClientProxy_" + ContestantsBenchClientProxy.getProxyId());
        this.sconi = sconi;
        this.contestantsBenchInterface = contestantsBenchInterface;

        this.contestantRegister = new int[10];
    }

    /**
     * Generation of the instantiation identifier.
     *
     * @return instantiation identifier
     */

    private static int getProxyId() {
        Class<?> cl = null; // representation of the ContestantsBenchClientProxy object in JVM
        int proxyId; // instantiation identifier

        try {
            cl = Class.forName("serverSide.entities.ContestantsBenchClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type ContestantsBenchClientProxy was not found!");
            e.printStackTrace();
            System.exit(1);
        }
        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    /**
     * Set coach id.
     *
     * @param id coach id
     */

    public void setCoachId(int id) {
        coachId = id;
    }

    /**
     * Get coach id.
     *
     * @return coach id
     */

    public int getCoachId() {
        return coachId;
    }

    /**
     * Set coach state.
     *
     * @param state new coach state
     */

    public void setCoachState(int state) {
        coachState = state;
    }

    /**
     * Get coach state.
     *
     * @return coach state
     */

    public int getCoachState() {
        return coachState;
    }

    /**
     * Set contestant id.
     *
     * @param id contestant id
     */

    public void setContestantId(int id) {
        contestantId = id;
    }

    /**
     * Get contestant id.
     *
     * @return contestant id
     */

    public int getContestantId() {
        return contestantId;
    }

    /**
     * Set contestant state.
     *
     * @param state new contestant state
     */

    public void setContestantState(int state) {
        this.contestantState = state;
    }

    /**
     * Get contestant state.
     *
     * @return contestant state
     */

    public int getContestantState() {
        return this.contestantState;
    }
    
    public int[] getContestantRegister() {
        return this.contestantRegister;
    }

    /**
     * Get contestant state.
     *
     * @return contestant state
     */

    public int getContestantStrength() {
        return this.contestantStrength;
    }

    public void setContestantStrength(int val) {
        this.contestantStrength = val;
    }

    /**
     * Life cycle of the service provider agent.
     */

    @Override
    public void run() {
        Message inMessage = null, // service request
                outMessage = null; // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject(); // get service request
        try {
            outMessage = contestantsBenchInterface.processAndReply(inMessage); // process it
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage); // send service reply
        sconi.close(); // close the communication channel
    }
}
