package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator {
    public static Collection<ChessMove> calculateRookMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        // King can go on space in any direction
        Collection<ChessMove> moves = new ArrayList<>();

        Collection<ChessPosition> positions = new ArrayList<>();
        ChessPosition position;

        //go down the row
        for(int i = myPosition.getRow() + 1; i <= 8; i++){
            position = new ChessPosition(i, myPosition.getColumn());
            positions.add(position);
            if(board.getPiece(position) != null) {
                break;
            }
        }
        for(int i = myPosition.getRow() - 1; i >= 1; i--){
            position = new ChessPosition(i, myPosition.getColumn());
            positions.add(position);
            if(board.getPiece(position) != null) {
                break;
            }
        }

        //go down the columm
        for(int i = myPosition.getColumn() + 1; i <= 8; i++){
            position = new ChessPosition(myPosition.getRow(), i);
            positions.add(position);
            if(board.getPiece(position) != null) {
                break;
            }
        }
        for(int i = myPosition.getColumn() - 1; i >= 1; i--){
            position = new ChessPosition(myPosition.getRow(), i);
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
