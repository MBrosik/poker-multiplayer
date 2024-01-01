package poker.client;

import lombok.Getter;
import poker.commons.Constants;
import poker.commons.JSONManager;
import poker.commons.MyLogger;
import poker.commons.socket.ReceiveData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClientManager {

    @Getter
    private SocketChannel socketChannel;
    public static final SocketClientManager i = new SocketClientManager();

    public SocketClientManager() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 8080));
        } catch (IOException e) {
            MyLogger.elogln("Something wrong with SocketClientManager");
        }
    }

    public ReceiveData send(ReceiveData data, boolean withResponse) {
        try {
            ByteBuffer buffer = JSONManager.jsonStringify(data);
            socketChannel.write(buffer);

            buffer.clear();
            buffer = ByteBuffer.allocate(Constants.BYTE_SIZE);

            if (withResponse) {
                socketChannel.read(buffer);
                String response = new String(buffer.array()).trim();

                buffer.clear();
                return JSONManager.jsonParse(response);
            }

        } catch (IOException e) {
            MyLogger.elogln("Something wrong with sending to server");
        }
        return null;
    }

    public ReceiveData getDataFromServer() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Constants.BYTE_SIZE);
        socketChannel.read(buffer);

        String response = new String(buffer.array()).trim();

        return JSONManager.jsonParse(response);
    }

}
