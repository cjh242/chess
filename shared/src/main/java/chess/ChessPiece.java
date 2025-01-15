package chess;

import chess.MoveCalculators.*;

import java.util.ArrayList;
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
        Collection<ChessMove> moves = new ArrayList<>();
        return switch (pieceType) {
            case KING -> {
                moves = KingMovesCalculator.CalculateKingMoves(board, myPosition);
                yield moves;
            }
            case QUEEN -> {
                moves = QueenMovesCalculator.CalculateQueenMoves(board, myPosition);
                yield moves;
            }
            case BISHOP -> {
                moves = BishopMovesCalculator.CalculateBishopMoves(board, myPosition);
                yield moves;
            }
            case KNIGHT -> {
                moves = KnightMovesCalculator.CalculateKnightMoves(board, myPosition);
                yield moves;
            }
            case ROOK -> {
                moves = RookMovesCalculator.CalculateRookMoves(board, myPosition);
                yield moves;
            }
            case PAWN -> {
                moves = PawnMovesCalculator.CalculatePawnMoves(board, myPosition);
                yield moves;
            }
        };
    }

    @Override
    public String toString() {
        return TYPE_TO_CHAR_MAP.get(pieceType);
    }
}
