package chess;

import chess.moveCalculators.*;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        pieceType = type;
    }

    public ChessPiece(ChessPiece piece){
        this.color = piece.color;
        this.pieceType = piece.pieceType;
    }

    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType pieceType;


    private static final Map<PieceType, String> TYPE_TO_CHAR_MAP = Map.of(
            ChessPiece.PieceType.PAWN, "p",
            ChessPiece.PieceType.KNIGHT, "n",
             ChessPiece.PieceType.ROOK, "r",
            ChessPiece.PieceType.QUEEN, "q",
             ChessPiece.PieceType.KING, "k",
             ChessPiece.PieceType.BISHOP, "b");

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, pieceType);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves;
        return switch (pieceType) {
            case KING -> {
                moves = KingMovesCalculator.calculateKingMoves(board, myPosition, getTeamColor());
                yield moves;
            }
            case QUEEN -> {
                moves = QueenMovesCalculator.calculateQueenMoves(board, myPosition, getTeamColor());
                yield moves;
            }
            case BISHOP -> {
                moves = BishopMovesCalculator.calculateBishopMoves(board, myPosition, getTeamColor());
                yield moves;
            }
            case KNIGHT -> {
                moves = KnightMovesCalculator.calculateKnightMoves(board, myPosition, getTeamColor());
                yield moves;
            }
            case ROOK -> {
                moves = RookMovesCalculator.calculateRookMoves(board, myPosition, getTeamColor());
                yield moves;
            }
            case PAWN -> {
                moves = PawnMovesCalculator.calculatePawnMoves(board, myPosition, getTeamColor());
                yield moves;
            }
        };
    }

    @Override
    public String toString() {

        if(color == ChessGame.TeamColor.WHITE) {
            return TYPE_TO_CHAR_MAP.get(pieceType).toUpperCase();
        }

        return TYPE_TO_CHAR_MAP.get(pieceType);
    }
}
