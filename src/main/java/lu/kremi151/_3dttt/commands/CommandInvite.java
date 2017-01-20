/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.commands;

import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.Optional;
import lu.kremi151._3dttt.Invitation;
import lu.kremi151._3dttt.Main;
import lu.kremi151._3dttt.events.MatchInviteEvent;
import lu.kremi151._3dttt.localization.LocalizableTexts;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

/**
 *
 * @author michm
 */
public class CommandInvite implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource cs, CommandContext cc) throws CommandException {
        if (cs instanceof Player) {
            Optional<Double> bet = cc.<Double>getOne("bet");

            Player host = (Player) cs;
            Player rival = cc.<Player>getOne("rival").get();

            BigDecimal finalBet = new BigDecimal(0.0);
            Currency defCurrency = null;

            if (bet.isPresent()) {
                Optional<EconomyService> econ = Main.instance.getEconomy();
                if (econ.isPresent()) {
                    Optional<UniqueAccount> hostAccount = econ.get().getOrCreateAccount(host.getUniqueId()),
                            rivalAccount = econ.get().getOrCreateAccount(rival.getUniqueId());
                    if (hostAccount.isPresent() && rivalAccount.isPresent()) {
                        finalBet = new BigDecimal(bet.get());
                        defCurrency = econ.get().getDefaultCurrency();
                        boolean hostEnough = hostAccount.get().getBalance(defCurrency).compareTo(finalBet) >= 0;
                        boolean rivalEnough = rivalAccount.get().getBalance(defCurrency).compareTo(finalBet) >= 0;
                        if (!hostEnough) {
                            BigDecimal diff = finalBet.subtract(hostAccount.get().getBalance(defCurrency));
                            host.sendMessage(LocalizableTexts.errorNotEnoughMoney.apply(ImmutableMap.of("amount", defCurrency.format(diff).toPlain())).build());
                        }
                        if (!rivalEnough) {
                            host.sendMessage(LocalizableTexts.errorRivalNotEnoughMoney.toText());
                        }
                        if (!hostEnough || !rivalEnough) {
                            return CommandResult.empty();
                        }
                    } else {
                        if (!hostAccount.isPresent()) {
                            cs.sendMessage(LocalizableTexts.errorCannotAccessEconomyAccount.apply(ImmutableMap.of("player", host.getName())).build());
                        }
                        if (!rivalAccount.isPresent()) {
                            cs.sendMessage(LocalizableTexts.errorCannotAccessEconomyAccount.apply(ImmutableMap.of("player", rival.getName())).build());
                        }
                        return CommandResult.empty();
                    }
                }
            }

            MatchInviteEvent event = new MatchInviteEvent(host, rival, Cause.of(NamedCause.source(Main.instance), NamedCause.simulated(host)));
            Sponge.getEventManager().post(event);

            if (!event.isCancelled()) {
                Invitation invite = Main.instance.newInvitation(host, rival, finalBet);

                Text.Builder declineBuilder = Text.builder();
                declineBuilder.append(Text.of(TextColors.GRAY, "["), Text.of(TextColors.RED, "Decline"), Text.of(TextColors.GRAY, "]"));
                declineBuilder.onHover(TextActions.showText(LocalizableTexts.hoverClickToDecline.toText()));
                declineBuilder.onClick(TextActions.runCommand("/ttt3d decline " + invite.getInvitationId().toString()));

                Text.Builder acceptBuilder = Text.builder();
                acceptBuilder.append(Text.of(TextColors.GRAY, "["), Text.of(TextColors.GREEN, "Accept"), Text.of(TextColors.GRAY, "]"));
                acceptBuilder.onHover(TextActions.showText(LocalizableTexts.hoverClickToAccept.toText()));
                acceptBuilder.onClick(TextActions.runCommand("/ttt3d accept " + invite.getInvitationId().toString()));

                Text optionsLine = Text.join(declineBuilder.build(), Text.of("    "), acceptBuilder.build());

                if (defCurrency != null && invite.getBet().compareTo(new BigDecimal(0.0)) > 0) {
                    rival.sendMessage(LocalizableTexts.challengeIncomingWithBet.apply(ImmutableMap.of("player", host.getName(), "bet", defCurrency.format(finalBet).toPlain())).build());
                } else {
                    rival.sendMessage(LocalizableTexts.challengeIncoming.apply(ImmutableMap.of("player", host.getName())).build());
                }
                rival.sendMessage(LocalizableTexts.doYouAccept.toText());
                rival.sendMessage(optionsLine);

                host.sendMessage(LocalizableTexts.invitationSendTo.apply(ImmutableMap.of("player", rival.getName())).build());
            }

            return CommandResult.success();

        } else {
            cs.sendMessage(LocalizableTexts.errorPlayerCommand.toText());
            return CommandResult.empty();
        }
    }

}
