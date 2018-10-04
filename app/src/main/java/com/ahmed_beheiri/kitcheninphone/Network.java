package com.ahmed_beheiri.kitcheninphone;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


final public class Network {
    private static final String key_api = "5e31c2219fdb33e6abd41ec7158a1d78";

    private static final String baseuri = "http://food2fork.com/api/";
    private static final String PAGE = "page";
    private static final String API_KEY = "key";
    private static final String SEARCH = "search";
    private static final String GET = "get";
    private static final String RID = "rId";

    public static URL urlBuilder(int page) {
        Uri builturi = Uri.parse(baseuri).buildUpon()
                .appendPath(SEARCH)
                .appendQueryParameter(API_KEY, key_api)
                .appendQueryParameter(PAGE, String.valueOf(page))
                .build();
        URL url = null;
        try {
            url = new URL(builturi.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static URL getUrlBuilder(String rId) {
        Uri builturi = Uri.parse(baseuri).buildUpon()
                .appendPath(GET)
                .appendQueryParameter(API_KEY, key_api)
                .appendQueryParameter(RID, rId).build();
        URL url = null;
        try {
            url = new URL(builturi.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
