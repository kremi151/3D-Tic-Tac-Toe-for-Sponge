/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.events;

import java.util.UUID;
import lu.kremi151._3dttt.Main;
import lu.kremi151._3dttt.Session;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;

/**
 *
 * @author michm
 */
public class MatchStartedEvent extends MatchEvent{
    
    private final Cause cause;
    
    public MatchStartedEvent(Session session, UUID initiator, UUID opponent, Cause cause){
        super(initiator, opponent, session);
        this.cause = cause;
    }

    @Override
    public Cause getCause() {
        return cause;
    }
    
}
