import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class FallbackCommand extends HystrixCommand<String> {

    private  final String str;

    public  FallbackCommand(String str){
        super(HystrixCommandGroupKey.Factory.asKey("fallback  Group"));
        this.str = str;

    }
    @Override
    public String run() throws  Exception{
        return "fallback occurred ";
    }

}
