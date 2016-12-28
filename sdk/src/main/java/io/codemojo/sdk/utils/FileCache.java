package io.codemojo.sdk.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by shoaib on 26/12/16.
 */
public class FileCache {

    private File cacheDir;

    public FileCache(Context context){
        //Find the dir to save cached images
        cacheDir = context.getCacheDir();
        if(!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}