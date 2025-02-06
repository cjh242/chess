package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    //copy constructor
    public ChessBoard(ChessBoard original) {
        this.board = new ChessPiece[original.board.length][];
        for (int i = 0; i < original.board.length; i++) {
            this.board[i] = new ChessPiece[original.board[i].length];
            for (int j = 0; j < original.board[i].length; j++) {
                if (original.board[i][j] != null) {
                    // Assuming ChessPiece has a copy constructor or clone method
                    this.board[i][j] = new ChessPiece(original.board[i][j]);
                }
            }
        }
    }

    private final ChessPiece[][] board;
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    public Collection<ChessPosition> getAllPositionsForColor(ChessGame.TeamColor color){
        Collection<ChessPosition> positions = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null && board[i][j].getTeamColor() == color) {
                    positions.add(new ChessPosition(i + 1, j + 1));
                }
            }
        }

        return positions;
    }

    /**
     * moves a piece from the start position to the end position
     */
    public void movePiece(ChessMove move, ChessGame.TeamColor color){

        ChessPiece piece = getPiece(move.getStartPosition());
        if(piece != null){
            addPiece(move.getStartPosition(), null);
            if(move.getPromotionPiece() != null){
                addPiece(move.getEndPosition(), new ChessPiece(color, move.getPromotionPiece()));
            }
            else{
                addPiece(move.getEndPosition(), piece);
            }
        }
    }

    public boolean checkKingPositions(ChessPosition position){
        ChessGame.TeamColor color = getPiece(position).getTeamColor();

        //need to check all 8 positions around
        Collection<ChessPosition> positions = new ArrayList<>();
        positions.add(new ChessPosition(position.getRow() + 1, position.getColumn()));
        positions.add(new ChessPosition(position.getRow() - 1, position.getColumn()));
        positions.add(new ChessPosition(position.getRow(), position.getColumn() + 1));
        positions.add(new ChessPosition(position.getRow(), position.getColumn() - 1));
        positions.add(new ChessPosition(position.getRow() + 1, position.getColumn() + 1));
        positions.add(new ChessPosition(position.getRow() + 1, position.getColumn() - 1));
        positions.add(new ChessPosition(position.getRow() - 1, position.getColumn() + 1));
        positions.add(new ChessPosition(position.getRow() - 1, position.getColumn() - 1));

        for (ChessPosition pos: positions){
            if(checkKingPosition(color, pos)){
                return true;
            }
        }

        return false;
    }

    private boolean checkKingPosition(ChessGame.TeamColor color, ChessPosition position){
        return (isOnBoard(position)
                && getPiece(position) != null
                && getPiece(position).getTeamColor() != color
                && getPiece(position).getPieceType() == ChessPiece.PieceType.KING);
    }

    public boolean checkPawnPositions(ChessPosition position){
        ChessGame.TeamColor color = getPiece(position).getTeamColor();

        //pawns cant come from behind, so just have two positions to check for each color
        ChessPosition position1;
        ChessPosition position2;
        if (color == ChessGame.TeamColor.WHITE){
            //up a column both directions
            position1 = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            position2 = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
        }
        else{
            //back a column both directions
            position1 = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            position2 = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
        }
        return checkPawnPositions(color, position1, position2);
    }

    private boolean checkPawnPositions(ChessGame.TeamColor color, ChessPosition position1, ChessPosition position2) {
        return (isOnBoard(position1)
                && getPiece(position1) != null
                && getPiece(position1).getTeamColor() != color
                && getPiece(position1).getPieceType() == ChessPiece.PieceType.PAWN)
                || (isOnBoard(position2)
                        && getPiece(position2) != null
                        && getPiece(position2).getTeamColor() != color
                        && getPiece(position2).getPieceType() == ChessPiece.PieceType.PAWN);
    }

    private boolean checkDiagonals(ChessPosition currPosition, int rowDif, int colDif, ChessGame.TeamColor color){
        ChessPiece currPiece;
        do {
            currPosition = new ChessPosition(currPosition.getRow() + rowDif, currPosition.getColumn() + colDif);

            if(isOnBoard(currPosition)){
                currPiece = getPiece(currPosition);
            }
            else{
                currPiece = null;
                continue;
            }


            if (currPiece != null
                    && currPiece.getTeamColor() != color
                    && (currPiece.getPieceType() == ChessPiece.PieceType.QUEEN
                    || currPiece.getPieceType() == ChessPiece.PieceType.BISHOP)){
                return true;
            }
        } while (isOnBoard(currPosition) && currPiece == null);

        return false;
    }
    /**
     * checks the diagonals for appropriate pieces and returns true if one is found
     */
    public boolean checkDiagonals(ChessPosition position) {
        ChessGame.TeamColor color = getPiece(position).getTeamColor();

        return checkDiagonals(position, -1, -1, color)
                || checkDiagonals(position, -1, 1, color)
                || checkDiagonals(position, 1, -1, color)
                || checkDiagonals(position, 1, 1, color);
    }

    private boolean checkStraights(ChessPosition currPosition, int rowDif, int colDif, ChessGame.TeamColor color){
        ChessPiece currPiece;
        do {
            currPosition = new ChessPosition(currPosition.getRow() + rowDif, currPosition.getColumn() + colDif);

            if(isOnBoard(currPosition)){
                currPiece = getPiece(currPosition);
            }
            else{
                currPiece = null;
                continue;
            }

            if (currPiece != null
                    && currPiece.getTeamColor() != color
                    && (currPiece.getPieceType() == ChessPiece.PieceType.ROOK
                    || currPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
        } while (isOnBoard(currPosition) && currPiece == null);

        return false;
    }

    /**
     * checks the straights for appropriate pieces and returns true if one is found
     */
    public boolean checkStraights(ChessPosition position) {
        ChessGame.TeamColor color = getPiece(position).getTeamColor();

        return checkStraights(position, 1, 0, color)
                || checkStraights(position, 0, 1, color)
                || checkStraights(position, -1, 0, color)
                || checkStraights(position, 0, -1, color);
    }


    private boolean checkKnightPosition(ChessPosition position, ChessGame.TeamColor color){
        return isOnBoard(position)
                && getPiece(position) != null
                && getPiece(position).getTeamColor() != color
                && getPiece(position).getPieceType() == ChessPiece.PieceType.KNIGHT;
    }
    /**
     * checks the possible knight positions and returns true if one is found
     */
    public boolean checkKnightPositions(ChessPosition position) {
        ChessGame.TeamColor color = getPiece(position).getTeamColor();

        return checkKnightPosition(new ChessPosition(position.getRow() + 2, position.getColumn() + 1), color)
                || checkKnightPosition(new ChessPosition(position.getRow() + 2, position.getColumn() - 1), color)
                || checkKnightPosition(new ChessPosition(position.getRow() - 2, position.getColumn() + 1), color)
                || checkKnightPosition(new ChessPosition(position.getRow() - 2, position.getColumn() - 1), color)
                || checkKnightPosition(new ChessPosition(position.getRow() + 1, position.getColumn() + 2), color)
                || checkKnightPosition(new ChessPosition(position.getRow() + 1, position.getColumn() - 2), color)
                || checkKnightPosition(new ChessPosition(position.getRow() - 1, position.getColumn() + 2), color)
                || checkKnightPosition(new ChessPosition(position.getRow() - 1, position.getColumn() - 2), color);
    }

    /**
     * finds the king for a specified color and returns its location
     */
    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = getPiece(position);
                if(piece != null && piece.getTeamColor() == color && piece.getPieceType() == ChessPiece.PieceType.KING){
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }

        // add all the white pawns
        for (int i = 1; i < 9; i++) {
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }

        //adding the rest of the white pieces
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));


        //add all the black pawns
        for (int i = 1; i < 9; i++) {
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        //adding the rest of the black pieces
        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }

    public boolean isValidPosition(ChessPosition position, ChessGame.TeamColor color) {
        return isOnBoard(position) && (getPiece(position) == null || getPiece(position).getTeamColor() != color);
    }

    private boolean isOnBoard(ChessPosition position){
        return position.getRow() <= 8 && position.getColumn() <= 8 && position.getRow() >= 1 && position.getColumn() >= 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Board :\n");

        for (int row = 0; row < 8; row++) {
            sb.append("|");
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece == null) {
                    sb.append(" ");
                } else {
                    sb.append(piece);
                }
                sb.append("|");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }


    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
