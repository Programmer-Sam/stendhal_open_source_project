package games.stendhal.server.maps.quests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import games.stendhal.common.Direction;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import static utilities.SpeakerNPCTestHelper.getReply;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.semos.wizardstower.WizardsGuardStatueNPC;
import games.stendhal.server.maps.semos.wizardstower.WizardsGuardStatueSpireNPC;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import utilities.PlayerTestHelper;
import utilities.RPClass.ItemTestHelper;

public class ZekielsPracticeTestQuestTest {

	private static final String QUEST_SLOT = "zekiels_practical_test";
	private final StendhalRPZone zone = new StendhalRPZone("admin_test");
	private final StendhalRPZone first = new StendhalRPZone("int_semos_wizards_tower_1");
	private final StendhalRPZone base = new StendhalRPZone("int_semos_wizards_tower_basement");
	
	@Before
	public void setUp() {
		MockStendlRPWorld.get();
		MockStendlRPWorld.get().addRPZone(zone);
		MockStendlRPWorld.get().addRPZone(base);
		MockStendlRPWorld.get().addRPZone(first);
		
		new WizardsGuardStatueNPC().configureZone(zone, null);
		new WizardsGuardStatueSpireNPC().configureZone(zone, null);	
		new ZekielsPracticalTestQuest().addToWorld();	
	}
	
	
	@Test
	public void candlesErasedWhenTestRestarts() {
		Player player = PlayerTestHelper.createPlayerWithOutFit("player");
		SpeakerNPC npc = SingletonRepository.getNPCList().get("Zekiel the guardian");
		Engine en = npc.getEngine();
		
		// Move to step of the quest after collecting items needed to start it
		player.setQuest(QUEST_SLOT, "candles_done");
		
		// Greet Zekiel after not attempting the test
		en.step(player, "hi");
		assertEquals("Greetings, I suppose you came back to #start with the practical test.", getReply(npc));
		assertTrue(npc.isTalking());
		
		// Request to start the test
		assertTrue(en.step(player, "start"));
		assertEquals("First you should #know some important things about the test and the wizards. I will #send you to the first step, if you are ready.", getReply(npc));
		assertTrue(npc.isTalking());
		
		// Actually begin the test
		assertTrue(en.step(player, "send"));
				
		// A dummy candle that is going to be placed on the zone of level 1
		Item candle = ItemTestHelper.createItem("candle");
		// Add this candle to the zone of floor 1
		first.add(candle);
		
		// Check if the number of candles on the ground is equal to 1 as 1 has been placed
		assertEquals(1, first.getItemsOnGround().size());
		
		// Teleport the placer back to Zekiel in the basement so we can restart the test
		player.teleport("int_semos_wizards_tower_basement", 2, 2, Direction.DOWN, player);
		
		// Greet Zekiel to start again
		en.step(player, "hi");
		assertEquals("Greetings! You have so far failed the practical test. Tell me, if you want me to #send you on it again right now, or if there is anything you want to #learn about it first.", getReply(npc));
		assertTrue(npc.isTalking());
		
		// Begin the test for the second time
		assertTrue(en.step(player, "send"));
		
		// Previous candle was dropped should be removed hence the num of candles is 0
		assertEquals(0, first.getItemsOnGround().size());
	}

}