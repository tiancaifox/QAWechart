package com.nutcracker.wedo.common.framework.beans.context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by huh on 2017/3/31.
 */
public final class AsyncCloseHelper implements DisposableBean {
    private static final Logger LOGGER = Logger.getLogger(AsyncCloseHelper.class);
    private static final BlockingQueue<AsyncCloseAction> QUEUE = new LinkedBlockingQueue();
    private static final Map<AsyncCloseAction, Integer> ASYNC_CLOSE_RETRY_TIMES = new HashMap();
    private static final int MAX_CANCLOSE_RETRY_TIMES = 5;
    private static final ExecutorService ASYNC_CLOSE_SERVICE = Executors.newSingleThreadExecutor();
    private static final long DEFAULT_INTERVAL = 200L;

    private static boolean addAndTestMaxRetryTimes(AsyncCloseAction task) {
        Integer times = (Integer)ASYNC_CLOSE_RETRY_TIMES.get(task);
        if(times == null) {
            ASYNC_CLOSE_RETRY_TIMES.put(task, Integer.valueOf(1));
            return false;
        } else if(times.intValue() > 5) {
            ASYNC_CLOSE_RETRY_TIMES.remove(task);
            return true;
        } else {
            ASYNC_CLOSE_RETRY_TIMES.put(task, Integer.valueOf(times.intValue() + 1));
            return false;
        }
    }

    public static void close() {
        ASYNC_CLOSE_SERVICE.shutdown();
    }

    public static void addTask(AsyncCloseAction action) {
        if(action != null) {
            QUEUE.add(action);
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Add a new task. " + action + " current size:" + getTaskSize());
            }
        }

    }

    public static int getTaskSize() {
        return QUEUE.size();
    }

    private AsyncCloseHelper() {
    }

    public void destroy() throws Exception {
        close();
    }

    static {
        Runnable asyncCloseTask = new Runnable() {
            public void run() {
                AsyncCloseAction task;
                while((task = (AsyncCloseAction)AsyncCloseHelper.QUEUE.poll()) != null) {
                    boolean canClose = false;

                    try {
                        canClose = task.canAsyncClose();
                    } catch (Exception var8) {
                        boolean maxRetry = AsyncCloseHelper.addAndTestMaxRetryTimes(task);
                        if(maxRetry) {
                            canClose = true;
                            if(AsyncCloseHelper.LOGGER.isInfoEnabled()) {
                                AsyncCloseHelper.LOGGER.info("task " + task + " \'canAsyncClose\' method throws great" + " than " + 5 + " time(s) exceptions will be to execute \'asyncClose\' method forcely.");
                            }
                        }

                        AsyncCloseHelper.LOGGER.error(var8.getMessage(), var8);
                    }

                    if(canClose) {
                        try {
                            task.asyncClose();
                            if(AsyncCloseHelper.LOGGER.isInfoEnabled()) {
                                AsyncCloseHelper.LOGGER.info("Asynchronuous closed task:" + task);
                            }
                        } catch (Exception var7) {
                            AsyncCloseHelper.LOGGER.error(var7.getMessage(), var7);
                        }
                    } else {
                        try {
                            AsyncCloseHelper.QUEUE.put(task);
                        } catch (InterruptedException var6) {
                            AsyncCloseHelper.LOGGER.error(var6.getMessage(), var6);
                        }
                    }

                    try {
                        Thread.sleep(200L);
                    } catch (Exception var5) {
                        AsyncCloseHelper.LOGGER.error(var5.getMessage(), var5);
                    }
                }

            }
        };
        ASYNC_CLOSE_SERVICE.execute(asyncCloseTask);
    }
}

