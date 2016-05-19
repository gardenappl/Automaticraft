package goldenapple.automaticraft.util;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public class TextHelper {
    public static void addChatMessage(EntityPlayer player, String message, Object... formatArgs) {
        player.addChatComponentMessage(new TextComponentTranslation(I18n.translateToLocal(message), formatArgs));
    }

    public static void addChatMessage(EntityPlayer player, Style style, String message, Object... formatArgs) {
        player.addChatComponentMessage(new TextComponentTranslation(I18n.translateToLocal(message), formatArgs).setChatStyle(style));
    }

    public static void addLocalisedTooltip(List<String> tooltip, String string, Object... formatArgs){
        if(!I18n.canTranslate(string + ".1")) {
            if(I18n.canTranslate(string)) {
                tooltip.add(I18n.translateToLocalFormatted(string, formatArgs));
            }else {
                tooltip.add(String.format(string, formatArgs));
            }
            return;
        }
        int line = 1;
        do {
            tooltip.add(I18n.translateToLocalFormatted(string + "." + line, formatArgs));
            line++;
        }while (I18n.canTranslate(string + "." + line));
    }
}
