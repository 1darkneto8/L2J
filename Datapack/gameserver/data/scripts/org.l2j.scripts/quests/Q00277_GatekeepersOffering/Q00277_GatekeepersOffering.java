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
package quests.Q00277_GatekeepersOffering;

import org.l2j.gameserver.enums.QuestSound;
import org.l2j.gameserver.model.actor.L2Npc;
import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.model.quest.Quest;
import org.l2j.gameserver.model.quest.QuestState;
import org.l2j.gameserver.model.quest.State;

/**
 * Gatekeeper's Offering (277)
 * @author malyelfik
 */
public class Q00277_GatekeepersOffering extends Quest
{
	// NPC
	private static final int TAMIL = 30576;
	// Monster
	private static final int GREYSTONE_GOLEM = 20333;
	// Items
	private static final int STARSTONE = 1572;
	private static final int GATEKEEPER_CHARM = 1658;
	// Misc
	private static final int MIN_LEVEL = 15;
	private static final int STARSTONE_COUT = 20;
	
	public Q00277_GatekeepersOffering()
	{
		super(277);
		addStartNpc(TAMIL);
		addTalkId(TAMIL);
		addKillId(GREYSTONE_GOLEM);
		registerQuestItems(STARSTONE);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, Player player)
	{
		final QuestState st = getQuestState(player, false);
		if ((st != null) && event.equalsIgnoreCase("30576-03.htm"))
		{
			if (player.getLevel() < MIN_LEVEL)
			{
				return "30576-01.htm";
			}
			st.startQuest();
			return event;
		}
		return null;
	}
	
	@Override
	public String onKill(L2Npc npc, Player killer, boolean isSummon)
	{
		final QuestState st = getQuestState(killer, false);
		if ((st != null) && st.isStarted() && (getQuestItemsCount(killer, STARSTONE) < STARSTONE_COUT))
		{
			giveItems(killer, STARSTONE, 1);
			if (getQuestItemsCount(killer, STARSTONE) >= STARSTONE_COUT)
			{
				st.setCond(2, true);
			}
			else
			{
				playSound(killer, QuestSound.ITEMSOUND_QUEST_ITEMGET);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, Player player)
	{
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		
		switch (st.getState())
		{
			case State.CREATED:
			{
				htmltext = "30576-02.htm";
				break;
			}
			case State.STARTED:
			{
				if (st.isCond(1))
				{
					htmltext = "30576-04.html";
				}
				else if (st.isCond(2) && (getQuestItemsCount(player, STARSTONE) >= STARSTONE_COUT))
				{
					giveItems(player, GATEKEEPER_CHARM, 2);
					st.exitQuest(true, true);
					htmltext = "30576-05.html";
				}
				break;
			}
		}
		return htmltext;
	}
}