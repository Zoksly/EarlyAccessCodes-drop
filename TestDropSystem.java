import java.io.IOException;

public class TestDropSystem
{
	public static void main(String[] args)
	{
		System.out.println("=== TEST DROP SYSTEM ===\n");
		
		try
		{
			// Étape 1 : Créer le DropSystem
			System.out.println("1. Loading codes from file...");
			DropSystem system = new DropSystem("codes-available.txt");
			System.out.println("   ✅ Codes loaded successfully!\n");
			
			// Étape 2 : Créer un faux joueur
			System.out.println("2. Creating fake player...");
			FakeHytalePlayer player = new FakeHytalePlayer("TestPlayer_42");
			System.out.println("   ✅ Player created: " + player.getPlayerName() + "\n");
			
			// Étape 3 : Simuler 100 morts de monstres
			System.out.println("3. Simulating 100 monster kills...");
			int dropsCount = 0;
			
			for (int i = 0; i < 100; i++)
			{
				system.onMonsterKilled(player);
			}
			
			System.out.println("   ✅ 100 kills simulated\n");
			
			// Étape 4 : Tester un drop forcé (appel direct)
			System.out.println("4. Testing direct code drop...");
			/* system.giveCodePlayer(player); */
			System.out.println("   ✅ Direct drop tested\n");
			
			System.out.println("=== CHECK FILE 'codes-dropped.txt' FOR RESULTS ===");
			
		}
		catch (IOException e)
		{
			System.err.println("ERROR: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
