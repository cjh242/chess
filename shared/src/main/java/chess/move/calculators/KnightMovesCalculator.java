package chess.move.calculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator {
    public static Collection<ChessMove> calculateKnightMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        // Knight can go vert 2 hort 1 or hort 2 vert 1
        Collection<ChessMove> moves = new ArrayList<>();

        Collection<ChessPosition> positions = new ArrayList<>();

        positions.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1));
        positions.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1));
        positions.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1));
        positions.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1));
        positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2));
        positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2));
        positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2));
        positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2));

        for(ChessPosition position : positions){
            if(board.isValidPosition(position, color)){
                moves.add(new ChessMove(myPosition, position, null));
            }
        }

        return moves;
    }
}
