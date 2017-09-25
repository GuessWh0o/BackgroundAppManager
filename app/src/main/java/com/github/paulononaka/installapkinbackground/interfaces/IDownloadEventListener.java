package com.github.paulononaka.installapkinbackground.interfaces;

/**
 * Created by Maks on 8/17/2017.
 */

public interface IDownloadEventListener {
    void onDownloadStatusChanged(String message, String status, String description, boolean updateWebRemote);

    void onDownloadStatusChanged(String message);

    void onSyncStatusChanged(String status);

    void displayProfileName(String profileName);

    void startPlaybackDuringSyncWebRemote();

    void scheduleDailyProfilePlaylistAndDbMaintenanceTimer();

    void onMemoryExceeded();

    void stopPlaybackDuringSync();

}
