package io.codemojo.sdk.services;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import io.codemojo.sdk.exceptions.UnknownMessageType;
import io.codemojo.sdk.utils.NotificationHandler;

/**
 * Created by shoaib on 14/08/16.
 */
public class MessagingService {

    private Context context;
    public final static int MESSAGE_TYPE_NOTIFICATION = 1;
    public final static int MESSAGE_TYPE_BIG_NOTIFICATION = 2;
    public final static int MESSAGE_TYPE_RICH_NOTIFICATION = 3;
    public final static int MESSAGE_TYPE_PING = 4;
    public final static int MESSAGE_TYPE_DEEPLINK = 5;
    public final static int MESSAGE_TYPE_CHAT = 6;

    public MessagingService(Context context) {
        this.context = context;
    }

    public boolean isCodemojoMessage(Bundle data) {
        Intent tmp = new Intent();
        tmp.putExtras(data);
        return checkMessageSignature(tmp);
    }

    public boolean isCodemojoMessage(Intent data) {
        return checkMessageSignature(data);
    }

    public boolean processMessage(Bundle message) throws UnknownMessageType {
        Intent tmp = new Intent();
        tmp.putExtras(message);
        return processMessage(tmp);
    }

    public boolean processMessage(Intent message) throws UnknownMessageType {
        NotificationHandler notificationHandler = new NotificationHandler(context, message);
        if(checkMessageSignature(message)){
            Bundle extras = message.getExtras();
            String title = extras.getString("cm_title"), content = extras.getString("cm_message"),
                    deepLink = extras.getString("cm_deeplink"), media_url = extras.getString("cm_media_url"),
                    deepLinkData = extras.getString("cm_deeplink_data");

            if(deepLink == null){
                deepLink = context.getPackageName();
            }
            Intent intent = notificationHandler.getIntentFromClassName(deepLink);
            if(intent == null){
                PackageManager pm = context.getPackageManager();
                intent = pm.getLaunchIntentForPackage(context.getPackageName());
            }
            if(deepLinkData != null) {
                try {
                    JSONObject jsonArray = new JSONObject(deepLinkData);
                    intent = addExtrasToIntent(jsonArray, intent);
                } catch (JSONException e) {
                }
            }

            switch (Integer.valueOf(extras.getString("cm_type"))){
                case MESSAGE_TYPE_NOTIFICATION:
                    notificationHandler.plainNotification(title, content, intent);
                    break;
                case MESSAGE_TYPE_RICH_NOTIFICATION:
                    notificationHandler.richNotification(title, content, media_url, intent);
                    break;
                case MESSAGE_TYPE_PING:
                    break;
                case MESSAGE_TYPE_DEEPLINK:
                    break;
                case MESSAGE_TYPE_CHAT:
                default:
                    throw new UnknownMessageType("Message type not supported, please check for an updated sdk");
            }
            return true;
        }
        return false;
    }

    private Intent addExtrasToIntent(JSONObject extras, Intent data){
        Iterator<String> itr = extras.keys();
        while(itr.hasNext()) {
            String key = itr.next();
            try {
                Object val = extras.get(key);
                if(val instanceof String) {
                    data.putExtra(key, (String) val);
                }else if(val instanceof Integer) {
                    data.putExtra(key, (Integer) val);
                }else if(val instanceof Boolean) {
                    data.putExtra(key, (Boolean) val);
                }else if(val instanceof Double) {
                    data.putExtra(key, (Double) val);
                }
            } catch (JSONException e) {
            }
        }
        return data;
    }
    private boolean checkMessageSignature(Intent data){
        return data.hasExtra("cm_message") && data.hasExtra("cm_version") && data.hasExtra("cm_type");
    }
}
