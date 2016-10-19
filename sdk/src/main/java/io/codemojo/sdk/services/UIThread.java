package io.codemojo.sdk.services;

import android.app.Activity;

/**
 * Created by shoaib on 30/06/16.
 */
public abstract class UIThread {

    private Activity context;

    public void setContext(Activity context) {
        this.context = context;
    }

    public boolean moveTo(Runnable runnable){
        if(context != null && context instanceof Activity) {
            context.runOnUiThread(runnable);
            return true;
        }else{
            runnable.run();
            return false;
        }
    }

    public Activity getContext() {
        return context;
    }
}
