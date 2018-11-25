package com.github.vindell.httpclient;

import java.io.IOException;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import com.github.vindell.httpclient.utils.SSLContextUtils;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientConnectionManagerBuilder {
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpClientConnectionManagerBuilder.class);
	
    private HostnameVerifier hostnameVerifier;
    private PublicSuffixMatcher publicSuffixMatcher;
    private TrustStrategy trustStrategy;
    private KeyStore keystore;
    private SSLContext sslContext;
    private LayeredConnectionSocketFactory sslSocketFactory;
   
    private SocketConfig defaultSocketConfig;
    private ConnectionConfig defaultConnectionConfig;
    private SchemePortResolver schemePortResolver;
    private DnsResolver dnsResolver;
    private boolean systemProperties;
    private int maxConnTotal = 0;
    // 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
    // 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)，路由是对maxTotal的细分。
    private int maxConnPerRoute = 2;
    private long connTimeToLive = -1;
    private TimeUnit connTimeToLiveTimeUnit = TimeUnit.MILLISECONDS;
    private HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
    /**
	 * SSL证书类型；TLS,SSL,SSLv2
	 */
	protected String protocol = SSLConnectionSocketFactory.TLS;
	
    public static HttpClientConnectionManagerBuilder create() {
        return new HttpClientConnectionManagerBuilder();
    }

    protected HttpClientConnectionManagerBuilder() {
        super();
    }
    

    /**
     * Assigns {@link javax.net.ssl.HostnameVerifier} instance.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     *   org.apache.http.conn.HttpClientConnectionManager)} and the {@link #setSSLSocketFactory(
     *   org.apache.http.conn.socket.LayeredConnectionSocketFactory)} methods.
     * </p>
     *
     *   @since 4.4
     */
    public final HttpClientConnectionManagerBuilder setSSLHostnameVerifier(final HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    /**
     * Assigns file containing public suffix matcher. Instances of this class can be created
     * with {@link org.apache.http.conn.util.PublicSuffixMatcherLoader}.
     *
     * @see org.apache.http.conn.util.PublicSuffixMatcher
     * @see org.apache.http.conn.util.PublicSuffixMatcherLoader
     *
     *   @since 4.4
     */
    public final HttpClientConnectionManagerBuilder setPublicSuffixMatcher(final PublicSuffixMatcher publicSuffixMatcher) {
        this.publicSuffixMatcher = publicSuffixMatcher;
        return this;
    }
    
    /**
     * Assigns {@link TrustStrategy} instance.
     */
    public final HttpClientConnectionManagerBuilder setTrustStrategy(TrustStrategy trustStrategy) {
        this.trustStrategy = trustStrategy;
        return this;
    }
    
    /**
     * Assigns {@link KeyStore} instance.
     */
    public final HttpClientConnectionManagerBuilder setKeyStore(KeyStore keystore) {
        this.keystore = keystore;
        return this;
    }
    
    /**
     * Assigns protocol value.
     */
    public final HttpClientConnectionManagerBuilder setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }
    
    /**
     * Assigns {@link SSLContext} instance.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     *   org.apache.http.conn.HttpClientConnectionManager)} and the {@link #setSSLSocketFactory(
     *   org.apache.http.conn.socket.LayeredConnectionSocketFactory)} methods.
     * </p>
     */
    public final HttpClientConnectionManagerBuilder setSSLContext(final SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    /**
     * Assigns {@link LayeredConnectionSocketFactory} instance.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     *   org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     */
    public final HttpClientConnectionManagerBuilder setSSLSocketFactory(
            final LayeredConnectionSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    /**
     * Assigns maximum total connection value.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     *   org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     */
    public final HttpClientConnectionManagerBuilder setMaxConnTotal(final int maxConnTotal) {
        this.maxConnTotal = maxConnTotal;
        return this;
    }

    /**
     * Assigns maximum connection per route value.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     *   org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     */
    public final HttpClientConnectionManagerBuilder setMaxConnPerRoute(final int maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
        return this;
    }

    /**
     * Assigns default {@link SocketConfig}.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     *   org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     */
    public final HttpClientConnectionManagerBuilder setDefaultSocketConfig(final SocketConfig config) {
        this.defaultSocketConfig = config;
        return this;
    }

    /**
     * Assigns default {@link ConnectionConfig}.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     *   org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     */
    public final HttpClientConnectionManagerBuilder setDefaultConnectionConfig(final ConnectionConfig config) {
        this.defaultConnectionConfig = config;
        return this;
    }
    
    /**
     * Assigns {@link SchemePortResolver} instance.
     */
    public final HttpClientConnectionManagerBuilder setSchemePortResolver(
            final SchemePortResolver schemePortResolver) {
        this.schemePortResolver = schemePortResolver;
        return this;
    }
    
    /**
     * Assigns {@link DnsResolver} instance.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(HttpClientConnectionManager)} method.
     */
    public final HttpClientConnectionManagerBuilder setDnsResolver(final DnsResolver dnsResolver) {
        this.dnsResolver = dnsResolver;
        return this;
    }

    /**
     * Sets maximum time to live for persistent connections
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     *   org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     *
     * @since 4.4
     */
    public final HttpClientConnectionManagerBuilder setConnectionTimeToLive(final long connTimeToLive, final TimeUnit connTimeToLiveTimeUnit) {
        this.connTimeToLive = connTimeToLive;
        this.connTimeToLiveTimeUnit = connTimeToLiveTimeUnit;
        return this;
    }
    
    /**
     * Use system properties when creating and configuring default
     * implementations.
     */
    public final HttpClientConnectionManagerBuilder useSystemProperties() {
        this.systemProperties = true;
        return this;
    }
     
    
    private static String[] split(final String s) {
        if (TextUtils.isBlank(s)) {
            return null;
        }
        return s.split(" *, *");
    }

	protected PoolingHttpClientConnectionManager instance(Registry<ConnectionSocketFactory> socketFactoryRegistry,
			HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory,
			SchemePortResolver schemePortResolver, DnsResolver dnsResolver, long connTimeToLive,
			TimeUnit connTimeToLiveTimeUnit) {
		return new PoolingHttpClientConnectionManager(socketFactoryRegistry, connFactory, schemePortResolver,
				dnsResolver, connTimeToLive,
				connTimeToLiveTimeUnit != null ? connTimeToLiveTimeUnit : TimeUnit.MILLISECONDS);
	}
    
   public HttpClientConnectionManager build(){
	   // Create main request executor
       // We copy the instance fields to avoid changing them, and rename to avoid accidental use of the wrong version
       PublicSuffixMatcher publicSuffixMatcherCopy = this.publicSuffixMatcher;
       if (publicSuffixMatcherCopy == null) {
           publicSuffixMatcherCopy = PublicSuffixMatcherLoader.getDefault();
       }
       LayeredConnectionSocketFactory sslSocketFactoryCopy = this.sslSocketFactory;
        if (sslSocketFactoryCopy == null) {
            final String[] supportedProtocols = systemProperties ? split(
                    System.getProperty("https.protocols")) : null;
            final String[] supportedCipherSuites = systemProperties ? split(
                    System.getProperty("https.cipherSuites")) : null;
            HostnameVerifier hostnameVerifierCopy = this.hostnameVerifier;
            if (hostnameVerifierCopy == null) {
                hostnameVerifierCopy = new DefaultHostnameVerifier(publicSuffixMatcherCopy);
            }
            if (keystore != null && trustStrategy != null) {
            	try {
					sslContext = SSLContextUtils.createSSLContext(keystore, trustStrategy);
				} catch (IOException e) {
				}
            } 
            if (sslContext != null) {
                sslSocketFactoryCopy = new SSLConnectionSocketFactory(
                        sslContext, supportedProtocols, supportedCipherSuites, hostnameVerifierCopy);
            } else {
                if (systemProperties) {
                    sslSocketFactoryCopy = new SSLConnectionSocketFactory(
                            (SSLSocketFactory) SSLSocketFactory.getDefault(),
                            supportedProtocols, supportedCipherSuites, hostnameVerifierCopy);
                } else {
                    sslSocketFactoryCopy = new SSLConnectionSocketFactory(
                            SSLContexts.createDefault(),
                            hostnameVerifierCopy);
                }
            }
        }
        
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
	        .register("http", PlainConnectionSocketFactory.getSocketFactory())
	        .register("https", sslSocketFactoryCopy)
	        .build();
        
        final PoolingHttpClientConnectionManager poolingmgr = this.instance(registry, connFactory, schemePortResolver, dnsResolver, connTimeToLive, connTimeToLiveTimeUnit);
        
        if (defaultSocketConfig != null) {
            poolingmgr.setDefaultSocketConfig(defaultSocketConfig);
        }
        if (defaultConnectionConfig != null) {
            poolingmgr.setDefaultConnectionConfig(defaultConnectionConfig);
        }
        if (systemProperties) {
            String s = System.getProperty("http.keepAlive", "true");
            if ("true".equalsIgnoreCase(s)) {
                s = System.getProperty("http.maxConnections", "5");
                final int max = Integer.parseInt(s);
                poolingmgr.setDefaultMaxPerRoute(max);
                poolingmgr.setMaxTotal(2 * max);
            }
        }
        if (maxConnTotal > 0) {
            poolingmgr.setMaxTotal(maxConnTotal);
        }
        if (maxConnPerRoute > 0) {
            poolingmgr.setDefaultMaxPerRoute(maxConnPerRoute);
        }
        
        return poolingmgr;
    }
    
    
}
