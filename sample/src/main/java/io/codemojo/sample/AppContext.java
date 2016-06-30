package io.codemojo.sample;

import android.content.Context;

import io.codemojo.sdk.Codemojo;

/**
 * Created by shoaib on 30/06/16.
 */
public class AppContext extends android.app.Application {

    private static Codemojo mojo;

    public static void init(Context context, String id) {
        mojo = new Codemojo(context, "99dd3c74dfbda4c8977743134804c2a04a75f26b", id, true);
    }

    public static Codemojo getCodemojoClient(){
        return mojo;
    }

}
