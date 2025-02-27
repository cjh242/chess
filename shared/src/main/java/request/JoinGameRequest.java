package request;

import chess.ChessGame;

public record JoinGameRequest(ChessGame.TeamColor playerColor, int gameID, String authToken) {
    public JoinGameRequest withAuthToken(String authToken) {
        return new JoinGameRequest(this.playerColor(), this.gameID(), authToken);
    }
}
