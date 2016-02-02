package game;

import database.DBService;
import database.DBServiceImpl;
import game.interfaces.BlitzGameInterface;
import game.interfaces.MapGameInterface;
import utils.ConfigGeneral;
import websocket.WebSocketService;

/**
 * alex on 30.01.16.
 */
@SuppressWarnings("RedundantInterfaceDeclaration")
public abstract class AbstractGame implements BlitzGameInterface, MapGameInterface {

    private GameMode mode;
    protected boolean playing;
    protected int currentRound;
    protected Question currentQuestion;
    protected int nextQuestionType;

    protected final WebSocketService webSocketService;
    protected final GameSession session;

    protected final DBService dbService = new DBServiceImpl();

    public AbstractGame(WebSocketService webSocketService, GameSession session, GameMode mode) {
        this.webSocketService = webSocketService;
        this.session = session;
        currentRound = 0;
        nextQuestionType = Question.DEFAULT_QUESTION_TYPE;
        this.mode = mode;
    }

    protected abstract void newRoundStart();

    @Override
    public void setPlayerAnswer(Player player, String answer) {
        session.setPlayerAnswer(player, answer, currentRound);
    }

    protected abstract void checkPlayersAnswers();

    protected void askQuestion() {
        currentQuestion = dbService.getRandomQuestion(nextQuestionType, session.getAskedQuestions());
        webSocketService.notifyNewQuestion(session.getPlayers(), currentQuestion);
    }

    protected void increasePlayerScore(Player player, int score) {
        session.increaseScore(player.getUserProfile(), score);
    }

    protected abstract void startGame();

    @Override
    public void play() {
        playing = true;
        startGame();
        new Thread(() -> {
            try {
                Thread.sleep(ConfigGeneral.getTimeForWaitingStartGameMS());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run();
        }).start();
    }

    protected abstract void run();

    protected abstract void gameStep();

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
