package org.clustermc.lib.player.storage

import org.bson.types.ObjectId
import org.clustermc.lib.punishment.data.Punishment

/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <buildfresh@gmail.com>
 * 
 * This file is part of Hub.
 * 
 * Hub can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

class PunishmentStorage {
  var _mute, _ban: Option[ObjectId] = None
  private[storage] def muted: Boolean = {
    if(_mute.isDefined){
      val punish = Punishment.load(_mute.get)
      if(punish.timed && punish.finished){ _mute = None ; return false }
      true
    }else false
  }
  private[storage] def banned: Boolean = {
    if(_ban.isDefined){
      val punish = Punishment.load(_ban.get)
      if(punish.timed && punish.finished){ _ban = None ; return false }
      true
    }else false
  }
}