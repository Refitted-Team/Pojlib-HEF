package pojlib;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import org.lwjgl.glfw.CallbackBridge;
import pojlib.android.BuildConfig;
import pojlib.util.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pojlib.util.JREUtils.initJavaRuntime;
import static pojlib.util.JREUtils.setJavaEnvironment;

public class API {

    /// this is referenced from native code in vloader.cpp.
    /// this variable is used to check if the vr mod is ready to render.
    /// the launcher should de-initialize as soon as reasonably possibly when this is set.
    /// the vr mod waits 5 seconds after declaring its ready before setting itself as the openxr session.
    public static boolean gameReady = false;


    // Step 1. check the prelaunch stuff
    @SuppressWarnings("unused")
    public static boolean prelaunch(Activity activity, String gameDir, String vrModel, String jvmHome) throws Throwable {
        gameReady = false; // todo: do betterer
        JREUtils.patchJre(activity, jvmHome);
        JREUtils.relocateLibPath(activity, jvmHome);
        setJavaEnvironment(activity, gameDir, vrModel, jvmHome);
        return initJavaRuntime(jvmHome);
    }


    /**
     * Step 2. Launch the game
     *
     * @param activity The activity of Android
     * @param username The user's username
     * @param versionName The Minecraft version name
     * @param gameDir The game directory, kind of like ".minecraft" for this
     * @param assetsDir The equivelant of .minecraft/assets
     * @param assetIndex This is the asset index Minecraft lists from the API
     * @param formattedUuid The user's UUID
     * @param accessToken The Minecraft login token
     * @param userType Should be "msa" always, "mojang" is legacy
     * @param memoryValue The amount of ram to allocate to Minecraft
     * @param vrModel The headset's model
     */
    @SuppressWarnings("unused")
    public static void startGame(
            Activity activity,
            String username,
            String versionName,
            String gameDir,
            String assetsDir,
            String assetIndex,
            String formattedUuid,
            String accessToken,
            String userType,
            String versionType,
            String memoryValue,
            String vrModel,
            String mainClass,
            String[] additionalArgs,
            String[] mcAdditionalArgs,
            String jvmHome
    ) {
        String[] mcArgs = {"--username", username, "--version", versionName, "--gameDir", gameDir,
                "--assetsDir", assetsDir, "--assetIndex", assetIndex, "--uuid", formattedUuid,
                "--accessToken", accessToken, "--userType", userType, "--versionType", versionType};

        List<String> allArgs = new ArrayList<>(Arrays.asList(additionalArgs));
        JREUtils.redirectAndPrintJRELog();
        VLoader.setAndroidInitInfo(activity);
        try {
            JREUtils.launchJavaVM(activity, allArgs, mcArgs, mcAdditionalArgs, gameDir, memoryValue, vrModel, mainClass, jvmHome);
        } catch (Throwable t) {
            throw new RuntimeException("JVM has stopped.", t);
        }
    }

    @SuppressWarnings("unused")
    public static String getPojlibInfo() {
        return  "Pojlib " + BuildConfig.BUILD_TYPE + " " + BuildConfig.BUILD_TIME;
    }

    /**
     * Kill the current instance
     *
     * @param activity Android activity object
     */
    @SuppressWarnings("unused")
    public static void restartLauncher(Activity activity) {
        Logger.getInstance().appendToLog("QuestCraft: Restarting launcher...");
        CallbackBridge.restartUnitySession(activity);
    }

    /* temporary */
    //private static boolean hasWifi;
    /**
     * Check if the device has a valid wifi connection
     *
     * @param activity activity object
     * @return true if the device has a valid wifi connection
     */
    public static boolean hasConnection(Context activity) {
        boolean hasNetwork = false;
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = connManager.getNetworkCapabilities(connManager.getActiveNetwork());

        if(capabilities != null) {
            hasNetwork = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        }

        if (hasNetwork) {
            try {
                URL url = new URL("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.connect();
                conn.disconnect();
                //hasWifi = true;
                return true;
            } catch (Exception e) {
                Logger.getInstance().appendToLog("WARN! Unable to reach Microsoft servers!");
                //hasWifi = false;
                return false;
            }
        } else {
            Logger.getInstance().appendToLog("WARN! Device has no wifi connection!");
            //hasWifi = false;
            return false;
        }
    }
}
