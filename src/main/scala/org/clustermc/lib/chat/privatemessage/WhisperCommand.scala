package org.clustermc.lib.chat.privatemessage

import org.bukkit.Bukkit
import org.clustermc.lib.chat.ColorFilter
import org.clustermc.lib.command.CommandContext
import org.clustermc.lib.player.storage.PlayerCoordinator
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

object WhisperCommand {

  //whisper|tell|t|w|msg|m|r|re|reply|message <player name> <message>
  val whisper = (context: CommandContext) => {
    val pplayer= PlayerCoordinator(context.sender.getUniqueId)
    if(!pplayer.muted){
      val player = Bukkit.getPlayer(context.args(0))
      if (player != null) {
        if (pplayer.receiveMessages.value.get) {
          if (PlayerCoordinator(player.getUniqueId).receiveMessages.value.get) {
            val sentence = ColorFilter.filter(pplayer.group, context.args.drop(1).mkString(" "))
            player.sendMessage(Messages("message.format.sender",
              MsgVar("{PLAYER", context.sender.getName),
              MsgVar("{MESSAGE}", sentence)))
            player.sendMessage(Messages("message.format.receiver",
              MsgVar("{PLAYER", context.sender.getName),
              MsgVar("{MESSAGE}", sentence)))
          } else context.sender.sendMessage(Messages("message.error.messagesTurnedOffOther"))
        } else context.sender.sendMessage(Messages("message.error.messagesTurnedOffSelf"))
      } else context.sender.sendMessage(Messages("player.error.noExist", MsgVar("{PLAYER}", context.args(0))))
    }else pplayer.message(Messages("punishment.youremuted", MsgVar("{TIME}", Punishment.timeLeft(pplayer.punishments._mute.get))))
  }
}
