// Programmer: Arav
// Square abstract class: represents a single square on the track or ladder.
// Enforces the rule that a square can hold at most 1 horse and manages traps.

public abstract class Square {
  private int position;
  private Horse occupant; // Holds the single horse currently on this square (or null if empty)
  private String trapColour; // Stores the colour of the trap owner (null if no trap)

  public Square(int position) {
    this.position = position;
    this.occupant = null;
    this.trapColour = null;
  }

  public int GetPosition() {
    return this.position;
  }

  public Horse GetOccupant() {
    return this.occupant;
  }

  public void SetOccupant(Horse h) {
    this.occupant = h;
  }

  public boolean IsEmpty() {
    return this.occupant == null;
  }

  public void RemoveOccupant() {
    this.occupant = null;
  }

  // --- TRAP METHODS ---

  // Checks if there is currently a trap on this square
  public boolean HasTrap() {
    return this.trapColour != null;
  }

  // Sets a trap owned by the given horse's colour (only Level 1 horses can set traps)
  public void SetTrap(Horse h) {
    if (h.GetLevel() == Horse.LEV_1) {
      this.trapColour = h.GetColour();
      System.out.println("TRAP SET! " + h.GetColour() + " Horse (Level 1) placed a trap at square " + this.position + ".");
    }
  }

  // Removes the trap from this square
  public void ClearTrap() {
    this.trapColour = null;
  }

  // Checks and triggers the trap when a horse lands on this square.
  // Returns true if the horse was killed by the trap, false otherwise.
  public boolean SpringTrap(Horse h) {
    if (HasTrap()) {
      // Same colour as the trap owner -> safe, nothing happens
      if (this.trapColour.equals(h.GetColour())) {
        return false;
      }
      // Different colour AND higher level than Level 1 -> killed and sent back to base
      if (h.GetLevel() > Horse.LEV_1) {
        System.out.println("TRAP SPRUNG! " + h.GetColour() + " Horse (Level " + h.GetLevel() +
                ") stepped on " + this.trapColour + "'s trap and was sent back to base!");
        RemoveOccupant();
        h.SendToBase();
        ClearTrap(); // Trap is consumed after springing
        return true;
      }
    }
    return false;
  }

  // --- CAPTURE & MOVEMENT METHODS ---

  // Handles capturing an opponent's horse if present
  public void Capture(Horse h) {
    if (this.occupant != null && this.occupant != h) {
      if (!this.occupant.GetColour().equals(h.GetColour())) {
        System.out.println("CAPTURE! Sending opponent's " + this.occupant.GetColour() +
                " Horse (Level " + this.occupant.GetLevel() + ") back to base.");
        this.occupant.SendToBase();
      }
    }
    this.occupant = h;
    h.SetCurrentSquare(this);
  }
  public String GetTrapColour() {
    return this.trapColour;
  }
  public abstract boolean CanEnter(Horse h, Dice dice);
  public abstract void LandOn(Horse h);
}