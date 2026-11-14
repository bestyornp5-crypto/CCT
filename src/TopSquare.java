public class TopSquare extends LadderSquare {

    public TopSquare(String colour) {
        super(colour, 6);
    }

    @Override
    public boolean CanEnter(Horse h, Dice dice) {
        // Chỉ quân cờ đúng màu mới được vào ô đích của màu đó
        return h.GetColour().equals(this.GetColour());
    }

    @Override
    public void LandOn(Horse h) {
        System.out.println("Chúc mừng! Quân cờ màu " + h.GetColour() + " (Level " + h.GetLevel() + ") đã VỀ ĐÍCH!");
    }
}