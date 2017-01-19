/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.util;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.text.Text;

/**
 *
 * @author michm
 */
public class EconomyHelper {
    
    public static Text format(BigDecimal amount, Currency currency){
        return currency.format(amount);
    }
    
    public static boolean giveMoney(UUID player, BigDecimal amount, Cause cause){
        Optional<EconomyService> econ = Sponge.getServiceManager().provide(EconomyService.class);
        if(econ.isPresent()){
            Optional<UniqueAccount> acc = econ.get().getOrCreateAccount(player);
            if(acc.isPresent()){
                return acc.get().deposit(econ.get().getDefaultCurrency(), amount, cause).getResult() == ResultType.SUCCESS;
            }
        }
        return false;
    }
    
    public static boolean takeMoney(UUID player, BigDecimal amount, Cause cause){
        Optional<EconomyService> econ = Sponge.getServiceManager().provide(EconomyService.class);
        if(econ.isPresent()){
            Optional<UniqueAccount> acc = econ.get().getOrCreateAccount(player);
            if(acc.isPresent()){
                return acc.get().withdraw(econ.get().getDefaultCurrency(), amount, cause).getResult() == ResultType.SUCCESS;
            }
        }
        return false;
    }
}
