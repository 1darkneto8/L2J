package org.l2j.gameserver.handler;

import org.l2j.gameserver.enums.InstanceType;
import org.l2j.gameserver.model.L2Object;
import org.l2j.gameserver.model.actor.instance.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface IActionShiftHandler {
    Logger LOGGER = LoggerFactory.getLogger(IActionShiftHandler.class.getName());

    boolean action(Player activeChar, L2Object target, boolean interact);

    InstanceType getInstanceType();
}