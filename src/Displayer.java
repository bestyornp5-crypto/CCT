public class Displayer {

    public Displayer() {
    }

    // Displays the 15x15 matrix board with actual horse positions
    public void ShowBoard(Board board) {
        int size = 15;
        String[][] matrix = new String[size][size];

        // 1. Initialize the entire board with empty cells [ . ]
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = "[ . ]";
            }
        }

        // 2. Set the 4 base camps at the corners (6x6 size)
        fillArea(matrix, 0, 0, 6, 6, "[StB]"); // Blue base
        fillArea(matrix, 0, 9, 6, 15, "[StR]"); // Red base
        fillArea(matrix, 9, 0, 15, 6, "[StY]"); // Yellow base
        fillArea(matrix, 9, 9, 15, 15, "[StG]"); // Green base

        // 3. Set the track lines corresponding to each colour
        for (int i = 1; i <= 6; i++) { matrix[i][7] = "[ B ]"; }
        for (int j = 8; j <= 13; j++) { matrix[7][j] = "[ R ]"; }
        for (int i = 8; i <= 13; i++) { matrix[i][7] = "[ G ]"; }
        for (int j = 1; j <= 6; j++) { matrix[7][j] = "[ Y ]"; }

        // 4. Map the 52 track squares using direct coordinate array lookup
        mapTrackToMatrix(board, matrix);

        // 5. Print the matrix board to the console
        System.out.println("\n=================== LUDO BOARD 15x15 ===================");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println("=========================================================\n");
    }

    // Helper method to fill base camp areas
    private void fillArea(String[][] board, int startRow, int startCol, int endRow, int endCol, String label) {
        for (int i = startRow; i < endRow; i++) {
            for (int j = startCol; j < endCol; j++) {
                board[i][j] = label;
            }
        }
    }

    // Maps the 52 track squares onto the matrix using the coordinate array
    private void mapTrackToMatrix(Board board, String[][] matrix) {
        for (int i = 0; i < 52; i++) {
            Square square = board.GetSquare(i);

            if (square != null) {
                int[] coords = getCoordinatesForSquare(i);
                if (coords != null) {
                    // Ưu tiên 1: Nếu có ngựa đang đứng trên ô này -> Hiển thị ngựa
                    if (!square.IsEmpty()) {
                        Horse horse = square.GetOccupant();
                        String horseSymbol = "[" + horse.GetColour().charAt(0) + horse.GetLevel() + "]";
                        matrix[coords[0]][coords[1]] = horseSymbol;
                    }
                    // Ưu tiên 2: Nếu không có ngựa, nhưng ô này đang có Trap -> Hiển thị Trap (GT, BT, YT, RT)
                    else if (square.HasTrap()) {
                        String trapColorChar = square.GetTrapColour().substring(0, 1).toUpperCase();
                        String trapSymbol = "[" + trapColorChar + "T]"; // Ví dụ: [GT], [RT], [BT], [YT]
                        matrix[coords[0]][coords[1]] = trapSymbol;
                    }
                }
            }
        }
    }

    // Master coordinate array [52][2] mapping the 52 track squares counter-clockwise
    // starting precisely from Red start {7, 13} and matching the cross track layout.
    // Master coordinate array [52][2] mapping the 52 track squares counter-clockwise
    // precisely onto the [ . ] track cells, avoiding the static color labels.
    // Master coordinate array [52][2] mapping the 52 track squares counter-clockwise
    // precisely along the inner cross track surrounding the base camps.
    /*private int[] getCoordinatesForSquare(int index) {
        int[][] trackCoords = {
                // --- RED SEGMENT (Indices 0 to 12 - Starts at Red start {7, 14} going left) ---
                {7, 14}, {7, 13}, {7, 12}, {7, 11}, {7, 10}, {7, 9}, {7, 8},
                {6, 8}, {5, 8}, {4, 8}, {3, 8}, {2, 8}, {1, 8},

                // --- BLUE SEGMENT (Indices 13 to 25) ---
                {0, 7}, {0, 6}, {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6}, {6, 6},
                {6, 5}, {6, 4}, {6, 3}, {6, 2}, {6, 1},

                // --- YELLOW SEGMENT (Indices 26 to 38) ---
                {7, 0}, {8, 1}, {8, 2}, {8, 3}, {8, 4}, {8, 5}, {8, 6}, {9, 6},
                {10, 6}, {11, 6}, {12, 6}, {13, 6}, {14, 6},

                // --- GREEN SEGMENT (Indices 39 to 51 - Chạy xuống cột 7 và vòng sang phải ở hàng 14) ---
                {14, 7}, {14, 8}, {13, 8}, {12, 8}, {11, 8}, {10, 8}, {9, 8},
                {8, 8}, {8, 9}, {8, 10}, {8, 11}, {8, 12}, {8, 13}
        };

        if (index >= 0 && index < trackCoords.length) {
            return trackCoords[index];
        }
        return null;
    } */
    // Bảng tọa độ 52 ô trên bàn cờ 15x15
    private int[] getCoordinatesForSquare(int index) {
        int[][] trackCoords = {
                {7, 14}, {7, 13}, {7, 12}, {7, 11}, {7, 10}, {7, 9}, {7, 8},
                {6, 8}, {5, 8}, {4, 8}, {3, 8}, {2, 8}, {1, 8},
                {0, 7}, {0, 6}, {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6}, {6, 6},
                {6, 5}, {6, 4}, {6, 3}, {6, 2}, {6, 1},
                {7, 0}, {8, 1}, {8, 2}, {8, 3}, {8, 4}, {8, 5}, {8, 6}, {9, 6},
                {10, 6}, {11, 6}, {12, 6}, {13, 6}, {14, 6},
                {14, 7}, {14, 8}, {13, 8}, {12, 8}, {11, 8}, {10, 8}, {9, 8},
                {8, 8}, {8, 9}, {8, 10}, {8, 11}, {8, 12}, {8, 13}
        };

        if (index >= 0 && index < trackCoords.length) {
            return trackCoords[index];
        }
        return null;
    }
}