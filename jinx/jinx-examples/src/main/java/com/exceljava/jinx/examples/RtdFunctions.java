package com.exceljava.jinx.examples;

import com.exceljava.jinx.ExcelArgument;
import com.exceljava.jinx.ExcelArguments;
import com.exceljava.jinx.ExcelFunction;
import com.exceljava.jinx.Rtd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class RtdFunctions {
    private static final Logger log = Logger.getLogger(RtdFunctions.class.getName());
    private final ScheduledExecutorService executor;

    /**
     * CurrentTimeRtd is returned by an ExcelFunction and calls Rtd.notify
     * periodically to update the value in Excel.
     *
     * When Excel first starts requesting values from the Rtd object,
     * Rtd.onConnected is called. When the Rtd values are not longer needed,
     * for example when the cell using them is cleared, Rtd.onDisconnected
     * is called.
     */
    static class CurrentTimeRtd extends Rtd<String> implements Runnable
    {
        private final DateFormat format;
        private final ScheduledExecutorService executor;
        private ScheduledFuture<?> future;

        CurrentTimeRtd(String format, ScheduledExecutorService executor) {
            this.executor = executor;
            this.format = new SimpleDateFormat(format);

            // call Rtd.notify with the initial value
            run();
        }

        private String getCurrentTime() {
            return format.format(new Date());
        }

        public void run() {
            // notify Excel with the latest value
            try {
                notify(getCurrentTime());
            }
            catch (Exception e) {
                notifyError(e);
            }
        }

        @Override
        public void onConnected() {
            log.info("CurrentTimeRtd connected");

            // schedule 'run' to update the value in Excel periodically
            this.future = executor.scheduleAtFixedRate(this, 0, 100, TimeUnit.MILLISECONDS);
        }

        @Override
        public void onDisconnected() {
            log.info("CurrentTimeRtd disconnected");

            // cancel the scheduler
            if (null != future)
                future.cancel(true);
        }
    }

    /**
     * The threads used are created as daemon threads so that if Excel
     * is closed while there are still threads running the process will
     * exit cleanly.
     */
    static class DaemonThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    }

    public RtdFunctions() {
        this.executor = Executors.newScheduledThreadPool(4, new DaemonThreadFactory());
    }

    @ExcelFunction(
            value = "jinx.currentTime",
            description = "Return the current time",

            // this causes the RTD function to be called when the workbook calling this function is opened
            isVolatile = true
    )
    @ExcelArguments({
            @ExcelArgument("format"),
    })
    public Rtd<String> currentTime(String format) {
        return new CurrentTimeRtd(format, executor);
    }
}
