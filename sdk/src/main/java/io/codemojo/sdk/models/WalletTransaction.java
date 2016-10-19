package io.codemojo.sdk.models;

/**
 * Created by shoaib on 16/06/16.
 */
public class WalletTransaction {

    private int transaction_type;
    private String customer_id;
    private float transaction_value;
    private int on_hold;
    private float refund;
    private String meta_key;
    private String expiry;
    private String created_at;
    private String meta;
    private String tag;


    public int getTransactionType() {
        return transaction_type;
    }

    public String getCustomerId() {
        return customer_id;
    }

    public float getTransactionValue() {
        return transaction_value;
    }

    public boolean isOnHold() {
        return on_hold == 1;
    }

    public float getRefundStatus() {
        return refund;
    }

    public String getMetaKey() {
        return meta_key;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getMeta() {
        return meta;
    }

    public String getTag() {
        return tag;
    }
}
