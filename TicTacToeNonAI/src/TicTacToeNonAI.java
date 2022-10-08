import java.util.Scanner;

public class TicTacToeNonAI {
    static int[][] board = { { 2, 2, 2 }, { 2, 2, 2 }, { 2, 2, 2 } };
    static int userinput;
    static int[] steps = new int[9];
    static int pos;
    static int turn;
    static int Uchoice;

    static void drawBoard() {
        for (int i = 0; i < 3; i++) {
            System.out.print(" _");
            for (int j = 0; j < 3; j++) {
                switch (board[i][j]) {
                    case 2:
                        System.out.print(" |");
                        break;
                    case 3:
                        System.out.print("X|");
                        break;
                    case 5:
                        System.out.print("O|");
                        break;
                }
                if (j <= 1) {
                    System.out.print("_");
                }

            }
            System.out.println();
        }
        System.out.println();
    }

    static void go(int position) {

        int posx, posy;
        posx = position / 3;
        posy = position % 3;
        if (ifmovpossible(position) == 1) {
            steps[turn] = position;
            if (turn % 2 == 0) {
                board[posx][posy] = 3;// starts from 0 and is value of X

            } else {
                board[posx][posy] = 5;// starts from 1 and is value of 0
            }

        } else {
            System.out.println("Entered position not empty!");
            getinput();
        }

    }

    static int ifmovpossible(int position) {
        int posx, posy;
        posx = position / 3;
        posy = position % 3;
        if (board[posx][posy] == 2) {
            return 1;
        } else {
            return 0;
        }
    }

    static void getinput() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter position :");
        pos = sc.nextInt();
        while (pos < 1 || pos > 9) {
            System.out.println("Enter valid number!");
            getinput();
        }
        if (pos >= 7) {
            pos = pos - 7;
            go(pos);
        } else if (pos >= 4) {
            pos = pos - 1;
            go(pos);
        } else {
            pos = pos + 5;
            go(pos);
        }
    }

    static int bestmov() {
        int move = 4;
        if (ifmovpossible(4) == 1) {
            move = 4;
        } else if (ifmovpossible(0) == 1) {
            move = 0;
        } else if (ifmovpossible(2) == 1) {
            move = 2;
        } else if (ifmovpossible(6) == 1) {
            move = 6;
        } else if (ifmovpossible(8) == 1) {
            move = 8;
        } else if (ifmovpossible(1) == 1) {
            move = 1;
        } else if (ifmovpossible(5) == 1) {
            move = 5;
        } else if (ifmovpossible(7) == 1) {
            move = 7;
        } else if (ifmovpossible(3) == 1) {
            move = 3;
        }
        return move;
    }

    static int checkwin(int lasmovpos){
        int posx, posy;
        posx = lasmovpos / 3;
        posy = lasmovpos % 3;
        // if (turn < 9) {
        if (lasmovpos % 2 != 0 && turn < 9) { // check if the las move was a non corner so that we only need to check 2
            // directions
            int rowprod = board[posx][posy] * board[posx][(posy + 1) % 3] * board[posx][(posy + 2) % 3]; // row
                                                                                                         // check
            int colprod = board[posx][posy] * board[(posx + 1) % 3][posy] * board[(posx + 2) % 3][posy]; // col
                                                                                                         // check
            // win
            if (rowprod == 27 || rowprod == 125) {
                return 1;
            } else if (colprod == 27 || colprod == 125) {
                return 1;
            }
        } else if (lasmovpos % 2 == 0 && turn < 9) {
            int rowprod = board[posx][posy] * board[posx][(posy + 1) % 3] * board[posx][(posy + 2) % 3]; // row
                                                                                                         // check
            int colprod = board[posx][posy] * board[(posx + 1) % 3][posy] * board[(posx + 2) % 3][posy]; // col
                                                                                                         // check
            int diaprod = board[posx][posy] * board[(posx + 1) % 3][(posy + 1) % 3]
                    * board[(posx + 2) % 3][(posy + 2) % 3]; // dia check
            if (rowprod == 27 || rowprod == 125) {
                return 1;
            } else if (colprod == 27 || colprod == 125) {
                return 1;
            } else if (diaprod == 27 || diaprod == 125) {
                return 1;
            }
        } else {
            // System.out.println("The Game was a Tie!");
            return 2;
        }
        // System.out.println("The Game was a Tie!");
        return 0;
    }

    static int poswin(int lasmovpos) {
        int posx, posy;
        posx = lasmovpos / 3;
        posy = lasmovpos % 3;
        if (lasmovpos % 2 != 0) { // check if the las move was a non corner so that we only need to check 2
                                  // directions
            int rowprod = board[posx][posy] * board[posx][(posy + 1) % 3] * board[posx][(posy + 2) % 3]; // row check
            int colprod = board[posx][posy] * board[(posx + 1) % 3][posy] * board[(posx + 2) % 3][posy]; // col check
            // win
            if (rowprod == 18 || rowprod == 50) {
                if (board[posx][(posy + 1) % 3] == 2) {
                    return (posx * 3) + ((posy + 1) % 3);
                } else {
                    return (posx * 3) + ((posy + 2) % 3);
                }
            } else if (colprod == 18 || colprod == 50) {
                if (board[(posx + 1) % 3][posy] == 2) {
                    return (((posx + 1) % 3) * 3) + posy;
                } else {
                    return (((posx + 2) % 3) * 3) + posy;
                }
            }
        } else {
            int rowprod = board[posx][posy] * board[posx][(posy + 1) % 3] * board[posx][(posy + 2) % 3]; // row check
            int colprod = board[posx][posy] * board[(posx + 1) % 3][posy] * board[(posx + 2) % 3][posy]; // col check
            int diaprod = board[posx][posy] * board[(posx + 1) % 3][(posy + 1) % 3]
                    * board[(posx + 2) % 3][(posy + 2) % 3]; // dia check
            if (rowprod == 18 || rowprod == 50) {
                if (board[posx][(posy + 1) % 3] == 2) {
                    return (posx * 3) + ((posy + 1) % 3);
                } else {
                    return (posx * 3) + ((posy + 2) % 3);
                }
            } else if (colprod == 18 || colprod == 50) {
                if (board[(posx + 1) % 3][posy] == 2) {
                    return (((posx + 1) % 3) * 3) + posy;
                } else {
                    return (((posx + 2) % 3) * 3) + posy;
                }
            } else if (diaprod == 18 || diaprod == 50) {
                if (board[(posx + 1) % 3][(posy + 1) % 3] == 2) {
                    return (((posx + 1) % 3) * 3) + ((posy + 1) % 3);
                } else {
                    return (((posx + 2) % 3) * 3) + ((posy + 2) % 3);
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose 1 for X or 0 for O :");
        Uchoice = sc.nextInt();
        while (Uchoice != 1 && Uchoice != 0) {
            System.out.println("Please Enter valid Input");
            Uchoice = sc.nextInt();
        }
        System.out.println("Game started ");
        drawBoard();

        while (turn < 9) {
            if (turn < 3) {
                if ((Uchoice == 1 && turn % 2 == 0) || (Uchoice == 0 && turn % 2 != 0)) {
                    getinput();
                } else {
                    go(bestmov());
                }
            } else {
                if ((Uchoice == 1 && turn % 2 == 0) || (Uchoice == 0 && turn % 2 != 0)) {
                    getinput();
                } else { // for non-Ai
                    if (poswin(steps[turn - 2]) != -1) { // check if self winning
                        go(poswin(steps[turn - 2]));
                    } else if (poswin(steps[turn - 1]) != -1) { // check if opponent winning
                        go(poswin(steps[turn - 1]));
                    } else {
                        go(bestmov());
                    }
                }
            }
            drawBoard();
            if (checkwin(steps[turn]) == 1) {
                System.out.println((turn % 2 == 0) ? "X won the Game!" : "O won the Game!");
                break;
            }
            turn++;
        }
        if (checkwin(steps[turn - 1]) == 2) {
            System.out.println("Game was a Tie!");
        }
        System.out.println("Game has ended!");
        sc.close();
    }
}