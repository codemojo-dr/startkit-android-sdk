package io.codemojo.sdk.models;

/**
 * Created by shoaib on 25/06/16.
 */
public class WalletSlot {
    private float raw;
    private float conversion;
    private float c;

    public float getRawPoints() {
        return raw;
    }

    public float getConvertedPoints() {
        return conversion;
    }

    public float getConversionRatio() {
        return c;
    }
}
