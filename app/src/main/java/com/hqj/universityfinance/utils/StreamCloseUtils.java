package com.hqj.universityfinance.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by wang on 17-12-6.
 */

public class StreamCloseUtils {

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
