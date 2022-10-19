package games.stendhal.server.maps.ados.city;

import static org.junit.Assert.*;

import static utilities.SpeakerNPCTestHelper.getReply;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.Outfit;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import utilities.ZonePlayerAndNPCTestImpl;
import utilities.RPClass.ItemTestHelper;

public class MakeupArtistNPCTest extends ZonePlayerAndNPCTestImpl{
	private Player player;
	private SpeakerNPC fidoreaNpc;
	private Engine fidoreaEngine;
	
	public void startConversationTest() throws Exception{
		fidoreaEngine.step(player, ConversationPhrases.GREETING_MESSAGES.get(0));
		assertTrue(fidoreaNpc.isTalking());
		assertEquals("Hi, there. Do you need #help with anything?", getReply(fidoreaNpc));
	}
	public void endConversationTest() throws Exception{
		assertEquals("Thanks, and please don't forget to #return it when you don't need it anymore!", getReply(fidoreaNpc));
		fidoreaEngine.step(player, "return");
		assertEquals("Thank you!", getReply(fidoreaNpc));
		fidoreaEngine.step(player, "bye");
		assertEquals("Bye, come back soon.", getReply(fidoreaNpc));
		assertFalse(fidoreaNpc.isTalking());
	}
	public void startBuyTest() throws Exception{
		fidoreaEngine.step(player, "buy");
		assertTrue(fidoreaNpc.isTalking());
		assertEquals("To buy a mask will cost 20. Do you want to buy it?", getReply(fidoreaNpc));
		fidoreaEngine.step(player, "yes");
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setupZone("test_zone", new MakeupArtistNPC());
	}
	
	@Override
	@Before
	public void setUp() throws Exception{
		player = createPlayer("player");
		fidoreaNpc = SingletonRepository.getNPCList().get("Fidorea");
		fidoreaEngine = fidoreaNpc.getEngine();
		
		final Item item = ItemTestHelper.createItem("money", 50000);
		player.getSlot("bag").add(item);
		
		Outfit original = new Outfit(null, null, null, null, null, null, null, null, null);
		player.setOutfit(original);
		
		startConversationTest();
		startBuyTest();
	}
	
	@After
	public void close()throws Exception{
		endConversationTest();
	}
	@Test
	public void pickMaskBearTest() throws Exception{
		// bear mask == 994
		fidoreaEngine.step(player, "bear");
		assertEquals(player.getOutfit().getLayer("mask"), Integer.valueOf(994));
	}
	@Test
	public void pickMaskFrogTest() throws Exception{
		// frog mask == 995
		fidoreaEngine.step(player, "frog");
		assertEquals(player.getOutfit().getLayer("mask"), Integer.valueOf(995));
	}
	@Test
	public void pickMaskPenguinTest() throws Exception{
		// Penguin mask == 996
		fidoreaEngine.step(player, "Penguin");
		assertEquals(player.getOutfit().getLayer("mask"), Integer.valueOf(996));
	}
	@Test
	public void pickMaskMonkeyTest() throws Exception{
		// Monkey mask == 997
		fidoreaEngine.step(player, "Monkey");
		assertEquals(player.getOutfit().getLayer("mask"), Integer.valueOf(997));
	}
	@Test
	public void pickMaskDogTest() throws Exception{
		// Dog mask == 998
		fidoreaEngine.step(player, "Dog");
		assertEquals(player.getOutfit().getLayer("mask"), Integer.valueOf(998));
	}
	@Test
	public void pickMaskTeddyTest() throws Exception{
		// Teddy mask == 999
		fidoreaEngine.step(player, "Teddy");
		assertEquals(player.getOutfit().getLayer("mask"), Integer.valueOf(999));
	}
	
}
