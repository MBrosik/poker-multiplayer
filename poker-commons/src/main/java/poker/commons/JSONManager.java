package poker.commons;

import com.google.gson.Gson;
import poker.commons.socket.ReceiveData;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class JSONManager {
    public static ReceiveData jsonParse(CharBuffer charbuff){
        Gson gson = new Gson();

        return gson.fromJson(charbuff.toString(), ReceiveData.class);
    }

    public static ByteBuffer jsonStringify(ReceiveData receiveData){
        Gson gson = new Gson();

        String message = gson.toJson(receiveData);


        return ByteBuffer.wrap(message.getBytes());
    }
}
