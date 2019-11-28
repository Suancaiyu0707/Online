package distributed;

import com.google.common.util.concurrent.RateLimiter;

/**
 * Created with IntelliJ IDEA.
 * User: zhifang.xu
 * Date: 2019/11/2
 * Time: 9:59 AM
 * Description: No Description
 */
public class RateLimiterDemo {
    public static void main(String[] args) throws InterruptedException {

        RateLimiter limiter = RateLimiter.create(10);

        Thread.sleep(5000);
        System.out.println(System.currentTimeMillis());
        limiter.acquire(30);
        limiter.acquire();
        System.out.println(System.currentTimeMillis());
    }
}
