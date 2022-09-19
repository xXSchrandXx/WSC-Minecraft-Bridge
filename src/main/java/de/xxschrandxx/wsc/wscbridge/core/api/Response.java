package de.xxschrandxx.wsc.wscbridge.core.api;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class Response<K ,V> {

    public final Type type = new TypeToken<HashMap<K, V>>(){}.getType();

    protected final Gson gson = new Gson();

    protected final Map<String, List<String>> header;
    protected final int code;
    protected final String message;
    protected HashMap<K, V> response = null;

    public Response(ResponseData data) {
        this(data.getCode(), data.getMessage(), data.getHeader(), data.getBody());
    }

    public Response(int code, String message, Map<String, List<String>> header, String body) {
        this.header = header;
        this.code = code;
        this.message = message;

        if (body == null) {
            return;
        }
        try {
            this.response = this.gson.fromJson(body, this.type);
        }
        catch (OutOfMemoryError | JsonSyntaxException e) {
        }
    }

    public Map<String, List<String>> getHeader() {
        return this.header;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Type getTypeOfResponse() {
        return this.type;
    }

    public HashMap<K, V> getResponse() {
        return this.response;
    }

    public V get(K key) {
        return this.response.get(key);
    }
}
