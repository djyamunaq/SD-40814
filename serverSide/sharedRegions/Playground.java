package serverSide.sharedRegions;

import serverSide.main.*;
import clientSide.entities.ContestantStates;
import clientSide.entities.GameConstants;
import clientSide.entities.RefereeStates;
import clientSide.stubs.GeneralReposStub;
import genclass.GenericIO;
import serverSide.entities.*;

/**
 * Playground
 * 
 * Responsible for keeping the interactions between referees, coaches and
 * contestants.
 * Provide mutual exclusion in shared resources.
 */
public class Playground {

    /**
     * Reference to repos shared resource
     */
    private final GeneralReposStub repos;

    /**
     * Value of the current game
     */
    private int currentGame;

    /**
     * Value of the current trial
     */
    private int currentTrial;

    /**
     * Number of wins of team 1
     */
    private int winsTeam1;

    /**
     * Number of wins of the team 2
     */
    private int winsTeam2;

    /**
     * Game won by knockout
     */
    private boolean isKnockOut = false;

    /**
     * Match ended
     */
    private boolean isMatchFinalized = false;

    /**
     * Game ended
     */
    private boolean isGameFinalized = false;

    private boolean trialStarted = false;

    private boolean trialAsserted = false;

    /**
     * Shift of the rope
     */
    private int ropeState = 0;

    private int contestantsDone[];

    /**
     * Playground instantiation.
     *
     * @param repos reference to the general repository
     */
    public Playground(GeneralReposStub repos) {
        this.repos = repos;

        /* Clear match variables */
        this.currentGame = 0;
        this.currentTrial = 0;

        this.winsTeam1 = 0;
        this.winsTeam2 = 0;

        this.contestantsDone = new int[2];
    }

    /**
     * Check if match ended
     * 
     * @return true if match ended, false otherwise
     */
    public boolean matchEnded() {
        return this.isMatchFinalized;
    }

    /**
     * Check if game ended
     * 
     * @return true if game ended, false otherwise
     */
    public boolean gameEnded() {
        return this.isGameFinalized;
    }

    /**
     * Contestant done pulling rope
     * 
     * Called by contestant: unblock referee to assert trial and then
     * wait for referee signal that trial is asserted
     */
    public synchronized void amDone() {
        PlaygroundClientProxy proxy = (PlaygroundClientProxy) Thread.currentThread();
        int team = proxy.getContestantId() < 5 ? 0 : 1;

        /* Notify referee contestant is done */
        this.contestantsDone[team]++;
        notifyAll();

        /* Block waiting for trial assert by referee */
        while (!this.trialAsserted) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        
        // GenericIO.writelnString("Game=" + this.currentGame + " | Trial=" + this.currentTrial + " | Team=" + team);
    }

    /**
     * Contestant get ready to trial
     * 
     * Called by contestant: wait for referee command to start trial,
     * then go to state DO_YOUR_BEST
     */
    public synchronized void getReady() {
        PlaygroundClientProxy proxy = (PlaygroundClientProxy) Thread.currentThread();
        int contestantId = proxy.getContestantId();

        /* Block waiting for referee command to start trial */
        while (!this.trialStarted) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        this.trialAsserted = false;

        /* Set and register contestant state DO_YOUR_BEST */
        proxy.setContestantState(ContestantStates.DO_YOUR_BEST);
        repos.setContestantState(contestantId, ContestantStates.DO_YOUR_BEST);
    }

    /**
     * Pull the rope
     * 
     * Called by contestant: modify rope state (Add 1 if in team 1, sub 1 if in team
     * 2)
     */
    public synchronized void pullTheRope() {
        /* Enter critical region */
        PlaygroundClientProxy proxy = (PlaygroundClientProxy) Thread.currentThread();

        int contestantTeam = proxy.getContestantId() > 4 ? GameConstants.TEAM_1 : GameConstants.TEAM_2;
        int contestantStrength = proxy.getContestantStrength();
        int contestantId = proxy.getContestantId();

        if (contestantTeam == GameConstants.TEAM_1) {
            this.ropeState++;
        } else if (contestantTeam == GameConstants.TEAM_2) {
            this.ropeState--;
        }

        /* Register contestant strength */
        repos.setRopeState(this.ropeState);

        /* */
        if (contestantStrength > 0) {
            proxy.setContestantStrength(contestantStrength - 1);
            repos.setContestantStrength(contestantId, contestantStrength);
        }

        /* Exit critical region */
    }

    /**
     * Start a new trial
     * 
     * Called by referee: wait for teams ready signal,
     * then go to WAIT_FOR_TRIAL_CONCLUSION state
     * and unblock participants to start trial
     */
    public synchronized void startTrial() {
        PlaygroundClientProxy proxy = (PlaygroundClientProxy) Thread.currentThread();

        this.isKnockOut = false;

        /* Set and register referee state WAIT_FOR_TRIAL_CONCLUSION */
        proxy.setRefereeState(RefereeStates.WAIT_FOR_TRIAL_CONCLUSION);
        this.repos.setRefereeState(RefereeStates.WAIT_FOR_TRIAL_CONCLUSION);

        /* Register trial number and rope state */
        this.repos.setRopeState(this.ropeState);
        this.repos.setTrialNumber(this.currentTrial + 1);

        /* Signal to participant contestants to start trial */
        this.trialStarted = true;
        notifyAll();
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
    public synchronized boolean assertTrialDecision() {
        /* Wait for signal for last contestant done signal */
        while (this.contestantsDone[0] % 3 == 0 && this.contestantsDone[1] % 3 == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        this.contestantsDone[0] %= 3;
        this.contestantsDone[1] %= 3;

        /* Reinitialize variable */
        this.trialStarted = false;

        /* Check if knock out */
        if (Math.abs(this.ropeState) >= 4) {
            this.isKnockOut = true;
        }

        /* Register ended trial */
        this.currentTrial++;

        /* Set game end at end of last trial */
        if ((this.currentTrial == GameConstants.NUM_TRIALS || this.isKnockOut)
                && this.currentGame == GameConstants.NUM_GAMES) {
            this.isMatchFinalized = true;
        }

        /* Notify coaches and contestants that the trial has been asserted */
        this.trialAsserted = true;
        notifyAll();

        /* Check if game finalized */
        if (this.currentTrial == GameConstants.NUM_TRIALS || this.isKnockOut) {
            /* Set game end */
            this.isGameFinalized = true;

            return false;
        }

        return true;
    }

    /**
     * Announce new game
     * 
     * Called by referee: set game variables and go to
     * START_OF_A_GAME state
     */
    public synchronized void announceNewGame() {
        PlaygroundClientProxy proxy = (PlaygroundClientProxy) Thread.currentThread();

        /* Reset game variables */
        this.currentTrial = 0;
        this.ropeState = 0;

        /* Set referee state */
        proxy.setRefereeState(RefereeStates.START_OF_A_GAME);

        this.currentGame++;

        /* Set and register game variables states */
        this.repos.setTrialNumber(this.currentTrial);
        this.repos.setRopeState(this.ropeState);
        this.repos.reportNewGame(this.currentGame);

        /* Set and register referee state */
        this.repos.setRefereeState(RefereeStates.START_OF_A_GAME);
    }

    /**
     * Declare game winner
     * 
     * Called by referee: go to END_OF_A_GAME state,
     * then decide and declare game winner
     */
    public synchronized void declareGameWinner() {
        PlaygroundClientProxy proxy = (PlaygroundClientProxy) Thread.currentThread();

        /* Set and register referee state END_OF_A_GAME */
        proxy.setRefereeState(RefereeStates.END_OF_A_GAME);
        this.repos.setRefereeState(RefereeStates.END_OF_A_GAME);

        /* Check who won the game */
        int gameWinner = 0;
        if (this.ropeState > 0) {
            this.winsTeam1++;
            gameWinner = 1;
        } else if (this.ropeState < 0) {
            this.winsTeam2++;
            gameWinner = 2;
        }

        /* Report game end */
        this.repos.setTrialNumber(this.currentTrial);
        this.repos.reportGameEnd(this.currentGame, gameWinner, this.currentTrial, this.isKnockOut);
    }

    /**
     * Declare match winner
     * 
     * Called by referee: go to END_OF_THE_MATCH state
     * and unblock possibly blocked contestants
     */
    public synchronized void declareMatchWinner() {
        /* Set and register referee state END_OF_THE_MATCH */
        repos.reportMatchEnd(this.winsTeam1, this.winsTeam2);
    }

    /**
     * Review notes (Coach sleep)
     * 
     * Called by coach to wait referee assert trial and go to
     * WAIT_FOR_REFEREE_COMMAND state
     */
    public synchronized void reviewNotes() {
        /* Wait while trial has not been asserted */
        while (!this.trialAsserted) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized void shutdown() {
        ServerPlayground.waitConnection = false;
        notifyAll();
    }

}
