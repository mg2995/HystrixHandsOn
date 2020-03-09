import ObservableHystrixCommands.SampleObservableCommand;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.Future;


public class ManagerApplication {

    public static void main(String[] args) {

        // for calling the sync HystrixCommand
        // RunHystrixCommand();
        try {
            // to listen multiple events which are triggered by observable
            RunHystrixObservableCommand();

            // to listen to single event (if we are sure that observable is going to trigger just 1 event i.e 1 onNext & then onCompleted)
//            RunHystrixObservableCommandWithBlocking();
        } catch (Exception e) {
            System.out.println("exception occurred");
        }

    }

    private static void RunHystrixObservableCommandWithBlocking() {
        SampleObservableCommand observableCommand = new SampleObservableCommand("single sample");
        // Do edits in the SampleObservableCommand before running, single onNext should be present in the SampleObservableCommand.
        Observable<String> observable = observableCommand.observe();

        try {
            Future<String> futureRes = observable.toBlocking().toFuture();
            System.out.println(futureRes.get());
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
    }

    private static void RunHystrixObservableCommand() throws Exception {
        SampleObservableCommand observableCommand = new SampleObservableCommand("sample");

        // toObservable actually calls the construct method when someone subscribe to it.
        Observable<String> observable = observableCommand.toObservable();


        // subscribes to the Observable that represents the response(s) from the dependency &
        // returns an Observable that replicates that source Observable. This can be illustrated via timeout
        //within construct method
        //Observable<String> observable = observableCommand.observe();

        Subscription subs = observable.subscribe(s -> {
                    System.out.println("On Next occured " + s);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        System.out.println("thread is interrupted");
//                    }
                }, s -> System.out.println("error occured " + s.getMessage())
                , () -> System.out.println("completed")
        );
        System.out.println(subs.isUnsubscribed());

    }

    private static void RunHystrixCommand() {
        int i = 0;
        String s = "";
        int fallbackCount = 0;
        int normal = 0;
        try {
            while (i < 100) {
                s = new SyncHystrixCommand("new one").execute();
//                System.out.println(s + "  called here");
                if (s.substring(0, 8).equals("fallback")) {
                    fallbackCount++;
                    System.out.println(fallbackCount + " fallback Count");
                } else {
                    normal++;
                    System.out.println(normal + " normal");
                }
                i++;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + "  & " + e.getCause());
            return;
        }

        System.out.println(s + " is result");
    }
}
