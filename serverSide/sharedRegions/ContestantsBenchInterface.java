package serverSide.sharedRegions;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import genclass.GenericIO;
import clientSide.entities.CoachStates;
import clientSide.entities.ContestantStates;
import clientSide.entities.GameConstants;
import serverSide.entities.ContestantsBenchClientProxy;

public class ContestantsBenchInterface {
    private final ContestantsBench contestantsBench;

    public ContestantsBenchInterface(ContestantsBench contestantsBench) {
        this.contestantsBench = contestantsBench;
    }

    /**
     * Processing of incoming messages
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        /* Validation of the incoming message */
        switch (inMessage.getMsgType()) {
            case MessageType.WAITFORCOACHCALL:
            case MessageType.SEATDOWN:
                if (inMessage.getContestantId() < 0 || inMessage.getContestantId() > 9) {
                    throw new MessageException("Invalid contestant id!", inMessage);
                } else if (inMessage.getContestantStrength() < 0
                        || inMessage.getContestantStrength() > GameConstants.MAX_STRENGTH) {
                    throw new MessageException("Invalid contestant strength!", inMessage);
                } else if (inMessage.getContestantState() < ContestantStates.SEAT_AT_THE_BENCH
                        || inMessage.getContestantState() > ContestantStates.DO_YOUR_BEST) {
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                break;
            case MessageType.FOLLOWCOACHADVICE:
                if (inMessage.getContestantId() < 0 || inMessage.getContestantId() > 9) {
                    throw new MessageException("Invalid contestant id!", inMessage);
                } else if (inMessage.getContestantState() < ContestantStates.SEAT_AT_THE_BENCH
                        || inMessage.getContestantState() > ContestantStates.DO_YOUR_BEST) {
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                break;
            case MessageType.CALLCONTESTANTS:
                if (inMessage.getCoachId() < 0 || inMessage.getCoachId() > 1) {
                    throw new MessageException("Invalid coach id!", inMessage);
                } else if (inMessage.getCoachState() < CoachStates.WAIT_FOR_REFEREE_COMMAND
                        || inMessage.getCoachState() > CoachStates.WATCH_TRIAL) {
                    throw new MessageException("Invalid coach state!", inMessage);
                }
                break;
            case MessageType.ENDMATCH:
            case MessageType.SHUT:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* Processing of the incoming message */
        switch (inMessage.getMsgType()) {
            case MessageType.WAITFORCOACHCALL:
                ((ContestantsBenchClientProxy) Thread.currentThread()).setContestantId(inMessage.getContestantId());
                ((ContestantsBenchClientProxy) Thread.currentThread())
                        .setContestantState(inMessage.getContestantState());
                ((ContestantsBenchClientProxy) Thread.currentThread())
                        .setContestantStrength(inMessage.getContestantStrength());

                contestantsBench.waitForCoachCall();

                outMessage = new Message(
                        MessageType.WAITFORCOACHCALLDONE,
                        GameConstants.TYPE_CONTESTANT,
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getContestantId(),
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getContestantState(),
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getContestantStrength());

                break;

            case MessageType.FOLLOWCOACHADVICE:
                ((ContestantsBenchClientProxy) Thread.currentThread()).setContestantId(inMessage.getContestantId());
                ((ContestantsBenchClientProxy) Thread.currentThread())
                        .setContestantState(inMessage.getContestantState());

                contestantsBench.followCoachAdvice();

                outMessage = new Message(MessageType.FOLLOWCOACHADVICEDONE, GameConstants.TYPE_CONTESTANT,
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getContestantId(),
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getContestantState());

                break;

            case MessageType.SEATDOWN:
                ((ContestantsBenchClientProxy) Thread.currentThread()).setContestantId(inMessage.getContestantId());
                ((ContestantsBenchClientProxy) Thread.currentThread())
                        .setContestantState(inMessage.getContestantState());
                ((ContestantsBenchClientProxy) Thread.currentThread())
                        .setContestantStrength(inMessage.getContestantStrength());

                contestantsBench.seatDown();

                outMessage = new Message(
                        MessageType.SEATDOWNDONE,
                        GameConstants.TYPE_CONTESTANT,
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getContestantId(),
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getContestantState(),
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getContestantStrength());

                break;

            case MessageType.CALLCONTESTANTS:
                ((ContestantsBenchClientProxy) Thread.currentThread()).setCoachId(inMessage.getCoachId());
                ((ContestantsBenchClientProxy) Thread.currentThread())
                        .setCoachState(inMessage.getCoachState());

                contestantsBench.callContestants();

                outMessage = new Message(MessageType.CALLCONTESTANTSDONE, GameConstants.TYPE_COACH,
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getCoachId(),
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getCoachState());

                break;

            case MessageType.ENDMATCH:
                ((ContestantsBenchClientProxy) Thread.currentThread()).setCoachId(inMessage.getCoachId());
                ((ContestantsBenchClientProxy) Thread.currentThread())
                        .setCoachState(inMessage.getCoachState());

                contestantsBench.endMatch();

                outMessage = new Message(MessageType.ENDMATCHDONE, GameConstants.TYPE_COACH,
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getCoachId(),
                        ((ContestantsBenchClientProxy) Thread.currentThread()).getCoachState());

                break;

            case MessageType.SHUT:
                contestantsBench.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return outMessage;
    }
}
