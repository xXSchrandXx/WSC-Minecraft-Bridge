package de.xxschrandxx.wsc.wscbridge.core.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public abstract class BridgeCoreAPI implements IBridgeCoreAPI {

    protected final String auth;
    public String getAuth() {
        return this.auth;
    }

    protected final IBridgeLogger logger;

    protected final Boolean debug;
    public Boolean isDebugModeEnabled() {
        return this.debug;
    }

    protected final Gson gson = new Gson();

    /**
     * MinecraftBridgeAPI
     * @param user Authentication User
     * @param password Authentication Password
     * @param logger Logger to log information
     * @param debug Weather debug mode is enabled
     */
    public BridgeCoreAPI(String user, String password, IBridgeLogger logger, Boolean debug) {
        this(new String(Base64.getEncoder().encode((user + ":" + password).getBytes())), logger, debug);
    }

    /**
     * MinecraftBridgeAPI
     * @param auth Authentication
     * @param logger Logger to log information
     * @param debug Weather debug mode is enabled
     */
    public BridgeCoreAPI(String auth, IBridgeLogger logger, Boolean debug) {
       this.auth = auth;
       this.logger = logger;
       this.debug = debug;
    }

    /**
     * MinecraftBridgeAPI
     * @param api
     * @param logger
     */
    public BridgeCoreAPI(BridgeCoreAPI api, IBridgeLogger logger) {
        this(api.getAuth(), logger, api.isDebugModeEnabled());
    }

    public void log(String message) {
        this.log(message, null);
    }

    public void log(String message, Exception exception) {
        if (exception == null) {
            this.logger.info("[Debug] " + message);
        }
        else {
            this.logger.warn("[Debug] " + message, exception);
        }
    }

    public abstract ISender<?> getSender(UUID uuid, IBridgePlugin<?> instance);

    public abstract ISender<?> getSender(String name, IBridgePlugin<?> instance);

    public abstract ArrayList<ISender<?>> getOnlineSender(IBridgePlugin<?> instance);

    public Response<String, Object> requestObject(URL url, HashMap<String, Object> postData) throws MalformedURLException, SocketTimeoutException, IOException {
        String postString = this.gson.toJson(postData);
        return this.requestObject(url, postString);
    }

    public Response<String, Object> requestObject(URL url, String postData) throws MalformedURLException, SocketTimeoutException, IOException {
        Response<String, Object> request = new Response<String, Object>(this.request(url, postData));
        return request;
    }

    public Response<String, String> requestString(URL url, HashMap<String, Object> postData) throws MalformedURLException, SocketTimeoutException, IOException {
        String postString = this.gson.toJson(postData);
        return this.requestString(url, postString);
    }

    public Response<String, String> requestString(URL url, String postData) throws MalformedURLException, SocketTimeoutException, IOException {
        Response<String, String> request = new Response<String, String>(this.request(url, postData));
        return request;
    }

    public ResponseData request(URL url, String postData) throws MalformedURLException, SocketTimeoutException, IOException {
        if (!url.getProtocol().equals("https") && !isDebugModeEnabled()) {
            throw new MalformedURLException("Only https is supportet. Given protocol: \"" + url.getProtocol() + "\"");
        }
        if (isDebugModeEnabled()) {
            this.log("Creating URLConnection.");
        }
        URLConnection c = url.openConnection();
        HttpURLConnection connection = null;
        if (!(c instanceof HttpsURLConnection) && !isDebugModeEnabled()) {
            throw new IOException("opened connection is not an HttpsURLConnection.");
        } else if (isDebugModeEnabled()) {
            connection = (HttpURLConnection) c;
        } else {
            connection = (HttpsURLConnection) c;
        }

        byte[] postDataByes = postData.getBytes();
        int postDataLength = postDataByes.length;
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            if (isDebugModeEnabled()) {
                this.log("Could not set method 'POST'", e);
            }
        }
        connection.setRequestProperty("Authorization", "Basic " + auth);
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        if (isDebugModeEnabled()) {
            this.log("Request-Header: " + connection.getRequestProperties().toString());
            this.log("Request-Body: " + connection.getRequestMethod() + " " + postData);
        }
        connection.setDoOutput(true);
        connection.setDoInput(true);
        try {
            connection.getOutputStream().write(postDataByes);
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
        }
        catch (UnknownServiceException e) {
        }
        if (isDebugModeEnabled()) {
            this.log("Opening connection.");
        }
        connection.connect();
        Map<String, List<String>> header = connection.getHeaderFields();
        if (isDebugModeEnabled()) {
            this.log("Response-Header: " + header.toString());
        }
        Integer code = connection.getResponseCode();
        if (isDebugModeEnabled()) {
            this.log("Response-Code: " + code.toString());
        }
        String message = connection.getResponseMessage();
        if (isDebugModeEnabled()) {
            this.log("Response-Message: " + message);
        }
        String body = null;
        try {
            InputStream input = connection.getInputStream();
            byte[] bodyBytes = input.readAllBytes();
            body = new String(bodyBytes, StandardCharsets.UTF_8);
            if (isDebugModeEnabled()) {
                this.log("Response-Body: " + body);
            }    
        }
        catch (IOException e) {
            if (isDebugModeEnabled()) {
                this.log("Response-Body: null", e);
            }
        }
        return new ResponseData(header, code, message, body);
    }

    public Response<String, Object> getObject(URL url) throws MalformedURLException, SocketTimeoutException, IOException {
        Response<String, Object> request = new Response<String, Object>(this.get(url));
        return request;
    }


    public Response<String, String> getString(URL url) throws MalformedURLException, SocketTimeoutException, IOException {
        Response<String, String> request = new Response<String, String>(this.get(url));
        return request;
    }

    public ResponseData get(URL url) throws MalformedURLException, SocketTimeoutException, IOException {
        if (!url.getProtocol().equals("https") && !isDebugModeEnabled()) {
            throw new MalformedURLException("Only https is supportet. Given protocol: \"" + url.getProtocol() + "\"");
        }
        if (!url.getProtocol().equals("https") && !isDebugModeEnabled()) {
            throw new MalformedURLException("Only https is supportet. Given protocol: \"" + url.getProtocol() + "\"");
        }
        if (isDebugModeEnabled()) {
            this.log("Creating URLConnection.");
        }
        URLConnection c = url.openConnection();
        HttpURLConnection connection = null;
        if (!(c instanceof HttpsURLConnection) && !isDebugModeEnabled()) {
            throw new IOException("opened connection is not an HttpsURLConnection.");
        } else if (isDebugModeEnabled()) {
            connection = (HttpURLConnection) c;
        } else {
            connection = (HttpsURLConnection) c;
        }
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            if (isDebugModeEnabled()) {
                this.log("Could not set method 'GET'", e);
            }
        }
        connection.setRequestProperty("Authorization", "Basic " + auth);
        if (isDebugModeEnabled()) {
            this.log("Request-Header: " + connection.getRequestProperties().toString());
        }
        connection.setDoInput(true);
        if (isDebugModeEnabled()) {
            this.log("Opening connection.");
        }
        connection.connect();
        Map<String, List<String>> header = connection.getHeaderFields();
        if (isDebugModeEnabled()) {
            this.log("Response-Header: " + header.toString());
        }
        Integer code = connection.getResponseCode();
        if (isDebugModeEnabled()) {
            this.log("Response-Code: " + code.toString());
        }
        String message = connection.getResponseMessage();
        if (isDebugModeEnabled()) {
            this.log("Response-Message: " + message);
        }
        String body = null;
        try {
            InputStream input = connection.getInputStream();
            byte[] bodyBytes = input.readAllBytes();
            body = new String(bodyBytes, StandardCharsets.UTF_8);
            if (isDebugModeEnabled()) {
                this.log("Response-Body: " + body);
            }    
        }
        catch (IOException e) {
            if (isDebugModeEnabled()) {
                this.log("Response-Body: null", e);
            }
        }
        return new ResponseData(header, code, message, body);
    }
}