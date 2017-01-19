/*
 * Michel Kremer
 */
package lu.kremi151._3dttt;

import java.util.Optional;
import java.util.UUID;
import lu.kremi151._3dttt.engine.GameHolder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author michm
 */
public class TPlayer {
    
    private final UUID uuid;
    private final String name;
    private final GameHolder.Type type;
    
    public TPlayer(UUID uuid, String name, GameHolder.Type type){
        this.uuid = uuid;
        this.name = name;
        this.type = type;
    }
    
    public UUID getUniqueId(){
        return uuid;
    }
    
    public String getName(){
        return name;
    }
    
    public GameHolder.Type getType(){
        return type;
    }
    
    public Optional<Player> getSpongePlayer(){
        return Sponge.getServer().getPlayer(uuid);
    }
}
