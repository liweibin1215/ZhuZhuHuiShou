/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lwb.framelibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static com.bumptech.glide.util.Preconditions.checkNotNull;


/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtil {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }
    public static void jump(Context old, Class<?> cls) {
        jump(old, cls, null);
    }
    /**
     * 窗体跳转
     *
     * @param old
     * @param cls
     */
    public static void jump(Context old, Class<?> cls, Bundle mBundle) {
        jump(old, cls, mBundle, false);
    }

    /**
     * 窗体跳转
     *
     * @param old
     * @param cls
     */
    public static void jump(Context old, Class<?> cls, Bundle mBundle,
                            boolean clearTop) {
        Intent intent = new Intent(old, cls);

        if (mBundle != null) {
            intent.putExtras(mBundle);
        }

        if (clearTop) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        old.startActivity(intent);
    }

    /**
     * 窗体跳转
     *
     * @param old
     * @param cls
     */
    public static void jumpForResult(Context old, Class<?> cls,
                                     int requestCode, Bundle mBundle) {
        Intent intent = new Intent();
        intent.setClass(old, cls);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        Activity activity = (Activity) old;
        activity.startActivityForResult(intent, requestCode);

    }
}
