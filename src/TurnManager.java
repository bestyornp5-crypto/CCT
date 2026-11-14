public class TurnManager {

    // ==========================================
    // ATTRIBUTES
    // ==========================================

    private Player[] players;

    private int currentPlayerIndex;
    private int totalTurns;
    private int consecutiveDoubles;

    private boolean extraTurn;
    private boolean turnInProgress;
    private boolean gameOver;

    private Player winner;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public TurnManager(Player[] players) {

        if (players == null || players.length < 2) {
            throw new IllegalArgumentException(
                    "A game requires at least two players."
            );
        }

        this.players = new Player[players.length];

        for (int i = 0; i < players.length; i++) {

            if (players[i] == null) {
                throw new IllegalArgumentException(
                        "Player cannot be null."
                );
            }

            this.players[i] = players[i];
        }

        this.currentPlayerIndex = 0;
        this.totalTurns = 0;
        this.consecutiveDoubles = 0;

        this.extraTurn = false;
        this.turnInProgress = false;
        this.gameOver = false;

        this.winner = null;
    }


    // ==========================================
    // TURN LIFECYCLE
    // ==========================================

    // Begins a new turn.
    public Player StartTurn() {

        if (gameOver) {
            throw new IllegalStateException(
                    "The game has already ended."
            );
        }

        if (turnInProgress) {
            throw new IllegalStateException(
                    "A turn is already in progress."
            );
        }

        turnInProgress = true;

        return GetCurrentPlayer();
    }


    // Completes the current turn.
    public void EndTurn() {

        if (gameOver) {
            throw new IllegalStateException(
                    "The game has already ended."
            );
        }

        if (!turnInProgress) {
            throw new IllegalStateException(
                    "No active turn to complete."
            );
        }

        totalTurns++;

        turnInProgress = false;

        if (!extraTurn) {

            MoveToNextPlayer();

            consecutiveDoubles = 0;

        } else {

            extraTurn = false;
        }
    }


    // ==========================================
    // DICE MANAGEMENT
    // ==========================================

    // Processes the result of a dice roll.
    public void RegisterDiceRoll(Dice dice) {

        if (dice == null) {
            throw new IllegalArgumentException(
                    "Dice cannot be null."
            );
        }

        if (!turnInProgress) {
            throw new IllegalStateException(
                    "Cannot register a roll outside an active turn."
            );
        }

        if (dice.IsDouble()) {

            consecutiveDoubles++;

            extraTurn = true;

        } else {

            consecutiveDoubles = 0;

            extraTurn = false;
        }
    }


    // ==========================================
    // PLAYER ROTATION
    // ==========================================

    private void MoveToNextPlayer() {

        currentPlayerIndex =
                (currentPlayerIndex + 1) % players.length;
    }


    // ==========================================
    // GAME STATE
    // ==========================================

    // Declares a winner and terminates the game.
    public void DeclareWinner(Player player) {

        if (player == null) {
            throw new IllegalArgumentException(
                    "Winner cannot be null."
            );
        }

        if (gameOver) {
            throw new IllegalStateException(
                    "The game has already ended."
            );
        }

        boolean validPlayer = false;

        for (int i = 0; i < players.length; i++) {

            if (players[i] == player) {
                validPlayer = true;
                break;
            }
        }

        if (!validPlayer) {
            throw new IllegalArgumentException(
                    "Winner does not belong to this game."
            );
        }

        winner = player;
        gameOver = true;
        turnInProgress = false;
        extraTurn = false;
    }


    // ==========================================
    // GETTERS
    // ==========================================

    public Player GetCurrentPlayer() {

        return players[currentPlayerIndex];
    }


    public int GetCurrentPlayerIndex() {

        return currentPlayerIndex;
    }


    public int GetTotalTurns() {

        return totalTurns;
    }


    public int GetConsecutiveDoubles() {

        return consecutiveDoubles;
    }


    public boolean HasExtraTurn() {

        return extraTurn;
    }


    public boolean IsTurnInProgress() {

        return turnInProgress;
    }


    public boolean IsGameOver() {

        return gameOver;
    }


    public Player GetWinner() {

        return winner;
    }


    public int GetNumberOfPlayers() {

        return players.length;
    }


    // ==========================================
    // RESET
    // ==========================================

    public void Reset() {

        currentPlayerIndex = 0;
        totalTurns = 0;
        consecutiveDoubles = 0;

        extraTurn = false;
        turnInProgress = false;
        gameOver = false;

        winner = null;
    }

}