/*
 * Michel Kremer
 */
package lu.kremi151._3dttt;

import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import lu.kremi151._3dttt.engine.GameHolder;
import lu.kremi151._3dttt.engine.GameHolder.Type;
import lu.kremi151._3dttt.engine.Position;
import lu.kremi151._3dttt.engine.Winner;
import lu.kremi151._3dttt.events.MatchFinishedEvent;
import lu.kremi151._3dttt.localization.LocalizableTexts;
import lu.kremi151._3dttt.util.EconomyHelper;
import lu.kremi151._3dttt.util.TextHelper;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

/**
 *
 * @author michm
 */
public class Session {

    private final UUID sessionId;
    private final TPlayer players[] = new TPlayer[2];
    private final BigDecimal bet;
    private final GameHolder gameHolder = new GameHolder();
    private int currentPlayer = 0;
    private final Random rand;

    public Session(UUID sessionId, Player player1, Player player2, BigDecimal bet) {
        this.sessionId = sessionId;
        this.rand = new Random(System.currentTimeMillis());
        this.bet = bet;

        Type firstType = Type.randomPlayable(rand);
        this.players[0] = new TPlayer(player1.getUniqueId(), player1.getName(), firstType);
        this.players[1] = new TPlayer(player2.getUniqueId(), player2.getName(), firstType.getOpposite());
        this.currentPlayer = rand.nextInt(2);
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public BigDecimal getBet() {
        return bet;
    }

    private TPlayer getPlaying() {
        return players[currentPlayer];
    }

    private TPlayer getWaiting() {
        return players[(currentPlayer + 1) % 2];
    }

    private void onPlayerWon(TPlayer player) {
        TPlayer opponent;
        if (player == players[0]) {
            opponent = players[1];
        } else if (player == players[1]) {
            opponent = players[0];
        } else {
            throw new RuntimeException("This should not happen");
        }
        if (bet.compareTo(new BigDecimal(0.0)) > 0 && EconomyHelper.takeMoney(opponent.getUniqueId(), bet, Cause.of(NamedCause.source(Main.instance)))) {
            if(EconomyHelper.giveMoney(player.getUniqueId(), bet, Cause.of(NamedCause.source(Main.instance)))){
                Optional<Player> op = player.getSpongePlayer();
                if(op.isPresent()){
                    op.get().sendMessage(LocalizableTexts.receivedMoney.apply(ImmutableMap.of("amount", Main.instance.getEconomy().get().getDefaultCurrency().format(bet).toPlain())).build());
                }
            }
        }
        MatchFinishedEvent event = new MatchFinishedEvent(this, players[0].getUniqueId(), players[1].getUniqueId(), players[1].getUniqueId().equals(player.getUniqueId()), Cause.of(NamedCause.source(Main.instance), NamedCause.simulated(player), NamedCause.simulated(opponent)));
        Sponge.getEventManager().post(event);
    }
    
    public boolean areOpponentsEqual(){
        return players[0].getUniqueId().equals(players[1].getUniqueId());
    }

    public void round() {
        Optional<Player> waiting = getWaiting().getSpongePlayer();
        Optional<Player> playing = getPlaying().getSpongePlayer();
        if (waiting.isPresent() && playing.isPresent()) {
            displayGame(waiting.get(), false, LocalizableTexts.waitingForOpponent.toText());
            displayGame(playing.get(), true);
        } else {
            Main.instance.removeSession(sessionId);
            broadcast(LocalizableTexts.playerLeftAbort.toText());

            if (waiting.isPresent()) {
                onPlayerWon(getWaiting());
            } else if (playing.isPresent()) {
                onPlayerWon(getPlaying());
            }
        }
    }

    public boolean makeMove(Player player, int x, int y, int z) {
        TPlayer src = accountFor(player.getUniqueId());
        if (src == getPlaying() || (areOpponentsEqual() && player.getUniqueId() == players[0].getUniqueId())) {
            gameHolder.set(x, y, z, src.getType());

            Optional<Winner> winner = gameHolder.checkForWinner();
            if (winner.isPresent()) {
                Optional<Player> waiting = getWaiting().getSpongePlayer();
                Optional<Player> playing = getPlaying().getSpongePlayer();
                if (waiting.isPresent()) {
                    displayGame(waiting.get(), false);
                }
                if (playing.isPresent()) {
                    displayGame(playing.get(), false);
                }
                broadcast(LocalizableTexts.playerWon.apply(ImmutableMap.of("player", getPlaying().getName())).build());
                onPlayerWon(getPlaying());
                Main.instance.removeSession(sessionId);
                return true;
            }

            currentPlayer = (currentPlayer + 1) % 2;
            round();
            return true;
        } else {
            return false;
        }
    }

    public void broadcast(Text msg) {
        for (TPlayer p : players) {
            Optional<Player> player = p.getSpongePlayer();
            if (player.isPresent()) {
                sendToPlayer(player.get(), msg);
            }
        }
    }

    public void sendToPlaying(Text msg) {
        Optional<Player> player = getPlaying().getSpongePlayer();
        if (player.isPresent()) {
            sendToPlayer(player.get(), msg);
        }
    }

    public void sendToWaiting(Text msg) {
        Optional<Player> player = getWaiting().getSpongePlayer();
        if (player.isPresent()) {
            sendToPlayer(player.get(), msg);
        }
    }

    private void sendToPlayer(Player player, Text msg) {
        player.sendMessage(msg);
    }

    private TPlayer accountFor(UUID player) {
        for (TPlayer a : players) {
            if (a.getUniqueId().equals(player)) {
                return a;
            }
        }
        throw new RuntimeException("This player is not a part of this session");
    }

    public void displayGame(Player p, boolean playable) {
        displayGame(p, playable, playable ? LocalizableTexts.yourTurn.toText() : LocalizableTexts.endResult.toText());
    }

    public void displayGame(Player p, boolean playable, Text title) {
        //p.sendMessage(Text.join(Text.of(TextColors.GRAY, "########"), title, Text.of(TextColors.GRAY, "########")));
        p.sendMessage(TextHelper.buildTitleLine(title, '#', TextColors.GRAY));
        TPlayer sessionAccount = accountFor(p.getUniqueId());
        TextColor firstColor = TextColors.GRAY, secondColor = TextColors.GRAY;
        if (sessionAccount == players[0]) {
            firstColor = TextColors.LIGHT_PURPLE;
        } else {
            secondColor = TextColors.LIGHT_PURPLE;
        }
        p.sendMessage(LocalizableTexts.versusLine.apply(ImmutableMap.of("player1", players[0].getName(), "symbol1", Text.of(firstColor, players[0].getType().getSymbol()), "player2", players[1].getName(), "symbol2", Text.of(secondColor, players[1].getType().getSymbol()))).build());
        for (int z = 0; z < 4; z++) {
            StringBuilder sb = new StringBuilder();
            for (int iz = 0; iz < z; iz++) {
                sb.append("            ");
            }
            Text margin = Text.of(sb.toString());
            for (int y = 0; y < 4; y++) {
                Text.Builder lineBuilder = margin.toBuilder();
                for (int x = 0; x < 4; x++) {
                    Text.Builder fieldBuilder = Text.builder();
                    Optional<Position> lastPosition = gameHolder.getLastPosition();
                    TextColor color = TextColors.GRAY;
                    if (lastPosition.isPresent()) {
                        Position pos = lastPosition.get();
                        if (pos.x == x && pos.y == y && pos.z == z) {
                            color = TextColors.GOLD;
                        }
                    }
                    Type type = gameHolder.get(x, y, z);
                    if (null != type) {
                        switch (type) {
                            case FREE:
                                //fieldBuilder.append(Text.of(color, " ■ "));
                                fieldBuilder.append(Text.of(color, " _ "));
                                if (playable) {
                                    fieldBuilder.onHover(TextActions.showText(LocalizableTexts.hoverClickToMakeMove.toText()));
                                    fieldBuilder.onClick(TextActions.runCommand("/ttt3d move " + sessionId.toString() + " " + x + " " + y + " " + z));
                                }
                                break;
                            case CIRCLE:
                                fieldBuilder.append(Text.of(color, " O "));
                                break;
                            case CROSS:
                                fieldBuilder.append(Text.of(color, " X "));
                                break;
                            default:
                                break;
                        }
                    }
                    lineBuilder.append(fieldBuilder.build());
                }
                p.sendMessage(lineBuilder.build());
            }
        }

        if (playable) {
            Text.Builder abortBuilder = Text.builder();
            abortBuilder.append(Text.of(TextColors.GRAY, "["), LocalizableTexts.actionAbort.toText(), Text.of(TextColors.GRAY, "]"));
            abortBuilder.onHover(TextActions.showText(LocalizableTexts.hoverClickToAbortMatch.toText()));
            abortBuilder.onClick(TextActions.runCommand("/ttt3d abort " + sessionId.toString()));
            p.sendMessage(abortBuilder.build());
        } else {
            p.sendMessage(Text.EMPTY);
        }

        //p.sendMessage(Text.of(TextColors.GRAY, "####################"));
        p.sendMessage(TextHelper.buildEmptyTitleLine('#', TextColors.GRAY));
    }
}
