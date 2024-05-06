package clientSide.entities;

import clientSide.stubs.ContestantsBenchStub;
import clientSide.stubs.PlaygroundStub;
import clientSide.stubs.RefereeSiteStub;
import genclass.GenericIO;

/**
 * Referee thread class
 */
public class Referee extends Thread {
    /**
     * Referee state
     */
    private int refereeState;

    /**
     * Reference to the playgeround
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
     * Instantiation of a referee thread
     *
     * @param name       thread name
     * @param playground reference to the playground
     */
    public Referee(String name, PlaygroundStub playground, ContestantsBenchStub contestantsBench, RefereeSiteStub refereeSite) {
        super(name);
        this.refereeState = RefereeStates.START_OF_THE_MATCH;
        this.playground = playground;
        this.contestantsBench = contestantsBench;
        this.refereeSite = refereeSite;
    }

    /**
     * Set referee state
     *
     * @param refereeState new referee state
     */
    public void setRefereeState(int refereeState) {
        this.refereeState = refereeState;
    }

    /**
     * Get referee state
     *
     * @return referee state
     */
    public int getRefereeState() {
        return this.refereeState;
    }

    /**
     * Life cycle of the referee
     */
    @Override
    public void run() {
        while (true) {
            /* Announce new game to coach and contestants */
            playground.announceNewGame();

            /* Announce trials while decision not asserted */
            do {
                /* Call contestants and coach to new trial */
                refereeSite.callTrial();

                /* Start new trial */
                playground.startTrial();
            } while (playground.assertTrialDecision());

            /* Declare game winner */
            playground.declareGameWinner();

            /* Check if match ended */
            if (playground.matchEnded()) {
                /* Declare match winner and end match */
                playground.declareMatchWinner();
                refereeSite.endMatch();
                contestantsBench.endMatch();

                break;
            }
        }
    }
}
