
import java.util.Scanner;
import java.util.HashSet;

class TicTacToeAi {

    private Board board;
    private Scanner sc = new Scanner(System.in);

    private TicTacToeAi() {
        board = new Board();
    }

    private void play() {

        System.out.println("Lets play!");

        while (true) {
            printGameStatus();
            playMove();

            if (board.isGameOver()) {
                printWinner();

                if (!tryAgain()) {
                    break;
                }
            }
        }
    }

    private void playMove() {
        if (board.getTurn() == Board.State.X) { // X is you
            getPlayerMove();
        } else {
            MiniMax.run(board.getTurn(), board, Double.POSITIVE_INFINITY); // 9 is max ply
        }
    }

    private void printGameStatus() {
        System.out.println("\n" + board + "\n");
        System.out.println(board.getTurn().name() + " its your turn!");
    }

    private void getPlayerMove() {
        System.out.print("Index of move: ");

        int move = sc.nextInt();
        if (move >= 7) {
            move = move - 7;
        } else if (move >= 4) {
            move = move - 1;
        } else {
            move = move + 5;
        }
        if (move < 0 || move >= Board.BOARD_WIDTH * Board.BOARD_WIDTH) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe index of the move must be between 0 and "
                    + (Board.BOARD_WIDTH * Board.BOARD_WIDTH - 1) + ", inclusive.");
        } else if (!board.move(move)) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe selected index must be blank.");
        }
    }

    private void printWinner() {
        Board.State winner = board.getWinner();

        System.out.println("\n" + board + "\n");

        if (winner == Board.State.Blank) {
            System.out.println("The TicTacToe is a Draw.");
        } else {
            System.out.println("Player " + winner.toString() + " wins!");
        }
    }

    private boolean tryAgain() {
        if (promptTryAgain()) {
            board.reset();
            System.out.println("Started new game.");
            System.out.println("X's turn.");
            return true;
        }
        return false;
    }

    private boolean promptTryAgain() {
        while (true) {
            System.out.print("Would you like to start a new game? (Y/N): ");
            String response = sc.next();
            if (response.equalsIgnoreCase("y")) {
                return true;
            } else if (response.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Invalid input.");
        }
    }

    public static void main(String[] args) {
        TicTacToeAi ticTacToe = new TicTacToeAi();
        ticTacToe.play();
    }
}

class MiniMax {

    private static double maxPly;

    private MiniMax() {
    }

    static void run(Board.State player, Board board, double maxPly) {
        if (maxPly < 1) {
            throw new IllegalArgumentException("Maximum depth must be greater than 0.");
        }
        MiniMax.maxPly = maxPly;
        miniMax(player, board, 0);
    }

    private static int miniMax(Board.State player, Board board, int currentPly) {
        if (currentPly++ == maxPly || board.isGameOver()) { // base case
            return score(player, board);
        }
        if (board.getTurn() == player) {
            return getMax(player, board, currentPly);
        } else {
            return getMin(player, board, currentPly);
        }
    }

    private static int getMax(Board.State player, Board board, int currentPly) {
        double bestScore = Double.NEGATIVE_INFINITY;
        int indexOfBestMove = -1;
        for (Integer theMove : board.getAvailableMoves()) {
            Board modifiedBoard = board.getDeepCopy();
            modifiedBoard.move(theMove);
            int score = miniMax(player, modifiedBoard, currentPly);
            if (score >= bestScore) {
                bestScore = score;
                indexOfBestMove = theMove;
            }
        }
        board.move(indexOfBestMove);
        return (int) bestScore;
    }

    private static int getMin(Board.State player, Board board, int currentPly) {
        double bestScore = Double.POSITIVE_INFINITY;
        int indexOfBestMove = -1;

        for (Integer theMove : board.getAvailableMoves()) {
            Board modifiedBoard = board.getDeepCopy();
            modifiedBoard.move(theMove);
            int score = miniMax(player, modifiedBoard, currentPly);
            if (score <= bestScore) {
                bestScore = score;
                indexOfBestMove = theMove;
            }
        }
        board.move(indexOfBestMove);
        return (int) bestScore;
    }

    private static int score(Board.State player, Board board) {
        Board.State opponent = (player == Board.State.X) ? Board.State.O : Board.State.X;

        if (board.isGameOver() && board.getWinner() == player) {
            return 10;
        } else if (board.isGameOver() && board.getWinner() == opponent) {
            return -10;
        } else {
            return 0;
        }
    }

}

class Board {

    static final int BOARD_WIDTH = 3;

    public enum State {
        Blank, X, O
    }

    private State[][] board;
    private State playersTurn;
    private State winner;
    private HashSet<Integer> movesAvailable;
    private int moveCount;
    private boolean gameOver;

    Board() {
        board = new State[BOARD_WIDTH][BOARD_WIDTH];
        movesAvailable = new HashSet<>();
        reset();
    }

    private void initialize() {
        for (int row = 0; row < BOARD_WIDTH; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                board[row][col] = State.Blank;
            }
        }
        movesAvailable.clear();
        for (int i = 0; i < BOARD_WIDTH * BOARD_WIDTH; i++) {
            movesAvailable.add(i);
        }
    }

    void reset() {
        moveCount = 0;
        gameOver = false;
        playersTurn = State.X;
        winner = State.Blank;
        initialize();
    }

    public boolean move(int index) {
        return move(index % BOARD_WIDTH, index / BOARD_WIDTH);
    }

    private boolean move(int x, int y) {

        if (board[y][x] == State.Blank) {
            board[y][x] = playersTurn;
        } else {
            return false;
        }

        moveCount++;
        movesAvailable.remove(y * BOARD_WIDTH + x);

        // The game is a draw.
        if (moveCount == BOARD_WIDTH * BOARD_WIDTH) {
            winner = State.Blank;
            gameOver = true;
        }

        // Check for a winner.
        checkRow(y);
        checkColumn(x);
        checkDiagonalFromTopLeft(x, y);
        checkDiagonalFromTopRight(x, y);

        playersTurn = (playersTurn == State.X) ? State.O : State.X;
        return true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    State[][] toArray() {
        return board.clone();
    }

    public State getTurn() {
        return playersTurn;
    }

    public State getWinner() {
        return winner;
    }

    public HashSet<Integer> getAvailableMoves() {
        return movesAvailable;
    }

    private void checkRow(int row) {
        for (int i = 1; i < BOARD_WIDTH; i++) {
            if (board[row][i] != board[row][i - 1]) {
                break;
            }
            if (i == BOARD_WIDTH - 1) {
                winner = playersTurn;
                gameOver = true;
            }
        }
    }

    private void checkColumn(int column) {
        for (int i = 1; i < BOARD_WIDTH; i++) {
            if (board[i][column] != board[i - 1][column]) {
                break;
            }
            if (i == BOARD_WIDTH - 1) {
                winner = playersTurn;
                gameOver = true;
            }
        }
    }

    private void checkDiagonalFromTopLeft(int x, int y) {
        if (x == y) {
            for (int i = 1; i < BOARD_WIDTH; i++) {
                if (board[i][i] != board[i - 1][i - 1]) {
                    break;
                }
                if (i == BOARD_WIDTH - 1) {
                    winner = playersTurn;
                    gameOver = true;
                }
            }
        }
    }

    private void checkDiagonalFromTopRight(int x, int y) {
        if (BOARD_WIDTH - 1 - x == y) {
            for (int i = 1; i < BOARD_WIDTH; i++) {
                if (board[BOARD_WIDTH - 1 - i][i] != board[BOARD_WIDTH - i][i - 1]) {
                    break;
                }
                if (i == BOARD_WIDTH - 1) {
                    winner = playersTurn;
                    gameOver = true;
                }
            }
        }
    }

    public Board getDeepCopy() {
        Board board = new Board();
        for (int i = 0; i < board.board.length; i++) {
            board.board[i] = this.board[i].clone();
        }
        board.playersTurn = this.playersTurn;
        board.winner = this.winner;
        board.movesAvailable = new HashSet<>();
        board.movesAvailable.addAll(this.movesAvailable);
        board.moveCount = this.moveCount;
        board.gameOver = this.gameOver;
        return board;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < BOARD_WIDTH; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {

                if (board[y][x] == State.Blank) {
                    sb.append("-");
                } else {
                    sb.append(board[y][x].name());
                }
                sb.append(" ");
            }
            if (y != BOARD_WIDTH - 1) {
                sb.append("\n");
            }
        }
        return new String(sb);
    }
}
