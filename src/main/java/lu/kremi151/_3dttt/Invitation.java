/*
 * Michel Kremer
 */
package lu.kremi151._3dttt;

import java.math.BigDecimal;
import java.util.UUID;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author michm
 */
public class Invitation {
    
    private final UUID id, player1, player2;
    private final BigDecimal bet;
    
    public Invitation(UUID id, Player player1, Player player2, BigDecimal bet){
        this.id = id;
        this.player1 = player1.getUniqueId();
        this.player2 = player2.getUniqueId();
        this.bet = bet;
    }
    
    public UUID getInvitationId(){
        return id;
    }
    
    public UUID getPlayer1(){
        return player1;
    }
    
    public UUID getPlayer2(){
        return player2;
    }
    
    public BigDecimal getBet(){
        return bet;
    }
}
