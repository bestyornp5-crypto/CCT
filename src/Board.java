public class Board{
  private Square[] track;
  private Square[][] ladders;

  public Board(){
    track = new Square[52];
    ladders = new Square[4][6];
    buildTrack();
    buildLadders();
  }

  private void buildTrack(){
    String[] colours = {"Red", "Blue", "Yellow", "Green"};
    for(int i = 0; i < 52; i++){
      if(i % 13 == 0){
        track[i] = new StartSquare(i, colours[i / 13]);
      }
      else{
        track[i] = new NormalSquare(i);
      }
    }
  }

  private void buildLadders(){
    String[] colours = {"Red", "Blue", "Yellow", "Green"};
    for(int c = 0; c < 4; c++){
      for(int step = 1; step <= 5; step++){
        ladders[c][step - 1] = new LadderSquare(colours[c], step);
      }
      ladders[c][5] = new TopSquare(colours[c]);
    }
  }

  private int colourIndex(String colour){
    if(colour.equals("Red")){ return 0; }
    else if(colour.equals("Blue")){ return 1; }
    else if(colour.equals("Yellow")){ return 2; }
    else if(colour.equals("Green")){ return 3; }
    return -1;
  }

  public Square GetSquare(int position){
    Square result = null;
    if(position >= 0 && position <= 51){
      result = track[position];
    }
    return result;
  }

  public Square GetLadderSquare(String colour, int step){
    int c = colourIndex(colour);
    Square result = null;
    if(c != -1 && step >= 1 && step <= 6){
      result = ladders[c][step - 1];
    }
    return result;
  }

  public Square GetStartSquare(String colour){
    int c = colourIndex(colour);
    Square result = null;
    if(c != -1){
      result = track[c * 13];
    }
    return result;
  }

  // Checks if there are any horses blocking the path along the track
  // Checks if there are any horses blocking the path along the track
  public boolean IsPathBlocked(Horse h, Dice dice) {
    int c = colourIndex(h.GetColour());
    int currentDist = h.GetDistance();
    int total = dice.GetTotal();

    if (currentDist >= 52) {
      return false; // Ladder movement handled separately
    }

    int targetDist = currentDist + total;

    // Check intermediate squares between current distance and target distance
    for (int d = currentDist + 1; d < targetDist; d++) {
      if (d < 52) {
        int pos = (c * 13 + d) % 52;
        if (!track[pos].IsEmpty()) {
          return true; // Blocked by a horse ahead!
        }
      }
    }

    // Check target square
    if (targetDist <= 52) {
      int targetPos = (c * 13 + targetDist) % 52;
      Square targetSquare = track[targetPos];
      if (!targetSquare.IsEmpty()) {
        if (targetSquare.GetOccupant().GetColour().equals(h.GetColour())) {
          return true; // Target square occupied by teammate
        }
      }
    } else {
      int ladderStep = targetDist - 52 + 1;
      if (ladderStep <= 6) {
        Square targetSquare = ladders[c][ladderStep - 1];
        if (!targetSquare.IsEmpty()) {
          if (targetSquare.GetOccupant().GetColour().equals(h.GetColour())) {
            return true; // Target ladder square occupied by teammate
          }
        }
      }
    }

    return false;
  }

  public Square FindTarget(Horse h, Dice dice){
    Square result = null;
    int c = colourIndex(h.GetColour());
    int distance = h.GetDistance();

    // Nếu quân cờ đang ở trong base, đích đến bắt buộc phải là ô xuất phát (StartSquare) với distance = 0
    if(h.GetCurrentSquare() == null){
      result = GetStartSquare(h.GetColour());
    }
    else if(distance >= 52){
      int nextStep = distance - 52 + 1;
      if(nextStep <= 6){
        result = ladders[c][nextStep - 1];
      }
    }
    else{
      int newDistance = distance + dice.GetTotal();
      if(newDistance <= 52){
        int position = (c * 13 + newDistance) % 52;
        result = track[position];
      } else {
        int ladderStep = newDistance - 52 + 1;
        if(ladderStep <= 6){
          result = ladders[c][ladderStep - 1];
        }
      }
    }
    return result;
  }

  public boolean CanMove(Horse h, Dice dice){
    // If the horse is on the board, check if its path is blocked
    if (h.GetCurrentSquare() != null && IsPathBlocked(h, dice)) {
      return false;
    }
    Square target = FindTarget(h, dice);
    if(target == null){
      return false;
    }
    return target.CanEnter(h, dice);
  }

  public void MoveHorse(Horse h, Dice dice) {
    if (CanMove(h, dice)) {
      Square target = FindTarget(h, dice);
      Square oldSquare = h.GetCurrentSquare();
      int newDistance;

      if (oldSquare == null) {
        newDistance = 0; // Vừa ra quân nằm ở ô StartSquare, distance = 0
      } else {
        // ĐÃ XÓA BỎ DÒNG oldSquare.ClearTrap() Ở ĐÂY
        // ĐỂ BẪY VẪN Ở LẠI TRÊN Ô ĐÓ SAU KHI NGỰA RỜI ĐI!

        oldSquare.RemoveOccupant();
        if (h.GetDistance() >= 52) {
          newDistance = h.GetDistance() + 1;
        } else {
          newDistance = h.GetDistance() + dice.GetTotal();
        }
      }

      // SỬA TỪ target.Capture(h) THÀNH target.LandOn(h)
      // ĐỂ KÍCH HOẠT KIỂM TRA BẪY (SpringTrap) VÀ BẮT QUÂN (Capture)
      target.LandOn(h);

      h.SetDistance(newDistance);
    }
  }
}