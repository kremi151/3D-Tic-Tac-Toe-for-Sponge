/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.events;

import java.util.Optional;
import java.util.UUID;
import lu.kremi151._3dttt.Session;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;

/**
 *
 * @author michm
 */
public abstract class MatchEvent implements Event {
    
    private final UUID initiator, opponent;
    private final Session session;
    
    public MatchEvent(UUID initiator, UUID opponent, Session session){
        this.initiator = initiator;
        this.opponent = opponent;
        this.session = session;
    }

    public UUID getInitiator() {
        return initiator;
    }
    
    public Optional<Player> tryGetInitiator(){
        return Sponge.getServer().getPlayer(initiator);
    }

    public UUID getOpponent() {
        return opponent;
    }
    
    public Optional<Player> tryGetOpponent(){
        return Sponge.getServer().getPlayer(opponent);
    }

    public Session getSession() {
        return session;
    }

}
