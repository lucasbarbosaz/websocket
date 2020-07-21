package com.eu.habbo.messages.incoming.rooms.users;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.modtool.ScripterManager;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomChatMessage;
import com.eu.habbo.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.habbo.habbohotel.rooms.RoomChatType;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboInfo;
import com.eu.habbo.habbohotel.users.HabboManager;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.generic.alerts.BubbleAlertComposer;
import com.eu.habbo.plugin.events.users.UserTalkEvent;
import gnu.trove.map.hash.THashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class RoomUserTalkEvent extends MessageHandler {

    @Override
    public void handle() throws Exception {
        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();
        if (room == null)
            return;

        if (!this.client.getHabbo().getHabboStats().allowTalk())
            return;

        RoomChatMessage message = new RoomChatMessage(this);

        if (message.getMessage().length() <= RoomChatMessage.MAXIMUM_LENGTH) {
            if (Emulator.getPluginManager().fireEvent(new UserTalkEvent(this.client.getHabbo(), message, RoomChatType.TALK)).isCancelled()) {
                return;
            }
//.replace(":O", "<img src='/swf/c_images/emojis/surpreso.png' height='20' width='20'>")
            room.talk(this.client.getHabbo(), message, RoomChatType.TALK);

            if (!message.isCommand) {
                if (RoomChatMessage.SAVE_ROOM_CHATS) {
                    Emulator.getThreading().run(message);
                }
            }

            if (this.client.getHabbo().getHabboInfo().getHabboStats().blockMencoesRequests && message.getMessage().startsWith("@")) {
                String finalName;
                String[] nome = message.getMessage().replace("@", "").split(" ");
                finalName = nome[0];
                Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(nome[0]);



                if (habbo == null)
                    return;

                try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
                     PreparedStatement pstmt = connection.prepareStatement("INSERT INTO socket_engagements (action, hash, label) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
                {  pstmt.setString(1, "mention");
                    pstmt.setInt(2, habbo.getHabboInfo().getId());
                    pstmt.setString(3, habbo.getHabboInfo().getUsername() + ";" + this.client.getHabbo().getHabboInfo().getUsername() + ";" + this.client.getHabbo().getHabboInfo().getLook() + ";" + message.getMessage().replace(";", " ") + ";");
                    pstmt.execute();
                } catch (SQLException e) {
                    Emulator.getLogging().logSQLException(e);
                }

                //THashMap<String, String> notifica = new THashMap<String, String>();
               // notifica.put("display", "BUBBLE");
               // notifica.put("image", "https://haibbo.in/premiar/" + this.client.getHabbo().getHabboInfo().getLook() + ".png");
                //notifica.put("linkUrl", "event:navigator/goto/" + this.client.getHabbo().getHabboInfo().getCurrentRoom().getId() + "");
               // notifica.put("message", "" + this.client.getHabbo().getHabboInfo().getUsername() + " mecionou você ( " + message.getMessage().replace(";", " ") + " ) ");
              //  habbo.getClient().sendResponse(new BubbleAlertComposer("mentioned", notifica));
              //  this.client.getHabbo().getClient().getHabbo().whisper("Você mencionou " + habbo.getHabboInfo().getUsername() + "", RoomChatMessageBubbles.ALERT);

                if (habbo == null)
                    habbo.whisper("Esse usuário não foi encontrado ou está offline");
                    return;

                }

        } else {
            String reportMessage = Emulator.getTexts().getValue("scripter.warning.chat.length").replace("%username%", this.client.getHabbo().getHabboInfo().getUsername()).replace("%length%", message.getMessage().length() + "");
            ScripterManager.scripterDetected(this.client, reportMessage);
            Emulator.getLogging().logUserLine(reportMessage);
        }
    }
}
