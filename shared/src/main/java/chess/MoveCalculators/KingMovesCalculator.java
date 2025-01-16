package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator {

    public static Collection<ChessMove> CalculateKingMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        // King can go on space in any direction
        Collection<ChessMove> moves = new ArrayList<>();

        Collection<ChessPosition> positions = new ArrayList<>();

        positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
        positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
        positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));
        positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
        positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
        positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
        positions.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1));
        positions.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1));

        for(ChessPosition position : positions){
            if(board.isValidPosition(position, color)){
                moves.add(new ChessMove(myPosition, position, null));
            }
        }

        return moves;
    }
}
