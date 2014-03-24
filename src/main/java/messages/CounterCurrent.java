package messages;

/**
 * Created by povilas on 3/19/14.
 */
public class CounterCurrent {
    public static final CounterCurrent instance;

    static {
        instance = new CounterCurrent();
    }

    private CounterCurrent() {}
}
