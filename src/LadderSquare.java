
public class LadderSquare extends Square {
    private String color;
    private int step;

    public LadderSquare(String colour, int step) {
        super(step);
        this.color = colour;
        this.step = step;
    }

    public String GetColour() {
        return this.color;
    }

    public int GetStep() {
        return this.step;
    }

    @Override
    public boolean CanEnter(Horse h, Dice dice) {
        // Only the horse with the matching colour can enter.
        if (!h.GetColour().equals(this.color)) {
            return false;
        }

        // A 1-6 roll cannot move a horse up the ladder.
        if (dice.IsOneAndSix()) {
            return false;
        }

        return true;
    }

    @Override
    public void LandOn(Horse h) {
        // No special landing effect on a ladder square.
    }
}
