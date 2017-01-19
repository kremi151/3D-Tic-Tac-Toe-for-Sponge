/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.commands;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import java.util.UUID;
import lu.kremi151._3dttt.Main;
import lu.kremi151._3dttt.Session;
import lu.kremi151._3dttt.engine.GameHolder;
import lu.kremi151._3dttt.localization.LocalizableTexts;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author michm
 */
public class CommandMove implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource cs, CommandContext cc) throws CommandException {
        if (cs instanceof Player) {
            String _uuid = cc.<String>getOne("uuid").get();
            UUID uuid;
            int x = cc.<Integer>getOne("x").get();
            int y = cc.<Integer>getOne("y").get();
            int z = cc.<Integer>getOne("z").get();

            try {
                uuid = UUID.fromString(_uuid);
            } catch (IllegalArgumentException e) {
                cs.sendMessage(LocalizableTexts.errorInvalidUUID.toText());
                return CommandResult.empty();
            }

            Optional<Session> session = Main.instance.getSession(uuid);

            if (session.isPresent()) {
                if (!session.get().makeMove((Player)cs, x, y, z)) {
                    cs.sendMessage(LocalizableTexts.errorNotYourTurn.toText());
                    return CommandResult.empty();
                }
            } else {
                cs.sendMessage(LocalizableTexts.errorSessionNotFound.apply(ImmutableMap.of("uuid", uuid.toString())).build());
                return CommandResult.empty();
            }

            return CommandResult.success();
        } else {
            cs.sendMessage(LocalizableTexts.errorPlayerCommand.toText());
            return CommandResult.empty();
        }
    }

}
