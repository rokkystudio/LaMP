package com.rokkystudio.lamp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rokkystudio.lamp.Player;
import com.rokkystudio.lamp.R;
import com.rokkystudio.lamp.service.LampClient;
import com.rokkystudio.lamp.views.PlayerView;

public class FragmentPlayer extends Fragment implements PlayerView.PlayerViewListener
{
    private LampClient mLampClient;
    private PlayerView mPlayerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        mPlayerView = (PlayerView) rootView.findViewById(R.id.player_view);
        mPlayerView.setPlayerViewListener(this);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLampClient = new LampClient(context);
    }

    @Override
    public void onDetach() {
        if (mLampClient != null && mLampClient.isServiceBound()) {
            mLampClient.unbindService();
        }
        mLampClient = null;
    }

    public void playPause() {
        if (mLampClient.isServiceBound()) {
            mLampClient.getService().getPlayer().playPause();
        }
    }

    public void nextTrack() {
        if (mLampClient.isServiceBound()) {
            mLampClient.getService().getPlayer().nextTrack();
        }
    }

    public void prevTrack() {
        if (mLampClient.isServiceBound()) {
            mLampClient.getService().getPlayer().prevTrack();
        }
    }

    public void setTrackPosition(float position) {
        // TODO Передавать параметр позиции трека 0-1 float
        if (mLampClient.isServiceBound()) {
            mLampClient.getService().getPlayer().setTrackPosition(position);
        }
    }

    public void excludeTrack() {
        if (mLampClient.isServiceBound()) {
            mLampClient.getService().getPlayer().excludeTrack();
        }
    }

    public void setVolume(int volume) {
        if (mLampClient.isServiceBound()) {
            mLampClient.getService().getPlayer().setVolume(volume);
        }
    }

    public void nextPlayerMode()
    {
        if (!mLampClient.isServiceBound()) return;
        Player player = mLampClient.getService().getPlayer();

        // После REPEAT_ONE включаем SHUFFLE_ALL
        if (player.getRepeatMode() == Player.RepeatMode.REPEAT_ONE) {
            player.setRepeatMode(Player.RepeatMode.REPEAT_ALL);
            player.setShuffleMode(Player.ShuffleMode.SHUFFLE_ALL);
            mPlayerView.setPlayerMode(PlayerView.PlayerMode.SHUFFLE_ALL);
        }
        // После SHUFFLE_ALL включаем REPEAT_ALL
        else if (player.getShuffleMode() == Player.ShuffleMode.SHUFFLE_ALL) {
            player.setRepeatMode(Player.RepeatMode.REPEAT_ALL);
            player.setShuffleMode(Player.ShuffleMode.SHUFFLE_NONE);
            mPlayerView.setPlayerMode(PlayerView.PlayerMode.REPEAT_ALL);
        }
        // После REPEAT_ALL включаем REPEAT_ONE
        else {
            player.setRepeatMode(Player.RepeatMode.REPEAT_ONE);
            player.setShuffleMode(Player.ShuffleMode.SHUFFLE_NONE);
            mPlayerView.setPlayerMode(PlayerView.PlayerMode.REPEAT_ONE);
        }
    }

    public void showSettings() {
        // TODO callback to Activity and Show Settings Fragment
    }

    @Override
    public void onModeButtonClick() {
        nextPlayerMode();
    }

    @Override
    public void onSettingsButtonClick() {
        getActivity().getFragmentManager()
            .beginTransaction()
            .replace(R.id.main_frame, new FragmentSettings())
            .addToBackStack(null)
            .commit();
    }
}
