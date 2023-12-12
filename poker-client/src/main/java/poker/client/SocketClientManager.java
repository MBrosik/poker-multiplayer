package poker.client;

import poker.commons.Constants;
import poker.commons.JSONManager;
import poker.commons.socket.ReceiveData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClientManager {
    public SocketChannel socketChannel;
    public static SocketClientManager i = new SocketClientManager();

    public SocketClientManager() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 8080));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReceiveData send(ReceiveData data, boolean withResponse) {
        try {
            ByteBuffer buffer = JSONManager.jsonStringify(data);
            socketChannel.write(buffer);

            buffer.clear();
            buffer = ByteBuffer.allocate(Constants.byteSize);

            if (withResponse) {
                socketChannel.read(buffer);
                String response = new String(buffer.array()).trim();

                buffer.clear();
                return JSONManager.jsonParse(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ReceiveData getDataFromServer() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Constants.byteSize);
        socketChannel.read(buffer);

        String response = new String(buffer.array()).trim();

        return JSONManager.jsonParse(response);
    }

}
