package com.my.televip.dex;

import android.content.Context;

import com.my.televip.ClientChecker;
import com.my.televip.logging.Logger;
import com.my.televip.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import dalvik.system.InMemoryDexClassLoader;

public class DexInjector {

    public static ClassLoader classLoader;

    public static void injectDex(Context context, ClassLoader parentClassLoader) {
        if (ClientChecker.check(ClientChecker.ClientType.Nekogram) || ClientChecker.check(ClientChecker.ClientType.Cherrygram)) return;
        try {
            byte[] dexBytes = loadDexBytes(context);
            if (dexBytes == null || dexBytes.length == 0) {
                Logger.w("Dex bytes empty, skipping dex injection");
                return;
            }

            ByteBuffer buffer = ByteBuffer.wrap(dexBytes);

            DexInjector.classLoader = new InMemoryDexClassLoader(
                    buffer,
                    parentClassLoader
            );
        } catch (Throwable e) {
            Logger.e(e);
        }
    }

    private static byte[] loadDexBytes(Context context) {
        if (context != null) {
            try (InputStream is = context.getAssets().open("settings_adapter.dex");
                 ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                return os.toByteArray();
            } catch (Throwable ignored) {
            }
        }

        if (Utils.modulePath != null) {
            try (ZipFile zipFile = new ZipFile(Utils.modulePath)) {
                ZipEntry entry = zipFile.getEntry("assets/settings_adapter.dex");
                if (entry != null) {
                    try (InputStream is = zipFile.getInputStream(entry);
                         ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        return os.toByteArray();
                    }
                }
            } catch (Throwable e) {
                Logger.e(e);
            }
        }
        return null;
    }

}
