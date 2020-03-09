package ObservableHystrixCommands;


import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
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
                        subscriber.onNext("Hello dude 1st");
                        //Thread.sleep(1000);
                        for(int ind = 0; ind < arr.length;ind++){
                            subscriber.onNext("Inside arr " + arr[ind]);
                            if(ind == 2){
                                throw new InterruptedException("interruption karenge");
                            }
                        }
                        subscriber.onCompleted();
                    }
                } catch (Exception e){
                    subscriber.onError( new InterruptedException("some issue has occured"));
                }
            }
        };
       return subs;
   }


}
