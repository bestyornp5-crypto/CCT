public class Player {
    private String color;
    private Horse[] horse;
    private int numHorse;

    public Player(String color){
        this.numHorse = 4;
        this.color = color;
        this.horse = new Horse[4];

        for ( int i = 0; i < horse.length; i++){
            horse[i] = new Horse(color,i + 1);
        }

    }
    public String GetColour(){
        return this.color;
    }

    public Horse[] GetAllHorse(){
        return this.horse;
    }

    public boolean UWon(){
        // Condition when TopSquare;
        return false;
    }



}
