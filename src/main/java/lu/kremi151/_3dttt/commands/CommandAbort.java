/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.commands;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import java.util.UUID;
import lu.kremi151._3dttt.Main;
import lu.kremi151._3dttt.Session;
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
public class CommandAbort implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource cs, CommandContext cc) throws CommandException {
        if (cs instanceof Player) {
            String _uuid = cc.<String>getOne("uuid").get();
            UUID uuid;

            try {
                uuid = UUID.fromString(_uuid);
            } catch (IllegalArgumentException e) {
                cs.sendMessage(LocalizableTexts.errorInvalidUUID.toText());
                return CommandResult.empty();
            }

            Optional<Session> session = Main.instance.removeSession(uuid);

            if (session.isPresent()) {
                session.get().sendToWaiting(LocalizableTexts.lastManStanding.toText());
            } else {
                cs.sendMessage(LocalizableTexts.errorSessionNotFound.apply(ImmutableMap.of("uuid", uuid.toString())).build());
                return CommandResult.empty();
            }

            return CommandResult.success();
        }else{
            cs.sendMessage(LocalizableTexts.errorPlayerCommand.toText());
            return CommandResult.empty();
        }
    }

}
