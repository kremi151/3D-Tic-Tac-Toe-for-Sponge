/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.util;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;

/**
 *
 * @author michm
 */
public class TextHelper {
    
    private static final int lineWidth = 50;

    public static Text buildTitleLine(Text title, char decor, TextColor decorColor) {
        int titleLength = title.toPlain().length();
        int sideLength = (lineWidth - titleLength) / 2;
        if (sideLength >= 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sideLength; i++) {
                sb.append(decor);
            }
            return Text.join(Text.of(decorColor, sb.toString()), title, Text.of(decorColor, sb.toString()));
        } else {
            return title;
        }
    }

    public static Text buildEmptyTitleLine(char decor, TextColor decorColor) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lineWidth; i++) {
            sb.append(decor);
        }
        return Text.of(decorColor, sb.toString());
    }
}
