package service;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.List;

import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.*;

public class PrintingHelper {

    public static void highlightValidMoves(ChessBoard board, ChessGame.TeamColor perspective, List<ChessPosition> positions) {
        System.out.print(ERASE_SCREEN); // Clear screen

        boolean isWhitePerspective = (perspective == ChessGame.TeamColor.WHITE);

        // Print Header (Column Labels - Top)
        printHeaderFooter(isWhitePerspective);

        // Iterate through rows based on perspective
        for (int row = (isWhitePerspective ? 8 : 1);
             isWhitePerspective ? row >= 1 : row <= 8;
             row += (isWhitePerspective ? -1 : 1)) {

            System.out.print(SET_BG_COLOR_LIGHT_GREY); // Set default background for row numbers
            System.out.print(SET_TEXT_COLOR_BLACK); // Set text color for row numbers
            System.out.print(row + " "); // Row label
            System.out.print(RESET_BG_COLOR); // Reset background after row number

            // Iterate through columns based on perspective
            for (int col = (isWhitePerspective ? 1 : 8);
                 isWhitePerspective ? col <= 8 : col >= 1;
                 col += (isWhitePerspective ? 1 : -1)) {

                ChessPosition currentPos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(currentPos);
                String pieceSymbol = getPieceSymbol(piece); // Get symbol (includes piece color)

                String bgColor;

                // *** CORE CHANGE: Check if the current square should be highlighted ***
                if (positions != null && positions.contains(currentPos)) {
                    bgColor = SET_BG_COLOR_YELLOW;
                } else {
                    // Original alternating background logic
                    boolean isDark = (row + col) % 2 == 0; // Standard dark check
                    // Adjust if perspective matters for *which* color is considered "dark" visually
                    // This keeps a consistent checkerboard pattern regardless of perspective start
                    // boolean isVisuallyDarkSquare = (row + col) % 2 != 0; // a1 is light
                    bgColor = isDark ? SET_BG_COLOR_DARK_GREY : SET_BG_COLOR_LIGHT_GREY;
                }

                // Print the square with the determined background and the piece symbol (which sets its own text color)
                System.out.print(bgColor + pieceSymbol + RESET_BG_COLOR); // pieceSymbol already resets BG, but good practice to reset again
            }
            System.out.print(SET_BG_COLOR_LIGHT_GREY); // Set default background for row numbers
            System.out.print(SET_TEXT_COLOR_BLACK); // Set text color for row numbers
            System.out.println(" " + row + RESET_BG_COLOR); // Row label (right side) + Reset BG
        }

        // Print Footer (Column Labels - Bottom)
        printHeaderFooter(isWhitePerspective);
        System.out.print(RESET_BG_COLOR); // Ensure all colors are reset at the end
    }

    /** Helper method to print the column labels */
    private static void printHeaderFooter(boolean isWhitePerspective) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY); // Set default background for labels
        System.out.print(SET_TEXT_COLOR_BLACK); // Set text color for labels
        System.out.print(" \u2003 "); // Use Unicode space for consistent width
        if (isWhitePerspective) {
            System.out.println("a\u2003 b\u2003 c\u2003 d\u2003 e\u2003 f\u2003 g\u2003 h");
        } else {
            System.out.println("h\u2003 g\u2003 f\u2003 e\u2003 d\u2003 c\u2003 b\u2003 a");
        }
        System.out.print(RESET_BG_COLOR); // Reset background after labels
    }

    public static void printBoard(ChessBoard board, int number, String name, ChessGame.TeamColor perspective) {
        System.out.print(ERASE_SCREEN); // Clear screen

        if (name != null) {
            System.out.println("Game Name: " + name);
        }

        if (number != -1){
            System.out.println("Game Number: " + number);
        }

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
                if(isWhitePerspective){
                    isDarkSquare = (row + col) % 2 == 0;
                }

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
        if (piece == null) {
            return EMPTY;
        }

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
