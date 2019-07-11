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

import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.IBaseEvent;
import org.l2j.gameserver.model.itemcontainer.ItemContainer;
import org.l2j.gameserver.model.items.instance.L2ItemInstance;

/**
 * @author UnAfraid
 */
public class OnPlayerItemTransfer implements IBaseEvent {
    private final Player _activeChar;
    private final L2ItemInstance _item;
    private final ItemContainer _container;

    public OnPlayerItemTransfer(Player activeChar, L2ItemInstance item, ItemContainer container) {
        _activeChar = activeChar;
        _item = item;
        _container = container;
    }

    public Player getActiveChar() {
        return _activeChar;
    }

    public L2ItemInstance getItem() {
        return _item;
    }

    public ItemContainer getContainer() {
        return _container;
    }

    @Override
    public EventType getType() {
        return EventType.ON_PLAYER_ITEM_TRANSFER;
    }
}
