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

public abstract class MinecraftBridgeCoreAPI {

    private final Integer id;
    private final String auth;
    protected final Logger logger;
    private final Boolean debug;
    protected final Gson gson = new Gson();

    /**
     * MinecraftBridgeAPI
     * @param id MinecraftID
     * @param user Authentication User
     * @param password Authentication Password
     * @param debug Weather debug mode is enabled
     */
    public MinecraftBridgeCoreAPI(Integer id, String user, String password, Logger logger, Boolean debug) {
        this.id = id;
        String authString = user + ":" + password;
        this.auth = new String(Base64.getEncoder().encode(authString.getBytes()));
        this.logger = logger;
        this.debug = debug;
    }

    /**
     * Weather debug mode is enabled
     * @return Boolean
     */
    public Boolean isDebugModeEnabled() {
        return this.debug;
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

    /**
     * Returns URL with added id for request.
     * @return URL
     * @throws MalformedURLException
     */
    public URL getURL(URL url) throws MalformedURLException {
        URL newURL = new URL(url, "&id=" + this.id);
        if (isDebugModeEnabled()) {
            this.log("New URL: " + newURL.toString());
        }
        return newURL;
    }

    /**
     * Sends a request to given url
     * @param url Url to server
     * @param postData Data for request
     * @return {@link Response}
     * @throws SocketTimeoutException
     * @throws MalformedURLException
     * @throws UnknownServiceException
     * @throws IllegalStateException
     * @throws NullPointerException
     * @throws ProtocolException
     * @throws SecurityException
     * @throws IOException
     */
    public Response<String, Object> requestObject(URL url, HashMap<String, Object> postData) throws MalformedURLException, SocketTimeoutException, IOException {
        String postString = this.gson.toJson(postData);
        return this.requestObject(url, postString);
    }

    /**
     * Sends a request to given url
     * @param url Url to server
     * @param postData Data for request
     * @return {@link Response}
     * @throws IOException
     * @throws SocketTimeoutException
     * @throws MalformedURLException
     * @throws UnknownServiceException
     * @throws IllegalStateException
     * @throws NullPointerException
     * @throws ProtocolException
     * @throws SecurityException
     */
    public Response<String, Object> requestObject(URL url, String postData) throws MalformedURLException, SocketTimeoutException, IOException {
        Response<String, Object> request = new Response<String, Object>(this.request(url, postData));
        return request;
    }

    /**
     * Sends a request to given url
     * @param url Url to server
     * @param postData Data for request
     * @return {@link Response}
     * @throws SocketTimeoutException
     * @throws MalformedURLException
     * @throws UnknownServiceException
     * @throws IllegalStateException
     * @throws NullPointerException
     * @throws ProtocolException
     * @throws SecurityException
     * @throws IOException
     */
    public Response<String, String> requestString(URL url, HashMap<String, Object> postData) throws MalformedURLException, SocketTimeoutException, IOException {
        String postString = this.gson.toJson(postData);
        return this.requestString(url, postString);
    }

    /**
     * Sends a request to given url
     * @param url Url to server
     * @param postData Data for request
     * @return {@link Response}
     * @throws IOException
     * @throws SocketTimeoutException
     * @throws MalformedURLException
     * @throws UnknownServiceException
     * @throws IllegalStateException
     * @throws NullPointerException
     * @throws ProtocolException
     * @throws SecurityException
     */
    public Response<String, String> requestString(URL url, String postData) throws MalformedURLException, SocketTimeoutException, IOException {
        Response<String, String> request = new Response<String, String>(this.request(url, postData));
        return request;
    }

    /**
     * Creates and connets to given url
     * @param url Url to server
     * @param postData Data for request
     * @return {@link ResponseData}
     * @throws MalformedURLException
     * @throws SocketTimeoutException
     * @throws IOException
     */
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
}