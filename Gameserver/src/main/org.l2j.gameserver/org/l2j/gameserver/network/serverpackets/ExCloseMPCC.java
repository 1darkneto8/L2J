package org.l2j.gameserver.network.serverpackets;

import io.github.joealisson.mmocore.StaticPacket;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerExPacketId;

/**
 * Close the CommandChannel Information window
 *
 * @author chris_00
 */
@StaticPacket
public class ExCloseMPCC extends ServerPacket {
    public static final ExCloseMPCC STATIC_PACKET = new ExCloseMPCC();

    private ExCloseMPCC() {
    }

    @Override
    public void writeImpl(GameClient client) {
        writeId(ServerExPacketId.EX_CLOSE_MPCC);

    }

}
