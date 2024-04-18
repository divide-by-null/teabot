# TeaBot
**TeaBot is a fork of [Haroolean's GnuTardBot](https://github.com/Haarolean/gnutard-bot)**

An anti-spam & moderation telegram bot for custom needs 

**Features:**
1. Check messages for spam links
2. Forward suggestions from subs
3. Block messages from channels
4. Ban command
5. Report command
6. Nuke command
7. Auto unpin channel's forwarded messages to a discussion group

## Building

`docker build -t teabot:latest .`

## Running

`docker run teabot:latest`

## Application properties

1. `bot.token` -- api token
2. `bot.admins` -- list of ids, comma separated
3. `bot.chatId` -- id of group linked to the channel
4. `bot.allowedSenderChatsUsernames` -- list of allowed channel usernames
