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

/**
 * The Class ConditionTargetInvSize.
 *
 * @author Zoey76
 */
public class ConditionTargetInvSize extends Condition {
    private final int _size;

    /**
     * Instantiates a new condition player inv size.
     *
     * @param size the size
     */
    public ConditionTargetInvSize(int size) {
        _size = size;
    }

    @Override
    public boolean testImpl(L2Character effector, L2Character effected, Skill skill, L2Item item) {
        if ((effected != null) && effected.isPlayer()) {
            final Player target = effected.getActingPlayer();
            return target.getInventory().getSize(i -> !i.isQuestItem()) <= (target.getInventoryLimit() - _size);
        }
        return false;
    }
}
