package game;

import utils.ConfigGeneral;
import websocket.WebSocketService;

/**
 * alex on 22.10.15.
 */
public class GameFieldImpl implements GameField {

    private boolean playing;
    private int currentRound;

    private final WebSocketService webSocketService;
    private final GameSession session;

    private final QuestionHelper questionHelper = new QuestionHelper();

    public GameFieldImpl(WebSocketService webSocketService, GameSession session) {
        this.webSocketService = webSocketService;
        this.session = session;
        currentRound = 0;
    }

    private void newRoundStart() {
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
    public void checkPlayerAnswer(Player player, String answer) {
        if (questionHelper.checkAnswer(currentRound % QuestionHelper.QUESTIONS_COUNT + 1, answer)) {
            webSocketService.notifyOnCorrectAnswer(player, true);
            increasePlayerScore(player);
        } else {
            webSocketService.notifyOnCorrectAnswer(player, false);
        }
    }

    private void askQuestion() {
        webSocketService.notifyNewQuestion(session.getPlayers(), questionHelper.getQuestion(currentRound % QuestionHelper.QUESTIONS_COUNT + 1));
    }

    private void increasePlayerScore(Player player) {
        session.increaseScore(player.getUserProfile(), ConfigGeneral.getPointsPerQuestion());
    }

    @Override
    public void play() {
        playing = true;
        webSocketService.notifyStartGame(session.getPlayers());
        new Thread(() -> {
            try {
                Thread.sleep(ConfigGeneral.getTimeForWaitingStartGameMS());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run();
        }).start();
    }

    private void run() {
        while (playing) {
            gameStep();
            try {
                Thread.sleep(ConfigGeneral.getTimePerQuestionMS());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            webSocketService.notifyNewScores(session.getPlayers());
        }
    }

    private void gameStep() {
        if (session.getSessionTime() > ConfigGeneral.getMaxGameTimeMS() ||
                (currentRound + 1 > ConfigGeneral.getMinRoundsPerGameCount() && session.getWinner() != null)) {
            gameOver();
        } else {
            newRoundStart();
        }
    }

    @Override
    public void gameOver() {
        webSocketService.notifyGameOver(session.getPlayers(), session.getWinner());
        session.stopGame();
        pause();
    }

    private void pause() {
        playing = false;
    }
}