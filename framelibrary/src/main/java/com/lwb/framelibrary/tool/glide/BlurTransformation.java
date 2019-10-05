package com.lwb.framelibrary.tool.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.ByteBuffer;
import java.security.MessageDigest;


public class BlurTransformation extends BitmapTransformation {

    private static final String ID = "com.totcy.atom.transformations.BlurTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private Context mContext;
    private int mRadius;
    private int mSampling;

    /**
     * Creates a blur transformation with the default blur radius of 20
     *
     * @param context Current context
     */
    public BlurTransformation(Context context) {
        this(context, 20);
    }

    /**
     * Creates a blur transformation
     *
     * @param context Current context
     * @param radius  Set the radius of the Blur. Supported range 0 < radius <= 25
     */
    public BlurTransformation(Context context, int radius) {
        this(context, radius, 1);
    }

    /**
     * Creates a blur transformation
     *
     * @param context  Current context
     * @param radius   Set the radius of the Blur. Supported range 0 < radius <= 25
     * @param sampling Sampling ratio, default ratio is 1
     */
    public BlurTransformation(Context context, int radius, int sampling) {
        mContext = context;
        mRadius = radius;
        mSampling = sampling;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return GlideTransformationUtils.blur(pool, toTransform, mContext, mRadius, mSampling);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlurTransformation that = (BlurTransformation) o;
        return mRadius == that.mRadius && mSampling == that.mSampling;
    }

    @Override
    public int hashCode() {
        int result = mRadius;
        result = 31 * result + mSampling;
        return ID.hashCode() + result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        int result = mRadius;
        result = 31 * result + mSampling;
        byte[] radiusData = ByteBuffer.allocate(4).putInt(result).array();
        messageDigest.update(radiusData);
    }

}
