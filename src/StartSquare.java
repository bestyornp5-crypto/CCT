public class StartSquare extends Square {
    private String colour;

    public StartSquare(int position, String colour) {
        super(position);
        this.colour = colour;
    }

    public String GetColour() {
        return this.colour;
    }

    @Override
    public boolean CanEnter(Horse h, Dice dice) {

        // A horse in the base can only leave
        // when the dice satisfy the release condition.
        if (h.GetCurrentSquare() == null) {

            return dice.CanLeaveBase();

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