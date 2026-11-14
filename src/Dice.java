// Programmer: Arav
// Dice class: holds the 2 dice for the game and answers questions about the roll
// (total, double, extra turn). The dice values can only change through Roll().

public class Dice{
  // the value showing on each die (1 to 6)
  private int die1;
  private int die2;
  
  // DEVELOPED BY: ARAV
  /* Creates the dice and rolls them once right away, so the dice always
   * hold a real value (1-6) and never a fake value like 0-0 that would
   * count as a double. */
  public Dice(){
    Roll();
  }
  
  // DEVELOPED BY: ARAV
  /* Rolls both dice. Each die gets its own random number from 1 to 6
   * using the Math.random() formula from Lesson 6 (min = 1, max = 6). */
  public void Roll(){
    die1 = (int)(Math.random()*((6-1)+1))+1;
    die2 = (int)(Math.random()*((6-1)+1))+1;
  }
  
  // DEVELOPED BY: ARAV
  /* Gives back what die 1 rolled. Used to show the roll and for
   * ladder step 1, which needs a 1-1.
   * @return - the value of die 1 (1 to 6). */
  public int GetDie1(){
    return die1;
  }
  
  // DEVELOPED BY: ARAV
  /* Gives back what die 2 rolled.
   * @return - the value of die 2 (1 to 6). */
  public int GetDie2(){
    return die2;
  }
  
  // DEVELOPED BY: ARAV
  /* Adds both dice together. This is how far a horse moves.
   * @return - the total of both dice (2 to 12). */
  public int GetTotal(){
    return die1+die2;
  }
  
  // DEVELOPED BY: ARAV
  /* Checks if both dice show the same number (a double).
   * Used for leaving base and climbing the ladder.
   * @return - true if it's a double, false if not. */
  public boolean IsDouble(){
    if(die1 == die2){
      return true;
    }
    else{
      return false;
    }
  }
  
  // DEVELOPED BY: ARAV
  /* Checks if this roll gives the player another turn (any double).
   * Kept as its own method so if the extra turn rule changes,
   * only this method needs to change.
   * @return - true if the player gets another turn, false if not. */
  public boolean GivesExtraTurn(){
    if(IsDouble() || IsOneAndSix()){
      return true;
    }
    else{
      return false;
    }
  }
  // Checks whether the roll allows a horse to leave the base.
  public boolean CanLeaveBase() {

    return IsDouble() || IsOneAndSix();
  }


  // Checks whether the two dice show 1 and 6,
// regardless of their order.
  public boolean IsOneAndSix() {

    return (die1 == 1 && die2 == 6)
            || (die1 == 6 && die2 == 1);
  }
}   