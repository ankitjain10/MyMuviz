package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dell.mymuviz.R;

/**
 * Created by dell on 10/7/2015.
 */
public class SearchViewHolder extends RecyclerView.ViewHolder{
    public SearchViewHolder(View itemView) {
        super(itemView);
    }
    Context mContext;
    ImageView searchMovieThumbnail;
    TextView searchMovieTitle;
    TextView searchMovieReleaseDate;
    RatingBar searchMovieAudienceScore;

    String TAG = HitViewHolder.class.getSimpleName();


    public SearchViewHolder(View itemView, Context mContext) {
        super(itemView);
        this.mContext = mContext;
        searchMovieThumbnail = (ImageView) itemView.findViewById(R.id.searchMovieThumbnail);
        searchMovieTitle = (TextView) itemView.findViewById(R.id.searchMovieTitle);
        searchMovieReleaseDate = (TextView) itemView.findViewById(R.id.searchMovieReleaseDate);
        searchMovieAudienceScore = (RatingBar) itemView.findViewById(R.id.searchMovieAudienceScore);
    }


}

