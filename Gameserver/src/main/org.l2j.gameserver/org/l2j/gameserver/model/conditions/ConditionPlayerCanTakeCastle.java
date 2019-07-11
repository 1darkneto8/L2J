/*
 * This file is part of the L2J Mobius project.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2j.gameserver.model.conditions;

import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.model.actor.L2Character;
import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.model.entity.Castle;
import org.l2j.gameserver.model.items.L2Item;
import org.l2j.gameserver.model.skills.Skill;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.util.GameUtils;

/**
 * Player Can Take Castle condition implementation.
 *
 * @author Adry_85
 */
public class ConditionPlayerCanTakeCastle extends Condition {
    private final boolean _val;

    public ConditionPlayerCanTakeCastle(boolean val) {
        _val = val;
    }

    @Override
    public boolean testImpl(L2Character effector, L2Character effected, Skill skill, L2Item item) {
        if ((effector == null) || !effector.isPlayer()) {
            return !_val;
        }

        final Player player = effector.getActingPlayer();
        boolean canTakeCastle = true;
        if (player.isAlikeDead() || player.isCursedWeaponEquipped() || !player.isClanLeader()) {
            canTakeCastle = false;
        }

        final Castle castle = CastleManager.getInstance().getCastle(player);
        SystemMessage sm;
        if ((castle == null) || (castle.getResidenceId() <= 0) || !castle.getSiege().isInProgress() || (castle.getSiege().getAttackerClan(player.getClan()) == null)) {
            sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS);
            sm.addSkillName(skill);
            player.sendPacket(sm);
            canTakeCastle = false;
        } else if (!castle.getArtefacts().contains(effected)) {
            player.sendPacket(SystemMessageId.INVALID_TARGET);
            canTakeCastle = false;
        } else if (!GameUtils.checkIfInRange(skill.getCastRange(), player, effected, true)) {
            player.sendPacket(SystemMessageId.THE_DISTANCE_IS_TOO_FAR_AND_SO_THE_CASTING_HAS_BEEN_CANCELLED);
            canTakeCastle = false;
        }
        return (_val == canTakeCastle);
    }
}
