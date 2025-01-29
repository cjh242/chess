package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator {

    public static Collection<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        // King can go on space in any direction
        Collection<ChessMove> moves = new ArrayList<>();

        Collection<ChessPosition> positions = new ArrayList<>();
        ChessPosition position;

        //go up and right
        for(int i = myPosition.getRow() + 1, j = myPosition.getColumn() + 1; i <= 8 && j <= 8; i++, j++){
            position = new ChessPosition(i, j);
            positions.add(position);
            if(board.getPiece(position) != null) {
                break;
            }
        }
        //go up and left
        for(int i = myPosition.getRow() + 1, j = myPosition.getColumn() - 1; i <= 8 && j >= 1; i++, j--){
            position = new ChessPosition(i, j);
            positions.add(position);
            if(board.getPiece(position) != null) {
                break;
            }
        }

        //go down and right
        for(int i = myPosition.getRow() - 1, j = myPosition.getColumn() + 1; i >= 1 && j <= 8; i--, j++){
            position = new ChessPosition(i, j);
            positions.add(position);
            if(board.getPiece(position) != null) {
                break;
            }
        }
        //go down and left
        for(int i = myPosition.getRow() - 1, j = myPosition.getColumn() - 1; i >= 1 && j >= 1; i--, j--){
            position = new ChessPosition(i, j);
            positions.add(position);
            if(board.getPiece(position) != null) {
                break;
            }
        }

        for(ChessPosition p : positions){
            if(board.isValidPosition(p, color)){
                moves.add(new ChessMove(myPosition, p, null));
            }
        }

        return moves;
    }
}
