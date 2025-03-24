import chess.*;
import client.ChessClient;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var client = new ChessClient();
        System.out.println("♕ 240 Chess Client: " + piece);
        client.RunChessClient(8080);
    }
}