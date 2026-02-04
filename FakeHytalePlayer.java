// Classe FAKE pour tester sans l'API Hytale
// Le head dev créera RealHytalePlayer avec la vraie API
public class FakeHytalePlayer implements IHytalePlayer
{
	private String name;

	// Constructeur : on crée un faux joueur avec juste un nom
	public FakeHytalePlayer(String playerName)
	{
		this.name = playerName;
	}

	// Implémentation 1 : Retourner le nom (simple)
	@Override
	public String getPlayerName()
	{
		return this.name;
	}

	// Implémentation 2 : Simuler le don d'item (juste un print)
	@Override
	public void giveItem(String itemId)
	{
		System.out.println("[FAKE] Gave item '" + itemId + "' to " + this.name);
	}

	// Implémentation 3 : Simuler l'envoi de message (juste un print)
	@Override
	public void sendMessage(String message)
	{
		System.out.println("[FAKE] Message to " + this.name + ": " + message);
	}
}
