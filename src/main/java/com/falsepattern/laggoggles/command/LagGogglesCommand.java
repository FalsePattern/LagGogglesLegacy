/*
 * Lag Goggles: Legacy
 *
 * Copyright (C) 2022 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice, this permission notice and the word "SNEED"
 * shall be included in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.falsepattern.laggoggles.command;

import com.falsepattern.laggoggles.Main;
import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.api.Profiler;
import com.falsepattern.laggoggles.client.gui.GuiScanResultsWorld;
import com.falsepattern.laggoggles.packet.ObjectData;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import com.falsepattern.laggoggles.profiler.ScanType;
import com.falsepattern.laggoggles.server.RequestResultHandler;
import com.falsepattern.laggoggles.server.ScanRequestHandler;
import com.falsepattern.laggoggles.util.ClickableLink;
import com.falsepattern.laggoggles.util.Perms;
import com.falsepattern.lib.text.FormattedText;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class LagGogglesCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return Tags.MOD_ID;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @SuppressWarnings("NoTranslation") //TODO
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length == 2 && args[0].equalsIgnoreCase("start")){
            if(!hasPerms(sender, Perms.Permission.START)){
                throw new CommandException("You don't have permission to do this!");
            }
            final int seconds = parseInt(sender, args[1]);
            if(!Profiler.canProfile()){
                throw new CommandException("Profiler is already running.");
            }
            if(sender instanceof EntityPlayerMP && hasPerms(sender, Perms.Permission.FULL) == false){
                long secondsLeft = ScanRequestHandler.secondsLeft(((EntityPlayerMP) sender).getGameProfile().getId());
                if(secondsLeft > 0) {
                    throw new CommandException("Please wait " + secondsLeft + " seconds.");
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Profiler.runProfiler(seconds, ScanType.WORLD, sender);
                    FormattedText.parse(EnumChatFormatting.GRAY + Tags.MOD_NAME + EnumChatFormatting.WHITE + ": You can see results using /" + getCommandName() + " dump").addChatMessage(sender);
                }
            }).start();
            return;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("dump")){
            if(!hasPerms(sender, Perms.Permission.GET)){
                throw new CommandException("You don't have permission to do this!");
            }
            dump(sender);
            return;
        }
        FormattedText.parse(EnumChatFormatting.GRAY + "Running LagGoggles version: " + EnumChatFormatting.GREEN + Tags.MOD_VERSION).addChatMessage(sender);
        sender.addChatMessage(ClickableLink.getLink("https://minecraft.curseforge.com/projects/laggoggles"));
        sender.addChatMessage(new ChatComponentText(""));
        FormattedText.parse(EnumChatFormatting.GRAY + "Available arguments:").addChatMessage(sender);
        FormattedText.parse(EnumChatFormatting.GRAY + "/" + getCommandName() + " " +EnumChatFormatting.WHITE + "start <seconds>").addChatMessage(sender);
        FormattedText.parse(EnumChatFormatting.GRAY + "/" + getCommandName() + " " +EnumChatFormatting.WHITE + "dump").addChatMessage(sender);
    }

    private boolean hasPerms(ICommandSender sender, Perms.Permission permission){
        if(sender instanceof MinecraftServer){
            return true;
        }else if(sender instanceof EntityPlayerMP){
            return Perms.hasPermission(((EntityPlayerMP) sender), permission);
        }else{
            Main.LOGGER.info("Unknown object is executing a command, assuming it's okay. Object: (" + sender + ") Class: (" + sender.getClass().toString() + ")");
            return true;
        }
    }

    @SuppressWarnings("NoTranslation")
    private void dump(ICommandSender sender) throws CommandException{
        ProfileResult fullResult = Profiler.getLatestResult();
        if(fullResult == null){
            throw new CommandException("No result available.");
        }
        if(fullResult.getType() != ScanType.WORLD){
            throw new CommandException("Result is not of type WORLD.");
        }
        ProfileResult result;
        if(sender instanceof EntityPlayerMP && hasPerms(sender, Perms.Permission.FULL) == false){
            long secondsLeft = RequestResultHandler.secondsLeft(((EntityPlayerMP) sender).getGameProfile().getId());
            if(secondsLeft > 0){
                throw new CommandException("Please wait " + secondsLeft + " seconds.");
            }
            result = Perms.getResultFor(((EntityPlayerMP) sender), fullResult);
        }else{
            result = fullResult;
        }
        msg(sender, "Total ticks", result.getTickCount());
        msg(sender, "Total time", result.getTotalTime()/1000/1000/1000 + " seconds");
        msg(sender, "TPS", Math.round(result.getTPS() * 100D)/100D);
        title(sender, "ENTITIES");
        boolean has = false;
        for(GuiScanResultsWorld.LagSource source : result.getLagSources()){
            if(source.data.type == ObjectData.Type.ENTITY) {
                msg(sender, muPerTickString(source.nanos, result), source.data);
                has = true;
            }
        }
        if(has == false){
            FormattedText.parse("None").addChatMessage(sender);
        }
        has = false;
        title(sender, "TILE ENTITIES");
        for(GuiScanResultsWorld.LagSource source : result.getLagSources()){
            if(source.data.type == ObjectData.Type.TILE_ENTITY) {
                msg(sender, muPerTickString(source.nanos, result), source.data);
                has = true;
            }
        }
        if(has == false){
            FormattedText.parse("None").addChatMessage(sender);
        }
        has = false;
        title(sender, "BLOCKS");
        for(GuiScanResultsWorld.LagSource source : result.getLagSources()){
            if(source.data.type == ObjectData.Type.BLOCK) {
                msg(sender, muPerTickString(source.nanos, result), source.data);
                has = true;
            }
        }
        if(has == false){
            FormattedText.parse("None").addChatMessage(sender);
        }
        has = false;
        title(sender, "EVENTS");
        for(GuiScanResultsWorld.LagSource source : result.getLagSources()){
            if(source.data.type == ObjectData.Type.EVENT_BUS_LISTENER) {
                msg(sender, muPerTickString(source.nanos, result), source.data);
                has = true;
            }
        }
        if(has == false){
            FormattedText.parse("None").addChatMessage(sender);
        }
        title(sender, "END");
        FormattedText.parse("Results printed, copy your log.").addChatMessage(sender);
    }

    private void msg(ICommandSender sender, String key, Object value){
        FormattedText.parse(key + ": " + value).addChatMessage(sender);
    }

    private void title(ICommandSender sender, String title){
        FormattedText.parse(EnumChatFormatting.GREEN + "---[ " + title + " ]---").addChatMessage(sender);
    }

    private static String muPerTickString(long nanos, ProfileResult result) {
        if(result == null){
            return "?";
        }
        return Double.valueOf((nanos / result.getTickCount()) / 1000).intValue() + " micro-s/t";
    }

}
