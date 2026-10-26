package pojlib.util;

import android.content.Context;
import androidx.annotation.Keep;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.WeakReference;

/** Singleton class made to log on one file
 * The singleton part can be removed but will require more implementation from the end-dev
 */
@Keep
public class Logger {

    private static Logger theLogger;


    /* Instance variables */
    private final File mLogFile;
    private final PrintStream mLogStream;
    private WeakReference<eventLogListener> mLogListenerWeakReference = null;


    private Logger(File logFile) throws IOException {
        // Make a new instance of the log file
        if (logFile.exists()) logFile.delete();
        logFile.createNewFile();
        mLogStream = new PrintStream(logFile.getAbsolutePath());
        mLogFile = logFile;

    }


    public static Logger getInstance(){
        Logger loge = theLogger;
        if (loge == null) throw new RuntimeException("Logger not initalized");
        return loge;
    }

    public static synchronized void begin(Context context) throws IOException {
        Logger current = theLogger;
        if (current != null) current.shutdown();
        theLogger = new Logger(new File(context.getExternalFilesDir(null), "latestlog.txt"));
    }


    /** Print the text to the log file if not censored */
    public void appendToLog(String text){
        if(shouldCensorLog(text)) return;
        appendToLogUnchecked(text);
    }

    /** Print the text to the log file, no china censoring there */
    public void appendToLogUnchecked(String text){
        mLogStream.println(text);
        notifyLogListener(text);
    }

    /** Disables the printing */
    public void shutdown(){
        mLogStream.close();
    }

    /**
     * Perform various checks to see if the log is safe to print
     * Subclasses may want to override this behavior
     * @param text The text to check
     * @return Whether the log should be censored
     */
    private static boolean shouldCensorLog(String text){
        return text.contains("Session ID is");
    }

    /** Small listener for anything listening to the log */
    public interface eventLogListener {
        void onEventLogged(String text);
    }

    /** Link a log listener to the logger */
    public void setLogListener(eventLogListener logListener){
        this.mLogListenerWeakReference = new WeakReference<>(logListener);
    }

    /** Notifies the event listener, if it exists */
    private void notifyLogListener(String text){
        if(mLogListenerWeakReference == null) return;
        eventLogListener logListener = mLogListenerWeakReference.get();
        if(logListener == null){
            mLogListenerWeakReference = null;
            return;
        }
        logListener.onEventLogged(text);
    }
}
