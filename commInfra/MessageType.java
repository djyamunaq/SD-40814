package commInfra;

/**
 * Type of the exchanged messages.
 * the
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class MessageType {
  /**
   * Initialization of the logging file name and the number of iterations (service
   * request).
   */

  public static final int SETNFIC = 1;

  /**
   * Logging file was initialized (reply).
   */

  public static final int NFICDONE = 2;

  /**
   * Wait for coach call (service request).
   */

  public static final int WAITFORCOACHCALL = 3;

  /**
   * Hair was cut (reply).
   */

  public static final int FOLLOWCOACHADVICE = 4;

  /**
   * Barber shop is full (reply).
   */

  public static final int SEATDOWN = 5;

  /**
   * Barber goes to sleep (service request).
   */

  public static final int CALLCONTESTANTS = 6;

  /**
   * Barber is asleep (reply).
   */

  public static final int ENDMATCH = 7;

  /**
   * Call a customer (service request).
   */

  public static final int SETCOACHES = 8;

  /**
   * Customer was called (reply).
   */

  public static final int SETREFEREE = 9;

  /**
   * Barber receives payment (service request).
   */

  public static final int REGISTERATTEAM = 10;

  /**
   * Payment was received (reply).
   */

  public static final int MATCHENDED = 11;

  /**
   * End of work - barber (service request).
   */

  public static final int GAMEENDED = 12;

  /**
   * Barber goes home (reply).
   */

  public static final int AMDONE = 13;

  /**
   * Server shutdown (service request).
   */

  public static final int GETREADY = 14;

  /**
   * Server was shutdown (reply).
   */

  public static final int PULLTHEROPE = 15;

  /**
   * Set barber state (service request).
   */

  public static final int STARTTRIAL = 16;

  /**
   * Set customer state (service request).
   */

  public static final int ASSERTTRIALDECISION = 17;

  /**
   * Set barber and customer states (service request).
   */

  public static final int ANNOUNCENEWGAME = 18;

  /**
   * Set barber and customer states (service request).
   */

  public static final int DECLAREGAMEWINNER = 19;

  /**
   * Set barber and customer states (service request).
   */

  public static final int DECLAREMATCHWINNER = 20;

  /**
   * Set barber and customer states (service request).
   */

  public static final int REVIEWNOTES = 21;

  /**
   * Wait for coach call (service request).
   */

  public static final int WAITFORCOACHCALLDONE = 22;

  /**
   * Hair was cut (reply).
   */

  public static final int FOLLOWCOACHADVICEDONE = 23;

  /**
   * Barber shop is full (reply).
   */

  public static final int SEATDOWNDONE = 24;

  /**
   * Barber goes to sleep (service request).
   */

  public static final int CALLCONTESTANTSDONE = 25;

  /**
   * Barber is asleep (reply).
   */

  public static final int ENDMATCHDONE = 26;

  /**
   * Call a customer (service request).
   */

  public static final int SETCOACHESDONE = 27;

  /**
   * Customer was called (reply).
   */

  public static final int SETREFEREEDONE = 28;

  /**
   * Barber receives payment (service request).
   */

  public static final int REGISTERATTEAMDONE = 29;

  /**
   * Payment was received (reply).
   */

  public static final int MATCHENDEDDONE = 30;

  /**
   * End of work - barber (service request).
   */

  public static final int GAMEENDEDDONE = 31;

  /**
   * Barber goes home (reply).
   */

  public static final int AMDONEDONE = 32;

  /**
   * Server shutdown (service request).
   */

  public static final int GETREADYDONE = 33;

  /**
   * Server was shutdown (reply).
   */

  public static final int PULLTHEROPEDONE = 34;

  /**
   * Set barber state (service request).
   */

  public static final int STARTTRIALDONE = 35;

  /**
   * Set customer state (service request).
   */

  public static final int ASSERTTRIALDECISIONDONE = 36;

  /**
   * Set barber and customer states (service request).
   */

  public static final int ANNOUNCENEWGAMEDONE = 37;

  /**
   * Set barber and customer states (service request).
   */

  public static final int DECLAREGAMEWINNERDONE = 38;

  /**
   * Set barber and customer states (service request).
   */

  public static final int DECLAREMATCHWINNERDONE = 39;

  /**
   * Set barber and customer states (service request).
   */

  public static final int REVIEWNOTESDONE = 40;

  /**
   * Setting acknowledged (reply).
   */

  public static final int SACK = 41;

  public static final int NONE = 42;

  public static final int WAITREFEREECALLTRIAL = 43;

  public static final int CALLTRIAL = 44;

  public static final int INFORMREFEREE = 45;

  public static final int WAITREFEREECALLTRIALDONE = 43;

  public static final int INFORMREFEREEDONE = 45;
  
  public static final int SETCOACHSTATE = 46;

  public static final int SETCONTESTANTSTATE = 47;

  public static final int SETCONTESTANTSTRENGTH = 48;

  public static final int SETCONTESTANTINTRIAL = 49;
  
  public static final int SETREFEREESTATE = 50;

  public static final int SETROPESTATE = 51;

  public static final int SETTRIALNUMBER = 52;
  
  public static final int REPORTNEWGAME = 53;

  public static final int REPORTGAMEEND = 54;

  public static final int REPORTMATCHEND = 55;

  public static final int SETCOACHSTATEDONE = 56;

  public static final int SETCONTESTANTSTATEDONE = 57;

  public static final int SETCONTESTANTSTRENGTHDONE = 58;

  public static final int SETCONTESTANTINTRIALDONE = 59;
  
  public static final int SETREFEREESTATEDONE = 60;

  public static final int SETROPESTATEDONE = 61;

  public static final int SETTRIALNUMBERDONE = 62;
  
  public static final int REPORTNEWGAMEDONE = 63;

  public static final int REPORTGAMEENDDONE = 64;

  public static final int REPORTMATCHENDDONE = 65;

  public static final int SHUT = 66;

  public static final int SHUTDONE = 67;

  public static final int CALLTRIALDONE = 68;

}
