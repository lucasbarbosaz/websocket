package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.habbo.habbohotel.rooms.RoomState;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.outgoing.generic.alerts.BubbleAlertComposer;
import com.eu.habbo.messages.outgoing.rooms.RoomSettingsUpdatedComposer;
import gnu.trove.map.hash.THashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class RoomYTCommand extends Command {
    public RoomYTCommand() {
        super(null, new String[]{"roomvideo", "video"});
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (gameClient.getHabbo().getHabboInfo().getCurrentRoom().getOwnerId() == gameClient.getHabbo().getHabboInfo().getId() || gameClient.getHabbo().getHabboInfo().getRank().getId() > 6) {
            String tipo = "";

            if (params.length > 1) {
                for (int i = 1; i < params.length; ++i) {
                    tipo = tipo.replace(";", "") + params[i];
                    tipo = tipo.replace(";", "");
                }

                for (Habbo habbo : gameClient.getHabbo().getHabboInfo().getCurrentRoom().getHabbos()) {
                    try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
                         PreparedStatement pstmt = connection.prepareStatement("INSERT INTO socket_engagements (action, hash, label) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                        pstmt.setString(1, "roomvideo");
                        pstmt.setInt(2, habbo.getHabboInfo().getId());
                        pstmt.setString(3, tipo.replace(";", ""));
                        pstmt.execute();
                    } catch (SQLException e) {
                        Emulator.getLogging().logSQLException(e);
                    }
                }
            } else {
                gameClient.getHabbo().whisper("Insira o video que quer ver", RoomChatMessageBubbles.ALERT);
            }
            return true;
        }
        return true;
    }
}
