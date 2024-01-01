package poker.server;

import poker.commons.Constants;
import poker.commons.MyLogger;
import poker.server.socket.SocketManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;


public class Main {
    private static final String POISON_PILL = "POISON_PILL";
    private static boolean variable = true;

    public static void main(String[] args) {
        try(
                Selector selector = Selector.open();
                ServerSocketChannel serverSocket = ServerSocketChannel.open()
        ) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(Constants.BYTE_SIZE);


            while (variable) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {

                    SelectionKey key = iter.next();

                    if (key.isAcceptable()) {
                        register(selector, serverSocket);
                    }

                    if (key.isReadable()) {
                        getMessage(buffer, key);
                    }
                }
                iter.remove();
            }
        } catch (IOException e) {
            MyLogger.elogln("Server closing");
        }
    }

    private static void getMessage(ByteBuffer buffer, SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        try {
            int r = client.read(buffer);

            if (r == -1 || new String(buffer.array()).trim()
                    .equals(POISON_PILL)) {
                client.close();
                MyLogger.logln("Not accepting client messages anymore");
            } else {
                buffer.flip();

                Charset charset = StandardCharsets.UTF_8;
                CharsetDecoder decoder = charset.newDecoder();
                CharBuffer charBuffer = decoder.decode(buffer);

                SocketManager.onMessage(charBuffer, key);

                MyLogger.log(":) " + charBuffer);
                buffer.clear();
            }
        } catch (IOException io) {
            MyLogger.logln("Not accepting client messages anymore");
            client.close();
        }
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }
}