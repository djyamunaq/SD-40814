package serverSide.sharedRegions;

import serverSide.main.*;
import commInfra.*;
import genclass.GenericIO;
import genclass.TextFile;
import java.util.Objects;

import clientSide.entities.CoachStates;
import clientSide.entities.ContestantStates;
import clientSide.entities.RefereeStates;

/**
 * General Repository.
 *
 * It is responsible to keep the visible internal state of the problem and to
 * provide means for it to be printed in the logging file.
 * It is implemented using semaphores for synchronization.
 * All public methods are executed in mutual exclusion.
 * There are no internal synchronization points.
 */
public class GeneralRepos {
   /**
    * Name of the logging file.
    */
   private final String logFileName;

   /**
    * State of the coaches
    */
   private final int[] coachState;

   /**
    * State of the contestants
    */
   private final int[] contestantState;

   /**
    * Strength of the contestants
    */
   private final int[] contestantStrength;

   /**
    * State of the referee
    */
   private int refereeState;

   /**
    * State of the rope
    */
   private int ropeState;

   /**
    * Current trial number
    */
   private int trialNumber;

   /**
    * Has game ented
    */
   private boolean gameEnd;

   /**
    * Register of contestants in match
    */
   private int[] contestantQueue;

   /**
    * Semaphore to ensure mutual exclusion on the execution of public methods.
    */
   private final Semaphore access;

   /**
    * Instantiation of a general repository object.
    *
    * @param logFileName name of the logging file
    */
   public GeneralRepos(String logFileName) {
      /* Create new file */
      if ((logFileName == null) || Objects.equals(logFileName, "")) {
         this.logFileName = "logger";
      } else {
         this.logFileName = logFileName;
      }

      /* Register coaches initial states */
      this.coachState = new int[2];
      for (int i = 0; i < 2; i++) {
         this.coachState[i] = CoachStates.WAIT_FOR_REFEREE_COMMAND;
      }

      /* Register contestant queue (Participants in trial pulling rope) */
      this.contestantQueue = new int[6];
      for (int i = 0; i < 6; i++) {
         this.contestantQueue[i] = 0;
      }

      /* Register contestants initial states and strength */
      this.contestantState = new int[10];
      this.contestantStrength = new int[10];
      for (int i = 0; i < 10; i++) {
         contestantState[i] = ContestantStates.SEAT_AT_THE_BENCH;
      }

      /* Initialize semaphore */
      this.access = new Semaphore();
      this.access.up();

      /* Report initial status */
      reportInitialStatus();

      /* Clear game end variable */
      this.gameEnd = false;
   }

   /**
    * Set coach state.
    *
    * @param id    coach id
    * @param state coach state
    */
   public void setCoachState(int id, int state) {
      /* Enter critical region */
      access.down();
      this.coachState[id] = state;
      reportStatus();
      /* Exit critical region */
      access.up();
   }

   /**
    * Set contestant state.
    *
    * @param id    contestant id
    * @param state contestant state
    */
   public void setContestantState(int id, int state) {
      /* Enter critical region */
      access.down();
      this.contestantState[id] = state;
      reportStatus();
      /* Exit critical region */
      access.up();
   }

   /**
    * Set contestant state.
    *
    * @param contestantId contestant id
    * @param pos          position of contestant in trial queue
    */
   public void setContestantInTrial(int contestantId, int pos) {
      /* Enter critical region */
      access.down();
      this.contestantQueue[pos] = contestantId;
      reportStatus();
      /* Exit critical region */
      access.up();
   }

   /**
    * Set contestant state.
    *
    * @param id    contestant id
    * @param value contestant strength
    */
   public void setContestantStrength(int id, int value) {
      /* Enter critical region */
      access.down();
      this.contestantStrength[id] = value;
      reportStatus();
      /* Exit critical region */
      access.up();
   }

   /**
    * Set referee state.
    *
    * @param refereeState referee state
    */
   public void setRefereeState(int refereeState) {
      /* Enter critical region */
      access.down();
      this.refereeState = refereeState;
      reportStatus();
      /* Exit critical region */
      access.up();
   }

   /**
    * Set trial number.
    *
    * @param value trial number
    */
   public void setTrialNumber(int value) {
      /* Enter critical region */
      access.down();
      this.trialNumber = value;
      reportStatus();
      /* Exit critical region */
      access.up();
   }

   /**
    * Set trial number.
    *
    * @param value trial number
    */
   public void setRopeState(int value) {
      /* Enter critical region */
      access.down();
      this.ropeState = value;
      reportStatus();
      /* Exit critical region */
      access.up();
   }

   /**
    * Write a state line at the end of the logging file.
    *
    * The current game number
    */
   public void reportNewGame(int gameNumber) {
      /* Instantiate text file handler */
      TextFile log = new TextFile();

      /* Check if file opened */
      if (!log.openForAppending(".", logFileName)) {
         GenericIO.writelnString("The operation of opening for appending the file " + logFileName + " failed!");
         System.exit(1);
      }

      /* Write game number to file */
      log.writelnString(" Game #" + gameNumber);
      GenericIO.writelnString(" Game #" + gameNumber);
      /* Close file */
      if (!log.close()) {
         GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
         System.exit(1);
      }
   }

   /**
    * Write a state line at the end of the logging file.
    *
    * The game result
    */
   public void reportGameEnd(int gameNumber, int gameWinner, int numTrials, boolean knockOut) {
      /* Instantiate text file handler */
      TextFile log = new TextFile();

      /* Check if file opened */
      if (!log.openForAppending(".", logFileName)) {
         GenericIO.writelnString("The operation of opening for appending the file " + logFileName + " failed!");
         System.exit(1);
      }

      /* Add game winner (Or draw) */
      log.writelnString();
      GenericIO.writelnString();
      if (knockOut) {
         log.writelnString(" Game #" + gameNumber + " was won by team #" + gameWinner + " by knock out in " + numTrials
               + " trials.");
         GenericIO.writelnString(
               " Game #" + gameNumber + " was won by team #" + gameWinner + " by knock out in " + numTrials
                     + " trials.");
      } else {
         if (gameWinner == 0) {
            log.writelnString(" Game #" + gameNumber + " was a draw. ");
            GenericIO.writelnString(" Game #" + gameNumber + " was a draw. ");

         } else {
            log.writelnString(" Game #" + gameNumber + " was won by team #" + gameWinner + " by points in " + numTrials
                  + " trials.");
            GenericIO.writelnString(
                  " Game #" + gameNumber + " was won by team #" + gameWinner + " by points in " + numTrials
                        + " trials.");
         }
      }
      log.writelnString();
      GenericIO.writelnString();
      /* Close file */
      if (!log.close()) {
         GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
         System.exit(1);
      }
   }

   /**
    * Write a state line at the end of the logging file
    *
    * The match end result in a line to be printed
    */
   public void reportMatchEnd(int wins1, int wins2) {
      /* Register game end */
      this.gameEnd = true;

      /* Instantiate text file handler */
      TextFile log = new TextFile();

      /* Check if file opened */
      if (!log.openForAppending(".", logFileName)) {
         GenericIO.writelnString("The operation of opening for appending the file " + logFileName + " failed!");
         System.exit(1);
      }

      /* Add game winner (Or draw) */
      log.writelnString();
      GenericIO.writelnString();
      if (wins1 > wins2) {
         log.writelnString(" Match was won by team #1 (" + wins1 + "-" + wins2 + ").");
         GenericIO.writelnString(" Match was won by team #1 (" + wins1 + "-" + wins2 + ").");
      } else if (wins1 < wins2) {
         log.writelnString(" Match was won by team #2 (" + wins2 + "-" + wins1 + ").");
         GenericIO.writelnString(" Match was won by team #2 (" + wins2 + "-" + wins1 + ").");
      } else {
         log.writelnString(" Match was a draw.");
         GenericIO.writelnString(" Match was a draw.");
      }

      /* Write to file */
      log.writelnString();
      GenericIO.writelnString();

      /* Close file */
      if (!log.close()) {
         GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
         System.exit(1);
      }
   }

   public synchronized void shutdown() {
      ServerGeneralRepos.waitConnection = false;
      notifyAll();
   }

   /**
    * Write a state line at the end of the logging file
    *
    * The current states of the referee, coaches and contestants are organized in a
    * line to
    * be printed
    */
   private void reportStatus() {
      /* Don't print if game ended */
      if (this.gameEnd) {
         return;
      }

      /* Don't print if trial hasn't began */
      if (this.trialNumber == 0) {
         return;
      }

      /* Instantiate text file handler */
      TextFile log = new TextFile();

      /* Empty line to be filled and printed */
      String lineStatus = "";

      /* Check if file opened */
      if (!log.openForAppending(".", logFileName)) {
         GenericIO.writelnString("The operation of opening for appending the file " + logFileName + " failed!");
         System.exit(1);
      }

      /* Add referee state */
      switch (refereeState) {
         case RefereeStates.END_OF_A_GAME:
            lineStatus += " ENDAGAME ";
            break;
         case RefereeStates.END_OF_THE_MATCH:
            lineStatus += " ENDMATCH ";
            break;
         case RefereeStates.START_OF_A_GAME:
            lineStatus += " STARGAME ";
            break;
         case RefereeStates.START_OF_THE_MATCH:
            lineStatus += " STARMTCH ";
            break;
         case RefereeStates.TEAMS_READY:
            lineStatus += " TEAMREAD ";
            break;
         case RefereeStates.WAIT_FOR_TRIAL_CONCLUSION:
            lineStatus += " WAITTRIA ";
            break;
      }

      /* Add coach 1 state */
      switch (coachState[0]) {
         case CoachStates.ASSEMBLE_TEAM:
            lineStatus += " ASMBTEAM ";
            break;
         case CoachStates.WAIT_FOR_REFEREE_COMMAND:
            lineStatus += " WAITREFC ";
            break;
         case CoachStates.WATCH_TRIAL:
            lineStatus += " WTCHTRIA ";
            break;
      }

      /* Add contestants in team 1 states */
      for (int i = 0; i < 5; i++) {
         switch (this.contestantState[i]) {
            case ContestantStates.DO_YOUR_BEST:
               lineStatus += " DOYOBEST ";
               break;
            case ContestantStates.SEAT_AT_THE_BENCH:
               lineStatus += " SEATBNCH ";
               break;
            case ContestantStates.STAND_IN_POSITION:
               lineStatus += " STANDPOS ";
               break;
         }

         String strength = Integer.toString(this.contestantStrength[i]);
         lineStatus += strength;
      }

      /* Add coach 2 state */
      switch (coachState[1]) {
         case CoachStates.ASSEMBLE_TEAM:
            lineStatus += " ASMBTEAM ";
            break;
         case CoachStates.WAIT_FOR_REFEREE_COMMAND:
            lineStatus += " WAITREFC ";
            break;
         case CoachStates.WATCH_TRIAL:
            lineStatus += " WTCHTRIA ";
            break;
      }

      /* Add contestants in team 2 states */
      for (int i = 5; i < 10; i++) {
         switch (this.contestantState[i]) {
            case ContestantStates.DO_YOUR_BEST:
               lineStatus += " DOYOBEST ";
               break;
            case ContestantStates.SEAT_AT_THE_BENCH:
               lineStatus += " SEATBNCH ";
               break;
            case ContestantStates.STAND_IN_POSITION:
               lineStatus += " STANDPOS ";
               break;
         }

         lineStatus += Integer.toString(this.contestantStrength[i]);
      }

      /* Add trial state (Contestants in queue, trial number and rope state) */
      lineStatus += (" (" + this.contestantQueue[0] + " " + this.contestantQueue[1] + " " + this.contestantQueue[2]
            + ")");
      lineStatus += " . ";
      lineStatus += ("(" + this.contestantQueue[3] + " " + this.contestantQueue[4] + " " + this.contestantQueue[5]
            + ")");

      lineStatus += "     ";
      lineStatus += Integer.toString(this.trialNumber);
      lineStatus += " ";

      lineStatus += " ";
      lineStatus += Integer.toString(this.ropeState);
      lineStatus += " ";

      /* Write to file */
      log.writelnString(lineStatus);
      GenericIO.writelnString(lineStatus);
      /* Close file */
      if (!log.close()) {
         GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
         System.exit(1);
      }
   }

   /**
    * Write the header to the logging file.
    */
   private void reportInitialStatus() {
      /* Instantiate text file handler */
      TextFile log = new TextFile();

      /* Check if file opened */
      if (!log.openForWriting(".", logFileName)) {
         GenericIO.writelnString("The operation of creating the file " + logFileName + " failed!");
         System.exit(1);
      }

      /* Write log header */
      log.writelnString("                Game of the Rope - Description of the internal state");
      log.writelnString(
            " Ref       Coa 1     Cont 1     Cont 2     Cont 3     Cont 4     Cont 5     Coa 2     Cont 1     Cont 2     Cont 3     Cont 4     Cont 5     Trial");

      GenericIO.writelnString("                Game of the Rope - Description of the internal state");
      GenericIO.writelnString(
            " Ref       Coa 1     Cont 1     Cont 2     Cont 3     Cont 4     Cont 5     Coa 2     Cont 1     Cont 2     Cont 3     Cont 4     Cont 5     Trial");
      /* Close file */
      if (!log.close()) {
         GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
         System.exit(1);
      }

      /* Call to write initial state */
      reportStatus();
   }

}
