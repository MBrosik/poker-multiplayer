package poker.server;

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

public class Test {

//    private static final String POISON_PILL = "POISON_PILL";
//
//    public static void main(String[] args) throws IOException {
//        Selector selector = Selector.open();
//        ServerSocketChannel serverSocket = ServerSocketChannel.open();
//        serverSocket.bind(new InetSocketAddress("localhost", 8080));
//        serverSocket.configureBlocking(false);
//        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
//        ByteBuffer buffer = ByteBuffer.allocate(256);
//
//        while (true) {
//            selector.select();
//            Set<SelectionKey> selectedKeys = selector.selectedKeys();
//            Iterator<SelectionKey> iter = selectedKeys.iterator();
//            while (iter.hasNext()) {
//
//                SelectionKey key = iter.next();
//
//                if (key.isAcceptable()) {
//                    register(selector, serverSocket);
//                }
//
//                if (key.isReadable()) {
//                    answerWithEcho(buffer, key);
//                }
//                iter.remove();
//            }
//        }
//    }
//
//    private static void answerWithEcho(ByteBuffer buffer, SelectionKey key) throws IOException {
//        SocketChannel client = (SocketChannel) key.channel();
//        int r = client.read(buffer);
//        if (r == -1 || new String(buffer.array()).trim()
//                .equals(POISON_PILL)) {
//            client.close();
//            System.out.println("Not accepting client messages anymore");
//        } else {
//            buffer.flip();
//
//            Charset charset = StandardCharsets.UTF_8; // Ustawienie odpowiedniego kodowania znaków
//            CharsetDecoder decoder = charset.newDecoder();
//            CharBuffer charBuffer = decoder.decode(buffer);
//
//
//            System.out.print(charBuffer.toString());
//            buffer.clear();
//        }
//    }
//
//    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
//        SocketChannel client = serverSocket.accept();
//        client.configureBlocking(false);
//        client.register(selector, SelectionKey.OP_READ);
//    }
}
//
//
//    private static final String POISON_PILL = "POISON_PILL";
//    Selector selector;
//    ServerSocketChannel serverSocket;
//    ByteBuffer buffer;
//
//    public SocketManager(){
//        try {
//            startServer();
//        } catch (IOException exception){
//            exception.printStackTrace();
//        }
//
//    }
//
//    public void startServer() throws IOException{
//        selector = Selector.open();
//        serverSocket = ServerSocketChannel.open();
//        serverSocket.bind(new InetSocketAddress("localhost", 8080));
//        serverSocket.configureBlocking(false);
//        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
//        buffer = ByteBuffer.allocate(1024);
//    }
//
//    public void listen() throws IOException{
//
//    }
//
//    private static void getMessage(ByteBuffer buffer, SelectionKey key) throws IOException {
//        SocketChannel client = (SocketChannel) key.channel();
//        int r = client.read(buffer);
//
//        if (r == -1 || new String(buffer.array()).trim()
//                .equals(POISON_PILL)) {
//            client.close();
//            System.out.println("Not accepting client messages anymore");
//        } else {
//            buffer.flip();
//
//            Charset charset = StandardCharsets.UTF_8;
//            CharsetDecoder decoder = charset.newDecoder();
//            CharBuffer charBuffer = decoder.decode(buffer);
//
//            SocketManager.parseMessage(charBuffer, key);
//
//            System.out.print(charBuffer.toString());
//            buffer.clear();
//        }
//    }
//
//    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
//        SocketChannel client = serverSocket.accept();
//        client.configureBlocking(false);
//        client.register(selector, SelectionKey.OP_READ);
//
////        System.out.println("Connection accepted: "+ client.getRemoteAddress());
//    }