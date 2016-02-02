package game;

import utils.ConfigGeneral;
import websocket.WebSocketService;

import java.util.Objects;

/**
 * alex on 22.10.15.
 */
public class BlitzGame extends AbstractGame {

    public BlitzGame(WebSocketService webSocketService, GameSession session) {
        super(webSocketService, session, GameMode.BLITZ);
    }

    @Override
    protected void newRoundStart() {
        ++currentRound;
        webSocketService.notifyNewRound(session.getPlayers(), currentRound);
        try {
            Thread.sleep(ConfigGeneral.getTimeForWaitingNewRoundStartMS());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        askQuestion();
    }

    @Override
    protected void checkPlayersAnswers() {
        session.getPlayers().stream().filter(player ->
                Objects.equals(
                        currentQuestion.getCorrectAnswer(),
                        session.getPlayerAnswer(player, currentRound)
                )
        ).forEach(player -> increasePlayerScore(player, ConfigGeneral.getPointsPerQuestion()));
        nextQuestionType = Question.DEFAULT_QUESTION_TYPE;
    }

    @Override
    protected void startGame() {
        webSocketService.notifyStartBlitzGame(session.getPlayers());
    }

    @Override
    protected void run() {
        while (playing) {
            gameStep();
            if (!playing) {
                break;
            }
            try {
                Thread.sleep(ConfigGeneral.getTimePerQuestionMS());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkPlayersAnswers();
            webSocketService.notifyPlayersAnswers(session.getPlayers(), currentQuestion.getCorrectAnswer(), session.getPlayersAnswers(currentRound));
            webSocketService.notifyNewScores(session.getPlayers());
            try {
                Thread.sleep(ConfigGeneral.getTimeForShowingPlayersAnswsMS());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void gameStep() {
        if (session.getSessionTime() > ConfigGeneral.getMaxGameTimeMS() ||
                (currentRound + 1 > ConfigGeneral.getMinRoundsPerGameCount() && session.getWinner() != null)) {
            gameOver();
        } else {
            newRoundStart();
        }
    }

    @Override
    public void setSelectedSegment(int segmentId) {
    }
}