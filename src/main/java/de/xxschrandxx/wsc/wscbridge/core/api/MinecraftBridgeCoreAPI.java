package de.xxschrandxx.wsc.wscbridge.core.api;

import java.io.IOException;
import java.io.InputStream;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

import de.xxschrandxx.wsc.wscbridge.core.IMinecraftBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public abstract class MinecraftBridgeCoreAPI implements IMinecraftBridgeCoreAPI {

    protected final Integer id;
    public Integer getID() {
        return this.id;
    }

    protected final String auth;
    public String getAuth() {
        return this.auth;
    }

    protected final Logger logger;

    protected final Boolean debug;
    public Boolean isDebugModeEnabled() {
        return this.debug;
    }

    protected final Gson gson = new Gson();

    /**
     * MinecraftBridgeAPI
     * @param id MinecraftID
     * @param user Authentication User
     * @param password Authentication Password
     * @param debug Weather debug mode is enabled
     */
    public MinecraftBridgeCoreAPI(Integer id, String user, String password, Logger logger, Boolean debug) {
        this(id, new String(Base64.getEncoder().encode((user + ":" + password).getBytes())), logger, debug);
    }

    /**
    * MinecraftBridgeAPI
    * @param id MinecraftID
    * @param auth Authentication
    * @param debug Weather debug mode is enabled
    */
   public MinecraftBridgeCoreAPI(Integer id, String auth, Logger logger, Boolean debug) {
       this.id = id;
       this.auth = auth;
       this.logger = logger;
       this.debug = debug;
   }

    public void log(String message) {
        this.log(message, null);
    }

    public void log(String message, Exception exception) {
        if (exception == null) {
            this.logger.log(Level.INFO, "[Debug] " + message);
        }
        else {
            this.logger.log(Level.INFO, "[Debug] " + message, exception);
        }
    }

    public abstract ISender<?> getSender(UUID uuid, IMinecraftBridgePlugin<?> instance);

    public abstract ISender<?> getSender(String name, IMinecraftBridgePlugin<?> instance);

    public abstract ArrayList<ISender<?>> getOnlineSender(IMinecraftBridgePlugin<?> instance);

    public URL getURL(URL url) throws MalformedURLException {
        URL newURL = new URL(url, "&id=" + this.id);
        if (isDebugModeEnabled()) {
            this.log("New URL: " + newURL.toString());
        }
        return newURL;
    }

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
        URL newURL = this.getURL(url);
        if (!newURL.getProtocol().equals("https")) {
            throw new MalformedURLException("Only https is supportet. Given protocol: \"" + url.getProtocol() + "\"");
        }
        if (isDebugModeEnabled()) {
            this.log("Creating URLConnection.");
        }
        URLConnection c = newURL.openConnection();
        if (!(c instanceof HttpsURLConnection)) {
            throw new IOException("opened connection is not an HttpsURLConnection.");
        }
        HttpsURLConnection connection = (HttpsURLConnection) c;

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
            this.log("Request-Body: " + postData);
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
        URL newURL = this.getURL(url);
        if (!newURL.getProtocol().equals("https")) {
            throw new MalformedURLException("Only https is supportet. Given protocol: \"" + url.getProtocol() + "\"");
        }
        if (isDebugModeEnabled()) {
            this.log("Creating URLConnection.");
        }
        URLConnection c = newURL.openConnection();
        if (!(c instanceof HttpsURLConnection)) {
            throw new IOException("opened connection is not an HttpsURLConnection.");
        }
        HttpsURLConnection connection = (HttpsURLConnection) c;
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