package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.dell.mymuviz.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import extras.Constants;
import pojo.Movie;
import singleton.MySingleton;

/**
 * Created by dell on 10/3/2015.
 */
public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingViewHolder> {
    String TAG=UpcomingAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater mLayoutInflater;
    List<Movie> list= new ArrayList<>();
    MySingleton mySingleton;
    ImageLoader imageLoader;
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public UpcomingAdapter(Context context){
        mContext=context;
        mLayoutInflater=LayoutInflater.from(context);
        mySingleton=MySingleton.getInstance();
        imageLoader=mySingleton.getImageLoader();
    }

    public void setMovieList(ArrayList<Movie> list){
        this.list = list;
        notifyItemRangeChanged(0,list.size());
        notifyDataSetChanged();
    }


    @Override
    public UpcomingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mLayoutInflater.inflate(R.layout.custom_movie_upcoming,parent,false);
        UpcomingViewHolder myViewHolder=new UpcomingViewHolder(view,mContext);
        //Log.v(TAG, "onCreateViewHolder " + viewType);

        return myViewHolder;
    }



    @Override
    public void onBindViewHolder(final UpcomingViewHolder holder, final int position) {
        Movie currentMovie=list.get(position);
        System.out.print("current :-"+ currentMovie);
        holder.upcomingMovieTitle.setText(currentMovie.getTitle());
        Date movieReleaseDate = currentMovie.getReleaseDateTheater();
        if (movieReleaseDate != null) {
            String formattedDate = mFormatter.format(movieReleaseDate);
            holder.upcomingMovieReleaseDate.setText(formattedDate);
        } else {
            holder.upcomingMovieReleaseDate.setText(Constants.NA);
        }

        int audienceScore = currentMovie.getAudienceScore();
        if (audienceScore == -1) {
            holder.upcomingMovieAudienceScore.setRating(0.0F);
            holder.upcomingMovieAudienceScore.setAlpha(0.5F);
        } else {
            holder.upcomingMovieAudienceScore.setRating(audienceScore / 20.0F);
            holder.upcomingMovieAudienceScore.setAlpha(1.0F);
        }
        imageLoader.get(currentMovie.getUrlThumbnail(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                holder.upcomingMovieThumbnail.setImageBitmap(imageContainer.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        //Log.v(TAG, "onBindViewHolder "+position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
