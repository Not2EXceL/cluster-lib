package org.clustermc.lib.chat

import java.util.regex.Pattern

import org.clustermc.lib.enums.{DonatorRank, PermissionRank}
import org.clustermc.lib.player.libplayer.LibPlayer

/**
  * Used from Essentials
  */
object ColorFilter {

    //Vanilla patterns used to strip existing formats
    val VANILLA_PATTERN = Pattern.compile("\u00a7+[0-9A-FK-ORa-fk-or]?")
    val VANILLA_COLOR_PATTERN = Pattern.compile("\u00a7+[0-9A-Fa-f]")
    val VANILLA_MAGIC_PATTERN = Pattern.compile("\u00a7+[Kk]")
    val VANILLA_FORMAT_PATTERN = Pattern.compile("\u00a7+[L-ORl-or]")
    //'&' convention colour codes
    val REPLACE_ALL_PATTERN = Pattern.compile("(?<!&)&([0-9a-fk-orA-FK-OR])")
    val REPLACE_COLOR_PATTERN = Pattern.compile("(?<!&)&([0-9a-fA-F])")
    val REPLACE_MAGIC_PATTERN = Pattern.compile("(?<!&)&([Kk])")
    val REPLACE_FORMAT_PATTERN = Pattern.compile("(?<!&)&([l-orL-OR])")
    val REPLACE_PATTERN = Pattern.compile("&&(?=[0-9a-fk-orA-FK-OR])")

    def filter(player: LibPlayer, input: String): String = {
        if(input == null) return ""
        var message: String = input

        if(player.hasRank(PermissionRank.ADMIN) || player.hasDonatorRank(DonatorRank.EPIC))
            message = replaceColor(input, REPLACE_COLOR_PATTERN)
        else message = stripColor(message, VANILLA_COLOR_PATTERN)
        if(player.hasRank(PermissionRank.OWNER))
            message = replaceColor(message, REPLACE_MAGIC_PATTERN)
        else message = stripColor(message, VANILLA_MAGIC_PATTERN)
        if(player.hasRank(PermissionRank.ADMIN) || player.hasDonatorRank(DonatorRank.MYTHIC))
            message = replaceColor(message, REPLACE_FORMAT_PATTERN)
        else message = stripColor(message, VANILLA_MAGIC_PATTERN)

        message
    }

    def replaceColor(input: String, pattern: Pattern): String = {
        REPLACE_PATTERN.matcher(pattern.matcher(input).replaceAll("\\u00a7$1")).replaceAll("&")
    }

    def stripColor(input: String, pattern: Pattern): String = {
        pattern.matcher(input).replaceAll("")
    }
}
