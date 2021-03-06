package org.clustermc.lib.xserver.messages

import org.clustermc.lib.xserver.MessageId

/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <buildfresh@gmail.com>
 * 
 * This file is part of Hub.
 * 
 * Hub can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

case class XServerMessage(identifier: MessageId, data: String, online: Boolean = true) extends XMessage{
  override val action: String = "Forward"

  override def send(): Unit ={
    send(if(online) "ONLINE" else "ALL", identifier, data)
  }

}
