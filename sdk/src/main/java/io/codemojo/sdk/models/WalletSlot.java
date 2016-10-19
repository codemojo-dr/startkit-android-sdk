package io.codemojo.sdk.models;

/**
 * Created by shoaib on 25/06/16.
 */
public class WalletSlot {
    private float raw;
    private float converted;
    private float c;

    public float getRawPoints() {
        return raw;
    }

    public float getConvertedPoints() {
        return converted;
    }

    public float getConversionRatio() {
        return c;
    }
}
