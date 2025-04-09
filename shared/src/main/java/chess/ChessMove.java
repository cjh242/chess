package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    public String toMessageString() { // Renamed to avoid confusion, operates on 'this'
        String startStr = positionToString(this.startPosition);
        String endStr = positionToString(this.endPosition);

        String message = startStr + " to " + endStr;

        // Append promotion information if available
        if (this.promotionPiece != null) {
            char promotionChar;
            switch (this.promotionPiece) {
                case QUEEN:  promotionChar = 'Q'; break;
                case ROOK:   promotionChar = 'R'; break;
                case BISHOP: promotionChar = 'B'; break;
                case KNIGHT: promotionChar = 'N'; break;
                // Should typically only be Q, R, B, N for promotion
                default:     promotionChar = '?'; break;
            }
            message += "=" + promotionChar;
        }

        return message;
    }

    private String positionToString(ChessPosition position) {
        if (position == null) {
            return "?"; // Handle null position gracefully
        }
        int row = position.getRow();
        int col = position.getColumn();

        char colChar = (char) ('a' + col - 1);

        // Combine column character and row number
        return "" + colChar + row;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startPosition, chessMove.startPosition)
                && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }
}
