package io.codemojo.sdk.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by shoaib on 13/08/16.
 */
public class NotificationHandler {

    private Bundle bundleData;
    private Intent intentData;
    private Context context;

    public NotificationHandler(Context context, Intent data) {
        this.context = context;
        intentData = data;
    }

    public NotificationHandler(Context context, Bundle data) {
        this.context = context;
        bundleData = data;
    }

    private Bitmap getRemoteImage(final URL aURL) {
        try {
            final URLConnection conn = aURL.openConnection();
            conn.connect();
            final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            final Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            return bm;
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    private NotificationCompat.Builder getBaseNofitication(){
        return new NotificationCompat.Builder(context)
                .setSmallIcon(context.getApplicationInfo().icon)
                .setAutoCancel(true);
    }

    private PendingIntent preparePendingIntent(String className){
        return preparePendingIntent(getIntentFromClassName(className));
    }

    private PendingIntent preparePendingIntent(Intent resultIntent){
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        if(resultIntent == null) {
            resultIntent = new Intent(context, context.getClass());
        } else if(resultIntent.getComponent() != null) {
            stackBuilder.addParentStack(resultIntent.getComponent());
        }

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        return resultPendingIntent;
    }

    public Intent getIntentFromClassName(String className){
        Intent resultIntent = null;
        try {
            resultIntent = new Intent(context, Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultIntent;
    }

    public void plainNotification(String title, String message, Intent target) {
        NotificationCompat.Builder mBuilder = getBaseNofitication();
        mBuilder.setContentTitle(title).setContentText(message);

        PendingIntent resultPendingIntent = preparePendingIntent(target);

        mBuilder.setContentIntent(resultPendingIntent);
        android.app.NotificationManager mNotificationManager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }

    public void richNotification(String title, String message, String imageURL){
        richNotification(title, message, imageURL, context.getPackageName());
    }

    public void richNotification(String title, String message, String imageURL, String className){
        richNotification(title, message, imageURL, getIntentFromClassName(className));
    }
    public void richNotification(String title, String message, String imageURL, Intent target){
        Bitmap thumb_d = null;
        try {
            URL thumb_u = new URL(imageURL);
            thumb_d = getRemoteImage(thumb_u);
        }
        catch (Exception e) {
            // handle it
        }

        NotificationCompat.Builder mBuilder = getBaseNofitication();
        mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(thumb_d))
                .setContentTitle(title).setContentText(message);

        PendingIntent resultPendingIntent = preparePendingIntent(target);

        mBuilder.setContentIntent(resultPendingIntent);
        android.app.NotificationManager mNotificationManager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }


}
