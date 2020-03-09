package ObservableHystrixCommands;


import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import rx.Observable;
import rx.Subscriber;


public class SampleObservableCommand extends HystrixObservableCommand<String> {

    String name ;
    public SampleObservableCommand(String input){
        super(HystrixCommandGroupKey.Factory.asKey("async sample group"));
        this.name = input;
    }


    @Override
    public Observable<String> construct(){
        String[] arr  = {"first part", "second Part", "third part"};
        return  Observable.create(getOnSubscribe(arr));
    }

   Observable.OnSubscribe<String> getOnSubscribe(String[] arr){
       Observable.OnSubscribe<String> subs =  new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    if(!subscriber.isUnsubscribed()){
                        Thread.sleep(100);
                        subscriber.onNext("Hello dude 1st");

                        for(int ind = 0; ind < arr.length;ind++){
                            subscriber.onNext("Inside arr " + arr[ind]);
                            if(ind == 2){
                                throw new InterruptedException("interruption karenge");
                            }
                            Thread.sleep(300);
                        }
                        subscriber.onCompleted();
                    }
                } catch (Exception e){
                    //throw HystrixBadRequestException if we want to analyse the error
                    subscriber.onError( new HystrixBadRequestException("this is custom error"));

                    // here if we try to throw any other error -> we will get a generic error to read
                    // this is because of HystrixRunTimeException (which cover all other responses)
                    //subscriber.onError( new Exception("this is won't get printed"));
                }
            }
        };
       return subs;
   }


}
