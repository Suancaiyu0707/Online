import com.online.util.ShutdownHookThread;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class Test {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Test.class.getName());
        //注册JVM钩子函数并启动服务器
        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread(logger, new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        }));
    }
}
