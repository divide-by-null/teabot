package dev.dividebynull.teabot.abilities

import dev.dividebynull.teabot.TeaBot
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.abilitybots.api.objects.Privacy
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

private val logger = KotlinLogging.logger {}

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class BanAbility(private val bot: TeaBot) : AbilityProvider {

    override fun buildAbility(): Ability {
        return Ability
            .builder()
            .name("ban")
            .info("ban")
            .locality(Locality.GROUP)
            .privacy(Privacy.ADMIN)
            .action { ban(it) }
            .post { bot.deleteMessage(it) }
            .build()
    }

    private fun ban(ctx: MessageContext) {
        val message = ctx.update().message
        val reply = message.replyToMessage ?: return
        val targetToBan = reply.from
        if (bot.isAdmin(targetToBan.id)) return
        if (bot.isGroupAdmin(ctx.update(), targetToBan.id)) return
        val chatId = message.chatId.toString()
        try {
            val banRequest = BanChatMember
                .builder()
                .chatId(chatId)
                .userId(targetToBan.id)
                .revokeMessages(false)
                .build()
            bot.execute(banRequest)
            val deleteRequest = DeleteMessage
                .builder()
                .chatId(chatId)
                .messageId(message.messageId)
                .build()
            bot.silent().execute(deleteRequest)
        } catch (e: TelegramApiException) {
            logger.error(e) { "Error" }
        }
    }
}
