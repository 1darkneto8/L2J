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
package org.l2j.gameserver.model.events.impl.character.player;

import org.l2j.gameserver.model.L2Clan;
import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.IBaseEvent;

/**
 * @author UnAfraid
 */
public class OnPlayerClanCreate implements IBaseEvent {
    private final Player _activeChar;
    private final L2Clan _clan;

    public OnPlayerClanCreate(Player activeChar, L2Clan clan) {
        _activeChar = activeChar;
        _clan = clan;
    }

    public Player getActiveChar() {
        return _activeChar;
    }

    public L2Clan getClan() {
        return _clan;
    }

    @Override
    public EventType getType() {
        return EventType.ON_PLAYER_CLAN_CREATE;
    }
}
