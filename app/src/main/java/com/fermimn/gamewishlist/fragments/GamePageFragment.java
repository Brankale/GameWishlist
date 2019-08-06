package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.Game;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GamePageFragment extends Fragment {

    private static final String TAG = GamePageFragment.class.getSimpleName();

    private Context mContext;
    private Game mGame;

    public GamePageFragment(Game game) {
        mGame = game;
    }

    /**
     * This method is called by Android in the lifecycle of the Fragment.
     * The context must be init here and not somewhere else to avoid crashes.
     * @param context app context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_page, container, false);

        // Get
        ImageView cover = view.findViewById(R.id.cover);
        TextView title = view.findViewById(R.id.title);
        TextView publisher = view.findViewById(R.id.publisher);
        TextView platform = view.findViewById(R.id.platform);
        TextView genres = view.findViewById(R.id.genres);
        TextView releaseDate = view.findViewById(R.id.releaseDate);



        title.setText( mGame.getTitle() );
        publisher.setText( mGame.getPublisher() );
        platform.setText( mGame.getPlatform() );



        if (mGame.hasGenres()) {
            List<String> tmpGenres = mGame.getGenres();
            for (int i = 0; i < tmpGenres.size(); ++i) {
                if (i==0) {
                    genres.setText(tmpGenres.get(i));
                } else {
                    genres.setText(genres.getText() + "/" + tmpGenres.get(i));
                }
            }
        }

        if (mGame.hasReleaseDate()) {
            releaseDate.setText( mGame.getReleaseDate() );
        }

        if (mGame.hasPlayers()) {
            view.findViewById(R.id.players_container).setVisibility(View.VISIBLE);
            TextView players = view.findViewById(R.id.players);
            players.setText( mGame.getPlayers() );
        }

        if (mGame.hasOfficialSite()) {
            view.findViewById(R.id.official_site_container).setVisibility(View.VISIBLE);
            TextView officialSite = view.findViewById(R.id.officialSite);
            officialSite.setText( mGame.getOfficialSite() );
        }

        Picasso.get().load( mGame.getCover() )
                .placeholder(null)
                .into(cover);

        return view;
    }


}
