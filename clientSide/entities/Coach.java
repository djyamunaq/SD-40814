package clientSide.entities;

import clientSide.stubs.ContestantsBenchStub;
import clientSide.stubs.PlaygroundStub;
import clientSide.stubs.RefereeSiteStub;

/**
 * Coach thread class
 */
public class Coach extends Thread {
    /**
     * coach identification
     */
    private int coachId;

    /**
     * Coach state
     */
    private int coachState;

    /**
     * Reference to the playground
     */
    private final PlaygroundStub playground;

    /**
     * Reference to the contestant bench
     */
    private final ContestantsBenchStub contestantsBench;

    /**
     * Reference to the referee site
     */
    private final RefereeSiteStub refereeSite;

    /**
     * Instantiation of a coach thread
     *
     * @param name             thread name
     * @param coachId          coach id
     * @param playground       reference to the playground
     * @param contestantsBench reference to the contestant bench
     * @param refereeSite      reference to the referee site
     */
    public Coach(String name, int coachId, PlaygroundStub playground, ContestantsBenchStub contestantsBench,
            RefereeSiteStub refereeSite) {
        super(name);
        this.coachId = coachId;
        this.coachState = CoachStates.WAIT_FOR_REFEREE_COMMAND;
        this.playground = playground;
        this.contestantsBench = contestantsBench;
        this.refereeSite = refereeSite;
    }

    /**
     * Set coach id
     *
     * @param id coach id
     */
    public void setCoachId(int id) {
        this.coachId = id;
    }

    /**
     * Get coach id
     *
     * @return coach id
     */
    public int getCoachId() {
        return this.coachId;
    }

    /**
     * Set coach state
     *
     * @param coachState new coach state
     */
    public void setCoachState(int coachState) {
        this.coachState = coachState;
    }

    /**
     * Get coach state
     *
     * @return coach state
     */
    public int getCoachState() {
        return this.coachState;
    }

    /**
     * Life cycle of the coach
     */
    @Override
    public void run() {
        while (true) {
            /* Wait for referee to call trial */
            this.refereeSite.waitRefereeCallTrial();

            /* Check if match ended */
            if (playground.matchEnded()) {
                break;
            }

            /* Call contestants to new trial */
            this.contestantsBench.callContestants();

            /* Inform referee team is assembled */
            this.refereeSite.informReferee();

            /* Wait for trial end */
            this.playground.reviewNotes();
        }
    }
}
