package com.ncm.btl_android.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ncm.btl_android.R;

public class FavoriteFragment extends Fragment {
    private View mView;

    MediaPlayer player;
    Button stop, play;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_favorite,container, false);






        player = MediaPlayer.create(getActivity(), R.raw.bai1);
        stop = mView.findViewById(R.id.stop);
        play = mView.findViewById(R.id.play);

        //player.start();


        stop.setOnClickListener(v -> {
            player.stop();
        });

        play.setOnClickListener(v -> {
            player.start();
        });








        return mView;
    }
}
