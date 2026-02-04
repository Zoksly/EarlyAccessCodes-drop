import java.time.LocalDate;

enum CodeStatus
{
    AVAILABLE,
    DROPPED,
    USED
}

public class EarlyAccessCode
{
    private CodeStatus status;
    private String code;
    private String owner = null;
    private String date;
    
    public EarlyAccessCode(String codeValue)
    {
        this.code = codeValue;
        this.status = CodeStatus.AVAILABLE;
        this.owner = null;
        this.date = null;
    }
    
	public void markAsDropped(String playerName)
	{
		this.status = CodeStatus.DROPPED;
		this.owner = playerName;
		this.date = LocalDate.now().toString();
	}

	@Override
	public String toString()
	{
		return String.format("[%s] %s (owner: %s, date: %s)", 
			this.status,
			this.code,
			this.owner != null ? this.owner : "NONE",
			this.date != null ? this.date : "null"
		);
	}

	public boolean isAvailable()
	{
		return this.status == CodeStatus.AVAILABLE;
	}

	public CodeStatus getStatus()
	{
		return this.status;
	}

	public String getCode()
	{
		return this.code;
	}
}