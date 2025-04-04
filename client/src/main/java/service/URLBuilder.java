package service;

public class URLBuilder {

    private static final String local = "://localhost:";
    private static final String http = "http";
    private static final String ws = "ws";

    public static String getHTTPURLFromPort(int port){
        return http + local + port;
    }

    public static String getWSURLFromPort(int port){
        return ws + local + port;
    }
}
