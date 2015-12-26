# https-zuul-proxy-spring-boot-app
This is an exaple application to build https proxy-application for another https web applications.

Please add your own keystore.jks
    •	Generate a Java keystore and key pair
         keytool -genkey -alias mydomain -keyalg RSA -keystore keystore.jks -keysize 2048
    
    •	Generate a certificate signing request (CSR) for an existing Java keystore
       keytool -certreq -alias mydomain -keystore keystore.jks -file mydomain.csr

Import thirdparty public certificate if your application is going make any https call.

     • Import a root or intermediate CA certificate to an existing Java keystore
         keytool -import -trustcacerts -alias root -file Thawte.crt -keystore keystore.jks
