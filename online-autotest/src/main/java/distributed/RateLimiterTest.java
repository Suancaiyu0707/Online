package distributed;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;

import java.util.List;
import java.util.concurrent.*;

/***
 * guava 实现令牌桶
 */
public class RateLimiterTest {
    //2.0代表一秒最多多少个
    public static RateLimiter limiter = RateLimiter.create(1.0);

    public static ExecutorService executorService =
                    new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                        Runtime.getRuntime().availableProcessors(),
                            0L,TimeUnit.MICROSECONDS,
                            new ArrayBlockingQueue<Runnable>(100));
    public static List<Runnable> t= Lists.newArrayList();

    public static void main(String[] args) {
        for(int i=0;i<100;i++){
            t.add(new MyRunable("runnable_"+i));
        }

        submitTask(t,executorService);
    }
    public static void submitTask(List<Runnable> tasks, Executor executor){
        for(Runnable task:tasks){
//            if(limiter.tryAcquire()){
//                System.out.println("获得令牌桶:");
//                executor.execute(task);
//            }else{
//                System.out.println("没有获得令牌桶:");
//            }
            limiter.acquire();
            executor.execute(task);

        }

    }
}

class MyRunable implements Runnable{
    private String name;
    public MyRunable(String name){
        this.name=name;
    }
    @Override
    public void run() {
        System.out.println(System.currentTimeMillis()+"==========线程名称:"+name+"=======");

    }
}
