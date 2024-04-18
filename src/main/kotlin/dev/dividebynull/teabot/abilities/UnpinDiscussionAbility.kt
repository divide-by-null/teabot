package dev.dividebynull.teabot.abilities

import dev.dividebynull.teabot.TeaBot
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.UnpinChatMessage

class UnpinDiscussionAbility(private val bot: TeaBot) : AbilityHandler {
    override fun isApplicable(ctx: MessageContext): Boolean {
        if (!ctx.update().hasMessage()) return false
        val message = ctx.update().message
        return message.isAutomaticForward == true
    }

    override fun handle(ctx: MessageContext) {
        val message = ctx.update().message
        bot.silent().execute(UnpinChatMessage(message.chatId.toString(), message.messageId))
    }
}
