package com.example.yetti.toneplayer.network;


public final class HttpContract {
    private static String  restUrl = "http://192.168.100.3:8080/api";
    private static String recommendationsPath= "/getRecommendations";
    private static String getNextSongPath = "/getNextSong";
    public static final String getNextSong = restUrl+getNextSongPath;
    public static final String getRecommendations = restUrl+ recommendationsPath;
    public static final String getMethod = "GET";
    public static final String postMethod = "POST";
}
