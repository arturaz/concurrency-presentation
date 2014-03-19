import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import messages.CounterCurrent;
import messages.CounterInc;

/**
 * Created by povilas on 3/19/14.
 */
public class JIncreaser extends UntypedActor {
    private final ActorRef manager, counter;

    public JIncreaser(ActorRef manager, ActorRef counter) {
        super();
        this.manager = manager;
        this.counter = counter;

        for (int i = 0; i < 100; i++)
            counter.tell(new CounterInc(1), self());
        counter.tell(CounterCurrent.instance, self());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        manager.tell(message, self());
    }
}
