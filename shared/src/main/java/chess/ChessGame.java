package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
        currentBoard = new ChessBoard();
        currentTeam = TeamColor.WHITE;
    }

    private TeamColor currentTeam;
    private ChessBoard currentBoard;

    private ChessBoard potentialBoard;

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece piece = currentBoard.getPiece(startPosition);
        if(piece == null) return null;
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> moves = piece.pieceMoves(currentBoard, startPosition);
        for(ChessMove move: moves){
            potentialBoard = currentBoard;
            potentialBoard.movePiece(move);
            if(isInCheck(piece.getTeamColor(), potentialBoard)){
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if(!validMoves.contains(move)){
            throw new InvalidMoveException();
        }

        currentBoard.movePiece(move);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        return isInCheck(teamColor, currentBoard);
    }

    /**
     * Determines if the given team is in check for the given board
     *
     * @param teamColor which team to check for check
     * @param board the board to checking, could be a current or potential board
     * @return True if the specified team is in check for the specified board
     */
    private boolean isInCheck(TeamColor teamColor, ChessBoard board) {

        ChessPosition king = board.getKingPosition(teamColor);

        return board.checkDiagonals(king) || board.checkStraights(king) || board.checkKnightPositions(king) || board.checkPawnPositions(king);
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        if(!isInCheck(teamColor)) return false;

        //get the king
        //check if he has any moves
        //if no moves return false
        //for each of the moves we need to do a similar check to isInCheck
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //iterate through all the pieces checking for valid moves,
        //exit early as soon as a move is found
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {

        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {

        return currentBoard;
    }
}
