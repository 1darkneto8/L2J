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

import org.l2j.gameserver.model.actor.L2Character;
import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.model.items.L2Item;
import org.l2j.gameserver.model.skills.Skill;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Sdw
 */
public class ConditionPlayerHasFreeSummonPoints extends Condition {
    private final int _summonPoints;

    public ConditionPlayerHasFreeSummonPoints(int summonPoints) {
        _summonPoints = summonPoints;
    }

    @Override
    public boolean testImpl(L2Character effector, L2Character effected, Skill skill, L2Item item) {
        final Player player = effector.getActingPlayer();
        if (player == null) {
            return false;
        }

        boolean canSummon = true;

        if ((_summonPoints == 0) && player.hasServitors()) {
            player.sendPacket(SystemMessageId.YOU_CANNOT_USE_THE_S1_SKILL_DUE_TO_INSUFFICIENT_SUMMON_POINTS);
            canSummon = false;
        } else if ((player.getSummonPoints() + _summonPoints) > player.getMaxSummonPoints()) {
            final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_USE_THE_S1_SKILL_DUE_TO_INSUFFICIENT_SUMMON_POINTS);
            sm.addSkillName(skill);
            player.sendPacket(sm);
            canSummon = false;
        }

        return canSummon;
    }
}
