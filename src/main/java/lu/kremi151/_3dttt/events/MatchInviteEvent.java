/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

/**
 *
 * @author michm
 */
public class MatchInviteEvent implements Event, Cancellable{
    
    private boolean cancelled = false;
    private final Cause cause;
    private final Player initiator, opponent;
    
    public MatchInviteEvent(Player initiator, Player opponent, Cause cause){
        this.initiator = initiator;
        this.opponent = opponent;
        this.cause = cause;
    }

    @Override
    public Cause getCause() {
        return cause;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean bln) {
        this.cancelled = bln;
    }
    
    public Player getInitiator(){
        return initiator;
    }
    
    public Player getOpponent(){
        return opponent;
    }
    
}
