package serverSide.sharedRegions;

import clientSide.entities.CoachStates;
import clientSide.entities.Contestant;
import clientSide.entities.ContestantStates;
import clientSide.entities.GameConstants;
import clientSide.entities.RefereeStates;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.PlaygroundClientProxy;

public class PlaygroundInterface {
    private final Playground playground;

    public PlaygroundInterface(Playground playground) {
        this.playground = playground;
    }

    /**
     * Processing of incoming messages
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        /* Validation of the incoming message */
        switch (inMessage.getMsgType()) {
            case MessageType.GETREADY:
                if (inMessage.getContestantId() < 0 || inMessage.getContestantId() > 9) {
                    throw new MessageException("Invalid contestant id!", inMessage);
                } else if (inMessage.getContestantState() < ContestantStates.SEAT_AT_THE_BENCH
                        || inMessage.getContestantState() > ContestantStates.DO_YOUR_BEST) {
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                break;

            case MessageType.PULLTHEROPE:
                if (inMessage.getContestantId() < 0 || inMessage.getContestantId() > 9) {
                    throw new MessageException("Invalid contestant id!", inMessage);
                } else if (inMessage.getContestantState() < ContestantStates.SEAT_AT_THE_BENCH
                        || inMessage.getContestantState() > ContestantStates.DO_YOUR_BEST) {
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                break;

            case MessageType.STARTTRIAL:
                if (inMessage.getRefereeState() < RefereeStates.START_OF_THE_MATCH
                        || inMessage.getRefereeState() > RefereeStates.END_OF_THE_MATCH) {
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;

            case MessageType.ANNOUNCENEWGAME:
                if (inMessage.getRefereeState() < RefereeStates.START_OF_THE_MATCH
                        || inMessage.getRefereeState() > RefereeStates.END_OF_THE_MATCH) {
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;

            case MessageType.DECLAREGAMEWINNER:
                if (inMessage.getRefereeState() < RefereeStates.START_OF_THE_MATCH
                        || inMessage.getRefereeState() > RefereeStates.END_OF_THE_MATCH) {
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;

            case MessageType.DECLAREMATCHWINNER:
                if (inMessage.getRefereeState() < RefereeStates.START_OF_THE_MATCH
                        || inMessage.getRefereeState() > RefereeStates.END_OF_THE_MATCH) {
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;

            case MessageType.REVIEWNOTES:
                if (inMessage.getCoachId() < 0 || inMessage.getCoachId() > 1) {
                    throw new MessageException("Invalid coach id!", inMessage);
                } else if (inMessage.getCoachState() < CoachStates.WAIT_FOR_REFEREE_COMMAND
                        || inMessage.getCoachState() > CoachStates.WATCH_TRIAL) {
                    throw new MessageException("Invalid coach state!", inMessage);
                }
                break;

            case MessageType.ASSERTTRIALDECISION:
            case MessageType.AMDONE:
            case MessageType.MATCHENDED:
            case MessageType.GAMEENDED:
            case MessageType.SHUT:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* Processing of the incoming message */
        switch (inMessage.getMsgType()) {
            case MessageType.MATCHENDED:
                if (playground.matchEnded()) {
                    outMessage = new Message(MessageType.MATCHENDEDDONE);
                } else {
                    outMessage = new Message(MessageType.NONE);
                }

                break;

            case MessageType.GAMEENDED:
                if (playground.matchEnded()) {
                    outMessage = new Message(MessageType.GAMEENDEDDONE);
                } else {
                    outMessage = new Message(MessageType.NONE);
                }

                break;

            case MessageType.AMDONE:
                playground.amDone();

                outMessage = new Message(MessageType.AMDONEDONE);

                break;

            case MessageType.GETREADY:
                ((PlaygroundClientProxy) Thread.currentThread()).setContestantId(inMessage.getContestantId());
                ((PlaygroundClientProxy) Thread.currentThread())
                        .setContestantState(inMessage.getContestantState());

                playground.getReady();

                outMessage = new Message(MessageType.GETREADYDONE, GameConstants.TYPE_CONTESTANT,
                        ((PlaygroundClientProxy) Thread.currentThread()).getContestantId(),
                        ((PlaygroundClientProxy) Thread.currentThread()).getContestantState());

                break;

            case MessageType.PULLTHEROPE:
                ((PlaygroundClientProxy) Thread.currentThread()).setContestantId(inMessage.getContestantId());
                ((PlaygroundClientProxy) Thread.currentThread()).setContestantState(inMessage.getContestantState());
                ((PlaygroundClientProxy) Thread.currentThread())
                        .setContestantStrength(inMessage.getContestantStrength());

                playground.pullTheRope();

                outMessage = new Message(
                        MessageType.PULLTHEROPEDONE, GameConstants.TYPE_CONTESTANT,
                        ((PlaygroundClientProxy) Thread.currentThread()).getContestantId(),
                        ((PlaygroundClientProxy) Thread.currentThread()).getContestantState(),
                        ((PlaygroundClientProxy) Thread.currentThread()).getContestantStrength());

                break;

            case MessageType.STARTTRIAL:
                ((PlaygroundClientProxy) Thread.currentThread()).setRefereeState(inMessage.getRefereeState());

                playground.startTrial();

                outMessage = new Message(MessageType.STARTTRIALDONE, GameConstants.TYPE_REFEREE,
                        0, ((PlaygroundClientProxy) Thread.currentThread()).getRefereeState());

                break;

            case MessageType.ASSERTTRIALDECISION:
                if (playground.assertTrialDecision()) {
                    outMessage = new Message(MessageType.ASSERTTRIALDECISIONDONE);
                } else {
                    outMessage = new Message(MessageType.NONE);
                }

                break;

            case MessageType.ANNOUNCENEWGAME:
                ((PlaygroundClientProxy) Thread.currentThread()).setRefereeState(inMessage.getRefereeState());

                playground.announceNewGame();

                outMessage = new Message(MessageType.ANNOUNCENEWGAMEDONE, GameConstants.TYPE_REFEREE,
                        0, ((PlaygroundClientProxy) Thread.currentThread()).getRefereeState());

                break;

            case MessageType.DECLAREGAMEWINNER:
                ((PlaygroundClientProxy) Thread.currentThread()).setRefereeState(inMessage.getRefereeState());

                playground.declareGameWinner();

                outMessage = new Message(MessageType.DECLAREGAMEWINNERDONE, GameConstants.TYPE_REFEREE,
                        0, ((PlaygroundClientProxy) Thread.currentThread()).getRefereeState());

                break;

            case MessageType.DECLAREMATCHWINNER:
                ((PlaygroundClientProxy) Thread.currentThread()).setRefereeState(inMessage.getRefereeState());

                playground.declareMatchWinner();

                outMessage = new Message(MessageType.DECLAREMATCHWINNERDONE, GameConstants.TYPE_REFEREE,
                        0, ((PlaygroundClientProxy) Thread.currentThread()).getRefereeState());

                break;

            case MessageType.REVIEWNOTES:
                ((PlaygroundClientProxy) Thread.currentThread()).setCoachId(inMessage.getCoachId());
                ((PlaygroundClientProxy) Thread.currentThread())
                        .setCoachState(inMessage.getCoachState());

                playground.reviewNotes();

                outMessage = new Message(MessageType.REVIEWNOTESDONE, GameConstants.TYPE_COACH,
                        ((PlaygroundClientProxy) Thread.currentThread()).getCoachId(),
                        ((PlaygroundClientProxy) Thread.currentThread()).getCoachState());

                break;

            case MessageType.SHUT:
                playground.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return outMessage;
    }
}
