package dataobjects;

import chess.ChessGame;
import request.JoinGameRequest;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData addWhiteUsername(String username) {
        return new GameData(this.gameID(), username, this.blackUsername(), this.gameName(), this.game());
    }
    public GameData addBlackUsername(String username) {
        return new GameData(this.gameID(), this.whiteUsername(), username, this.gameName(), this.game());
    }
}
