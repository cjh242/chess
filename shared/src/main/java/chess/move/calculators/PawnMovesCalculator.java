package chess.move.calculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator {

    public static Collection<ChessMove> calculatePawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        //can go one square forward, two if it is the initial turn
        Collection<ChessMove> moves = new ArrayList<>();
        Collection<ChessPosition> positions = new ArrayList<>();

        //add non-taking positions
        if (color == ChessGame.TeamColor.WHITE) {
            positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
            if (myPosition.getRow() == 2 && board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null) {
                positions.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()));
            }
        }
        else {
            positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
            if (myPosition.getRow() == 7 && board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null) {
                positions.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()));
            }
        }

        //add non-taking moves
        for (ChessPosition position : positions) {
            if(board.isValidPosition(position, color) && board.getPiece(position) == null) {
                pawnMovesHelper(myPosition, moves, position);
            }
        }

        positions.clear();

        //add taking positions
        if (color == ChessGame.TeamColor.WHITE) {
            positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
            positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));
        }
        else {
            positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
            positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
        }

        //add taking moves
        for (ChessPosition position : positions) {
            if(board.isValidPosition(position, color) && board.getPiece(position) != null) {
                pawnMovesHelper(myPosition, moves, position);
            }
        }

        return moves;
    }

    private static void pawnMovesHelper(ChessPosition myPosition, Collection<ChessMove> moves, ChessPosition position) {
        if(position.getRow() == 8 || position.getRow() == 1) {
            for(ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
                if(pieceType != ChessPiece.PieceType.PAWN && pieceType != ChessPiece.PieceType.KING) {
                    moves.add(new ChessMove(myPosition, position, pieceType));
                }
            }
        } else {
            moves.add(new ChessMove(myPosition, position, null));
        }
    }
}
