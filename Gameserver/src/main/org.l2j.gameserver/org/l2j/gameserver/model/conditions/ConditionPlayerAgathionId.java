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

import org.l2j.gameserver.engine.skill.api.Skill;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.items.ItemTemplate;

/**
 * The Class ConditionPlayerAgathionId.
 */
public class ConditionPlayerAgathionId extends Condition {
    private final int _agathionId;

    /**
     * Instantiates a new condition player agathion id.
     *
     * @param agathionId the agathion id
     */
    public ConditionPlayerAgathionId(int agathionId) {
        _agathionId = agathionId;
    }

    @Override
    public boolean testImpl(Creature effector, Creature effected, Skill skill, ItemTemplate item) {
        return (effector.getActingPlayer() != null) && (effector.getActingPlayer().getAgathionId() == _agathionId);
    }
}
