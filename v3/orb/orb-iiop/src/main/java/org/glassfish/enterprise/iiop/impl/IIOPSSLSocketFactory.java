/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.enterprise.iiop.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import com.sun.corba.ee.spi.orb.ORB;
import com.sun.logging.LogDomains;
import org.glassfish.security.common.CipherInfo;


/**
 * This is socket factory used to create either plain sockets or SSL
 * sockets based on the target's policies and the client policies.
 * @author Vivek Nagar
 * @author Shing Wai Chan
 */
public class IIOPSSLSocketFactory /* implements ORBSocketFactory, Serializable */
{ 
    private static Logger _logger = null;
    static{
        _logger=LogDomains.getLogger(IIOPSSLSocketFactory.class, LogDomains.CORBA_LOGGER);
    }



    private static final String TLS = "TLS";
    private static final String SSL3 = "SSLv3";
    private static final String SSL2 = "SSLv2";
    private static final String SSL = "SSL";
    private static final String SSL_MUTUALAUTH = "SSL_MUTUALAUTH";
    private static final String PERSISTENT_SSL = "PERSISTENT_SSL";

    private static final int BACKLOG = 50;

    private static SecureRandom sr = null; // TODO J2EEServer.secureRandom;

    /* this is stored for the Server side of SSL Connections.
     * Note: There will be only a port per iiop listener and a corresponding
     * ctx for that port
     */
    /*
     * @todo provide an interface to the admin, so that whenever a iiop-listener
     * is added / removed, we modify the hashtable,
     */ 
    private Map portToSSLInfo = new Hashtable();
    /* this is stored for the client side of SSL Connections. 
     * Note: There will be only 1 ctx for the client side, as we will reuse the 
     * ctx for all SSL connections
     */
    private SSLInfo clientSslInfo = null;

    private ORB orb;

    /**
     * Constructs an <code>IIOPSSLSocketFactory</code>
     */
    public IIOPSSLSocketFactory() {
        /**
        try {
            if (Switch.getSwitch().getContainerType() == Switch.EJBWEB_CONTAINER) {
                ConfigContext configContext =
                    ApplicationServer.getServerContext().getConfigContext();
                IiopService iiopBean = ServerBeansFactory.getIiopServiceBean(configContext);

                IiopListener[] iiopListeners = iiopBean.getIiopListener();
                int listenersLength = (iiopListeners != null) ? iiopListeners.length : 0;
                for (int i = 0; i < listenersLength; i++) {
                    Ssl ssl = iiopListeners[i].getSsl();
                    SSLInfo sslInfo = null;
                    if (iiopListeners[i].isSecurityEnabled()) {
                        if (ssl != null) {
                            sslInfo = init(ssl.getCertNickname(),
                                ssl.isSsl2Enabled(), ssl.getSsl2Ciphers(),
                                ssl.isSsl3Enabled(), ssl.getSsl3TlsCiphers(),
                                ssl.isTlsEnabled());
                        } else {
                            sslInfo = getDefaultSslInfo();
                        }
                        portToSSLInfo.put(
                            new Integer(iiopListeners[i].getPort()), sslInfo);
                    }
                }

                if (iiopBean.getSslClientConfig() != null &&
                        iiopBean.getSslClientConfig().isEnabled()) {
                    Ssl outboundSsl = iiopBean.getSslClientConfig().getSsl();
                    if (outboundSsl != null) {
                        clientSslInfo = init(outboundSsl.getCertNickname(),
                            outboundSsl.isSsl2Enabled(),
                            outboundSsl.getSsl2Ciphers(),
                            outboundSsl.isSsl3Enabled(),
                            outboundSsl.getSsl3TlsCiphers(),
                            outboundSsl.isTlsEnabled());
                    }
                }
                if (clientSslInfo == null) {
                    clientSslInfo = getDefaultSslInfo();
                }
            } else {
                com.sun.enterprise.config.clientbeans.Ssl clientSsl =
                        SSLUtils.getAppclientSsl();
                if (clientSsl != null) {
                    clientSslInfo = init(clientSsl.getCertNickname(),
                        clientSsl.isSsl2Enabled(), clientSsl.getSsl2Ciphers(),
                        clientSsl.isSsl3Enabled(), clientSsl.getSsl3TlsCiphers(),
                        clientSsl.isTlsEnabled());
                } else { // include case keystore, truststore jvm option
                    clientSslInfo = getDefaultSslInfo();
                } 
            }
        } catch (Exception e) {
            _logger.log(Level.SEVERE,"iiop.init_exception",e);
            throw new IllegalStateException(e.toString());
        }

         **/
    }

    /**
     * Return a default SSLInfo object.
     */
    private SSLInfo getDefaultSslInfo() throws Exception {
       /** return init(null, false, null, true, null, true);    **/
    return null;
    }

    /**
     * serveralias/clientalias cannot be set at the same time. 
     * this method encapsulates the common code for both the client side and
     * server side to create a SSLContext
     * it is called once for each serveralias and once for each clientalias 
     */
    private SSLInfo init(String alias,
            boolean ssl2Enabled, String ssl2Ciphers,
            boolean ssl3Enabled, String ssl3TlsCiphers,
            boolean tlsEnabled) throws Exception {

        /*
        String protocol;
        if (tlsEnabled) {
            protocol = TLS;
        } else if (ssl3Enabled) {
            protocol = SSL3;
        } else if (ssl2Enabled) {
            protocol = SSL2;
        } else { // default
            protocol = "SSL";
        }

        String[] ssl3TlsCipherArr = null;
        if (tlsEnabled || ssl3Enabled) {
            ssl3TlsCipherArr = getEnabledCipherSuites(ssl3TlsCiphers,
                    false, ssl3Enabled, tlsEnabled);
        }

        String[] ssl2CipherArr = null;
        if (ssl2Enabled) {
            ssl2CipherArr = getEnabledCipherSuites(ssl2Ciphers,
                    true, false, false);
        }

        SSLContext ctx = SSLContext.getInstance(protocol);

        if (alias != null && !SSLUtils.isTokenKeyAlias(alias)) {
            throw new IllegalStateException(getFormatMessage(
                    "iiop.cannot_find_keyalias", new Object[] { alias }));
        }
            
        KeyManager[] mgrs = SSLUtils.getKeyManagers();
        if (alias != null && mgrs != null && mgrs.length > 0) {
            KeyManager[] newMgrs = new KeyManager[mgrs.length];
            for (int i = 0; i < mgrs.length; i++) {
                if (_logger.isLoggable(Level.FINE)) {
                    StringBuffer msg = new StringBuffer("Setting J2EEKeyManager for ");
                    msg.append(" alias : "+alias);
                    _logger.log(Level.FINE, msg.toString());
                }
                newMgrs[i] = new J2EEKeyManager((X509KeyManager)mgrs[i], alias);
            }
            mgrs = newMgrs;
        }
        ctx.init(mgrs, SSLUtils.getTrustManagers(), sr);

        return new SSLInfo(ctx, ssl3TlsCipherArr, ssl2CipherArr);

        */
        return null;
    }

    //----- implements com.sun.corba.ee.spi.transport.ORBSocketFactory -----

    public void setORB(ORB orb) {
        this.orb = orb;
    }

    /**
     * Create a server socket on the specified InetSocketAddress  based on the
     * type of the server socket (SSL, SSL_MUTUALAUTH, PERSISTENT_SSL or CLEAR_TEXT).
     * @param type type of socket to create.
     * @param  inetSocketAddress the InetSocketAddress
     * @return the server socket on the specified InetSocketAddress
     * @exception IOException if an I/O error occurs during server socket
     * creation
     */
    public ServerSocket createServerSocket(String type,
            InetSocketAddress inetSocketAddress) throws IOException {

        /*

	if (_logger.isLoggable(Level.FINE)) {
	    _logger.log(Level.FINE, "Creating server socket for type =" + type
                + " inetSocketAddress =" + inetSocketAddress);
	}

	if(type.equals(SSL_MUTUALAUTH) || type.equals(SSL) || 
		type.equals(PERSISTENT_SSL)) {
	    return createSSLServerSocket(type, inetSocketAddress);
	} else {
            ServerSocket serverSocket = null;
            if (orb.getORBData().acceptorSocketType().equals(
                    ORBConstants.SOCKETCHANNEL)) {
                ServerSocketChannel serverSocketChannel =
                        ServerSocketChannel.open();
                serverSocket = serverSocketChannel.socket();
            } else {
                serverSocket = new ServerSocket();
            }

	    serverSocket.bind(inetSocketAddress);
	    return serverSocket;

	}
	*/
        return null;
    }

    /**
     * Create a client socket for the specified InetSocketAddress. Creates an SSL
     * socket if the type specified is SSL or SSL_MUTUALAUTH.
     * @param type
     * @param inetSocketAddress
     * @return the socket.
     */
    public Socket createSocket(String type, InetSocketAddress inetSocketAddress)
            throws IOException {

        /*

	try {
	    String host = inetSocketAddress.getHostName();
	    int port = inetSocketAddress.getPort();
	    if (_logger.isLoggable(Level.FINE)) {
		_logger.log(Level.FINE, "createSocket(" + type + ", " + host + ", " +port + ")");
	    }
	    if (type.equals(SSL) || type.equals(SSL_MUTUALAUTH)) {
		return createSSLSocket(host, port);
	    } else {
                Socket socket = null;
		if (_logger.isLoggable(Level.FINE)) {
		    _logger.log(Level.FINE, "Creating CLEAR_TEXT socket for:" +port);
		}

                if (orb.getORBData().connectionSocketType().equals(
                        ORBConstants.SOCKETCHANNEL)) {
	            SocketChannel socketChannel = ORBUtility.openSocketChannel(inetSocketAddress);
	            socket = socketChannel.socket();
	        } else {
                    socket = new Socket(inetSocketAddress.getHostName(),
                        inetSocketAddress.getPort());
	        }

                // Disable Nagle's algorithm (i.e. always send immediately).
		socket.setTcpNoDelay(true);
                return socket;
	    }
	} catch ( Exception ex ) {
	    if(_logger.isLoggable(Level.FINE)) {
		_logger.log(Level.FINE,"Exception creating socket",ex);
	    }
	    throw new RuntimeException(ex);
	}
    }

    public void setAcceptedSocketOptions(Acceptor acceptor,
            ServerSocket serverSocket, Socket socket) throws SocketException {

	if (_logger.isLoggable(Level.FINE)) {
	    _logger.log(Level.FINE, "setAcceptedSocketOptions: " + acceptor
                    + " " + serverSocket + " " + socket);
	}
        // Disable Nagle's algorithm (i.e., always send immediately).
        socket.setTcpNoDelay(true);

        */
        return null;
    }

    //----- END implements com.sun.corba.ee.spi.transport.ORBSocketFactory -----

    /**
     * Create an SSL server socket at the specified InetSocketAddress. If the type
     * is SSL_MUTUALAUTH then SSL client authentication is requested.
     */
    private ServerSocket createSSLServerSocket(String type,
            InetSocketAddress inetSocketAddress) throws IOException {
        /*

        if (inetSocketAddress == null) {
            throw new IOException(getFormatMessage(
                "iiop.invalid_sslserverport",
                new Object[] { null }));
        }
        int port = inetSocketAddress.getPort();
        Integer iport = new Integer(port);
        SSLInfo sslInfo = (SSLInfo)portToSSLInfo.get(iport);
        if (sslInfo == null) {
            throw new IOException(getFormatMessage(
                "iiop.invalid_sslserverport",
                new Object[] { iport }));
        }
        SSLServerSocketFactory ssf = sslInfo.getContext().getServerSocketFactory();   
        String[] ssl3TlsCiphers = sslInfo.getSsl3TlsCiphers();
        String[] ssl2Ciphers = sslInfo.getSsl2Ciphers();
        String[] ciphers = null;
        if (ssl3TlsCiphers != null || ssl2Ciphers != null) {
            String[] socketCiphers = ssf.getDefaultCipherSuites();
            ciphers = mergeCiphers(socketCiphers, ssl3TlsCiphers, ssl2Ciphers);
        }

	String cs[] = null;

	if(_logger.isLoggable(Level.FINE)) {
	    cs = ssf.getSupportedCipherSuites();
	    for(int i=0; i < cs.length; ++i) {
		_logger.log(Level.FINE,"Cipher Suite: " + cs[i]);
	    }
	}
	ServerSocket ss = null;
        try{
            // bugfix for 6349541
            // specify the ip address to bind to, 50 is the default used
            // by the ssf implementation when only the port is specified
            ss = ssf.createServerSocket(port, BACKLOG, inetSocketAddress.getAddress());
            if (ciphers != null) {
                ((SSLServerSocket)ss).setEnabledCipherSuites(ciphers);
            }
        } catch(IOException e) {
            _logger.log(Level.SEVERE, "iiop.createsocket_exception",
                new Object[] { type, String.valueOf(port) });
            _logger.log(Level.SEVERE, "", e);
            throw e;
        }

	try {
	    if(type.equals(SSL_MUTUALAUTH)) {
		_logger.log(Level.FINE,"Setting Mutual auth");
		((SSLServerSocket)ss).setNeedClientAuth(true);
	    }
	} catch(Exception e) {
	    _logger.log(Level.SEVERE,"iiop.cipher_exception",e);
	    throw new IOException(e.getMessage());
	}
	if(_logger.isLoggable(Level.FINE)) {
	    _logger.log(Level.FINE,"Created server socket:" + ss);
	}
	return ss;
	    */
        return null;
    }

    /**
     * Create an SSL socket at the specified host and port.
     * @param host
     * @param port
     * @return the socket.
     */
    private Socket createSSLSocket(String host, int port)
        throws IOException {
        	SSLSocket socket = null;
        /*
            

	SSLSocketFactory factory = null;
        try{
            // get socketfactory+sanity check
            // clientSslInfo is never null
            factory = clientSslInfo.getContext().getSocketFactory();

            if(_logger.isLoggable(Level.FINE)) {
                  _logger.log(Level.FINE,"Creating SSL Socket for host:" + host +" port:" + port);
            }
            String[] ssl3TlsCiphers = clientSslInfo.getSsl3TlsCiphers();
            String[] ssl2Ciphers = clientSslInfo.getSsl2Ciphers();
            String[] clientCiphers = null;
            if (ssl3TlsCiphers != null || ssl2Ciphers != null) {
                String[] socketCiphers = factory.getDefaultCipherSuites();
                clientCiphers = mergeCiphers(socketCiphers, ssl3TlsCiphers, ssl2Ciphers);
            }

            socket = (SSLSocket)factory.createSocket(host, port);
            if (clientCiphers != null) {
                socket.setEnabledCipherSuites(clientCiphers);
            }
        }catch(Exception e) {
            if(_logger.isLoggable(Level.FINE)) {
                _logger.log(Level.FINE, "iiop.createsocket_exception",
                new Object[] { host, String.valueOf(port) });
                _logger.log(Level.FINE, "", e);
            }
            IOException e2 = new IOException(
            "Error opening SSL socket to host="+host+" port="+port);
            e2.initCause(e);
            throw e2;
        }

        */
        return socket;
    }

    /**
     * This API return an array of String listing the enabled cipher suites.
     * Input is the cipherSuiteStr from xml which a space separated list
     * ciphers with a prefix '+' indicating enabled, '-' indicating disabled.
     * If no cipher is enabled, then it returns an empty array.
     * If no cipher is specified, then all are enabled and it returns null.
     * @param cipherSuiteStr cipherSuiteStr from xml
     * @param ssl2Enabled
     * @param ssl3Enabled
     * @param tlsEnabled
     * @return an array of enabled Ciphers
     */
    private String[] getEnabledCipherSuites(String cipherSuiteStr,
            boolean ssl2Enabled, boolean ssl3Enabled, boolean tlsEnabled) {
        String[] cipherArr = null;
        /*
        if (cipherSuiteStr != null && cipherSuiteStr.length() > 0) {
            ArrayList cipherList = new ArrayList();
            StringTokenizer tokens = new StringTokenizer(cipherSuiteStr, ",");
            while (tokens.hasMoreTokens()) {
                String cipherAction = tokens.nextToken();
                if (cipherAction.startsWith("+")) {
                    String cipher = cipherAction.substring(1);
                    CipherInfo cipherInfo = CipherInfo.getCipherInfo(cipher);
                    if (cipherInfo != null &&
                            isValidProtocolCipher(cipherInfo, ssl2Enabled,
                                ssl3Enabled, tlsEnabled)) {
                        cipherList.add(cipherInfo.getCipherName());
                    } else {
                        throw new IllegalStateException(getFormatMessage(
                            "iiop.unknown_cipher",
                            new Object[] { cipher }));
                    }
                } else if (cipherAction.startsWith("-")) {
                    String cipher = cipherAction.substring(1);
                    CipherInfo cipherInfo = CipherInfo.getCipherInfo(cipher);
                    if (cipherInfo == null ||
                            !isValidProtocolCipher(cipherInfo, ssl2Enabled,
                                ssl3Enabled, tlsEnabled)) {
                        throw new IllegalStateException(getFormatMessage(
                            "iiop.unknown_cipher",
                            new Object[] { cipher }));
                    }
                } else if (cipherAction.trim().length() > 0) {
                    throw new IllegalStateException(getFormatMessage(
                        "iiop.invalid_cipheraction",
                        new Object[] { cipherAction }));
                }
            }

            cipherArr = (String[])cipherList.toArray(
                    new String[cipherList.size()]);
        }
        */
        return cipherArr;
    }

    /**
     * Return an array of merged ciphers.
     * @param enableCiphers  ciphers enabled by socket factory
     * @param ssl3TlsCiphers
     * @param ssl2Ciphers
     */
    private String[] mergeCiphers(String[] enableCiphers,
            String[] ssl3TlsCiphers, String[] ssl2Ciphers) {

        /*

        if (ssl3TlsCiphers == null && ssl2Ciphers == null) {
            return null;
        }

        int eSize = (enableCiphers != null)? enableCiphers.length : 0;

        if (_logger.isLoggable(Level.FINE)) {
            StringBuffer buf = new StringBuffer("Default socket ciphers: ");
            for (int i = 0; i < eSize; i++) {
                buf.append(enableCiphers[i] + ", ");
            }
            _logger.log(Level.FINE, buf.toString());
        }

        ArrayList cList = new ArrayList();
        if (ssl3TlsCiphers != null) {
            for (int i = 0; i < ssl3TlsCiphers.length; i++) {
                cList.add(ssl3TlsCiphers[i]);
            }
        } else {
            for (int i = 0; i < eSize; i++) {
                String cipher = enableCiphers[i];
                CipherInfo cInfo = CipherInfo.getCipherInfo(cipher);
                if (cInfo != null && (cInfo.isTLS() || cInfo.isSSL3())) {
                    cList.add(cipher);
                }
            }
        }
        
        if (ssl2Ciphers != null) {
            for (int i = 0; i < ssl2Ciphers.length; i++) {
                cList.add(ssl2Ciphers[i]);
            }
        } else {
            for (int i = 0; i < eSize; i++) {
                String cipher = enableCiphers[i];
                CipherInfo cInfo = CipherInfo.getCipherInfo(cipher);
                if (cInfo != null && cInfo.isSSL2()) {
                    cList.add(cipher);
                }
            }
        }

        if (_logger.isLoggable(Level.FINE)) {
            _logger.log(Level.FINE, "Merged socket ciphers: " + cList);
        }

        return (String[])cList.toArray(new String[cList.size()]);
        */
        return null;
    }

    /**
     * Check whether given cipherInfo belongs to given protocol.
     * @param cipherInfo
     * @param ssl2Enabled
     * @param ssl3Enabled
     * @param tlsEnabled
     */
    private boolean isValidProtocolCipher(CipherInfo cipherInfo,
            boolean ssl2Enabled, boolean ssl3Enabled, boolean tlsEnabled) {
        /*
        return (tlsEnabled && cipherInfo.isTLS() ||
                ssl3Enabled && cipherInfo.isSSL3() ||
                ssl2Enabled && cipherInfo.isSSL2());
                */
        return false;
    }

    /**
     * This API get the format string from resource bundle of _logger.
     * @param key the key of the message
     * @param params the parameter array of Object
     * @return the format String for _logger
     */
    private String getFormatMessage(String key, Object[] params) {
        return MessageFormat.format(
            _logger.getResourceBundle().getString(key), params);
    }

    class SSLInfo {
        private SSLContext ctx;
        private String[] ssl3TlsCiphers = null;
        private String[] ssl2Ciphers = null;

        SSLInfo(SSLContext ctx, String[] ssl3TlsCiphers, String[] ssl2Ciphers) {
            this.ctx = ctx;
            this.ssl3TlsCiphers = ssl3TlsCiphers;
            this.ssl2Ciphers = ssl2Ciphers;
        }

        SSLContext getContext() {
            return ctx;
        }

        String[] getSsl3TlsCiphers() {
            return ssl3TlsCiphers;
        }

        String[] getSsl2Ciphers() {
            return ssl2Ciphers;
        }
    }
} 
