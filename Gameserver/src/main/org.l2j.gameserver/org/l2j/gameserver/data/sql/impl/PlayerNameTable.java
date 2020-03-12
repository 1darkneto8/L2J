package org.l2j.gameserver.data.sql.impl;

import io.github.joealisson.primitive.CHashIntIntMap;
import io.github.joealisson.primitive.CHashIntMap;
import io.github.joealisson.primitive.IntIntMap;
import io.github.joealisson.primitive.IntMap;
import org.l2j.gameserver.data.database.dao.CharacterDAO;
import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.settings.GeneralSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static java.util.Objects.nonNull;
import static org.l2j.commons.configuration.Configurator.getSettings;
import static org.l2j.commons.database.DatabaseAccess.getDAO;
import static org.l2j.commons.util.Util.isNullOrEmpty;
import static org.l2j.commons.util.Util.zeroIfNullOrElse;

/**
 * Loads name and access level for all players.
 *
 * @author JoeAlisson
 */
public class PlayerNameTable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerNameTable.class);

    private final IntMap<String> playerData = new CHashIntMap<>();
    private final IntIntMap accessLevels = new CHashIntIntMap();

    private PlayerNameTable() {
        if (getSettings(GeneralSettings.class).cachePlayersName()) {
            loadAll();
        }
    }

    public final void addName(Player player) {
        if (nonNull(player)) {
            addName(player.getObjectId(), player.getName());
            accessLevels.put(player.getObjectId(), player.getAccessLevel().getLevel());
        }
    }

    private void addName(int objectId, String name) {
        if (nonNull(name)) {
            if (!name.equals(playerData.get(objectId))) {
                playerData.put(objectId, name);
            }
        }
    }

    public final void removeName(int objId) {
        playerData.remove(objId);
        accessLevels.remove(objId);
    }

    public final int getIdByName(String name) {
        if (isNullOrEmpty(name)){
            return -1;
        }

        for (var entry : playerData.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(name)) {
                return entry.getKey();
            }
        }

        if (getSettings(GeneralSettings.class).cachePlayersName()) {
            return -1;
        }

        var characterData = getDAO(CharacterDAO.class).findIdAndAccessLevelByName(name);
        if(nonNull(characterData)) {
            playerData.put(characterData.getCharId(), name);
            accessLevels.put(characterData.getCharId(), characterData.getAccessLevel());
            return  characterData.getCharId();
        }

        return -1; // not found
    }

    public final String getNameById(int id) {
        if (id <= 0) {
            return null;
        }

        String name = playerData.get(id);
        if (nonNull(name)) {
            return name;
        }

        if (getSettings(GeneralSettings.class).cachePlayersName()) {
            return null;
        }

        var data = getDAO(CharacterDAO.class).findNameAndAccessLevelById(id);
        if(nonNull(data)) {
            playerData.put(id, data.getName());
            accessLevels.put(id, data.getAccessLevel());
        }
        return null; // not found
    }

    public final int getAccessLevelById(int objectId) {
        return zeroIfNullOrElse(getNameById(objectId), name -> accessLevels.get(objectId));
    }

    public synchronized boolean doesCharNameExist(String name) {
        return getDAO(CharacterDAO.class).existsByName(name);
    }

    public int getAccountCharacterCount(String account) {
        return getDAO(CharacterDAO.class).playerCountByAccount(account);
    }

    public int getClassIdById(int objectId) {
        return getDAO(CharacterDAO.class).findClassIdById(objectId);
    }

    private void loadAll() {
        getDAO(CharacterDAO.class).withPlayersDataDo(resultSet -> {
            try {
                while (resultSet.next()) {
                    final int id = resultSet.getInt("charId");
                    playerData.put(id, resultSet.getString("char_name"));
                    accessLevels.put(id, resultSet.getInt("accesslevel"));
                }
            } catch (SQLException e) {
                LOGGER.warn(getClass().getSimpleName() + ": Couldn't retrieve all char id/name/access: " + e.getMessage(), e);
            }
        });
        LOGGER.info("Loaded {} player names.", playerData.size());
    }

    public static PlayerNameTable getInstance() {
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        private static final PlayerNameTable INSTANCE = new PlayerNameTable();
    }
}
