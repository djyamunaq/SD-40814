package serverSide.sharedRegions;

import clientSide.entities.CoachStates;
import clientSide.entities.ContestantStates;
import clientSide.entities.GameConstants;
import serverSide.entities.ContestantsBenchClientProxy;
import serverSide.main.ServerContestantsBench;
import clientSide.stubs.GeneralReposStub;

/**
 * Contestants bench
 * 
 * Keep track of contestants and their strength
 * 
 * Public methods are executed in mutual exclusion
 * 
 * Sync points:
 * Contestants wait for coach to select them for trial
 * Coaches wait for contestants to follow its advice
 */
public class ContestantsBench {

  /**
   * Reference to repos shared resource
   */
  private final GeneralReposStub repos;

  /**
   * Array of contestants selected to trial
   */
  private boolean contestantSelected[][];

  /**
   * Array of number of contestants in position at each team
   */
  private int nContestantStandInPos[];

  /**
   * Array of strength per contestant
   */
  private int contestantRegister[];

  /**
   * Boolean flag to match end
   */
  private boolean isMatchEnded = false;

  public ContestantsBench(GeneralReposStub repos) {
    this.repos = repos;

    this.contestantSelected = new boolean[2][5];
    this.nContestantStandInPos = new int[2];
    this.contestantRegister = new int[10];
  }

  /**
   * Contestant wait for coach signal
   * 
   * Called by contestant: wait for coach selection,
   * then go to STAND_IN_POSITION state and unblock coach
   */
  public synchronized void waitForCoachCall() {
    ContestantsBenchClientProxy proxy = (ContestantsBenchClientProxy) Thread.currentThread();
    int contestantId = proxy.getContestantId();
    int coachId = proxy.getCoachId();

    /* Set and register contestant state SEAT_AT_THE_BENCH */
    proxy.setContestantState(ContestantStates.SEAT_AT_THE_BENCH);
    repos.setContestantState(contestantId, ContestantStates.SEAT_AT_THE_BENCH);

    int contestantStrength = proxy.getContestantStrength();
    this.contestantRegister[contestantId] = contestantStrength;

    repos.setContestantStrength(contestantId, contestantStrength);

    /* Wait for coach signal that was selected */
    while (!this.isMatchEnded && !contestantSelected[coachId][contestantId % 5]) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
  }

  /**
   * Contestant stay in position if selected by coach
   * 
   * Called by contestant: go to STAND_IN_POSITION state and unblock coach
   */
  public synchronized void followCoachAdvice() {
    ContestantsBenchClientProxy proxy = (ContestantsBenchClientProxy) Thread.currentThread();

    int contestantId = proxy.getContestantId();
    int team = contestantId < 5 ? 0 : 1;

    /* Set and register contestant state */
    proxy.setContestantState(ContestantStates.STAND_IN_POSITION);
    this.repos.setContestantState(contestantId, ContestantStates.STAND_IN_POSITION);

    /* Notify coach advice was followed */
    this.nContestantStandInPos[team]++;
    notifyAll();
  }

  /**
   * Contestant go back to bench
   * 
   * Called by contestant: go to state SEAT_AT_THE_BENCH
   */
  public synchronized void seatDown() {
    ContestantsBenchClientProxy proxy = (ContestantsBenchClientProxy) Thread.currentThread();

    int contestantId = proxy.getContestantId();
    int contestantStrength = proxy.getContestantStrength();

    /* Set and register contestant state SEAT_AT_THE_BENCH */
    proxy.setContestantState(ContestantStates.SEAT_AT_THE_BENCH);

    /* */
    if (contestantStrength < GameConstants.MAX_STRENGTH) {
      proxy.setContestantStrength(contestantStrength + 1);
      repos.setContestantStrength(contestantId, contestantStrength);
    }

    repos.setContestantState(contestantId, ContestantStates.SEAT_AT_THE_BENCH);
  }

  /**
   * Call contestants to trial
   * 
   * Called by coach: wait referee command to assert trial,
   * then go to ASSEMBLE_TEAM state, assemble team and give
   * signal to contestants to participate in trial
   */
  public synchronized void callContestants() {
    ContestantsBenchClientProxy proxy = (ContestantsBenchClientProxy) Thread.currentThread();
    int coachId = proxy.getCoachId();

    /* Set and register coach state ASSEMBLE_TEAM */
    proxy.setCoachState(CoachStates.ASSEMBLE_TEAM);
    this.repos.setCoachState(coachId, CoachStates.ASSEMBLE_TEAM);

    /* Initialize to default false */
    this.contestantSelected[coachId] = new boolean[5];

    int c1, c2, c3, i1, i2, i3;
    c1 = c2 = c3 = this.contestantRegister[5 * coachId];
    i1 = i2 = i3 = 5 * coachId;
    for (int i = 5 * coachId; i < 5 + 5 * coachId; i++) {
      if (this.contestantRegister[i] > c1) {
        c1 = this.contestantRegister[i];
        i1 = i;
      }
    }
    if (i1 == 5 * coachId) {
      c2 = this.contestantRegister[5 * coachId + 1];
      i2 = 5 * coachId + 1;
    }
    for (int i = 5 * coachId; i < 5 + 5 * coachId; i++) {
      if (i == i1) {
        continue;
      }

      if (this.contestantRegister[i] > c2) {
        c2 = this.contestantRegister[i];
        i2 = i;
      }
    }
    if (i2 == 5 * coachId || i1 == 5 * coachId) {
      c3 = this.contestantRegister[5 * coachId + 1];
      i3 = 5 * coachId + 1;
      if (i2 == 5 * coachId + 1 || i1 == 5 * coachId + 1) {
        c3 = this.contestantRegister[5 * coachId + 2];
        i3 = 5 * coachId + 2;
      }
    }
    for (int i = 5 * coachId; i < 5 + 5 * coachId; i++) {
      if (i == i1 || i == i2) {
        continue;
      }
      if (this.contestantRegister[i] > c3) {
        c3 = this.contestantRegister[i];
        i3 = i;
      }
    }

    /* Register contestant in current trial */
    this.repos.setContestantInTrial(1 + i1, coachId * 3);
    this.repos.setContestantInTrial(1 + i2, coachId * 3 + 1);
    this.repos.setContestantInTrial(1 + i3, coachId * 3 + 2);

    this.contestantSelected[coachId][i1 % 5] = true;
    this.contestantSelected[coachId][i2 % 5] = true;
    this.contestantSelected[coachId][i3 % 5] = true;

    /* Notify selected contestant to stand in position */
    notifyAll();

    /* Wait for contestants signal that followed coach advice */
    while (this.nContestantStandInPos[coachId] < 3) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
    // GenericIO.writelnString("Coach " + coachId + " ready to trial");

    this.nContestantStandInPos[coachId] = 0;
  }

  /**
   * End match
   * 
   * Called by referee
   */
  public synchronized void endMatch() {
    /* Notify teams game ended */
    this.isMatchEnded = true;
    notifyAll();
  }

  /**
   * Shut down contestants bench server
   */
  public synchronized void shutdown() {
    ServerContestantsBench.waitConnection = false;
    notifyAll();
  }

}
