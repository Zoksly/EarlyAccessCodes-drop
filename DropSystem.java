import java.util.concurrent.ThreadLocalRandom;
import java.io.IOException;

public class DropSystem
{
	private CodeManager manager;
	private static final int WINNING_NB = 27;

	public DropSystem (String filePath) throws IOException
	{
		this.manager = new CodeManager();
		this.manager.loadFromFile(filePath);
	}

	public void onMonsterKilled (IHytalePlayer killer) throws IOException
	{
		int random = ThreadLocalRandom.current().nextInt(1000);
		if (random == WINNING_NB)
		{
			giveCodePlayer(killer);
		}
	}

	public void giveCodePlayer(IHytalePlayer player) throws IOException
	{
		EarlyAccessCode code = manager.getRandomAvailableCode();
		if (code == null)
		{
			return;
		}
		code.markAsDropped(player.getPlayerName());
		manager.saveCodeDropped(code);
		player.giveItem(code.getCode());
		player.sendMessage("You received an Early Access code, GG ! (0.1%) ");
	}
}