package com.falsepattern.laggoggles.util;


import com.falsepattern.lib.text.FormattedText;
import lombok.val;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ClickableLink {

    public static ChatComponentText getLink(String link){
        val text = FormattedText.parse(EnumChatFormatting.BLUE + link).toChatText();
        val style = text.getChatStyle();
        style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
        return text;
    }
}
