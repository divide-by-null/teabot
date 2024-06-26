package dev.dividebynull.teabot.abilities

import dev.dividebynull.teabot.TeaBot
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatSenderChat
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Message

class BanSenderChatMessagesAbility(private val bot: TeaBot): AbilityHandler {
    override fun isApplicable(ctx: MessageContext): Boolean {
        val update = ctx.update()
        val message: Message = when {
            update.hasMessage() -> update.message
            update.hasEditedMessage() -> update.editedMessage
            else -> return false
        }
        if(message.isUserMessage) return false
        if(!message.senderChat.isChannelChat) return false
        val senderChatId = message.senderChat.id
        val chat = bot.execute(GetChat
            .builder()
            .chatId(message.chatId)
            .build()
        )
        if(chat.linkedChatId == senderChatId) return false
        bot.properties.allowedSenderChatsUsernames.forEach {
            if(message.senderChat.userName == it) return false
        }
        return true
    }

    override fun handle(ctx: MessageContext) {
        var message = ctx.update().message
        if (!ctx.update().hasMessage()) message = ctx.update().editedMessage
        val messageId = message.messageId
        val chatId = message.chatId
        val senderChatId = message.senderChat.id
        val banSenderChatRequest = BanChatSenderChat
            .builder()
            .chatId(chatId)
            .senderChatId(senderChatId)
            .build()
        val deleteMessageRequest = DeleteMessage
            .builder()
            .chatId(chatId)
            .messageId(messageId)
            .build()
        bot.silent().execute(banSenderChatRequest)
        bot.silent().execute(deleteMessageRequest)
    }
}
