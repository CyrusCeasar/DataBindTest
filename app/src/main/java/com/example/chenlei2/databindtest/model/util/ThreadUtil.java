package com.example.chenlei2.databindtest.model.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;

import com.apkfuns.logutils.LogUtils;


/**
 * 线程池辅助类 
 * @author  zhenghonglian
 * @version  [版本号, 2014-8-21]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class ThreadUtil
{
 
    private ExecutorService executorService;
    
    private Handler mUIHandler;
    
    private static ThreadUtil inst = new ThreadUtil();
    
    /** 
     * @Fields POOL_SIZE : 单个CPU线程池大小
     */
    private static final int POOL_SIZE = 5;


    private int supportMaxPoolSize;//最大支持线程大小，Cache模式下为无限制 -1
    
    public int getSupportMaxPoolSize()
    {
        return supportMaxPoolSize;
    }
    
    public static ThreadUtil getInstance()
    {
        return inst;
    }
    
    /**
     * 初始化
     * 必须在UI线程调用
     * @return void [返回类型说明]
     */
    public void init()
    {
        if (mUIHandler == null)
        {
            mUIHandler = new Handler();
        }
    }
    
    private ThreadUtil()
    {
        //获取当前系统的CPU 数目 
        int cpuNums = Runtime.getRuntime().availableProcessors();
        
        LogUtils.i("ThreadUtil() - cpuNums: " + cpuNums);
        
        int supportMaxPoolSize = -1;//最大支持线程大小，Cache模式下为无限制 -1
        
        if (cpuNums >= 3)
        {
            executorService = Executors.newCachedThreadPool();
            supportMaxPoolSize = -1;
            LogUtils.i("ThreadUtil() - pool.size is default.");
        }
        else
        {
            supportMaxPoolSize = cpuNums * POOL_SIZE;
            
            //ExecutorService通常根据系统资源情况灵活定义线程池大小 
            executorService = Executors.newFixedThreadPool(supportMaxPoolSize);
        }
        
        if (executorService instanceof ThreadPoolExecutor)
        {
            ThreadPoolExecutor pool = (ThreadPoolExecutor) executorService;
            pool.setKeepAliveTime(60, TimeUnit.SECONDS);//线程保留60秒
            
            final ThreadFactory factory = pool.getThreadFactory();
            pool.setThreadFactory(new ThreadFactory()
            {
                @Override
                public Thread newThread(Runnable r)
                {
                    Thread th = factory.newThread(r);
                    
                    if (th.getName().startsWith("pool-"))
                    {
                        LogUtils.d("Runnable: " + r.getClass().getSimpleName());
                        th.setName("ThreadUtil-" + lastName);
                    }
                    return th;
                }
            });
        }
        LogUtils.i("ThreadUtil() - pool.size: " + supportMaxPoolSize);
    }
    
    /**
     * 释放资源
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void destroy()
    {
        executorService.shutdown();
    }
    
    private String lastName = null;
    
    /**
     * 后台执行任务
     * @param task [参数说明] 任务对象
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public synchronized void execute(Runnable task)
    {
        lastName = task.getClass().getSimpleName();
        executorService.execute(task);
    }


    public void runOnUiThread(Runnable action)
    {
        mUIHandler.post(action);
    }
}
