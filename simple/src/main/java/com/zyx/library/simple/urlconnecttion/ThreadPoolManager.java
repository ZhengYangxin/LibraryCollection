package com.zyx.library.simple.urlconnecttion;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author pielan
 * @date 28/5/19 上午12:40
 * @dec
 */
public class ThreadPoolManager {

    public static ThreadPoolManager instance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    // 先进先出
    private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    // 失败队列
    private DelayQueue<HttpTask> delayQueue = new DelayQueue<>();

    public void addDelayTask(HttpTask httpTask) {
        if (httpTask != null) {
            httpTask.setDelayTime(3000);
            delayQueue.add(httpTask);
        }
    }

    public void addTask(Runnable runnable) {
        if (runnable != null) {
            try {
                queue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ThreadPoolExecutor threadPoolExecutor;

    private ThreadPoolManager() {
        threadPoolExecutor = new ThreadPoolExecutor(3, 10, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        addTask(r);
                    }
                });

        threadPoolExecutor.execute(coreRunnable);
        threadPoolExecutor.execute(delayRunnable);
    }


    // 创建核心线程，不停的从队列中取出请求，并交给线程池去执行
    public Runnable coreRunnable = new Runnable() {
        Runnable runnable = null;
        @Override
        public void run() {
            while (true) {
                try {
                    runnable = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                threadPoolExecutor.execute(runnable);
            }
        }
    };


    // 创建延迟线程，不停的去延迟队列中取数据，提交给线程池处理
    public Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    HttpTask task = delayQueue.take();
                    if (task.getRetryCount() < 3) {
                        threadPoolExecutor.execute(task);
                        task.setRetryCount(task.getRetryCount() + 1);
                        Log.i("ThreadPoolManager", "第" + task.getRetryCount() + "重试");

                    } else {
                        Log.i("ThreadPoolManager", "失败");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
