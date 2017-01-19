package lu.kremi151._3dttt;

import com.google.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lu.kremi151._3dttt.events.MatchStartedEvent;
import lu.kremi151._3dttt.localization.LocalizableTexts;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;

@Plugin(authors = "kremi151", id = "ttt3d", name = "3D TicTacToe", version = "0.0.3.0", description = "3D Tic Tac Toe for Sponge API, playable in the Minecraft chat")
public class Main {

    private final HashMap<UUID, Session> sessions = new HashMap<>();
    private final HashMap<UUID, Invitation> invitations = new HashMap<>();

    public static Main instance;

    private final Random rand;

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File privateConfigDir;

    private File langPath;

    private String currentLocalizationFile = "en-US";

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    private ConfigurationLoader<CommentedConfigurationNode> localizationManager;

    public Main() {
        instance = this;
        rand = new Random(System.currentTimeMillis());
    }

    @Listener
    public void onServerStarting(GameStartingServerEvent event) {
        Commands.register();

        try {
            ConfigurationNode rootNode = configManager.load();

            ConfigurationNode localizationNode = rootNode.getNode("localization");
            currentLocalizationFile = localizationNode.getString("en-US");

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        loadLanguageFile();
    }
    
    @Listener
    public void onServerStopping(GameStoppingServerEvent event){
        
    }
    
    private void loadLanguageFile(){
        File langFile = new File(getLangPath(), currentLocalizationFile + ".conf");

        localizationManager = HoconConfigurationLoader.builder().setPath(langFile.toPath()).build();

        if (!langFile.exists()) {
            try {
                logger.log(Level.INFO, "Writing default localization texts to non existing language file {0} ...", langFile.getName());
                ConfigurationNode rootNode = localizationManager.createEmptyNode(ConfigurationOptions.defaults());
                LocalizableTexts.write(rootNode);
                localizationManager.save(rootNode);
                logger.info("Done.");
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error writing default localization texts to " + langFile.getName(), ex);
            }
        }else{
            try {
                LocalizableTexts.load(localizationManager.load());
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error loading localization texts from " + langFile.getName(), ex);
            }
        }
    }
    
    public Optional<EconomyService> getEconomy(){
        return Sponge.getServiceManager().provide(EconomyService.class);
    }

    private File getLangPath() {
        if (langPath == null) {
            langPath = new File(privateConfigDir, "lang");
            if (!langPath.exists()) {
                langPath.mkdirs();
            }
        }
        return langPath;
    }

    public Optional<Session> getSession(UUID uuid) {
        return Optional.ofNullable(sessions.get(uuid));
    }

    public Optional<Session> removeSession(UUID uuid) {
        return Optional.ofNullable(sessions.remove(uuid));
    }

    public Session newSession(Player p1, Player p2, BigDecimal bet) {
        Session s = new Session(UUID.randomUUID(), p1, p2, bet);
        sessions.put(s.getSessionId(), s);
        return s;
    }

    public Optional<Session> transformInvitation(Invitation invitation) {
        Optional<Player> p1 = Sponge.getServer().getPlayer(invitation.getPlayer1());
        Optional<Player> p2 = Sponge.getServer().getPlayer(invitation.getPlayer2());
        if (p1.isPresent() && p2.isPresent()) {
            Session s;
            if (rand.nextBoolean()) {
                s = new Session(invitation.getInvitationId(), p1.get(), p2.get(), invitation.getBet());
            } else {
                s = new Session(invitation.getInvitationId(), p2.get(), p1.get(), invitation.getBet());
            }
            sessions.put(s.getSessionId(), s);
            invitations.remove(invitation.getInvitationId());
            MatchStartedEvent event = new MatchStartedEvent(s, p1.get().getUniqueId(), p2.get().getUniqueId(), Cause.of(NamedCause.source(this), NamedCause.simulated(p1.get()), NamedCause.simulated(p2.get())));
            Sponge.getEventManager().post(event);
            return Optional.of(s);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Invitation> getInvitation(UUID uuid) {
        return Optional.ofNullable(invitations.get(uuid));
    }

    public Optional<Invitation> removeInvitation(UUID uuid) {
        return Optional.ofNullable(invitations.remove(uuid));
    }

    public Invitation newInvitation(Player p1, Player p2, BigDecimal bet) {
        Invitation s = new Invitation(UUID.randomUUID(), p1, p2, bet);
        invitations.put(s.getInvitationId(), s);
        return s;
    }

}
