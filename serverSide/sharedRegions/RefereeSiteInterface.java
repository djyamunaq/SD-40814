package serverSide.sharedRegions;

import clientSide.entities.CoachStates;
import clientSide.entities.GameConstants;
import clientSide.entities.RefereeStates;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.RefereeSiteClientProxy;

public class RefereeSiteInterface {

    private final RefereeSite refereeSite;

    public RefereeSiteInterface(RefereeSite refereeSite) {
        this.refereeSite = refereeSite;
    }

    /**
     * Processing of incoming messages
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        /* Validation of the incoming message */
        switch (inMessage.getMsgType()) {
            case MessageType.WAITREFEREECALLTRIAL:
            case MessageType.INFORMREFEREE:
                if (inMessage.getCoachId() < 0 || inMessage.getCoachId() > 1) {
                    throw new MessageException("Invalid coach id!", inMessage);
                } else if (inMessage.getCoachState() < CoachStates.WAIT_FOR_REFEREE_COMMAND
                        || inMessage.getCoachState() > CoachStates.WATCH_TRIAL) {
                    throw new MessageException("Invalid coach state!", inMessage);
                }
                break;

            case MessageType.ENDMATCH:
            case MessageType.CALLTRIAL:
                if (inMessage.getRefereeState() < RefereeStates.START_OF_THE_MATCH
                        || inMessage.getRefereeState() > RefereeStates.END_OF_THE_MATCH) {
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;

            case MessageType.SHUT:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* Processing of the incoming message */
        switch (inMessage.getMsgType()) {
            case MessageType.ENDMATCH:
                ((RefereeSiteClientProxy) Thread.currentThread()).setRefereeState(inMessage.getRefereeState());

                refereeSite.endMatch();

                outMessage = new Message(MessageType.ENDMATCHDONE, GameConstants.TYPE_REFEREE,
                        0,
                        ((RefereeSiteClientProxy) Thread.currentThread()).getRefereeState());

                break;

            case MessageType.WAITREFEREECALLTRIAL:
                ((RefereeSiteClientProxy) Thread.currentThread()).setCoachId(inMessage.getCoachId());
                ((RefereeSiteClientProxy) Thread.currentThread())
                        .setCoachState(inMessage.getCoachState());

                refereeSite.endMatch();

                outMessage = new Message(MessageType.WAITREFEREECALLTRIALDONE, GameConstants.TYPE_COACH,
                        ((RefereeSiteClientProxy) Thread.currentThread()).getCoachId(),
                        ((RefereeSiteClientProxy) Thread.currentThread()).getCoachState());

                break;

            case MessageType.CALLTRIAL:
                ((RefereeSiteClientProxy) Thread.currentThread()).setRefereeState(inMessage.getRefereeState());

                refereeSite.callTrial();

                outMessage = new Message(MessageType.CALLTRIALDONE, GameConstants.TYPE_REFEREE,
                        0, ((RefereeSiteClientProxy) Thread.currentThread()).getRefereeState());

                break;

            case MessageType.INFORMREFEREE:
                ((RefereeSiteClientProxy) Thread.currentThread()).setCoachId(inMessage.getCoachId());
                ((RefereeSiteClientProxy) Thread.currentThread())
                        .setCoachState(inMessage.getCoachState());

                refereeSite.informReferee();

                outMessage = new Message(MessageType.INFORMREFEREEDONE, GameConstants.TYPE_COACH,
                        ((RefereeSiteClientProxy) Thread.currentThread()).getCoachId(),
                        ((RefereeSiteClientProxy) Thread.currentThread()).getCoachState());

                break;

            case MessageType.SHUT:
                refereeSite.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return outMessage;
    }
}
