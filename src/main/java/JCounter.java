import akka.actor.UntypedActor;
import messages.CounterCurrentReply;
import messages.CounterCurrent;
import messages.CounterInc;

public class JCounter extends UntypedActor {
    private int count;

    public JCounter(int count) {
        super();
        this.count = count;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof CounterInc) {
            CounterInc msg = (CounterInc) message;
            count += msg.by;
        }
        else if (message instanceof CounterCurrent) {
            sender().tell(new CounterCurrentReply(count), self());
        }
    }
}
