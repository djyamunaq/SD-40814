package clientSide.entities;

import java.util.Comparator;
import java.util.Random;

import clientSide.stubs.ContestantsBenchStub;
import clientSide.stubs.PlaygroundStub;
import genclass.GenericIO;

/**
 * Contestant thread class
 */
public class Contestant extends Thread {
    /**
     * Contestant original force
     */
    private int baseStrength;

    /**
     * Contestant identification
     */
    private int contestantId;

    /**
     * Contestant team
     */
    private int contestantTeam;

    /**
     * Contestant strength
     */
    private int strength;

    /**
     * contestant state
     */
    private int contestantState;

    /**
     * Reference to the playgeround
     */
    private final PlaygroundStub playground;

    /**
     * Reference to the contestant bench
     */
    private final ContestantsBenchStub contestantsBench;

    /**
     * Instantiation of a contestant thread
     *
     * @param name         thread name
     * @param contestantId contestant id
     * @param playground   reference to the playground
     */
    public Contestant(String name, int contestantId, int strength, PlaygroundStub playground,
            ContestantsBenchStub contestantsBench) {
        super(name);
        this.contestantId = contestantId;
        this.contestantState = ContestantStates.SEAT_AT_THE_BENCH;
        this.strength = strength;
        this.baseStrength = strength;
        this.playground = playground;
        this.contestantsBench = contestantsBench;
    }

    /**
     * Life cycle of the contestant
     */
    @Override
    public void run() {
        while (true) {
            /* Go to sleep waiting to be selected */
            this.contestantsBench.waitForCoachCall();

            /* Check if match ended */
            if (playground.matchEnded()) {
                break;
            }

            /* Check if selected for next trial */
            contestantsBench.followCoachAdvice();

            /* Get ready for trial */
            playground.getReady();

            /* Generate random number of iterations in interval [1, 5] */
            Random rand = new Random();
            int iterations = rand.nextInt(5) + 1;

            /* Pull rope */
            while (iterations-- > 0) {
                /* Check if contestant has strength */
                if (this.strength == 0) {
                    break;
                }

                playground.pullTheRope();
            }

            /* End work */
            playground.amDone();

            /* Go seat in bench */
            contestantsBench.seatDown();
        }
    }

    /**
     * Set contestant id
     *
     * @param id contestant id
     */
    public void setContestantId(int id) {
        this.contestantId = id;
    }

    /**
     * Get contestant id
     *
     * @return contestant id
     */
    public int getContestantId() {
        return this.contestantId;
    }

    /**
     * Set contestant id
     *
     * @param id contestant id
     */
    public void setContestantbaseStrength(int value) {
        this.baseStrength = value;
    }

    /**
     * Set contestant id
     *
     * @param id contestant id
     */
    public int getContestantbaseStrength() {
        return this.baseStrength;
    }

    /**
     * Set contestant team
     *
     * @param team contestant team
     */
    public void setcontestantTeam(int team) {
        this.contestantTeam = team;
    }

    /**
     * Get contestant team
     *
     * @return contestant team
     */
    public int getContestantTeam() {
        return this.contestantTeam;
    }

    /**
     * Set contestant state
     *
     * @param contestantState new contestant state
     */

    public void setContestantState(int contestantState) {
        this.contestantState = contestantState;
    }

    /**
     * Get contestant state
     *
     * @return contestant state
     */
    public int getContestantState() {
        return this.contestantState;
    }

    /**
     * Refresh contestant strengh to a percentage of the original
     */
    public void refreshContestantStrength() {
        if (this.strength < this.baseStrength) {
            this.baseStrength = (int) (this.baseStrength * 0.8);
            this.strength = this.baseStrength;
        }
    }

    /**
     * Get contestant strength
     * 
     * @return contestant strength
     */
    public int getContestantStrength() {
        return this.strength;
    }

    public void setContestantStrength(int val) {
        this.strength = val;
    }

    public int getCoachId() {
        return this.contestantId < 5 ? 0 : 1;
    }
}
