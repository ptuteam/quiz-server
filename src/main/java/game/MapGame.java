package game;

import utils.ConfigGeneral;
import websocket.WebSocketService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * alex on 29.01.16.
 */
public class MapGame extends AbstractGame {

    private int playerTurn;
    private Player currentPlayer;
    private Set<Integer> currentAllowableMove;
    private int selectedMapSegmentId;
    private String currentOpponent;
    private Integer invadedSegmentId;
    private Map<String, Integer> currentBlitzScores;

    private GameMap gameMap;

    public MapGame(WebSocketService webSocketService, GameSession session) {
        super(webSocketService, session, GameMode.MAPGAME);
        gameMap = new GameMap();
        int i = 0;
        int segmentsPerPlayerCount = gameMap.getMapSegments().size() / session.getPlayers().size();
        for (Player player : session.getPlayers()) {
            for (int j = i * segmentsPerPlayerCount; j < (i + 1) * segmentsPerPlayerCount; ++j) {
                player.increaseScore(gameMap.invadeSegment(player, j));
            }
            ++i;
        }
        playerTurn = -1;
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
    }

    private int getNextPlayerTurn() {
        return ++playerTurn % session.getPlayers().size();
    }

    private void playerTurnStart() {
        int nextPlayerTurn = getNextPlayerTurn();
        currentPlayer = (Player) session.getPlayers().toArray()[nextPlayerTurn];
        currentAllowableMove = gameMap.getAllowableMoveForPlayer(currentPlayer);
        selectedMapSegmentId = -1;
        invadedSegmentId = null;
        currentOpponent = null;
        currentBlitzScores = new HashMap<>();
        webSocketService.notifyPlayerTurnStart(session.getPlayers(), currentPlayer, currentAllowableMove);
        try {
            Thread.sleep(ConfigGeneral.getTimeForWaitingPlayerTurnStartMS());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void playerTurnFinish() {
        webSocketService.notifyPlayerTurnFinish(session.getPlayers(), currentPlayer, invadedSegmentId);
        try {
            Thread.sleep(ConfigGeneral.getTimeForWaitingPlayerTurnFinishMS());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void roundFinish() {
        webSocketService.notifyRoundFinish(session.getPlayers());
        try {
            Thread.sleep(ConfigGeneral.getTimeForWaitingRoundFinishMS());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void attackSegment(int segmentId) {
        nextQuestionType = Question.DEFAULT_QUESTION_TYPE;
        currentBlitzScores.put(currentPlayer.getUserEmail(), 0);
        currentBlitzScores.put(currentOpponent, 0);
        while (true) {
            session.clearPlayerAnswers();
            askQuestion();
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
            if (currentPlayerWinBlitz()) {
                invadedSegmentId = segmentId;
                break;
            } else if (currentOpponentWinBlitz()) {
                invadedSegmentId = null;
                break;
            }
            nextQuestionType = Question.SPECIAL_QUESTION_TYPE;
        }
    }

    private boolean currentPlayerWinBlitz() {
        return currentBlitzScores.get(currentPlayer.getUserEmail()) > currentBlitzScores.get(currentOpponent);
    }

    private boolean currentOpponentWinBlitz() {
        return currentBlitzScores.get(currentOpponent) > currentBlitzScores.get(currentPlayer.getUserEmail());
    }

    @Override
    protected void checkPlayersAnswers() {
        session.getPlayers().stream().filter(player ->
                Objects.equals(
                        currentQuestion.getCorrectAnswer(),
                        session.getPlayerAnswer(player, currentRound)
                )
        ).forEach(player -> currentBlitzScores.put(player.getUserEmail(), currentBlitzScores.get(player.getUserEmail()) + ConfigGeneral.getPointsPerQuestion()));
    }

    @Override
    protected void startGame() {
        webSocketService.notifyStartMapGame(session.getPlayers(), gameMap.getMapSegments());
    }

    @Override
    protected void run() {
        while (playing) {
            gameStep();
            if (!playing) {
                break;
            }
            do {
                playerTurnStart();
                try {
                    Thread.sleep(ConfigGeneral.getTimeForWaitingPlayerSegmentSelect());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (selectedMapSegmentId < 0) {
                    playerTurnFinish();
                    continue;
                }
                attackSegment(selectedMapSegmentId);
                playerTurnFinish();
            } while (playerTurn % (session.getPlayers().size() - 1) != 0 || playerTurn < 1);
            roundFinish();
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
        if (currentAllowableMove.contains(segmentId)) {
            selectedMapSegmentId = segmentId;
            currentOpponent = gameMap.getSegmentOwner(segmentId);
            webSocketService.notifyPlayerSelectSegment(session.getPlayers(), segmentId, currentOpponent);
        } else {
            webSocketService.notifyPlayerIncorrectSelectSegment(currentPlayer);
        }
    }
}
