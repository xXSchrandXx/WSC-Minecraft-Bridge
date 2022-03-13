package de.xxschrandxx.wsc.core;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;

public class MinecraftLinkerHandler {

    private final Logger logger;

    private HttpServer httpServer;
    private BasicAuthenticator auth;

    /**
     * Creates the handler.
     * @param logger the logger to use.
     * @param address the address to use.
     * @param useSSL weather ssl should be used.
     * @param user the user for the {@link BasicAuthenticator}
     * @param password the password for the {@link BasicAuthenticator}
     * @throws BindException @see {@link HttpServer#create(InetSocketAddress, int)}
     * @throws IOException @see {@link HttpServer#create(InetSocketAddress, int)}
     */
    public MinecraftLinkerHandler(Logger logger, InetSocketAddress address, boolean useSSL, String user, String password) throws BindException, IOException {
        this.logger = logger;
        if (useSSL)
            this.httpServer = HttpsServer.create(address, 0);
        else
            this.httpServer = HttpServer.create(address, 0);
        this.auth = new BasicAuthenticator("wsclinker") {
            @Override
            public boolean checkCredentials(String username, String passwd) {
                if (user.equals(username) && passwd.equals(password)) {
                    return true;
                }
                else {
                    return false;
                }
            }
        };
    }

    /**
     * Adds a {@link HttpServer#createContext(String, HttpHandler)} with given path.
     * @param path url path to listen on.
     * @param handler handler to add.
     * @return @see HttpServer#createContext(String, HttpHandler)
     */
    public HttpContext addHandler(String path, HttpHandler handler) {
        return httpServer.createContext(path, handler);
    }

    /**
     * Adds a {@link HttpHandler} with given path.
     * Automativally sets the {@link BasicAuthenticator}
     * @param path url path to listen on.
     * @param handler handler to add.
     * @return @see HttpServer#createContext(String, HttpHandler)
     */
    public HttpContext addPasswordHandler(String path, HttpHandler handler) {
        HttpContext context = this.addHandler(path, handler);
        context.setAuthenticator(this.auth);
        return context;
    }

    /**
     * Starts the webserver.
     * @return weather https server could be started
     */
    public boolean startHttp() {
        try {
            this.httpServer.start();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @see #startHttps(String, char[], File, String, char[])
     */
    public boolean startHttps(String keyStorePath, String storePassword, File dataFolder, String alias, String keyPassword) {
        return startHttps(keyStorePath, storePassword.toCharArray(), dataFolder, alias, keyPassword.toCharArray());
    }

    /**
     * <p>Starts the webserver.</p>
     *
     * <p>This method uses Parts of Player Analytics (Plan).</p>
     * <ul>
     * <li>Author <a href="https://github.com/AuroraLS3">AuroraLS3</a></li>
     * <li>Source: <a href="https://github.com/plan-player-analytics/Plan">plan-player-analytics/Plan</a></li>
     * <li>License: GNU Lesser General Public License v3</li>
     * </ul>
     * <div style="text-indent: 40px; line-height: 0.5;">
     * <p>Plan is free software: you can redistribute it and/or modify
     * it under the terms of the GNU Lesser General Public License v3 as published by
     * the Free Software Foundation, either version 3 of the License, or
     * (at your option) any later version.</p>
     * <p>Plan is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
     * GNU Lesser General Public License for more details.</p>
     * <p>You should have received a copy of the GNU Lesser General Public License
     * along with Plan. If not, see <a href="https://www.gnu.org/licenses/">&lt;https://www.gnu.org/licenses/&gt;</a>.</p>
     * </div>
     *
     * @param keyStorePath path to key storage
     * @param storePassword password for key storage
     * @param dataFolder datafolder for relative paths
     * @param alias the alias of the key
     * @param keyPassword the password for the key
     * @return weather https server could be started
     */
    // Following method is licensed under the same license: GNU Lesser General Public License v3
    public boolean startHttps(String keyStorePath, char[] storePassword, File dataFolder, String alias, char[] keyPassword) {
        try {
            if (!Paths.get(keyStorePath).isAbsolute()) {
                keyStorePath = dataFolder + File.separator + keyStorePath;
            }
        } catch (InvalidPathException e) {
            logger.log(Level.SEVERE, "WebServer: Could not find Keystore: ",  e);
        }

        boolean startSuccessful = false;
        String keyStoreKind = keyStorePath.endsWith(".p12") ? "PKCS12" : "JKS";
        try (FileInputStream fIn = new FileInputStream(keyStorePath)) {
            KeyStore keystore = KeyStore.getInstance(keyStoreKind);

            keystore.load(fIn, storePassword);
            Certificate cert = keystore.getCertificate(alias);

            if (cert == null) {
                throw new IllegalStateException("Alias: '" + alias + "' was not found in file " + keyStorePath + ".");
            }

            logger.log(Level.INFO, "WebServer: Certificate: " + cert.getType());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keystore, keyPassword);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keystore);

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), null/* trustManagerFactory.getTrustManagers() */, null);

            ((HttpsServer) this.httpServer).setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                @Override
                public void configure(HttpsParameters params) {
                    SSLEngine engine = getSSLContext().createSSLEngine();

                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    SSLParameters defaultSSLParameters = getSSLContext().getDefaultSSLParameters();
                    params.setSSLParameters(defaultSSLParameters);
                }
            });

            this.httpServer.start();

            startSuccessful = true;
        } catch (IllegalStateException e) {
            logger.log(Level.SEVERE, "WebServer: ", e);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "WebServer: SSL Context Initialization Failed.", e);
        } catch (EOFException e) {
            logger.log(Level.SEVERE, "WebServer: EOF when reading Certificate file. (Check that the file is not empty)", e);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "WebServer: No certificate file in " + keyStorePath + " not found!");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "WebServer: ", e);
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException e) {
            logger.log(Level.SEVERE, "WebServer: SSL Certificate loading Failed.", e);
        }

        return startSuccessful;
    }
    // End of GNU Lesser General Public License v3 licensed method

    /**
     * Stops the webserver.
     */
    public void stop() {
        this.httpServer.stop(0);
    }

}
