import java.util.Scanner;

public class LudoSys {

    private Player[] players;
    private Board board;
    private Dice dice;
    private Displayer displayer;
    private TurnManager turnManager;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public LudoSys() {

        board = new Board();
        dice = new Dice();
        displayer = new Displayer();

        players = new Player[4];

        players[0] = new Player("Red");
        players[1] = new Player("Green");
        players[2] = new Player("Blue");
        players[3] = new Player("Yellow");

        turnManager = new TurnManager(players);
    }


    // ==========================================
    // MAIN GAME LOOP
    // ==========================================

    public void PlayGame() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("========== LUDO GAME ==========");

        while (!turnManager.IsGameOver()) {

            Player currentPlayer = turnManager.StartTurn();

            System.out.println("\n================================");
            System.out.println("PLAYER: " + currentPlayer.GetColour());
            System.out.println("================================");

            // 1. ASK PLAYER TO ROLL

            System.out.println("Press ENTER to roll the dice...");
            scanner.nextLine();

            // 2. ROLL DICE
            dice.Roll();
            turnManager.RegisterDiceRoll(dice);
            System.out.println("\nYour dice result:");
            System.out.println("Die 1: " + dice.GetDie1());
            System.out.println("Die 2: " + dice.GetDie2());
            System.out.println("Total: " + dice.GetTotal());
            if (dice.IsDouble()) {
                System.out.println("DOUBLE! Extra turn granted.");
            }
            if (dice.IsOneAndSix()) {
                System.out.println("SPECIAL ROLL: 1 - 6!");
            }
            // 3. DISPLAY BOARD
            displayer.ShowBoard(board);
            // 4. HANDLE PLAYER DECISION
            HandlePlayerMove(currentPlayer, scanner);
            // 5. DISPLAY UPDATED BOARD
            System.out.println("\nUpdated board:");
            displayer.ShowBoard(board);
            // 5. CHECK WINNER
            if (currentPlayer.UWon()) {
                turnManager.DeclareWinner(currentPlayer);
                System.out.println(
                        currentPlayer.GetColour() + " WINS!"
                );
            }
            // 6. END TURN
            if (!turnManager.IsGameOver()) {
                boolean hasExtraTurn = turnManager.HasExtraTurn();
                turnManager.EndTurn();
                if (hasExtraTurn) {
                    System.out.println("You have another turn!");
                }
            }
        }
        System.out.println("========== GAME OVER ==========");
    }
    // ==========================================
    // PLAYER MOVE DECISION
    // ==========================================
    private void HandlePlayerMove(Player player, Scanner scanner) {
        Horse[] horses = player.GetAllHorse();
        boolean[] baseHorses = new boolean[horses.length];
        boolean[] movableHorses = new boolean[horses.length];
        int baseCount = 0;
        int movableCount = 0;
        boolean hasBlockedHorse = false;
        // Identify horses in the base and horses that can move.
        for (int i = 0; i < horses.length; i++) {
            Horse horse = horses[i];
            if (horse.GetCurrentSquare() == null) {
                if (dice.CanLeaveBase()) {

                    baseHorses[i] = true;
                    baseCount++;

                }
            } else {
                // Special 1-6 roll cannot move a horse
                // that is already on the ladder.
                boolean onLadder = horse.GetDistance() >= 52;
                if (dice.IsOneAndSix() && onLadder) {
                    continue;
                }
                if (board.CanMove(horse, dice)) {
                    movableHorses[i] = true;
                    movableCount++;

                } else if (board.IsPathBlocked(horse, dice)) {
                    hasBlockedHorse = true;
                }
            }
        }


        // ==========================================
        // NO AVAILABLE MOVE
        // ==========================================
        if (baseCount == 0 && movableCount == 0) {
            if (hasBlockedHorse) {
                System.out.println("Invalid distance because there is an opponent/teammate ahead.");
            } else {
                System.out.println("You have no available moves. Turn skipped.");
            }
            return;
        }

        // ==========================================
        // BOTH OPTIONS AVAILABLE
        // ==========================================

        if (baseCount > 0 && movableCount > 0) {

            System.out.println("\nChoose your action:");

            System.out.println("1. Release a horse from base");

            System.out.println("2. Move a horse on the board");

            int choice = ReadChoice(scanner, 1, 2);
            if (choice == 1) {
                SelectHorseFromBase(horses, baseHorses, baseCount, scanner);
            } else {
                SelectHorseToMove(horses, movableHorses, movableCount, scanner);
            }

        }
        // ==========================================
        // ONLY BASE OPTION AVAILABLE
        // ==========================================
        else if (baseCount > 0) {
            System.out.println("\nYou can release a horse from base.");
            SelectHorseFromBase(horses, baseHorses, baseCount, scanner);
        }
        // ==========================================
        // ONLY BOARD OPTION AVAILABLE
        // ==========================================
        else {
            System.out.println("\nYou must move an existing horse.");
            SelectHorseToMove(horses, movableHorses, movableCount, scanner);
        }
    }

    // ==========================================
    // SELECT HORSE FROM BASE
    // ==========================================

    private void SelectHorseFromBase(Horse[] horses, boolean[] available, int count, Scanner scanner) {
        System.out.println("\nHorses available in base:");
        for (int i = 0; i < horses.length; i++) {
            if (available[i]) {
                System.out.println(
                        (i + 1) + ". Horse Level " + horses[i].GetLevel());

            }
        }
        int choice = ReadAvailableHorse(scanner, available, "Choose a horse to release: ");

        Horse selectedHorse = horses[choice];
        board.MoveHorse(selectedHorse, dice);
        System.out.println("Horse Level " + selectedHorse.GetLevel() + " has left the base!");
        // Kiểm tra logic đặt bẫy nếu vừa thả ngựa Level 1 ra ô StartSquare
        CheckAndHandleTrap(selectedHorse, scanner);
    }
    // ==========================================
    // SELECT HORSE TO MOVE
    // ==========================================
    private void SelectHorseToMove(
            Horse[] horses,
            boolean[] available,
            int count,
            Scanner scanner
    ) {
        System.out.println("\nHorses available to move:");
        for (int i = 0; i < horses.length; i++) {
            if (available[i]) {
                System.out.println(
                        (i + 1) + ". Horse Level " +
                                horses[i].GetLevel() +
                                " | Distance: " +
                                horses[i].GetDistance());

            }
        }
        int choice = ReadAvailableHorse(scanner, available, "Choose a horse to move: ");
        Horse selectedHorse = horses[choice];

        board.MoveHorse(selectedHorse, dice);

        System.out.println(
                "Horse Level " + selectedHorse.GetLevel() +
                        " moved successfully!"
        );

        // Kiểm tra logic đặt bẫy sau khi di chuyển
        CheckAndHandleTrap(selectedHorse, scanner);
    }
    // ==========================================
    // CHECK AND HANDLE TRAP (BỔ SUNG)
    // ==========================================
    // ==========================================
    // CHECK AND HANDLE TRAP (ĐÃ SỬA LẠI ĐÚNG YÊU CẦU)
    // ==========================================
    private void CheckAndHandleTrap(Horse horse, Scanner scanner) {
        // Điều kiện để hỏi đặt bẫy:
        // 1. Phải là ngựa Level 1
        // 2. Phải đang đứng trên ô hợp lệ (không phải null)
        // 3. QUAN TRỌNG: Con ngựa này CHƯA TỪNG đặt bẫy trước đó trong cả ván đấu
        if (horse.GetLevel() == 1 && horse.GetCurrentSquare() != null && !horse.HasPlacedTrap()) {
            Square currentSq = horse.GetCurrentSquare();

            // Ô hiện tại phải chưa có bẫy nào của ai đặt
            if (!currentSq.HasTrap()) {
                System.out.println("\n[TRAP SYSTEM] Quan ngua nay la Level 1 va chua dat bay bao gio.");
                System.out.print("Ban co muon dat Trap tai o nay khong? (1: Co, 0: Khong): ");
                int choice = ReadChoice(scanner, 0, 1);

                if (choice == 1) {
                    currentSq.SetTrap(horse);
                    // Đánh dấu là con ngựa này đã dùng quyền đặt bẫy rồi, các lượt sau KHÔNG BAO GIỜ HỎI NỮA
                    horse.SetPlacedTrap(true);
                    System.out.println("Da dat Trap! Quan ngua nay se khong the dat them bay nua.");
                }
            }
        }
    }
    // ==========================================
    // INPUT VALIDATION
    // ==========================================
    private int ReadChoice(Scanner scanner, int min, int max) {

        while (true) {

            System.out.print("Your choice: ");

            String input = scanner.nextLine();

            try {

                int choice = Integer.parseInt(input);

                if (choice >= min && choice <= max) {

                    return choice;

                }

            } catch (NumberFormatException e) {

                // Invalid input. Ask again.

            }

            System.out.println("Invalid choice. Try again.");
        }
    }


    private int ReadAvailableHorse(
            Scanner scanner,
            boolean[] available,
            String message
    ) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine();

            try {

                int choice = Integer.parseInt(input) - 1;

                if (
                        choice >= 0 &&
                                choice < available.length &&
                                available[choice]
                ) {

                    return choice;

                }

            } catch (NumberFormatException e) {

                // Invalid input. Ask again.

            }

            System.out.println(
                    "This horse is not available. Try again."
            );
        }
    }


    // ==========================================
    // GETTERS
    // ==========================================
    public Board GetBoard() {
        return board;
    }
    public Dice GetDice() {
        return dice;
    }
    public Player[] GetPlayers() {
        return players;
    }
    public TurnManager GetTurnManager() {
        return turnManager;
    }
}