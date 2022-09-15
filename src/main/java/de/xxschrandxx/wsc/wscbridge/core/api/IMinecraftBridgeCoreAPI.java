package de.xxschrandxx.wsc.wscbridge.core.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.xxschrandxx.wsc.wscbridge.core.IMinecraftBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public interface IMinecraftBridgeCoreAPI {

    public Integer getID();

    public String getAuth();

    /**
     * Weather debug mode is enabled
     * @return Boolean
     */
    public Boolean isDebugModeEnabled();

    public void log(String message);

    public void log(String message, Exception exception);

    public ISender<?> getSender(UUID uuid, IMinecraftBridgePlugin<?> instance);

    public ISender<?> getSender(String name, IMinecraftBridgePlugin<?> instance);

    public ArrayList<ISender<?>> getOnlineSender(IMinecraftBridgePlugin<?> instance);

    /**
     * Returns URL with added id for request.
     * @return URL
     * @throws MalformedURLException
     */
    public URL getURL(URL url) throws MalformedURLException;

    /**
     * Sends a request to given url
     * @param url Url to server
     * @param postData Data for request
     * @return {@link Response}
     * @throws IOException
     * @throws SocketTimeoutException
     * @throws MalformedURLException
     */
    public Response<String, Object> requestObject(URL url, HashMap<String, Object> postData) throws MalformedURLException, SocketTimeoutException, IOException;

    /**
     * Sends a request to given url
     * @param url Url to server
     * @param postData Data for request
     * @return {@link Response}
     * @throws IOException
     * @throws SocketTimeoutException
     * @throws MalformedURLException
     */
    public Response<String, Object> requestObject(URL url, String postData) throws MalformedURLException, SocketTimeoutException, IOException;


    /**
     * Sends a request to given url
     * @param url Url to server
     * @param postData Data for request
     * @return {@link Response}
     * @throws IOException
     * @throws SocketTimeoutException
     * @throws MalformedURLException
     */
    public Response<String, String> requestString(URL url, HashMap<String, Object> postData) throws MalformedURLException, SocketTimeoutException, IOException;

    /**
     * Sends a request to given url
     * @param url Url to server
     * @param postData Data for request
     * @return {@link Response}
     * @throws IOException
     * @throws SocketTimeoutException
     * @throws MalformedURLException
     */
    public Response<String, String> requestString(URL url, String postData) throws MalformedURLException, SocketTimeoutException, IOException;

    /**
     * Creates and connets to given url
     * @param url Url to server
     * @param postData Data for request
     * @return {@link ResponseData}
     * @throws IOException
     * @throws SocketTimeoutException
     * @throws MalformedURLException
     */
    public ResponseData request(URL url, String postData) throws MalformedURLException, SocketTimeoutException, IOException;
}
