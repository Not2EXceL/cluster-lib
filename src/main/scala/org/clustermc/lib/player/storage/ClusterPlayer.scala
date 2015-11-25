package org.clustermc.lib.player.storage

import java.util.UUID

import org.clustermc.lib.PermGroup
import org.clustermc.lib.chat.channel.Channel
import org.clustermc.lib.data.values.mutable.impl.SettingDataValues.BooleanSetting
import org.clustermc.lib.econ.Bank
import org.clustermc.lib.player.PlayerWrapper
import org.clustermc.lib.player.storage.coordinator.PlayerCoordinator

/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <buildfresh@gmail.com>
 * 
 * This file is part of Hub.
 * 
 * Hub can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

abstract class ClusterPlayer(uuid: UUID) extends PlayerWrapper(uuid){
  //----------MISC----------
  val showPlayers = BooleanSetting(default = true, value = true)
  lazy val latestName: String = offlineBukkitPlayer.getName

  //----------PERMISSIONS----------
  var group: PermGroup = PermGroup.MEMBER
  def hasRank(permGroup: PermGroup): Boolean = group.ordinal() >= permGroup.ordinal()

  //----------ECONOMY----------
  val bank: Bank = new Bank()

  //----------CHAT----------
  val channelStorage = new ChannelStorage(this.bukkitPlayer)
  val chatMention, receiveMessages = BooleanSetting(default = true, value = true)

  //----------PUNISHMENTS----------
  val punishments: PunishmentStorage = new PunishmentStorage
  def banned = punishments.banned ; def muted = punishments.muted

}

object ClusterPlayer extends PlayerCoordinator[ClusterPlayer]{
  protected override def beforeUnload(uuid: UUID): Unit = {
    apply(uuid).channelStorage.subscribedChannels.foreach(c => c.leave(uuid))
  }

  protected override def afterLoad(player: ClusterPlayer): Unit = {
    player.channelStorage.focus(Channel.get("general").get)
  }
}
