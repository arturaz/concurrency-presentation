import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import messages.CounterCurrentReply;

/**
 * Created by povilas on 3/19/14.
 */
public class JManager extends UntypedActor {
    public JManager() {
        super();
        System.out.println("Manager starting");
        ActorRef counter =
            context().actorOf(Props.create(JCounter.class, 0), "counter");
        for (int i = 0; i < 10; i++) context().actorOf(
                Props.create(JIncreaser.class, self(), counter),
                "increaser-" + i
        );
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof CounterCurrentReply) {
            CounterCurrentReply msg = (CounterCurrentReply) message;
            System.out.println(String.format(
               "%s reported count: %d", sender(), msg.count
            ));
        }
    }
}
