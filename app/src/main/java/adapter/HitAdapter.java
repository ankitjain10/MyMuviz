package adapter;

import android.content.Context;
import android.graphics.Color;
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

public class HitAdapter extends RecyclerView.Adapter<HitViewHolder> {
    String TAG = HitAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater mLayoutInflater;
    List<Movie> list = new ArrayList<>();
    MySingleton mySingleton;
    ImageLoader imageLoader;
    private String[] bgColors;
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public HitAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mySingleton = MySingleton.getInstance();
        imageLoader = mySingleton.getImageLoader();
        bgColors = context.getResources().getStringArray(R.array.movie_serial_bg);
    }

    public void setMovieList(ArrayList<Movie> list) {
        this.list = list;
        notifyItemRangeChanged(0, list.size());
        notifyDataSetChanged();
    }


    @Override
    public HitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.custom_movie_hit, parent, false);
        HitViewHolder myViewHolder = new HitViewHolder(view, mContext);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final HitViewHolder holder, final int position) {
        Movie currentMovie = list.get(position);
        String color = bgColors[position % bgColors.length];
        holder.movieTitle.setTextColor(Color.parseColor(color));
        holder.movieReleaseDate.setTextColor(Color.parseColor(color));
        holder.movieTitle.setText(currentMovie.getTitle());
        Date movieReleaseDate = currentMovie.getReleaseDateTheater();
        if (movieReleaseDate != null) {
            String formattedDate = mFormatter.format(movieReleaseDate);
            holder.movieReleaseDate.setText(formattedDate);
        } else {
            holder.movieReleaseDate.setText(Constants.NA);
        }

        int audienceScore = currentMovie.getAudienceScore();
        if (audienceScore == -1) {
            holder.movieAudienceScore.setRating(0.0F);
            holder.movieAudienceScore.setAlpha(0.5F);
        } else {
            holder.movieAudienceScore.setRating(audienceScore / 20.0F);
            holder.movieAudienceScore.setAlpha(1.0F);
        }
        imageLoader.get(currentMovie.getUrlThumbnail(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                holder.movieThumbnail.setImageBitmap(imageContainer.getBitmap());
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
