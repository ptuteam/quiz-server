package game;

import utils.ConfigGeneral;
import websocket.WebSocketService;

/**
 * alex on 22.10.15.
 */
public class GameFieldImpl implements GameField {
    private static final int START_IDLE_TIME = 5 * 1000;
    private static final int GAME_TIME = 5 * 60 * 1000;
    private static final int STEP_TIME = 5 * 100;

    private boolean playing;
    private int nextQuestion;

    private final WebSocketService webSocketService;
    private final GameSession session;

    private final QuestionHelper questionHelper = new QuestionHelper();

    public GameFieldImpl(WebSocketService webSocketService, GameSession session) {
        this.webSocketService = webSocketService;
        this.session = session;
        nextQuestion = 0;
    }

    @Override
    public void checkPlayerAnswer(Player player, String answer) {
        if (questionHelper.checkAnswer(nextQuestion, answer)) {
            increasePlayerScore(player);
        }
        nextQuestion++;
        if (nextQuestion < QuestionHelper.QUESTIONS_COUNT) {
            askNextQuestion();
        }
    }

    private void askNextQuestion() {
        webSocketService.notifyNewQuestion(session.getPlayers(), questionHelper.getQuestionWithAnswersJson(nextQuestion));
    }

    private void increasePlayerScore(Player player) {
        session.increaseScore(player.getUserProfile(), ConfigGeneral.getPointsPerQuestion());
        webSocketService.notifyNewScore(session.getPlayers(), player);
    }

    @Override
    public void play() {
        playing = true;
        webSocketService.notifyStartGame(session.getPlayers());
        new Thread(() -> {
            try {
                Thread.sleep(START_IDLE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run();
        }).start();
    }

    private void run() {
        askNextQuestion();
        while (playing) {
            gmStep();
            try {
                Thread.sleep(STEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void gmStep() {
        if (session.getSessionTime() > GAME_TIME || nextQuestion > QuestionHelper.QUESTIONS_COUNT - 1) {
            gameOver();
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