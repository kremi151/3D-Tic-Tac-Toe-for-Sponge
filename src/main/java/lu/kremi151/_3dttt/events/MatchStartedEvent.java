/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.events;

import lu.kremi151._3dttt.Session;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

/**
 *
 * @author michm
 */
public class MatchStartedEvent implements Event{
    
    private final Player initiator, opponent;
    private final Session session;
    private final Cause cause;
    
    public MatchStartedEvent(Session session, Player initiator, Player opponent, Cause cause){
        this.initiator = initiator;
        this.opponent = opponent;
        this.session = session;
        this.cause = cause;
    }

    public Player getInitiator() {
        return initiator;
    }

    public Player getOpponent() {
        return opponent;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public Cause getCause() {
        return cause;
    }
    
}
