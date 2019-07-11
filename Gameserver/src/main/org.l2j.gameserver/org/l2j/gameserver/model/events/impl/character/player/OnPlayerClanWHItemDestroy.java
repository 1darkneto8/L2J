package org.l2j.gameserver.model.events.impl.character.player;

import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.IBaseEvent;
import org.l2j.gameserver.model.itemcontainer.ItemContainer;
import org.l2j.gameserver.model.items.instance.L2ItemInstance;

/**
 * @author UnAfraid
 */
public class OnPlayerClanWHItemDestroy implements IBaseEvent {
    private final String _process;
    private final Player _activeChar;
    private final L2ItemInstance _item;
    private final long _count;
    private final ItemContainer _container;

    public OnPlayerClanWHItemDestroy(String process, Player activeChar, L2ItemInstance item, long count, ItemContainer container) {
        _process = process;
        _activeChar = activeChar;
        _item = item;
        _count = count;
        _container = container;
    }

    public String getProcess() {
        return _process;
    }

    public Player getActiveChar() {
        return _activeChar;
    }

    public L2ItemInstance getItem() {
        return _item;
    }

    public long getCount() {
        return _count;
    }

    public ItemContainer getContainer() {
        return _container;
    }

    @Override
    public EventType getType() {
        return EventType.ON_PLAYER_CLAN_WH_ITEM_DESTROY;
    }
}
