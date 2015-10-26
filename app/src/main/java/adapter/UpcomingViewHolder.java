package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dell.mymuviz.R;

public class UpcomingViewHolder extends RecyclerView.ViewHolder {
    Context mContext;
    ImageView upcomingMovieThumbnail;
    TextView upcomingMovieTitle;
    TextView upcomingMovieReleaseDate;
    RatingBar upcomingMovieAudienceScore;
    String TAG = UpcomingViewHolder.class.getSimpleName();


    public UpcomingViewHolder(View itemView, Context mContext) {
        super(itemView);
        this.mContext = mContext;
        upcomingMovieThumbnail = (ImageView) itemView.findViewById(R.id.upcomingMovieThumbnail);
        upcomingMovieTitle = (TextView) itemView.findViewById(R.id.upcomingMovieTitle);
        upcomingMovieReleaseDate = (TextView) itemView.findViewById(R.id.upcomingMovieReleaseDate);
        upcomingMovieAudienceScore = (RatingBar) itemView.findViewById(R.id.upcomingMovieAudienceScore);
    }

}
