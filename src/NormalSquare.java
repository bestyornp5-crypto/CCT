public class NormalSquare extends Square {

    public NormalSquare(int position) {
        super(position);
    }

    @Override
    public boolean CanEnter(Horse h, Dice dice) {
        // Cannot enter if occupied by a teammate
        if (!this.IsEmpty()) {
            if (this.GetOccupant().GetColour().equals(h.GetColour())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void LandOn(Horse h) {
        if (!SpringTrap(h)) {
            Capture(h);
        }
    }
}