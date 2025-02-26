import chess.*;
import dataaccess.IAuthDAO;
import dataaccess.IGameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import server.Server;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        IGameDAO gameData = new MemoryGameDAO();
        IAuthDAO authData = new MemoryAuthDAO();
        var authService = new AuthService(authData);
        var gameService = new GameService(gameData, authService);
        var userService = new UserService();

        var server = new Server(gameService, userService, authService);
        server.run(8080);
    }
}