package service;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.*;

public class PrintingHelper {

    public static void printBoard(ChessBoard board, int number, String name, ChessGame.TeamColor perspective) {
        System.out.print(ERASE_SCREEN); // Clear screen
        System.out.println("Game Name: " + name);
        System.out.println("Game Number: " + number);

        boolean isWhitePerspective = (perspective == ChessGame.TeamColor.WHITE);

        // Column labels
        System.out.print(" \u2003 ");
        if (isWhitePerspective) {
            System.out.println("a\u2003 b\u2003 c\u2003 d\u2003 e\u2003 f\u2003 g\u2003 h");
        } else {
            System.out.println("h\u2003 g\u2003 f\u2003 e\u2003 d\u2003 c\u2003 b\u2003 a");
        }

        for (int row = (isWhitePerspective ? 8 : 1);
             isWhitePerspective ? row >= 1 : row <= 8;
             row += (isWhitePerspective ? -1 : 1)) {

            System.out.print(row + " "); // Row label

            for (int col = (isWhitePerspective ? 1 : 8);
                 isWhitePerspective ? col <= 8 : col >= 1;
                 col += (isWhitePerspective ? 1 : -1)) {

                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                boolean isDarkSquare = (row + col) % 2 != 0;

                String bgColor = isDarkSquare ? SET_BG_COLOR_DARK_GREY : SET_BG_COLOR_LIGHT_GREY;
                String pieceSymbol = getPieceSymbol(piece);

                System.out.print(bgColor + pieceSymbol + RESET_BG_COLOR);
            }

            System.out.println(" " + row);
        }

        // Column labels again
        System.out.print(" \u2003 ");
        if (isWhitePerspective) {
            System.out.println("a\u2003 b\u2003 c\u2003 d\u2003 e\u2003 f\u2003 g\u2003 h");
        } else {
            System.out.println("h\u2003 g\u2003 f\u2003 e\u2003 d\u2003 c\u2003 b\u2003 a");
        }
    }


    private static String getPieceSymbol(ChessPiece piece) {
        if (piece == null) return EMPTY;

        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN -> piece.getTeamColor() == WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> piece.getTeamColor() == WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> piece.getTeamColor() == WHITE ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> piece.getTeamColor() == WHITE ? WHITE_PAWN : BLACK_PAWN;
            default -> EMPTY;
        };
    }
}
