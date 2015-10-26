package json;

import com.example.dell.mymuviz.MyApplication;

import static extras.UrlEndpoints.URL_BOX_OFFICE;
import static extras.UrlEndpoints.URL_CHAR_AMEPERSAND;
import static extras.UrlEndpoints.URL_CHAR_QUESTION;
import static extras.UrlEndpoints.URL_CHAR_SEARCH;
import static extras.UrlEndpoints.URL_PARAM_API_KEY;
import static extras.UrlEndpoints.URL_PARAM_LIMIT;
import static extras.UrlEndpoints.URL_SEARCH;
import static extras.UrlEndpoints.URL_UPCOMING;

/**
 * Created by Windows on 02-03-2015.
 */
public class Endpoints {
    public static String getRequestUrlBoxOfficeMovies(int limit) {

        return URL_BOX_OFFICE
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;
    }

    public static String getRequestUrlUpcomingMovies(int limit) {

        return URL_UPCOMING
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;
    }
    public static String getRequestUrlSearchMovies(String searchText,int limit) {

        return URL_SEARCH
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                +URL_CHAR_SEARCH
                +searchText
                + URL_PARAM_LIMIT + limit;
    }

}
