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
        currentBoard.resetBoard();
    }

    private TeamColor currentTeam;
    private ChessBoard currentBoard;

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

    private void toggleTeamTurn() {
        setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
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
        if(piece == null) {return null;}
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> moves = piece.pieceMoves(currentBoard, startPosition);
        for(ChessMove move: moves){
            ChessBoard potentialBoard = new ChessBoard(currentBoard);
            potentialBoard.movePiece(move, currentTeam);
            System.out.println(potentialBoard);
            if(!isInCheck(piece.getTeamColor(), potentialBoard)){
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

        if(currentBoard.getPiece(move.getStartPosition()) == null || currentBoard.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException();
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if(!validMoves.contains(move)){
            throw new InvalidMoveException();
        }

        currentBoard.movePiece(move, currentTeam);

        toggleTeamTurn();
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

        return board.checkDiagonals(king)
                || board.checkStraights(king)
                || board.checkKnightPositions(king)
                || board.checkPawnPositions(king)
                || board.checkKingPositions(king);
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        if(!isInCheck(teamColor)) {return false;}

        return hasNoValidMoves(teamColor);
    }

    private boolean hasNoValidMoves(TeamColor teamColor) {
        Collection<ChessPosition> positions = currentBoard.getAllPositionsForColor(teamColor);

        for(ChessPosition position: positions){
            if (!validMoves(position).isEmpty()){
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }

        return hasNoValidMoves(teamColor);
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
