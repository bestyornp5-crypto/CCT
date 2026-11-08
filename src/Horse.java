public class Horse {
    public static int LEV_1 = 1;
    public static int LEV_2 = 2;
    public static int LEV_3 = 3;
    public static int LEV_4 = 4;

    public static boolean CAN_KICK = true;

    public static boolean CANNOT_KICK = false;

    public static boolean CAN_TRAP = true;
    public static boolean CANNOT_TRAP = false;
    int level;
    String color;



    Square currentSquare;// Wait partner;
    public Horse(String color, int level ){
        this.level = 1;
        this.color = color;

        // I need to calculation to can put down the horse depend on the color


    }
    // My Helper Method for Horse
    private boolean CanKick(int level, int enemyLevel){
        if (level > enemyLevel) {
            return CanKick;
        }
        else {
            return CannotKick;
        }

    }


    // My Public Method for Horse

    public CanMove(int new_step){
        // Instead of using use Dice
        // I roll dice in other class and after I take the step ( 2 - 12 ) of dices to move

    }

    public boolean Attack(Horse enemy){
        // So I will take the enemy by the next square and after that I will attack if the level of each other is correct
        // Nếu như level > can kick (Only)

    }

    public boolean Trap(){
        if (this.level > 1 ){
            return CANNOT_TRAP;
        }
        else {
            // Update the this Square has trap
            // To do that --> the Square should cotain the current locationof this horse

        }
    }

    public int GetLevel(){
        return this.level;
    }


}
