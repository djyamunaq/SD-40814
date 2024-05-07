package serverSide.sharedRegions;

import serverSide.main.*;
import clientSide.entities.CoachStates;
import clientSide.entities.RefereeStates;
import clientSide.stubs.GeneralReposStub;
import serverSide.entities.*;
import java.util.Arrays;
import java.util.Comparator;
import commInfra.*;
import genclass.GenericIO;

public class RefereeSite {

  /**
   * Reference to repos shared resource
   */
  private final GeneralReposStub repos;

  /**
   * Match finalized
   */
  private boolean isMatchFinalized = false;

  private int nTeamsReady = 0;

  private boolean trialCalled = false;

  public RefereeSite(GeneralReposStub repos) {
    this.repos = repos;
  }

  /**
   * Declare match winner
   * 
   * and unblock coaches
   */
  public synchronized void endMatch() {
    RefereeSiteClientProxy proxy = (RefereeSiteClientProxy) Thread.currentThread();

    /* Set and register referee state END_OF_THE_MATCH */
    proxy.setRefereeState(RefereeStates.END_OF_THE_MATCH);

    repos.setRefereeState(RefereeStates.END_OF_THE_MATCH);

    /* Notify coaches the match is finalized */
    this.isMatchFinalized = true;
    notifyAll();
  }

  public synchronized void waitRefereeCallTrial() {
    RefereeSiteClientProxy proxy = (RefereeSiteClientProxy) Thread.currentThread();
    int coachId = proxy.getCoachId();

    /* Set and register coach state */
    proxy.setCoachState(CoachStates.WAIT_FOR_REFEREE_COMMAND);
    this.repos.setCoachState(coachId, CoachStates.WAIT_FOR_REFEREE_COMMAND);

    while (!this.isMatchFinalized && !this.trialCalled) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
  }

  public synchronized void callTrial() {
    RefereeSiteClientProxy proxy = (RefereeSiteClientProxy) Thread.currentThread();

    proxy.setRefereeState(RefereeStates.TEAMS_READY);
    this.repos.setRefereeState(RefereeStates.TEAMS_READY);

    /* Notify coach the trial has been called */
    this.trialCalled = true;
    notifyAll();

    /* Block while coaches select team */
    while (this.nTeamsReady < 2) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }

    /* Reinitialize variable */
    this.nTeamsReady = 0;
    this.trialCalled = false;
  }

  /**
   * Inform referee team is ready
   * 
   * Called by coach: wait team ready signal from participants,
   * then go to WATCH_TRIAL state and inform referee the team is ready to trial
   */
  public synchronized void informReferee() {
    RefereeSiteClientProxy proxy = (RefereeSiteClientProxy) Thread.currentThread();

    int coachId = proxy.getCoachId();

    /* Set and register coach state WATCH_TRIAL */
    proxy.setCoachState(CoachStates.WATCH_TRIAL);
    this.repos.setCoachState(coachId, CoachStates.WATCH_TRIAL);

    /* Signal referee that the teams are selected */
    this.nTeamsReady++;
    notifyAll();
  }

  public synchronized void shutdown() {
    ServerRefereeSite.waitConnection = false;
    notifyAll();
  }

}
