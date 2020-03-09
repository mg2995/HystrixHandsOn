import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import java.util.Random;

public class SyncHystrixCommand extends HystrixCommand<String> {

    private final String name;
    private final FallbackCommand fallbackCommand;

     SyncHystrixCommand(String name){
        super(HystrixCommandGroupKey.Factory.asKey("Some Command Group"));
        this.name = name;
        this.fallbackCommand = new FallbackCommand(name);


    }
    @Override
    public String  run() throws Exception{
        Random random = new Random();
        int i =random.nextInt()%500;
        Thread.sleep(i);

        String s =  new String("run is called with -  " + name);

        return s;
    }

    @Override
    public String getFallback(){

        if(isCircuitBreakerOpen()){
            System.out.println("circuit is open now");
        }
        //System.out.println("fallback call no happened");
        return fallbackCommand.execute();
    }

//    @Override
//    public String getCacheKey(){
//        return String.valueOf(name);
//    }
}
