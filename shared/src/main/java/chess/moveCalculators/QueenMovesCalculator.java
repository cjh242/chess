package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class QueenMovesCalculator {
    public static Collection<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        //basically a rook and a bishop combined
        Collection<ChessMove> moves = BishopMovesCalculator.calculateBishopMoves(board, myPosition, color);
        moves.addAll(RookMovesCalculator.calculateRookMoves(board, myPosition, color));

        return moves;


    }
}
