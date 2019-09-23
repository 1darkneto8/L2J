package org.l2j.gameserver.model.actor.instance;

import org.l2j.gameserver.data.xml.impl.TeleportersData;
import org.l2j.gameserver.enums.InstanceType;
import org.l2j.gameserver.enums.TeleportType;
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.templates.NpcTemplate;
import org.l2j.gameserver.model.teleporter.TeleportHolder;
import org.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2j.gameserver.util.GameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringTokenizer;

import static java.util.Objects.isNull;
import static org.l2j.commons.util.Util.SPACE;
import static org.l2j.commons.util.Util.parseNextInt;


/**
 * @author NightMarez
 */
public final class Teleporter extends Npc {
    private static final Logger LOGGER = LoggerFactory.getLogger(Teleporter.class);

    public Teleporter(NpcTemplate template) {
        super(template);
        setInstanceType(InstanceType.L2TeleporterInstance);
    }

    @Override
    public boolean isAutoAttackable(Creature attacker) {
        return GameUtils.isMonster(attacker) || super.isAutoAttackable(attacker);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        // Process bypass
        final StringTokenizer st = new StringTokenizer(command, SPACE);
        switch (st.nextToken()) {
            case "showNoblesSelect": {
                sendHtmlMessage(player, "data/html/teleporter/" + (player.isNoble() ? "nobles_select" : "not_nobles") + ".htm");
                break;
            }
            case "showTeleports": {
                final String listName = (st.hasMoreTokens()) ? st.nextToken() : TeleportType.NORMAL.name();
                final TeleportHolder holder = TeleportersData.getInstance().getHolder(getId(), listName);
                if (isNull(holder)) {
                    LOGGER.warn("Player {} requested show teleports for list with name {}  at NPC {}!", player.getObjectId(), listName, getId());
                    return;
                }
                holder.showTeleportList(player, this);
                break;
            }
            case "showTeleportsHunting":
            {
                final String listName = (st.hasMoreTokens()) ? st.nextToken() : TeleportType.HUNTING.name();
                final TeleportHolder holder = TeleportersData.getInstance().getHolder(getId(), listName);
                if (holder == null)
                {
                    LOGGER.warn("Player {} requested show teleports for hunting list with name {}  at NPC {}!", player.getObjectId(), listName, getId());
                    return;
                }
                holder.showTeleportList(player, this);
                break;
            }
            case "teleport": {
                // Check for required count of params.
                if (st.countTokens() != 2) {
                    LOGGER.warn("Player {} send unhandled teleport command: {}", player, command);
                    return;
                }

                final String listName = st.nextToken();
                final TeleportHolder holder = TeleportersData.getInstance().getHolder(getId(), listName);
                if (isNull(holder)) {
                    LOGGER.warn("Player {} requested unknown teleport list: {} for npc: {}!", player, listName, getId());
                    return;
                }
                holder.doTeleport(player, this, parseNextInt(st, -1));
                break;
            }
            case "chat": {
                int val = 0;
                try {
                    val = Integer.parseInt(command.substring(5));
                } catch (IndexOutOfBoundsException | NumberFormatException ignored) {
                }
                showChatWindow(player, val);
                break;
            }
            default: {
                super.onBypassFeedback(player, command);
            }
        }
    }

    @Override
    public String getHtmlPath(int npcId, int val) {
        final String pom = (val == 0) ? String.valueOf(npcId) : (npcId + "-" + val);
        return "data/html/teleporter/" + pom + ".htm";
    }

    @Override
    public void showChatWindow(Player player) {
        // Teleporter isn't on castle ground
        if (CastleManager.getInstance().getCastle(this) == null) {
            super.showChatWindow(player);
            return;
        }

        // Teleporter is on castle ground
        String filename = "data/html/teleporter/castleteleporter-no.htm";
        if ((player.getClan() != null) && (getCastle().getOwnerId() == player.getClanId())) // Clan owns castle
        {
            filename = getHtmlPath(getId(), 0); // Owner message window
        } else if (getCastle().getSiege().isInProgress()) // Teleporter is busy due siege
        {
            filename = "data/html/teleporter/castleteleporter-busy.htm"; // Busy because of siege
        }
        sendHtmlMessage(player, filename);
    }

    private void sendHtmlMessage(Player player, String filename) {
        final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile(player, filename);
        html.replace("%objectId%", String.valueOf(getObjectId()));
        html.replace("%npcname%", getName());
        player.sendPacket(html);
    }
}
