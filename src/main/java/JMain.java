import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.IOException;

/**
 * Created by povilas on 3/19/14.
 */
public class JMain {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting actor system");
        ActorSystem system = ActorSystem.apply("main");
        System.out.println("Creating manager");
        system.actorOf(Props.create(Manager.class), "manager");
        System.out.println("Waiting for keypress");
        int ignored = System.in.read();
    }
}
