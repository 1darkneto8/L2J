package org.l2j.gameserver.taskmanager;

import org.l2j.commons.threading.ThreadPoolManager;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Summon;
import org.l2j.gameserver.network.serverpackets.AutoAttackStop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import static org.l2j.gameserver.util.GameUtils.isPlayer;


/**
 * Attack stance task manager.
 *
 * @author Luca Baldi, Zoey76
 */
public class AttackStanceTaskManager {
    public static final long COMBAT_TIME = 15_000;
    protected static final Logger LOGGER = LoggerFactory.getLogger(AttackStanceTaskManager.class);
    protected static final Map<Creature, Long> _attackStanceTasks = new ConcurrentHashMap<>();

    /**
     * Instantiates a new attack stance task manager.
     */
    private AttackStanceTaskManager() {
        ThreadPoolManager.scheduleAtFixedRate(new FightModeScheduler(), 0, 1000);
    }

    /**
     * Adds the attack stance task.
     *
     * @param actor the actor
     */
    public void addAttackStanceTask(Creature actor) {
        if (actor != null) {
            _attackStanceTasks.put(actor, System.currentTimeMillis());
        }
    }

    /**
     * Removes the attack stance task.
     *
     * @param actor the actor
     */
    public void removeAttackStanceTask(Creature actor) {
        if (actor != null) {
            if (actor.isSummon()) {
                actor = actor.getActingPlayer();
            }
            _attackStanceTasks.remove(actor);
        }
    }

    /**
     * Checks for attack stance task.<br>
     *
     * @param actor the actor
     * @return {@code true} if the character has an attack stance task, {@code false} otherwise
     */
    public boolean hasAttackStanceTask(Creature actor) {
        if (actor != null) {
            if (actor.isSummon()) {
                actor = actor.getActingPlayer();
            }
            return _attackStanceTasks.containsKey(actor);
        }
        return false;
    }

    public static AttackStanceTaskManager getInstance() {
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        private static final AttackStanceTaskManager INSTANCE = new AttackStanceTaskManager();
    }

    protected class FightModeScheduler implements Runnable {
        @Override
        public void run() {
            final long current = System.currentTimeMillis();
            try {
                final Iterator<Entry<Creature, Long>> iter = _attackStanceTasks.entrySet().iterator();
                Entry<Creature, Long> e;
                Creature actor;
                while (iter.hasNext()) {
                    e = iter.next();
                    if ((current - e.getValue()) > COMBAT_TIME) {
                        actor = e.getKey();
                        if (actor != null) {
                            actor.broadcastPacket(new AutoAttackStop(actor.getObjectId()));
                            actor.getAI().setAutoAttacking(false);
                            if (isPlayer(actor) && actor.hasSummon()) {
                                final Summon pet = actor.getPet();
                                if (pet != null) {
                                    pet.broadcastPacket(new AutoAttackStop(pet.getObjectId()));
                                }
                                actor.getServitors().values().forEach(s -> s.broadcastPacket(new AutoAttackStop(s.getObjectId())));
                            }
                        }
                        iter.remove();
                    }
                }
            } catch (Exception e) {
                // Unless caught here, players remain in attack positions.
                LOGGER.warn("Error in FightModeScheduler: " + e.getMessage(), e);
            }
        }
    }
}
