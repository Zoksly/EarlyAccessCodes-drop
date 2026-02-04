import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class CodeManager
{
	private ArrayList<EarlyAccessCode> codes;

	public CodeManager()
	{
		this.codes = new ArrayList<>();
	}

	public void loadFromFile(String filePath) throws IOException
	{
		try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
		{ String line;
			while ((line = br.readLine()) != null)
			{
				this.codes.add(new EarlyAccessCode(line));
			}
		}
	}

	public EarlyAccessCode getRandomAvailableCode()
	{
		ArrayList<EarlyAccessCode> available = new ArrayList<>();
		for (EarlyAccessCode code : codes)
		{
			if (code.isAvailable())
			{
				available.add(code);
			}
		}
		if (available.isEmpty())
		{
			return null;
		}
		Random rand = new Random();
		int index = rand.nextInt(available.size());

		return available.get(index);
	}

	public void saveCodeDropped(EarlyAccessCode code) throws IOException
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("codes-dropped.txt", true))) 
		{
			writer.write(code.toString());
			writer.newLine();
		}
	}
}