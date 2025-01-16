package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class QueenMovesCalculator {
    public static Collection<ChessMove> CalculateQueenMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        //basically a rook and a bishop combined
        Collection<ChessMove> moves = BishopMovesCalculator.CalculateBishopMoves(board, myPosition, color);
        moves.addAll(RookMovesCalculator.CalculateRookMoves(board, myPosition, color));

        return moves;


    }
}
