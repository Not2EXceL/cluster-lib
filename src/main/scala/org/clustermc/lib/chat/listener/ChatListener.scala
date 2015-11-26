package org.clustermc.lib.chat.listener

import org.bukkit.Bukkit
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.{EventHandler, EventPriority, Listener}
import org.clustermc.lib.ClusterLib
import org.clustermc.lib.chat.ColorFilter
import org.clustermc.lib.chat.channel.Channel
import org.clustermc.lib.player.ClusterPlayer
import org.clustermc.lib.punishment.data.Punishment
import org.clustermc.lib.utils.messages.{Messages, MsgVar}

/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <buildfresh@gmail.com>
 * 
 * This file is part of Hub.
 * 
 * Hub can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
class ChatListener extends Listener {

  Bukkit.getServer.getPluginManager.registerEvents(this, ClusterLib.instance)

    @EventHandler(priority = EventPriority.HIGHEST)
    def asyncChat(event: AsyncPlayerChatEvent): Unit = {
        val pplayer = ClusterPlayer(event.getPlayer.getUniqueId)
        if(pplayer.muted){
            pplayer.message(Messages("punishment.youremuted",
                MsgVar("{TIME}", Punishment.timeLeft(pplayer.punishments._mute.get))))
            event.setCancelled(true)
            return
        }

        val chatData = pplayer.channelStorage
        val focused = chatData.focusedChannel

        if(!focused.canSend(event.getPlayer)) {
            event.getPlayer.sendMessage(Messages("channel.error.access.noLongerFocus", MsgVar("{NAME}", focused.name)))
            event.setCancelled(true)
            if(!focused.canSubscribe(event.getPlayer)){
                event.getPlayer.sendMessage(Messages("channel.error.access.noLongerSubscribe", MsgVar("{NAME}", focused.name)))
                chatData.unsubscribe(focused)
            }
            chatData.focus(Channel.get("general").get)
            return
        }

        val group = pplayer.chatRank

        event.setMessage(ColorFilter.filter(pplayer, event.getMessage))

        event.setFormat(focused.format
          .replace("{PLAYER}", "%1$s")
          .replace("{PLAYER_COLOR}", group.player)
          .replace("{PREFIX}", group.prefix)
          .replace("{SUFFIX}", group.suffix)
          .replace("{MESSAGE_COLOR}", group.chat)
          .replace("{MESSAGE}", "%2$s"))

        //strip players
        val iter = event.getRecipients.iterator()
        while(iter.hasNext)
          if(!ClusterPlayer(iter.next().getUniqueId).channelStorage.isSubscribed(focused)){
            iter.remove()
          }
    }
}
