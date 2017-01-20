/*
 * Michel Kremer
 */
package lu.kremi151._3dttt;

import lu.kremi151._3dttt.commands.CommandAbort;
import lu.kremi151._3dttt.commands.CommandAccept;
import lu.kremi151._3dttt.commands.CommandDecline;
import lu.kremi151._3dttt.commands.CommandInvite;
import lu.kremi151._3dttt.commands.CommandMove;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 *
 * @author michm
 */
public class Commands {

    private static final CommandSpec declineCommand = CommandSpec.builder()
            .description(Text.of("Declines an invitation"))
            .executor(new CommandDecline())
            .arguments(
                    GenericArguments.string(Text.of("uuid"))
            )
            .build();

    private static final CommandSpec acceptCommand = CommandSpec.builder()
            .description(Text.of("Accepts an invitation"))
            .executor(new CommandAccept())
            .permission("ttt3d.accept")
            .arguments(
                    GenericArguments.string(Text.of("uuid"))
            )
            .build();

    private static final CommandSpec inviteCommand = CommandSpec.builder()
            .description(Text.of("Invites a player to a match"))
            .executor(new CommandInvite())
            .permission("ttt3d.invite")
            .arguments(
                    GenericArguments.player(Text.of("rival")),
                    GenericArguments.optional(GenericArguments.doubleNum(Text.of("bet")))
            )
            .build();

    private static final CommandSpec abortCommand = CommandSpec.builder()
            .description(Text.of("Aborts a session"))
            .executor(new CommandAbort())
            .arguments(
                    GenericArguments.string(Text.of("uuid"))
            )
            .build();

    private static final CommandSpec moveCommand = CommandSpec.builder()
            .description(Text.of("Continues a session by executing a move"))
            .executor(new CommandMove())
            .arguments(
                    GenericArguments.string(Text.of("uuid")),
                    GenericArguments.integer(Text.of("x")),
                    GenericArguments.integer(Text.of("y")),
                    GenericArguments.integer(Text.of("z"))
            )
            .build();

    private static final CommandSpec rootCommand = CommandSpec.builder()
            .description(Text.of("Root command for 3D Tic Tac Toe"))
            .child(moveCommand, "move")
            .child(abortCommand, "abort")
            .child(inviteCommand, "invite")
            .child(acceptCommand, "accept")
            .child(declineCommand, "decline")
            .build();
    
    static void register(){
        Sponge.getGame().getCommandManager().register(Main.instance, rootCommand, "ttt3d");
    }

}
