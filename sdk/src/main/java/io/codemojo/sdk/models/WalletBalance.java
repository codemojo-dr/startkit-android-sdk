package io.codemojo.sdk.models;

/**
 * Created by shoaib on 25/06/16.
 */
public class WalletBalance {

    private WalletSlot slot1;
    private WalletSlot slot2;
    private WalletSlot slot3;
    private float total;

    public float getTotal() {
        return total;
    }

    public WalletSlot getSlot1() {
        return slot1;
    }

    public WalletSlot getSlot2() {
        return slot2;
    }

    public WalletSlot getSlot3() {
        return slot3;
    }

}
