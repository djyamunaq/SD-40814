package commInfra;

import java.io.*;

import clientSide.entities.GameConstants;
import genclass.GenericIO;

/**
 * Internal structure of the exchanged messages.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class Message implements Serializable {
   /**
    * Serialization key.
    */

   private static final long serialVersionUID = 2021L;

   /**
    * Message type.
    */

   private int msgType = -1;

   /**
    * Referee state.
    */

   private int refereeState = -1;

   /**
    * Coach identification.
    */
   private int coachId = -1;

   /**
    * Coach state.
    */

   private int coachState = -1;

   /**
    * Customer identification.
    */

   private int contestantId = -1;

   /**
    * Customer state.
    */

   private int contestantState = -1;

   private int contestantStrength = -1;

   private int contestantTeam = -1;

   private int contestantPos = -1;

   private int ropeState = -1;

   private int trialNumber = -1;

   private int gameNumber = -1;

   private int gameWinner = -1;

   private int wins1 = -1;

   private int wins2 = -1;

   private boolean knockOut = false;

   /**
    * End of operations (barber).
    */

   private boolean endOp = false;

   /**
    * Name of the logging file.
    */

   private String fName = null;

   /**
    * Message instantiation (form 1).
    *
    * @param type message type
    */

   public Message(int type) {
      msgType = type;
   }

   /**
    * Message instantiation (form 2).
    *
    * @param type  message type
    * @param id    barber / customer identification
    * @param state barber / customer state
    */

   public Message(int type, int entityType, int id, int state) {
      this.msgType = type;
      switch (entityType) {
         case GameConstants.TYPE_REFEREE:
            this.refereeState = state;
            break;

         case GameConstants.TYPE_COACH:
            this.coachId = id;
            this.coachState = state;
            break;

         case GameConstants.TYPE_CONTESTANT:
            if (this.msgType == MessageType.SETCONTESTANTSTRENGTH || this.msgType == MessageType.SETCONTESTANTSTRENGTHDONE) {
               this.contestantId = id;
               this.contestantStrength = state;
            } else {
               this.contestantId = id;
               this.contestantState = state;
            }

            break;

         default:
            GenericIO.writelnString("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
      }
   }

   public Message(int type, int entityType, int id, int state, int val) {
      this.msgType = type;
      switch (entityType) {
         case GameConstants.TYPE_REFEREE:
            break;

         case GameConstants.TYPE_COACH:
            break;

         case GameConstants.TYPE_CONTESTANT:
            if (this.msgType == MessageType.SETCONTESTANTSTRENGTH || this.msgType == MessageType.WAITFORCOACHCALL || this.msgType == MessageType.PULLTHEROPE || this.msgType == MessageType.SEATDOWN || this.msgType == MessageType.SETCONTESTANTSTATEDONE || this.msgType == MessageType.WAITFORCOACHCALLDONE || this.msgType == MessageType.PULLTHEROPEDONE || this.msgType == MessageType.SEATDOWNDONE) {
               this.contestantId = id;
               this.contestantState = state;
               this.contestantStrength = val;
            }

            break;

         default:
            GenericIO.writelnString("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
      }
   }

   /**
    * Message instantiation (form 7).
    *
    * @param type message type
    * @param name name of the logging file
    */
   public Message(int type, String name) {
      this.msgType = type;
      this.fName = name;
   }

   public Message(int type, int val) {
      this.msgType = type;
      if (this.msgType == MessageType.SETROPESTATE) {
         this.ropeState = val;
      } else if (this.msgType == MessageType.SETTRIALNUMBER) {
         this.trialNumber = val;
      } else if (this.msgType == MessageType.REPORTNEWGAME) {
         this.gameNumber = val;
      }
   }

   public Message(int type, int gameNumber, int gameWinner, int numTrials, boolean knockOut) {
      this.msgType = type;
      this.gameNumber = gameNumber;
      this.gameWinner = gameWinner;
      this.trialNumber = numTrials;
      this.knockOut = knockOut;
   }

   /**
    * Message instantiation (form 4).
    *
    * @param type  message type
    * @param id    coach identification
    * @param endOP end of operations flag
    */

   public Message(int type, int id, boolean endOp) {
      this.msgType = type;
      this.coachId = id;
      this.endOp = endOp;
   }

   public Message(int type, int val1, int val2) {
      this.msgType = type;

      if (this.msgType == MessageType.REPORTMATCHEND) {
         this.wins1 = val1;
         this.wins2 = val2;
      } else if (this.msgType == MessageType.SETCONTESTANTINTRIAL) {
         this.contestantId = val1;
         this.contestantPos = val2;
      }
   }

   /**
    * Getting message type.
    *
    * @return message type
    */

   public int getMsgType() {
      return (msgType);
   }

   /**
    * Getting barber identification.
    *
    * @return barber identification
    */

   public int getRopeState() {
      return this.ropeState;
   }

   /**
    * Getting barber identification.
    *
    * @return barber identification
    */

   public int getTrialNumber() {
      return this.trialNumber;
   }

   public int getGameNumber() {
      return this.gameNumber;
   }

   public int getGameWinner() {
      return this.gameWinner;
   }

   public int getWins1() {
      return this.wins1;
   }

   public int getWins2() {
      return this.wins2;
   }

   public boolean getKnockOut() {
      return this.knockOut;
   }

   /**
    * Getting barber identification.
    *
    * @return barber identification
    */
   public int getRefereeState() {
      return this.refereeState;
   }

   public int getCoachId() {
      return this.coachId;
   }

   /**
    * Getting barber state.
    *
    * @return barber state
    */

   public int getCoachState() {
      return this.coachState;
   }

   /**
    * Getting customer identification.
    *
    * @return customer identification
    */

   public int getContestantId() {
      return this.contestantId;
   }

   /**
    * Getting customer state.
    *
    * @return customer state
    */

   public int getContestantState() {
      return this.contestantState;
   }

   /**
    * Getting customer identification.
    *
    * @return customer identification
    */

   public int getContestantTeam() {
      return this.contestantTeam;
   }

   public int getContestantStrength() {
      return this.contestantStrength;
   }

   public int getContestantPos() {
      return this.contestantPos;
   }

   /**
    * Getting end of operations flag (barber).
    *
    * @return end of operations flag
    */

   public boolean getEndOp() {
      return (endOp);
   }

   /**
    * Getting name of logging file.
    *
    * @return name of the logging file
    */

   public String getLogFName() {
      return (fName);
   }

   /**
    * Printing the values of the internal fields.
    *
    * It is used for debugging purposes.
    *
    * @return string containing, in separate lines, the pair field name - field
    *         value
    */

   @Override
   public String toString() {
      return ("Message type = " + this.msgType +
            "\nCoach Id = " + this.coachId +
            "\nCoach State = " + this.coachState +
            "\nContestant Id = " + this.contestantId +
            "\nContestant State = " + this.contestantState +
            "\nContestant Strength = " + this.contestantStrength +
            "\nReferee State = " + this.refereeState +
            "\nRope State = " + this.ropeState +
            "\nTrial number = " + this.trialNumber +
            "\nGame number = " + this.gameNumber +
            "\nWins 1 = " + this.wins1 +
            "\nWins 2 = " + this.wins2 +
            "\nEnd of Operations = " + this.endOp +
            "\nName of logging file = " + this.fName);
   }
}
