package service;

public class URLBuilder {

    private static final String LOCAL = "://localhost:";
    private static final String HTTP = "http";
    private static final String WS = "ws";

    public static String getHTTPURLFromPort(int port){
        return HTTP + LOCAL + port;
    }

    public static String getWSURLFromPort(int port){
        return WS + LOCAL + port;
    }
}
