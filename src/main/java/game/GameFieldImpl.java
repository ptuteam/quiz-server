package game;

import utils.ConfigGeneral;
import websocket.WebSocketService;

/**
 * alex on 22.10.15.
 */
public class GameFieldImpl implements GameField {

    private boolean playing;
    private int currentRound;
    private Question currentQuestion;
    private int nextQuestionType;

    private final WebSocketService webSocketService;
    private final GameSession session;

    private final QuestionHelper questionHelper = new QuestionHelper();

    public GameFieldImpl(WebSocketService webSocketService, GameSession session) {
        this.webSocketService = webSocketService;
        this.session = session;
        currentRound = 0;
        nextQuestionType = Question.DEFAULT_QUESTION_TYPE;
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
    public void setPlayerAnswer(Player player, String answer) {
        session.setPlayerAnswer(player, answer, currentRound);
    }

    private void checkPlayersAnswers() {
        session.getPlayers().stream().filter(player -> questionHelper.checkAnswer(
                currentQuestion, session.getPlayerAnswer(player, currentRound))
        ).forEach(this::increasePlayerScore);
        nextQuestionType = Question.DEFAULT_QUESTION_TYPE;
    }

    private void askQuestion() {
        currentQuestion = questionHelper.getRandomQuestion(nextQuestionType, session.getAskedQuestions());
        webSocketService.notifyNewQuestion(session.getPlayers(), currentQuestion);
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