package com.microservices.security.common.ssl;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.spi.Metadata;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.camel.util.jsse.TrustManagersParameters;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// see: https://pgaemers.wordpress.com/2017/02/17/creating-an-insecure-http4-component-in-apache-camel/

@SuppressWarnings("WeakerAccess")
@Metadata(
        label = "verifiers",
        enums = "parameters,connectivity"
)
public class InsecureSSLComponent extends HttpComponent {
    private Logger log = LoggerFactory.getLogger(InsecureSSLComponent.class);

    @Override
    public void setSslContextParameters(SSLContextParameters sslContextParameters) {
        // super.setSslContextParameters(sslContextParameters);
        TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
        X509ExtendedTrustManager extendedTrustManager = new InsecureX509TrustManager();
        trustManagersParameters.setTrustManager(extendedTrustManager);

        sslContextParameters.setTrustManagers(trustManagersParameters);
        super.getSslContextParameters().setTrustManagers(trustManagersParameters);
    }

    @Override
    public void setX509HostnameVerifier(HostnameVerifier x509HostnameVerifier) {
        // super.setX509HostnameVerifier(x509HostnameVerifier);
        super.setX509HostnameVerifier(AllowAllHostnameVerifier.INSTANCE);
    }

    private static class InsecureX509TrustManager extends X509ExtendedTrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException { }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException { }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException { }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException { }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
