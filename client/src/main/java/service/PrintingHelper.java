package service;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.*;

public class PrintingHelper {

    public static void printBoard(ChessBoard board, int number, String name) {
        System.out.print(ERASE_SCREEN); // Clear screen
        System.out.println("Game Name: " + name);
        System.out.println("Game Number: " + number);
        System.out.println(" \u2003 a\u2003 b\u2003 c\u2003 d\u2003 e\u2003 f\u2003 g\u2003 h"); // Column labels

        for (int row = 8; row >= 1; row--) { // Print from row 8 down to 1
            System.out.print(row + " "); // Row label

            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                boolean isDarkSquare = (row + col) % 2 != 0; // Checkered pattern

                String bgColor = isDarkSquare ? SET_BG_COLOR_DARK_GREY : SET_BG_COLOR_LIGHT_GREY;
                String pieceSymbol = getPieceSymbol(piece);

                System.out.print(bgColor + pieceSymbol + RESET_BG_COLOR);
            }

            System.out.println(" " + row); // End row with label
        }

        System.out.println(" \u2003 a\u2003 b\u2003 c\u2003 d\u2003 e\u2003 f\u2003 g\u2003 h"); // Column labels again
    }

    private static String getPieceSymbol(ChessPiece piece) {
        if (piece == null) return EMPTY;

        switch (piece.getPieceType()) {
            case KING:
                return piece.getTeamColor() == WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN:
                return piece.getTeamColor() == WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP:
                return piece.getTeamColor() == WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT:
                return piece.getTeamColor() == WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK:
                return piece.getTeamColor() == WHITE ? WHITE_ROOK : BLACK_ROOK;
            case PAWN:
                return piece.getTeamColor() == WHITE ? WHITE_PAWN : BLACK_PAWN;
            default:
                return EMPTY;
        }
    }
}
