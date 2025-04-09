package dataobjects;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData addWhiteUsername(String username) {
        return new GameData(this.gameID(), username, this.blackUsername(), this.gameName(), this.game());
    }
    public GameData addBlackUsername(String username) {
        return new GameData(this.gameID(), this.whiteUsername(), username, this.gameName(), this.game());
    }

    public GameData removeWhiteUsername() {
        return new GameData(this.gameID(), null, this.blackUsername(), this.gameName(), this.game());
    }

    public GameData removeBlackUsername() {
        return new GameData(this.gameID(), this.whiteUsername(), null, this.gameName(), this.game());
    }
}
