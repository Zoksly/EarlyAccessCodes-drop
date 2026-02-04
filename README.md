# Hytale Code Drop System

A production-ready Java system for dropping rare promotional codes when players kill monsters. Built with clean architecture and fully testable without Hytale SDK.

---

## 📦 What's Included

### Core Components (Production Ready - DO NOT MODIFY)
- `EarlyAccessCode.java` - Data model for promotional codes
- `CodeManager.java` - Manages code collection and file persistence
- `DropSystem.java` - Main orchestration and drop logic
- `IHytalePlayer.java` - Interface defining player contract

### Test Components (For Development Only)
- `FakeHytalePlayer.java` - Mock implementation for testing
- `TestDropSystem.java` - Test suite
- `codes-available.txt` - Sample data file

---

## 🏗️ Architecture Overview

```
DropSystem
    ↓ uses
CodeManager
    ↓ manages
EarlyAccessCode (status: AVAILABLE → DROPPED → USED)
    
DropSystem
    ↓ depends on interface
IHytalePlayer
    ↑ implemented by
RealHytalePlayer (YOU create this)
```

**Key Design:** The system uses `IHytalePlayer` interface instead of Hytale SDK classes directly. This allows:
- Testing without Hytale SDK
- Zero modifications to core code when integrating
- Clean separation of concerns

---

## 📖 Class Descriptions

### `EarlyAccessCode`
Represents a single promotional code with:
- `code` - The actual code string (e.g., "HYTALE-PREMIUM-XYZ")
- `status` - Current state (AVAILABLE, DROPPED, USED)
- `owner` - Player who received it
- `date` - When it was dropped

**Key Methods:**
- `markAsDropped(playerName)` - Updates status when given to player
- `isAvailable()` - Checks if code can be dropped
- `getCode()` - Returns the code string

---

### `CodeManager`
Manages the collection of codes and handles file I/O.

**Key Methods:**
- `loadFromFile(path)` - Loads codes from text file
- `getRandomAvailableCode()` - Returns random available code (or null if none)
- `saveCodeDropped(code)` - Appends dropped code to log file

---

### `DropSystem`
Main system that orchestrates everything.

**Key Methods:**
- `DropSystem(filePath)` - Constructor, loads codes from file
- `onMonsterKilled(IHytalePlayer killer)` - Call this when monster dies
  - 1/1000 chance to drop a code
  - Automatically handles marking, saving, and giving item

**Core Logic:**
1. Random roll (1/1000)
2. If won, get random available code
3. Mark as dropped
4. Save to file
5. Give item to player
6. Send notification

---

### `IHytalePlayer` (Interface)
Contract defining what the drop system needs from a player object.

**Required Methods:**
- `String getPlayerName()` - Returns player's display name
- `void giveItem(String itemId)` - Adds item to player's inventory
- `void sendMessage(String message)` - Sends chat message to player

**Why it exists:** Your `DropSystem` code uses this interface, not Hytale SDK classes. This allows compilation and testing before Hytale integration.

---

## 🔧 Integration Steps

### Step 1: Create Hytale Adapter

Create `RealHytalePlayer.java`:

```java
import com.hytale.api.Player; // Your Hytale SDK import

public class RealHytalePlayer implements IHytalePlayer
{
    private Player player;
    
    public RealHytalePlayer(Player hytalePlayer)
    {
        this.player = hytalePlayer;
    }
    
    @Override
    public String getPlayerName()
    {
        return player.getDisplayName();
        // OR: player.getName() - adjust to your API
    }
    
    @Override
    public void giveItem(String itemId)
    {
        player.getInventory().addItem(itemId);
        // OR: player.giveItem(new ItemStack(itemId)) - adjust to your API
    }
    
    @Override
    public void sendMessage(String message)
    {
        player.sendMessage(message);
        // OR: player.sendChatMessage(message) - adjust to your API
    }
}
```

**Adjust the method calls to match YOUR Hytale SDK API.**

---

### Step 2: Hook into Monster Death Event (10 minutes)

In your event listener class:

```java
public class YourMonsterListener
{
    private DropSystem dropSystem;
    
    // On plugin startup
    public void init()
    {
        try {
            dropSystem = new DropSystem("path/to/codes-available.txt");
        } catch (IOException e) {
            log("Failed to load drop system: " + e.getMessage());
        }
    }
    
    // When monster dies
    @EventHandler
    public void onMonsterDeath(MonsterDeathEvent event)
    {
        Player killer = event.getKiller();
        if (killer == null) return;
        
        try {
            // Wrap Hytale player in our interface
            IHytalePlayer wrappedPlayer = new RealHytalePlayer(killer);
            
            // Trigger drop check
            dropSystem.onMonsterKilled(wrappedPlayer);
            
        } catch (IOException e) {
            log("Drop failed: " + e.getMessage());
        }
    }
}
```

---

### Step 3: Setup Data Files (2 minutes)

Create `codes-available.txt` with one code per line:
```
HYTALE-PREMIUM-ABC123
HYTALE-PREMIUM-XYZ789
HYTALE-PREMIUM-QWE456
```

The system will auto-create `codes-dropped.txt` for logging.

---

## ⚙️ Configuration

### Change Drop Probability

**File:** `DropSystem.java`, line 16

```java
int random = ThreadLocalRandom.current().nextInt(1000); // 1/1000 (0.1%)
```

**Examples:**
- `nextInt(500)` = 1/500 (0.2%)
- `nextInt(100)` = 1/100 (1%)
- `nextInt(10000)` = 1/10000 (0.01%)

---

## ✅ Testing

**Before integration:**
```bash
javac *.java
java TestDropSystem
```

Expected output:
- Codes loaded successfully
- Player created
- 100 monster kills simulated (may drop 0-3 codes due to randomness)
- Check `codes-dropped.txt` for results

**After integration:**
- Kill 1000 monsters → expect ~1 code drop
- Check `codes-dropped.txt` for logged drops
- Verify player receives item and message

---

## 📋 Integration Checklist

- [ ] `RealHytalePlayer.java` created with correct API calls
- [ ] Monster death event hooked to `dropSystem.onMonsterKilled()`
- [ ] `codes-available.txt` populated with real codes
- [ ] File paths configured correctly
- [ ] Tested in dev environment: 1000 kills ≈ 1 drop
- [ ] Error handling verified (IOException catches)
- [ ] Remove test files: `FakeHytalePlayer.java`, `TestDropSystem.java`

---

## 🚨 Important Notes

**Thread Safety:** Uses `ThreadLocalRandom` for thread-safe random generation.

**Error Handling:** Always wrap `onMonsterKilled()` in try-catch to prevent server crashes.

**File Format:** `codes-available.txt` must be one code per line, no formatting.

**Reloading Codes:** Create new `DropSystem` instance: `new DropSystem(path)`

---

## 📞 Support

**Total integration time:** ~15-20 minutes

**Questions?** All core logic is complete and tested. You only need to:
1. Create the adapter class (5 min)
2. Hook the event (10 min)
3. Test (5 min)

**Delivered by:** [Your Name]  
**Date:** 2026-02-04  
**Status:** Production Ready ✅
