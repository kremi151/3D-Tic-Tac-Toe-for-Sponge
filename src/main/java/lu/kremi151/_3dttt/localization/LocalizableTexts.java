/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.localization;

import com.google.common.reflect.TypeToken;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import lu.kremi151._3dttt.annotations.Localizeable;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.text.TextTemplate;
import static org.spongepowered.api.text.TextTemplate.*;
import org.spongepowered.api.text.format.TextColors;

/**
 *
 * @author michm
 */
public class LocalizableTexts {
    
    @Localizeable(node="waitingForOpponent")
    public static TextTemplate waitingForOpponent = of(TextColors.GOLD, "Waiting for your opponent...");
    
    @Localizeable(node="playerLeftAbort")
    public static TextTemplate playerLeftAbort = of(TextColors.RED, "The game has been aborted because at least one player left the game");
    
    @Localizeable(node="yourTurn")
    public static TextTemplate yourTurn = of(TextColors.GOLD, "Your turn");
    
    @Localizeable(node="endResult")
    public static TextTemplate endResult = of(TextColors.GOLD, "End result");
    
    @Localizeable(node="lastManStanding")
    public static TextTemplate lastManStanding = of(TextColors.GREEN, "You won this match because the other player aborted");
    
    @Localizeable(node="matchWillBegin")
    public static TextTemplate matchWillBegin = of(TextColors.GREEN, "The match will begin...");
    
    @Localizeable(node="opponentDeclined")
    public static TextTemplate opponentDeclined = of(TextColors.RED, "Your opponent declined your invitation");
    
    @Localizeable(node="invitationDeclined")
    public static TextTemplate invitationDeclined = of(TextColors.RED, "The invitation has been declined");
    
    @Localizeable(node="doYouAccept")
    public static TextTemplate doYouAccept = of(TextColors.GRAY, "Do you accept this invitation?");
    
    @Localizeable(node="playerWon")
    public static TextTemplate playerWon = of(TextColors.GREEN, "The player ", arg("player").color(TextColors.GOLD), " has won this match");
    
    @Localizeable(node="versusLine")
    public static TextTemplate versusLine = of(TextColors.GRAY, arg("player1").color(TextColors.GOLD), " (", arg("symbol1"), ") vs ", arg("player2").color(TextColors.GOLD), " (", arg("symbol2"), ")");
    
    @Localizeable(node="challengeIncoming")
    public static TextTemplate challengeIncoming = of(TextColors.GRAY, "The player ", arg("player").color(TextColors.GOLD), " would like to challenge you in a match of ", TextColors.GOLD, "3D Tic Tac Toe");
    
    @Localizeable(node="challengeIncomingWithBet")
    public static TextTemplate challengeIncomingWithBet = of(TextColors.GRAY, "The player ", arg("player").color(TextColors.GOLD), " would like to challenge you in a match of ", TextColors.GOLD, "3D Tic Tac Toe", TextColors.GRAY, " with a bet of ", arg("bet").color(TextColors.GOLD));
    
    @Localizeable(node="invitationSendTo")
    public static TextTemplate invitationSendTo = of(TextColors.GREEN, "An invitation has been send to ", arg("player").color(TextColors.GOLD));
    
    @Localizeable(node="receivedMoney")
    public static TextTemplate receivedMoney = of(TextColors.GREEN, "You received ", arg("amount").color(TextColors.GOLD));
    
    
    @Localizeable(rootNode="hover", node="clickToMakeMove")
    public static TextTemplate hoverClickToMakeMove = of(TextColors.GOLD, "Click to make your move at this field");
    
    @Localizeable(rootNode="hover", node="clickToAbortMatch")
    public static TextTemplate hoverClickToAbortMatch = of(TextColors.GOLD, "Click here to abort this match");
    
    @Localizeable(rootNode="hover", node="clickToDecline")
    public static TextTemplate hoverClickToDecline = of(TextColors.GOLD, "Click here to decline this invitation");
    
    @Localizeable(rootNode="hover", node="clickToAccept")
    public static TextTemplate hoverClickToAccept = of(TextColors.GOLD, "Click here to accept this invitation");
    
    
    @Localizeable(rootNode="action", node="abort")
    public static TextTemplate actionAbort = of(TextColors.RED, "Abort");
    
    
    @Localizeable(rootNode="error", node="invalidUUID")
    public static TextTemplate errorInvalidUUID = of(TextColors.RED, "Invalid UUID format");
    
    @Localizeable(rootNode="error", node="sessionNotFound")
    public static TextTemplate errorSessionNotFound = of(TextColors.RED, "Session with id ", arg("uuid"), "not found");
    
    @Localizeable(rootNode="error", node="invitationNotFound")
    public static TextTemplate errorInvitationNotFound = of(TextColors.RED, "Invitation with id ", arg("uuid"), "not found");
    
    @Localizeable(rootNode="error", node="sessionCannotBeCreated")
    public static TextTemplate errorSessionCannotBeCreated = of(TextColors.RED, "The session could not be created");
    
    @Localizeable(rootNode="error", node="playerCommand")
    public static TextTemplate errorPlayerCommand = of(TextColors.RED, "This command has to be executed by a player");
    
    @Localizeable(rootNode="error", node="notYourTurn")
    public static TextTemplate errorNotYourTurn = of(TextColors.RED, "It is not your turn now");
    
    @Localizeable(rootNode="error", node="notEnoughMoney")
    public static TextTemplate errorNotEnoughMoney = of(TextColors.RED, "You are ", arg("amount").color(TextColors.GOLD), " short");
    
    @Localizeable(rootNode="error", node="rivalNotEnoughMoney")
    public static TextTemplate errorRivalNotEnoughMoney = of(TextColors.RED, "Your rival has not enough money to accept this bet");
    
    @Localizeable(rootNode="error", node="cannotAccessEconomyAccount")
    public static TextTemplate errorCannotAccessEconomyAccount= of(TextColors.RED, "The economy data of", arg("player").color(TextColors.GOLD), " is not accessible");
    
    public static void load(ConfigurationNode root){
        Field fields[] = LocalizableTexts.class.getDeclaredFields();
        for(Field f : fields){
            f.setAccessible(true);
            Localizeable al;
            if(f.getType() == TextTemplate.class && (al = f.getAnnotation(Localizeable.class)) != null){
                try {
                    ConfigurationNode locNode = root.getNode(al.rootNode(), al.node());
                    f.set(null, tryLoad(locNode, (TextTemplate) f.get(null)));
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(LocalizableTexts.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static void write(ConfigurationNode root){
        Field fields[] = LocalizableTexts.class.getDeclaredFields();
        for(Field f : fields){
            f.setAccessible(true);
            Localizeable al;
            if(f.getType() == TextTemplate.class && (al = f.getAnnotation(Localizeable.class)) != null){
                try {
                    ConfigurationNode locNode = root.getNode(al.rootNode(), al.node());
                    locNode.setValue(TypeToken.of(TextTemplate.class), (TextTemplate)f.get(null));
                } catch (IllegalArgumentException | IllegalAccessException | ObjectMappingException ex) {
                    Logger.getLogger(LocalizableTexts.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private static TextTemplate tryLoad(ConfigurationNode node, TextTemplate def){
        if(node == null || def == null)throw new NullPointerException();
        if(node.isVirtual()){
            return def;
        }else{
            try {
                return node.getValue(TypeToken.of(TextTemplate.class));
            } catch (ObjectMappingException ex) {
                Logger.getLogger(LocalizableTexts.class.getName()).log(Level.SEVERE, null, ex);
                return def;
            }
        }
    }
}
