package com.test.jangleproducer;


import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
public class AppExecutors {

    private final Executor diskIO;

    private final Executor networkIO;

    private final Executor mainThread;

    private final Executor mainThreadDelayed;

    private static long timeDelayed = 0;

    public AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread, Executor mainThreadDelayed) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
        this.mainThreadDelayed = mainThreadDelayed;
    }


    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3), new MainThreadExecutor(), new MainThreadExecutorDelayed());

    }


    public Executor diskIO() {

        return diskIO;
    }

    public Executor networkIO() {

        return networkIO;
    }


    public Executor mainThread() {

        return mainThread;
    }

    public Executor mainThreadDelayed(long timeDelayed) {

        this.timeDelayed = timeDelayed;
        return mainThreadDelayed;
    }


    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());


        @Override
        public void execute(@NonNull Runnable command) {

            mainThreadHandler.post(command);
        }
    }

    private static class MainThreadExecutorDelayed implements Executor {

        private Handler mainDelayedThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {

            mainDelayedThreadHandler.postDelayed(command, timeDelayed);
        }
    }

}

