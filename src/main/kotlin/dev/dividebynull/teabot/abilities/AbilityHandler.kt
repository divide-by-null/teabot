package dev.dividebynull.teabot.abilities

import org.telegram.abilitybots.api.objects.MessageContext

interface AbilityHandler {
    fun isApplicable(ctx: MessageContext): Boolean
    fun handle(ctx: MessageContext)
}
