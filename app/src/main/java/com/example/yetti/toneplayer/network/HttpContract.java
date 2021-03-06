package com.example.yetti.toneplayer.network;

public final class HttpContract {

    private static final String REST_API_URL = "http://192.168.56.1:8080/api";
    private static final String RECOMMENDATIONS_PATH = "/getRecommendations";
    private static final String GET_NEXT_SONG_PATH = "/getNextSong";
    private static final String POST_TEST_PATH = "/testPOST";
    private static final String GET_TEST_PATH = "/testGET";
    private static final String GET_ARTIST_ART_PATH="/getArtist/";
    public static final String GET_ARTIST =REST_API_URL+GET_ARTIST_ART_PATH;
    public static final String GET_NEXT_SONG = REST_API_URL + GET_NEXT_SONG_PATH;
    public static final String GET_RECOMMENDATIONS_LIST = REST_API_URL + RECOMMENDATIONS_PATH;
    public static final String POST_TEST = REST_API_URL + POST_TEST_PATH;
    public static final String GET_TEST = REST_API_URL + GET_TEST_PATH;
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
    public static final String HTTP="http";
    public static final String HTTPS="https";
    public static final String UTF_8="UTF-8";
}
