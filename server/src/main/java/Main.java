import chess.*;
import dataaccess.IGameDAO;
import dataaccess.MemoryGameDAO;
import server.Server;
import service.GameService;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        IGameDAO gameData = new MemoryGameDAO();
        var gameService = new GameService(gameData);
        var userService = new UserService();

        var server = new Server(gameService, userService);
        server.run(8080);
    }
}