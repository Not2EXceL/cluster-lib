package org.clustermc.lib

import org.bukkit.scheduler.BukkitTask
import org.clustermc.lib.chat.announcer.Announcer
import org.clustermc.lib.player.storage.ClusterPlayer
import org.clustermc.lib.utils.cooldown.CooldownHandler
import org.clustermc.lib.utils.database.Mongo
import org.clustermc.lib.utils.{ClusterServerPlugin, CustomConfig}

/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <buildfresh@gmail.com>
 * 
 * This file is part of Hub.
 * 
 * Hub can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

class ClusterLib extends ClusterServerPlugin("lib"){

    private var _mongoDB: Mongo = null
    private var _cooldowns: CooldownHandler = null

    private var cooldownTask: BukkitTask = null

    def database = _mongoDB

    def cooldowns = _cooldowns

    override def onEnable(): Unit = {
        ClusterLib._instance = this
        _cooldowns = new CooldownHandler

        _mongoDB = new Mongo(new CustomConfig(this.getDataFolder, "db").getConfig)
        _mongoDB.open()

        cooldownTask = getServer.getScheduler.runTaskTimerAsynchronously(this, new Runnable {
            override def run(): Unit = _cooldowns.handleCooldowns()
        }, 20L, 5L)
        Announcer.start()
    }

    override def onDisable(): Unit = {
        ClusterPlayer.unloadAll()
        Announcer.end()
        ClusterLib._instance = null
        _mongoDB.getClient.close()
        cooldownTask.cancel()
    }
}

object ClusterLib {
    private var _instance: ClusterLib = null
    def instance: ClusterLib = _instance
}
