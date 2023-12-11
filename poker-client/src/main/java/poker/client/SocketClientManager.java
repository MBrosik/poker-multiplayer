package poker.client;

import poker.commons.Constants;
import poker.commons.JSONManager;
import poker.commons.socket.ReceiveData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClientManager {
    SocketChannel socketChannel;


    public static SocketClientManager i = new SocketClientManager();

    public SocketClientManager() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 8080));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String send(ReceiveData data, boolean withResponse) {
        try {
//            SocketChannel socketChannel = SocketChannel.open();
//            socketChannel.connect(new InetSocketAddress("localhost", 8080));


            ByteBuffer buffer = JSONManager.jsonStringify(data);
            socketChannel.write(buffer);

            buffer.clear();
            buffer = ByteBuffer.allocate(Constants.byteSize);

            String response = null;

            if (withResponse) {
                socketChannel.read(buffer);
                response = new String(buffer.array()).trim();

                System.out.println("response=" + response);
                buffer.clear();
            }

//            socketChannel.close();

            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
