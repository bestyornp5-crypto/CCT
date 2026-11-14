public class Horse {
    public static int LEV_1 = 1;
    public static int LEV_2 = 2;
    public static int LEV_3 = 3;
    public static int LEV_4 = 4;

    private int level;
    private int distanceGone;
    private String color;



    private boolean hasPlacedTrap;

    Square currentSquare;

    public Horse(String color, int level ){
        this.level = level;
        this.color = color;
        this.distanceGone = 0;
        this.currentSquare = null;
        this.hasPlacedTrap = false; // Khởi tạo chưa đặt bẫy
    }

    // --- THÊM CÁC HÀM GETTER/SETTER NÀY VÀO ---
    public boolean HasPlacedTrap() {
        return this.hasPlacedTrap;
    }

    public void SetPlacedTrap(boolean placedTrap) {
        this.hasPlacedTrap = placedTrap;
    }

    public int GetLevel(){
        return this.level;
    }
    public String GetColour(){
        return this.color;
    }

    public int GetDistance(){
        return this.distanceGone;
    }

    public void SetDistance(int distance){
        this.distanceGone = distance;
    }
    public Square GetCurrentSquare(){
        return this.currentSquare;
    }

    public void SetCurrentSquare(Square square){
        this.currentSquare = square;
    }

    public void SendToBase(){
        this.currentSquare = null;
        this.distanceGone = 0;
    }


}
