package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomState;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.generic.alerts.BubbleAlertComposer;
import com.eu.habbo.messages.outgoing.rooms.RoomSettingsUpdatedComposer;
import gnu.trove.map.hash.THashMap;

import java.sql.*;
import java.util.Map;

public class EventCommand extends Command {
    public EventCommand() {
        super("cmd_event", Emulator.getTexts().getValue("commands.keys.cmd_event").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        String tipo = "";
            if (params.length >= 1) {
                StringBuilder message = new StringBuilder();

                for (int i = 1; i < params.length; ++i) {
                    tipo = tipo.replace(";", "") + params[i];
                    tipo = tipo.replace(";", "");
                }
                // ALERTA SOCKET
               // try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
                 //    PreparedStatement pstmt = connection.prepareStatement("INSERT INTO socket_engagements (action, type, hash, label) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
                //     {  pstmt.setString(1, "alert");
                //        pstmt.setString(2, "eha");
                //        pstmt.setString(3, "all");
                //        pstmt.setString(4, tipo + ";" + gameClient.getHabbo().getHabboInfo().getCurrentRoom().getName() + ";" + gameClient.getHabbo().getHabboInfo().getUsername() + ";" + gameClient.getHabbo().getHabboInfo().getLook() + ";" + gameClient.getHabbo().getHabboInfo().getCurrentRoom().getId());
                //       pstmt.execute();
                //     } catch (SQLException e) {
               //     Emulator.getLogging().logSQLException(e);
               // }

                String messagem = "Está neste momento a começar um novo evento realizado por <b>" + gameClient.getHabbo().getHabboInfo().getUsername() + "</b>\n";
   messagem += "Evento: <b> " + gameClient.getHabbo().getHabboInfo().getCurrentRoom().getName() + "</b>\n";
   messagem += "Para ir ao evento clique no botão abaixo.\n\n";
  messagem += "Para desativar os alertas use <b> :alertasoff</b>\n\n\n\n";
    THashMap<String, String> codes = new THashMap<>();
   codes.put("display", "POP_UP");
   codes.put("ROOMNAME", gameClient.getHabbo().getHabboInfo().getCurrentRoom().getName());
   codes.put("ROOMID", gameClient.getHabbo().getHabboInfo().getCurrentRoom().getId() + "");
  codes.put("USERNAME", gameClient.getHabbo().getHabboInfo().getUsername());
  codes.put("LOOK", gameClient.getHabbo().getHabboInfo().getLook());
   codes.put("TIME", Emulator.getDate().toString());
  codes.put("message", messagem);
  codes.put("title", "Novo evento");

  ServerMessage msg = new BubbleAlertComposer("hotel.event", codes).compose();



  for (Map.Entry<Integer, Habbo> set : Emulator.getGameEnvironment().getHabboManager().getOnlineHabbos().entrySet()) {
    Habbo habbo = set.getValue();
   if (habbo.getHabboStats().blockStaffAlerts)
     continue;

  habbo.getClient().sendResponse(msg);
  }

                THashMap<String, String> keys = new THashMap();
                keys.put("display", "BUBBLE");
                keys.put("image", "${image.library.url}notifications/evento.png");
                keys.put("linkUrl", "event:navigator/goto/" + gameClient.getHabbo().getHabboInfo().getCurrentRoom().getId() + "");
                keys.put("message", "Está havendo um novo evento ( "+ gameClient.getHabbo().getHabboInfo().getCurrentRoom().getName() +" ), clique aqui para participar!");
                Emulator.getGameServer().getGameClientManager().sendBroadcastResponse(new BubbleAlertComposer("", keys));

                gameClient.getHabbo().getHabboInfo().getCurrentRoom().setState(RoomState.OPEN);

                THashMap<String, String> keysa = new THashMap();
                keysa.put("display", "BUBBLE");
                keysa.put("image", "${image.library.url}notifications/quarto_aberto.png");
                keysa.put("message", "Este quarto foi aberto a todos os usuários");

                Room room = gameClient.getHabbo().getHabboInfo().getCurrentRoom();
                if (room != null) {
                    room.sendComposer((new BubbleAlertComposer("", keysa)).compose());
                    return true;
                }

                gameClient.getHabbo().getHabboInfo().getCurrentRoom().sendComposer((new RoomSettingsUpdatedComposer(gameClient.getHabbo().getHabboInfo().getCurrentRoom())).compose());
                return true;
            }

        return false;
    }
}


//for (int i = 2; i < params.length; i++) {
//    message.append(params[i]);
//    message.append(" ");
// }
//   String messagem = "Está neste momento a começar um novo evento realizado por <b>" + gameClient.getHabbo().getHabboInfo().getUsername() + "</b>\n";
//   messagem += "Evento: <b> " + gameClient.getHabbo().getHabboInfo().getCurrentRoom().getName() + "</b>\n";
//   messagem += "Para ir ao evento clique no botão abaixo.\n\n";
//  messagem += "Para desativar os alertas use <b> :alertasoff</b>\n\n\n\n";
//    THashMap<String, String> codes = new THashMap<>();
//   codes.put("display", "POP_UP");
//   codes.put("ROOMNAME", gameClient.getHabbo().getHabboInfo().getCurrentRoom().getName());
//   codes.put("ROOMID", gameClient.getHabbo().getHabboInfo().getCurrentRoom().getId() + "");
//  codes.put("USERNAME", gameClient.getHabbo().getHabboInfo().getUsername());
//  codes.put("LOOK", gameClient.getHabbo().getHabboInfo().getLook());
//   codes.put("TIME", Emulator.getDate().toString());
////  codes.put("message", messagem);
//  codes.put("title", "Novo evento");

//  ServerMessage msg = new BubbleAlertComposer("hotel.event", codes).compose();

//

//  for (Map.Entry<Integer, Habbo> set : Emulator.getGameEnvironment().getHabboManager().getOnlineHabbos().entrySet()) {
//    Habbo habbo = set.getValue();
//   if (habbo.getHabboStats().blockStaffAlerts)
//     continue;

//  habbo.getClient().sendResponse(msg);
//  }
