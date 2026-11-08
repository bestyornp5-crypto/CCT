public class Player {
    String color;
    Horse horse;
    int numHorseLeft;
    public Player(String color, int level){
        this.horse = new Horse(color, level);

    }
}
