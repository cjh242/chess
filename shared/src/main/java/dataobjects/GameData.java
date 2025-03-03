package dataobjects;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName) {
    public GameData addWhiteUsername(String username) {
        return new GameData(this.gameID(), username, this.blackUsername(), this.gameName());
    }
    public GameData addBlackUsername(String username) {
        return new GameData(this.gameID(), this.whiteUsername(), username, this.gameName());
    }
}
