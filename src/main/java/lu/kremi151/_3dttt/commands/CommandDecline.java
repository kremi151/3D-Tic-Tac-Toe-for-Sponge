/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.commands;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import java.util.UUID;
import lu.kremi151._3dttt.Invitation;
import lu.kremi151._3dttt.Main;
import lu.kremi151._3dttt.localization.LocalizableTexts;
import org.spongepowered.api.Sponge;
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
public class CommandDecline implements CommandExecutor {

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

            Optional<Invitation> invite = Main.instance.removeInvitation(uuid);

            if (invite.isPresent()) {
                Optional<Player> host = Sponge.getServer().getPlayer(invite.get().getPlayer1());
                if (host.isPresent()) {
                    host.get().sendMessage(LocalizableTexts.opponentDeclined.toText());
                }
                cs.sendMessage(LocalizableTexts.invitationDeclined.toText());
            } else {
                cs.sendMessage(LocalizableTexts.errorInvitationNotFound.apply(ImmutableMap.of("uuid", uuid.toString())).build());
                return CommandResult.empty();
            }

            return CommandResult.success();
        } else {
            cs.sendMessage(LocalizableTexts.errorPlayerCommand.toText());
            return CommandResult.success();
        }
    }

}
