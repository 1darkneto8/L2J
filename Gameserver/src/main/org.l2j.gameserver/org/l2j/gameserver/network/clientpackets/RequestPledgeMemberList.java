package org.l2j.gameserver.network.clientpackets;

import org.l2j.gameserver.model.L2Clan;
import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.network.serverpackets.PledgeShowMemberListAll;

/**
 * This class ...
 *
 * @version $Revision: 1.5.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestPledgeMemberList extends ClientPacket {
    @Override
    public void readImpl() {

    }

    @Override
    public void runImpl() {
        final Player activeChar = client.getActiveChar();
        if (activeChar == null) {
            return;
        }
        final L2Clan clan = activeChar.getClan();
        if (clan != null) {
            PledgeShowMemberListAll.sendAllTo(activeChar);
        }
    }
}
