/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.events;

import java.util.UUID;
import lu.kremi151._3dttt.Session;
import org.spongepowered.api.event.cause.Cause;

/**
 *
 * @author michm
 */
public class MatchAbortedEvent extends MatchEvent{
    
    private final Cause cause;
    
    public MatchAbortedEvent(Session session, UUID initiator, UUID opponent, Cause cause){
        super(initiator, opponent, session);
        this.cause = cause;
    }

    @Override
    public Cause getCause() {
        return cause;
    }
    
}
