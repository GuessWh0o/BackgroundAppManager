package com.github.paulononaka.installapkinbackground.interfaces;

/**
 * Created by Maks on 8/17/2017.
 */

// The S3 callbacks can fail to fire, at times the file is downloaded based on the progress
// callback, but the
//

public interface IS3StallListener {
    void onCallbackStalled(int downloadId);
}
