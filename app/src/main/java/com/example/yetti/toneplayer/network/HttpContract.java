package com.example.yetti.toneplayer.network;


public final class HttpContract {
    private static String REST_API_URL = "http://192.168.100.3:8080/api";
    private static String RECOMMENDATIONS_PATH = "/getRecommendations";
    private static String GET_NEXT_SONG_PATH = "/getNextSong";
    private static String POST_TEST_PATH="/testPOST";
    private static String GET_TEST_PATH="/testGET";
    public static final String GET_NEXT_SONG = REST_API_URL + GET_NEXT_SONG_PATH;
    public static final String GET_RECOMMENDATIONS_LIST = REST_API_URL + RECOMMENDATIONS_PATH;
    public static final String POST_TEST = REST_API_URL +POST_TEST_PATH;
    public static final String GET_TEST = REST_API_URL + GET_TEST_PATH;
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
}
