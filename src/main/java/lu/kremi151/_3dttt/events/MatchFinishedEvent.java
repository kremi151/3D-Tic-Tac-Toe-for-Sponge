/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.events;

import java.util.Optional;
import java.util.UUID;
import lu.kremi151._3dttt.Session;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;

/**
 *
 * @author michm
 */
public class MatchFinishedEvent extends MatchEvent{
    
    private final UUID winner, loser;
    private final Cause cause;
    
    public MatchFinishedEvent(Session session, UUID initiator, UUID opponent, boolean reverse, Cause cause){
        super(initiator, opponent, session);
        this.winner = reverse?opponent:initiator;
        this.loser = reverse?initiator:opponent;
        this.cause = cause;
    }
    
    public UUID getWinner(){
        return winner;
    }
    
    public Optional<Player> tryGetWinner(){
        return Sponge.getServer().getPlayer(winner);
    }
    
    public UUID getLoser(){
        return loser;
    }
    
    public Optional<Player> tryGetLoser(){
        return Sponge.getServer().getPlayer(loser);
    }

    @Override
    public Cause getCause() {
        return cause;
    }
    
}
