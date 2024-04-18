package dev.dividebynull.teabot.abilities

import dev.dividebynull.teabot.TeaBot
import dev.dividebynull.teabot.util.MemberUtils
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.telegrambots.meta.api.methods.ForwardMessage
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

class ForwardSuggestionsAbility(private val bot: TeaBot): AbilityHandler {
    private val memberUtils: MemberUtils = (MemberUtils(bot))

    override fun isApplicable(ctx: MessageContext): Boolean {
        val update = ctx.update()
        val message: Message = when {
            update.hasMessage() -> update.message
            update.hasEditedMessage() -> update.editedMessage
            else -> return false
        }
        if (bot.isAdmin(message.from.id)) return false
        if(!message.isUserMessage) return false
        val chat = bot.execute(GetChat(bot.properties.chatId))
        if(!memberUtils.isUserKnown(chat.linkedChatId.toString(), message.from.id)) return false
        return message.hasText() || message.hasPhoto() || message.hasVideo()
    }

    override fun handle(ctx: MessageContext) {
        var message = ctx.update().message
        if (!ctx.update().hasMessage()) message = ctx.update().editedMessage
        val chatId = message.chatId
        bot.admins().forEach {
            try {
                bot.execute(ForwardMessage(it.toString(), chatId.toString(), message.messageId))
            } catch (e: TelegramApiException) {
                if (e.message?.contains("[400]") == true
                    || e.message?.contains("[403]") == true
                ) return
                throw RuntimeException(e)
            }
        }
    }
}
