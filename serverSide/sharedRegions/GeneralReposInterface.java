package serverSide.sharedRegions;

import commInfra.*;
import clientSide.entities.CoachStates;
import clientSide.entities.ContestantStates;
import clientSide.entities.GameConstants;
import clientSide.entities.RefereeStates;

/**
 * Interface to the General Repository of Information.
 *
 * It is responsible to validate and process the incoming message, execute the
 * corresponding method on the
 * General Repository and generate the outgoing message.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class GeneralReposInterface {
    /**
     * Reference to the general repository.
     */

    private final GeneralRepos repos;

    /**
     * Instantiation of an interface to the general repository.
     *
     * @param repos reference to the general repository
     */

    public GeneralReposInterface(GeneralRepos repos) {
        this.repos = repos;
    }

    /**
     * Processing of the incoming messages.
     *
     * Validation, execution of the corresponding method and generation of the
     * outgoing message.
     *
     * @param inMessage service request
     * @return service reply
     * @throws MessageException if the incoming message is not valid
     */

    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null; // mensagem de resposta

        /* validation of the incoming message */

        switch (inMessage.getMsgType()) {
            case MessageType.SETREFEREESTATE:
                if ((inMessage.getRefereeState() < RefereeStates.START_OF_THE_MATCH)
                        || (inMessage.getRefereeState() > RefereeStates.END_OF_THE_MATCH)) {
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;

            case MessageType.SETCOACHSTATE:
                if ((inMessage.getCoachId() < 0) || (inMessage.getCoachId() > 1))
                    throw new MessageException("Invalid coach id!", inMessage);
                else if ((inMessage.getCoachState() < CoachStates.WAIT_FOR_REFEREE_COMMAND)
                        || (inMessage.getCoachState() > CoachStates.WATCH_TRIAL))
                    throw new MessageException("Invalid coach state!", inMessage);
                break;

            case MessageType.SETCONTESTANTSTATE:
                if ((inMessage.getContestantId() < 0) || (inMessage.getContestantId() > 9)) {
                    throw new MessageException("Invalid contestant id!", inMessage);
                } else if ((inMessage.getContestantState() < ContestantStates.SEAT_AT_THE_BENCH)
                        || (inMessage.getContestantState() > ContestantStates.DO_YOUR_BEST)) {
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                break;
            case MessageType.SETCONTESTANTINTRIAL:
                if ((inMessage.getContestantPos() < 0) || (inMessage.getContestantPos() > 5)) {
                    throw new MessageException("Invalid contestant pos in trial!", inMessage);
                }
                break;
            case MessageType.SETCONTESTANTSTRENGTH:
                if ((inMessage.getContestantStrength() < 0)
                        || (inMessage.getContestantStrength() > GameConstants.MAX_STRENGTH)) {
                    throw new MessageException("Invalid contestant strength!", inMessage);
                }
                break;

            case MessageType.SETTRIALNUMBER:
                if (inMessage.getTrialNumber() < 0) {
                    throw new MessageException("Invalid trial number!", inMessage);
                }
                break;

            case MessageType.REPORTNEWGAME:
                if (inMessage.getGameNumber() < 0) {
                    throw new MessageException("Invalid game number!", inMessage);
                }
                break;

            case MessageType.REPORTGAMEEND:
                if (inMessage.getGameNumber() < 0) {
                    throw new MessageException("Invalid game number!", inMessage);

                } else if (inMessage.getTrialNumber() < 0) {
                    throw new MessageException("Invalid trial number!", inMessage);
                }
                break;

            case MessageType.REPORTMATCHEND:
                if (inMessage.getWins1() < 0 || inMessage.getWins2() < 0) {
                    throw new MessageException("Invalid number of wins!", inMessage);
                }
                break;

            case MessageType.SETROPESTATE:
                break;

            case MessageType.SHUT:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType()) {
            // case MessageType.SETNFIC:
            // repos.initSimul(inMessage.getLogFName());
            // outMessage = new Message(MessageType.NFICDONE);
            // break;

            case MessageType.SETCOACHSTATE:
                repos.setCoachState(inMessage.getCoachId(), inMessage.getCoachState());
                outMessage = new Message(MessageType.SETCOACHSTATEDONE);
                break;

            case MessageType.SETCONTESTANTSTATE:
                repos.setContestantState(inMessage.getContestantId(), inMessage.getContestantState());
                outMessage = new Message(MessageType.SETCONTESTANTSTATEDONE);
                break;

            case MessageType.SETCONTESTANTSTRENGTH:
                repos.setContestantStrength(inMessage.getContestantId(), inMessage.getContestantStrength());
                outMessage = new Message(MessageType.SETCONTESTANTSTRENGTHDONE);
                break;

            case MessageType.SETCONTESTANTINTRIAL:
                repos.setContestantInTrial(inMessage.getContestantId(), inMessage.getContestantPos());
                outMessage = new Message(MessageType.SETCONTESTANTINTRIALDONE);
                break;

            case MessageType.SETREFEREESTATE:
                repos.setRefereeState(inMessage.getRefereeState());
                outMessage = new Message(MessageType.SETREFEREESTATEDONE);
                break;

            case MessageType.SETROPESTATE:
                repos.setRopeState(inMessage.getRopeState());
                outMessage = new Message(MessageType.SETROPESTATEDONE);
                break;

            case MessageType.SETTRIALNUMBER:
                repos.setTrialNumber(inMessage.getTrialNumber());
                outMessage = new Message(MessageType.SETTRIALNUMBERDONE);
                break;

            case MessageType.REPORTNEWGAME:
                repos.reportNewGame(inMessage.getGameNumber());
                outMessage = new Message(MessageType.REPORTNEWGAMEDONE);
                break;

            case MessageType.REPORTGAMEEND:
                repos.reportGameEnd(inMessage.getGameNumber(), inMessage.getGameWinner(), inMessage.getTrialNumber(),
                        inMessage.getKnockOut());
                outMessage = new Message(MessageType.REPORTGAMEENDDONE);
                break;

            case MessageType.REPORTMATCHEND:
                repos.reportMatchEnd(inMessage.getWins1(), inMessage.getWins2());
                outMessage = new Message(MessageType.REPORTMATCHENDDONE);
                break;

            case MessageType.SHUT:
                repos.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;

        }

        return outMessage;
    }
}
