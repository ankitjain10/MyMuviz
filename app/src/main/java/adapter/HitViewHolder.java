package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dell.mymuviz.R;

/**
 * Created by dell on 9/26/2015.
 */
public class HitViewHolder extends RecyclerView.ViewHolder {
    Context mContext;
    ImageView movieThumbnail;
    TextView movieTitle;
    TextView movieReleaseDate;
    RatingBar movieAudienceScore;

    String TAG = HitViewHolder.class.getSimpleName();


    public HitViewHolder(View itemView, Context mContext) {
        super(itemView);
        this.mContext = mContext;
        movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
        movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
        movieReleaseDate = (TextView) itemView.findViewById(R.id.movieReleaseDate);
        movieAudienceScore = (RatingBar) itemView.findViewById(R.id.movieAudienceScore);
    }


}
