package bryce_david;

import othello.*;

import java.util.ArrayList;

public class MyPlayer extends AIPlayer {
    @Override
    public String getName() {   return ("Mr. Pence");   }

    @Override
    public void getNextMove(Board board, int[] bestMove) throws IllegalCellException, IllegalMoveException {
        long[] random = new long[]{0};
        try {
            minimax(board, 9, true, bestMove, random);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public double evaluate(Board board) {

//        double score = 0.0;
//
//        ArrayList<Pair> whiteCorners = new ArrayList<>();
//        ArrayList<Pair> blackCorners = new ArrayList<>();
//        boolean Done = false;
//
//        //Check corner 00 and family
//        Pair current = new Pair(0,0);
//        whiteCorners.add(current);
//        blackCorners.add(current);
//
//        while (!Done) {
//            boolean vertical = false;
//            while (!vertical) {
//
//
//            }
//        }



//        return score;

        //Check corner 70 and family
        //Check corner 07 and family
        //check corner 77 and family

        double score = 0.0;

        //loop through every cell on board
        for (int x=0; x< Board.BOARD_DIM; x++){
            for (int y=0; y< Board.BOARD_DIM; y++){
                //Check or do something with each cell
                try {
                    int cell = board.getCell(new int[]{x, y});
                    if (cell == Board.WHITE){
                        score -= 1;
                    } else if (cell == Board.BLACK){
                        score += 1;
                    }
                } catch (othello.IllegalCellException e){
                }
            }
        }

        return score;
    }

    @Override
    public double minimax(Board board, int depthLimit, boolean useAlphaBetaPruning, int[] bestMove, long[] numNodesExplored) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        int currPlayer = board.getPlayer();

        double bestValue = 0;
        if(currPlayer == Board.BLACK) bestValue = Integer.MIN_VALUE;
        else if(currPlayer == Board.WHITE) bestValue = Integer.MAX_VALUE;

        for (Pair move: getPossibleMoves(board)) {
            Board boardCopy = board.getClone();
            try {
                boardCopy.makeMove(new int[]{move.x, move.y});
            } catch (othello.IllegalMoveException ignored){
            }
            if(currPlayer == Board.BLACK) {
                double temp = maxValue(boardCopy, depthLimit, useAlphaBetaPruning, bestMove, numNodesExplored, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if(temp>bestValue) {
                    bestValue = temp;
                    bestMove[0] = move.x;
                    bestMove[1] = move.y;
                }
            } else if (currPlayer == Board.WHITE) {
                double temp = minValue(boardCopy, depthLimit, useAlphaBetaPruning, bestMove, numNodesExplored, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if(temp<bestValue) {
                    bestValue = temp;
                    bestMove[0] = move.x;
                    bestMove[1] = move.y;                }
            }
        }

        return bestValue;
    }

//    public ALPHA-BETA-SEARCH(state) returns an action {
//        v <- MAX-VALUE(state, -inf, inf);
//        return the action in ACTIONS(state) with value v;
//    }

//    private MAX-VALUE(state, alpha, beta) returns a utility value {
    private double maxValue(Board board, int depthLimit, boolean useAlphaBetaPruning, int[] bestMove, long[] numNodesExplored, double alpha, double beta) throws InterruptedException {
        //        if TERMINAL-TEST(state) then return UTILITY(state);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        numNodesExplored[0]++;
        if(!(depthLimit != 0 && board.getWinner() == Board.EMPTY) /*TODO: or someone won or there's no more moves*/) {
            return evaluate(board);
        }
        depthLimit--;

//        v <- -inf;
        double value = Double.MIN_VALUE;
//        for each a in ACTIONS(state) do {
        for(Pair move: getPossibleMoves(board)) {
            Board boardCopy = board.getClone();
            try {
                boardCopy.makeMove(new int[]{move.x, move.y});
            } catch (othello.IllegalMoveException ignored){
            }

//            v <- MAX(v, MIN-VALUE(RESULT(s,a), alpha, beta));
            value = Math.max(value,  minValue(board, depthLimit, useAlphaBetaPruning, bestMove, numNodesExplored, alpha, beta));
//            if v >= beta then return v;
            if(value >= beta) return value;
//            alpha <- MAX(alpha, v);
            alpha = Math.max(alpha, value);
//        }
        }
//        return v;
        return value;
//    }
    }

//    private MIN-VALUE(state, alpha, beta) returns a utility value {
    private double minValue(Board board, int depthLimit, boolean useAlphaBetaPruning,  int[] bestMove, long[] numNodesExplored, double alpha, double beta) throws InterruptedException {
//        if TERMINAL-TEST(state) then return UTILITY(state);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        numNodesExplored[0]++;
        if(!(depthLimit != 0 && board.getWinner() == Board.EMPTY) /*TODO: or someone won or there's no more moves*/) {
            return evaluate(board);
        }
        depthLimit--;

//        v <- inf;
        double value = Double.MAX_VALUE;

//        for each a in ACTIONS(state) do {
        for(Pair move: getPossibleMoves(board)) {
            Board boardCopy = board.getClone();
            try {
                boardCopy.makeMove(new int[]{move.x, move.y});
            } catch (othello.IllegalMoveException ignored){
            }
//            v <- MIN(v, MAX-VALUE(RESULT(s,a), alpha, beta));
            value = Math.min(value,  maxValue(board, depthLimit, useAlphaBetaPruning, bestMove, numNodesExplored, alpha, beta));

//            if v <= alpha then return v;
            if(value <= alpha) return value;

//            beta <- MIN(beta, v);
            beta = Math.min(beta, value);

//        }
        }
//        return v;
        return value;
//    }
    }

    private ArrayList<Pair> getPossibleMoves(Board board){
        ArrayList<Pair> out = new ArrayList<>();

        for (int x=0; x< Board.BOARD_DIM; x++){
            for (int y=0; y< Board.BOARD_DIM; y++){
                if (board.isLegalMove(new int[]{x, y}) == true){
                    out.add(new Pair(x, y));
                }
            }
        }

        return out;
    }
}

class Pair {
    public int x;
    public int y;
    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


//
//public class OthelloMinimax {
//    // Variables to store the best move
//    private int[] bestMove;
//
//    // Function to evaluate the board position
//    public int evaluate(char[][] board) {
//        // Implement your evaluation function here
//        return 0;
//    }
//
//    // Minimax algorithm with alpha-beta pruning
//    public int minimax(char[][] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
//        // Base case: if depth is 0 or game is over
//        if (depth == 0 || /*game is over*/) {
//            return evaluate(board);
//        }
//
//        // If it's maximizing player's turn
//        if (maximizingPlayer) {
//            int maxEval = Integer.MIN_VALUE;
//            // Generate possible moves for the current player and iterate over them
//            for (/* each possible move */) {
//                // Apply the move to get a new board state
//                char[][] newBoard = /* apply move */;
//                // Recursively call minimax on the new board state
//                int eval = minimax(newBoard, depth - 1, alpha, beta, false);
//                // Update alpha
//                maxEval = Math.max(maxEval, eval);
//                alpha = Math.max(alpha, eval);
//                // Perform alpha-beta pruning
//                if (beta <= alpha) {
//                    break;
//                }
//            }
//            return maxEval;
//        } else { // If it's minimizing player's turn
//            int minEval = Integer.MAX_VALUE;
//            // Generate possible moves for the opponent and iterate over them
//            for (/* each possible move */) {
//                // Apply the move to get a new board state
//                char[][] newBoard = /* apply move */;
//                // Recursively call minimax on the new board state
//                int eval = minimax(newBoard, depth - 1, alpha, beta, true);
//                // Update beta
//                minEval = Math.min(minEval, eval);
//                beta = Math.min(beta, eval);
//                // Perform alpha-beta pruning
//                if (beta <= alpha) {
//                    break;
//                }
//            }
//            return minEval;
//        }
//    }
//
//    // Function to find the best move using minimax algorithm
//    public void findBestMove(char[][] board, int depth) {
//        int bestMoveValue = Integer.MIN_VALUE;
//        int alpha = Integer.MIN_VALUE;
//        int beta = Integer.MAX_VALUE;
//        bestMove = new int[2]; // Initialize bestMove array
//
//        // Generate possible moves for the current player and iterate over them
//        for (/* each possible move */) {
//            // Apply the move to get a new board state
//            char[][] newBoard = /* apply move */;
//            // Call minimax on the new board state
//            int moveValue = minimax(newBoard, depth - 1, alpha, beta, false);
//            // Update best move if this move is better
//            if (moveValue > bestMoveValue) {
//                bestMoveValue = moveValue;
//                bestMove[0] = /* move row */;
//                bestMove[1] = /* move column */;
//            }
//        }
//
//        // Output the best move
//        System.out.println("Best Move: Row = " + bestMove[0] + ", Col = " + bestMove[1]);
//    }
//
//    // Main function to test the OthelloMinimax class
//    public static void main(String[] args) {
//        // Initialize the Othello board
//        char[][] board = /* initialize board */;
//
//        // Create an instance of OthelloMinimax
//        OthelloMinimax othelloMinimax = new OthelloMinimax();
//
//        // Call findBestMove function to find the best move
//        othelloMinimax.findBestMove(board, /* depth */);
//
//        // Access bestMove array after finding the best move
//        int[] bestMove = othelloMinimax.bestMove;
//        System.out.println("Best Move: Row = " + bestMove[0] + ", Col = " + bestMove[1]);
//    }
//}


