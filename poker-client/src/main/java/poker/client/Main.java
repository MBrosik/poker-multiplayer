package poker.client;

import poker.client.game.BeforeGame;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        BeforeGame.start();
        SocketClientManager.i.socketChannel.close();
    }
}