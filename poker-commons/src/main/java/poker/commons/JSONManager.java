package poker.commons;

import com.google.gson.Gson;
import poker.commons.socket.ReceiveData;

import java.nio.ByteBuffer;

public class JSONManager {
    public static ReceiveData jsonParse(String charbuff){
        Gson gson = new Gson();

        return gson.fromJson(charbuff, ReceiveData.class);
    }

    public static <T> T jsonParse1(String charbuff, Class<T> clazz){
        Gson gson = new Gson();

        return gson.fromJson(charbuff, clazz);
    }

    public static ByteBuffer jsonStringify(ReceiveData receiveData){
        Gson gson = new Gson();
        String message = gson.toJson(receiveData)+"\n";

        return ByteBuffer.wrap(message.getBytes());
    }

    public static <T> String jsonStringify1(T object){
        Gson gson = new Gson();
        String message = gson.toJson(object);

        return message;
    }

    public static <T> T reparseJson(Object object, Class<T> clazz){
        return jsonParse1(jsonStringify1(object), clazz);
    }

}
