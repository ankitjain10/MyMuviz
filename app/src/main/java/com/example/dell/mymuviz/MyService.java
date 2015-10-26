package com.example.dell.mymuviz;

import java.util.ArrayList;

import extras.L;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;
import pojo.Movie;
import task.TaskLoadHitMovies;
import task.TaskLoadUpcomingMovies;

/**
 * Created by dell on 10/2/2015.
 */
public class MyService extends JobService implements HitMoviesLoadedListener,UpcomingMoviesLoadedListener {

    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        L.t(this, "services started");
        this.jobParameters = jobParameters;
        new TaskLoadUpcomingMovies(this).execute();
        new TaskLoadHitMovies(this).execute();
        L.t(this, "services finished");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        L.t(this, "services stopped ");
        return false;
    }

    @Override
    public void onBoxOfficeMoviesLoaded(ArrayList<Movie> listMovies) {
        L.t(this, "onBoxOfficeMoviesLoaded finished");
        jobFinished(jobParameters, false);
    }

    @Override
    public void onUpcomingMoviesLoaded(ArrayList<Movie> listMovies) {
        L.t(this, "onUpcomingMoviesLoaded finished");
        jobFinished(jobParameters, false);

    }
}
/*

    private static class MyTask extends AsyncTask<me.tatarka.support.job.JobParameters, Void, me.tatarka.support.job.JobParameters> {
        MyService myService;
        MySingleton mySingleton;
        RequestQueue requestQueue;
        String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?apikey=54wzfswsa4qmjg8hjwa64d4c&limit=50&country=in";
        ArrayList<Movie> movieList = new ArrayList<>();


        MyTask(MyService myService) {
            this.myService = myService;
            mySingleton = MySingleton.getInstance();
            requestQueue = mySingleton.getRequestQueue();

        }

        @Override
        protected me.tatarka.support.job.JobParameters doInBackground(me.tatarka.support.job.JobParameters... params) {
            JSONObject response = sendJsonRequest();
            movieList = parseJSON(response);
            MyApplication.getWritableAdapter().insertMovies(movieList, true);
            L.m("insert success");

            return params[0];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private JSONObject sendJsonRequest() {
            JSONObject response = null;
            RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, requestFuture, requestFuture);
            requestQueue.add(request);
            try {
                response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            super.onPostExecute(jobParameters);
            myService.jobFinished(jobParameters, false);

        }
    }

    private static ArrayList<Movie> parseJSON(JSONObject jsonObject) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Movie> movieList = new ArrayList<>();
        if (jsonObject != null && jsonObject.length() > 0) {
            try {
                JSONArray arrayMovies = jsonObject.getJSONArray(KEY_MOVIES);
                for (int i = 0; i < arrayMovies.length(); i++) {
                    long id = -1;
                    String title = Constants.NA;
                    String releaseDate = Constants.NA;
                    int audienceScore = -1;
                    String synopsis = Constants.NA;
                    String urlThumbnail = Constants.NA;
                    String urlSelf = Constants.NA;
                    String urlCast = Constants.NA;
                    String urlReviews = Constants.NA;
                    String urlSimilar = Constants.NA;
                    JSONObject currentMovie = arrayMovies.getJSONObject(i);

                    if (Utils.contains(currentMovie, KEY_ID)) {
                        id = currentMovie.getLong(KEY_ID);
                    }

                    if (Utils.contains(currentMovie, KEY_TITLE)) {
                        title = currentMovie.getString(KEY_TITLE);
                    }

                    if (Utils.contains(currentMovie, KEY_RELEASE_DATES)) {
                        JSONObject objectReleaseDates = currentMovie.getJSONObject(KEY_RELEASE_DATES);

                        if (Utils.contains(objectReleaseDates, KEY_THEATER)) {
                            releaseDate = objectReleaseDates.getString(KEY_THEATER);
                        }
                    }
                    if (Utils.contains(currentMovie, KEY_RATINGS)) {
                        JSONObject objectRatings = currentMovie.getJSONObject(KEY_RATINGS);
                        if (Utils.contains(objectRatings, KEY_AUDIENCE_SCORE)) {
                            audienceScore = objectRatings.getInt(KEY_AUDIENCE_SCORE);
                        }
                    }

                    if (Utils.contains(currentMovie, KEY_SYNOPSIS)) {
                        synopsis = currentMovie.getString(KEY_SYNOPSIS);
                    }

                    if (Utils.contains(currentMovie, KEY_POSTERS)) {
                        JSONObject objectPosters = currentMovie.getJSONObject(KEY_POSTERS);

                        if (Utils.contains(objectPosters, KEY_THUMBNAIL)) {
                            urlThumbnail = objectPosters.getString(KEY_THUMBNAIL);
                        }
                    }
                    if (Utils.contains(currentMovie, KEY_LINKS)) {
                        JSONObject objectLinks = currentMovie.getJSONObject(KEY_LINKS);
                        if (Utils.contains(objectLinks, KEY_SELF)) {
                            urlSelf = objectLinks.getString(KEY_SELF);
                        }
                        if (Utils.contains(objectLinks, KEY_CAST)) {
                            urlCast = objectLinks.getString(KEY_CAST);
                        }
                        if (Utils.contains(objectLinks, KEY_REVIEWS)) {
                            urlReviews = objectLinks.getString(KEY_REVIEWS);
                        }
                        if (Utils.contains(objectLinks, KEY_SIMILAR)) {
                            urlSimilar = objectLinks.getString(KEY_SIMILAR);
                        }
                    }
                    Movie movie = new Movie();
                    movie.setId(id);
                    movie.setTitle(title);
                    Date date = null;
                    try {
                        date = dateFormat.parse(releaseDate);
                    } catch (Exception e) {
                    }
                    movie.setReleaseDateTheater(date);
                    movie.setAudienceScore(audienceScore);
                    movie.setSynopsis(synopsis);
                    movie.setUrlThumbnail(urlThumbnail);
                    movie.setUrlSelf(urlSelf);
                    movie.setUrlCast(urlCast);
                    movie.setUrlThumbnail(urlThumbnail);
                    movie.setUrlReviews(urlReviews);
                    movie.setUrlSimilar(urlSimilar);
                    if (id != -1 && !title.equals(Constants.NA)) {
                        movieList.add(movie);
                    }
                }

            } catch (JSONException e) {
                L.m(""+e.getMessage());
            }
            L.m(""+movieList.size() + " rows fetched");
        }
        return movieList;
    }

}

*/
