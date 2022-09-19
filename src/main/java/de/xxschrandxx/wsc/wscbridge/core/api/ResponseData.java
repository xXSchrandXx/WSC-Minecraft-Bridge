package de.xxschrandxx.wsc.wscbridge.core.api;

import java.util.List;
import java.util.Map;

public class ResponseData {
    private final Map<String, List<String>> header;
    public Map<String, List<String>> getHeader() {
        return this.header;
    }
    private final Integer code;
    public Integer getCode() {
        return this.code;
    }
    private final String message;
    public String getMessage() {
        return this.message;
    }
    private final String body;
    public String getBody() {
        return this.body;
    }

    public ResponseData(Map<String, List<String>> header, Integer code, String message, String body) {
        this.header = header;
        this.code = code;
        this.message = message;
        this.body = body;
    }
}
