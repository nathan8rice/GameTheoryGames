import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

/**
* This program plays Nim with you.
*
* @author Nathan Rice
*/
public class Nim
{
  private static Scanner in = new Scanner(System.in);

  private static Random rand = new Random();

  public static void main(String args[])
  {
    System.out.println("\n***NIM***\nHello and Welcome to Nim!!! What is nim you ask? Well, we "+
                       "start off with at least one pile of something; let's use "+
                        "coins. Two players take turns taking coins from the piles (player 1 goes first). "+
                        "The person who takes the last coin wins. Each turn, you "+
                        "can take as many coins as you want, but only from one pile.");
    System.out.println("Would you like to create a game, or would you like me to "+
                        "randomly generate one?");
    System.out.println("Enter:\n"+
                       "(1) Create your own\n"+
                       "(2) Randomly generated");
    int input = getIntInput(1,2);


    //Create game
    int[] game;
    if(input == 1)
    {
      //Let user create custom game
      game = getCustomGame();
      System.out.println();
    }
    else
    {
      //Create randomly generated game
      System.out.println("Choose a level of difficulty:");
      System.out.println("Enter:\n"+
                         "(1) Easy\n"+
                         "(2) Medium\n"+
                         "(3) Hard");
      input = getIntInput(1,3);
      game = getRandomGame(input);
    }
    System.out.println("Alright, here's our game:");
    printGame(game);


    //Determine which player can guarantee a win and let user choose who they want to be
    int nimSum = getNimSum(game);
    if(nimSum == 0)
    {
      System.out.println("It's possible for you to win if you're player 2. Do you want "+
                          "a chance at winning as player 2, or do you want to try and "+
                          "combat mathematical impossibility as player 1?");
    }
    else
    {
      System.out.println("It's possible for you to win if you're player 1. Do you want "+
                          "a chance at winning as player 1, or do you want to try and "+
                          "combat mathematical impossibility as player 2?");
    }
    System.out.println("Enter:\n"+
                       "(1) Player 1 (Goes first)\n"+
                       "(2) Player 2 (Goes second)");
    input = getIntInput(1,2);


    //Play the game
    boolean userIsP1 = true;
    if(input == 2)
    {
      userIsP1 = false;
    }
    boolean p1Turn = false;
    boolean userTurn = true;//Determine's whose turn it is between the user and the computer
    boolean toldYaLoser = false;//Used to tell the user they've lost when they make their first wrong move.
    int computerPile;//Computer's pile choice
    int computerAmount;//Computer's choice for how many coins they want to remove
    int userPile;//User's pile choice
    int userAmount;//User's choice for how many coins they want to remove
    while( !(isEmptyArray(game)) )
    {
      p1Turn = !p1Turn;
      userTurn = (p1Turn && userIsP1) || (!p1Turn && !userIsP1);
      if(userTurn)
      {
        //User makes a move
        System.out.println("Here's the game right now:");
        printGame(game);
        System.out.println("Which pile do you want to remove from?");
        userPile = getIntInput(1,game.length) - 1;
        while(game[userPile] == 0)
        {
          System.out.println("Pile "+(userPile+1)+" is empty. Choose another.");
          userPile = getIntInput(1,game.length) - 1;
        }
        System.out.println("How many coins do you want to remove from pile "+(userPile+1)+"?");
        userAmount = getIntInput(1, game[userPile]);
        game[userPile] -= userAmount;
      }
      else
      {
        //Computer makes a move
        nimSum = getNimSum(game);
        if(!toldYaLoser && (nimSum != 0))
        {
          System.out.println("You just lost lol");
          toldYaLoser = true;
        }
        System.out.println("Here's the game right now:");
        printGame(game);
        if(nimSum == 0)
        {
          //Computer makes random move
          computerPile = rand.nextInt(game.length);
          while(game[computerPile] == 0)
          {
            computerPile = rand.nextInt(game.length);
          }
          computerAmount = rand.nextInt(game[computerPile])+1;
          game[computerPile] -= computerAmount;
          System.out.println("I take "+computerAmount+" away from pile "+(computerPile+1));
        }
        else
        {
          //Computer has won and makes ideal move
          computerPile = nimPile(game, nimSum);
          computerAmount = game[computerPile] - (game[computerPile]^nimSum);
          game[computerPile] -= computerAmount;
          System.out.println("I take "+computerAmount+" away from pile "+(computerPile+1));
        }
      }
    }
    if(userTurn)
    {
      System.out.println("You just made the last move! That means you won!!!");
    }
    else
    {
      System.out.println("I just made the last move. That means you lost :(. Better luck next time!");
    }
  }


  /**
  * Gets the user's input as integer between min and max.
  *
  * @param min  the smallest integer the user can enter
  * @param max  the largest integer the user can enter
  * @return     integer input of the user
  */
  private static int getIntInput(int min, int max)
  {
    boolean done = false;
    int input = 0;
    while(!done)
    {
      if(in.hasNextInt())
      {
        input = in.nextInt();
        if(input <= max && input >= min)
        {
          done = true;
        }
        else
        {
          System.out.println("This integer is not between "+min+" and "+max+". Please try again.");
        }
      }
      else
      {
        System.out.println("This input is not an integer. Please try again.");
        in.next();
      }
    }
    System.out.println();
    return input;
  }


  /**
  * Prompts the user to create a game.
  *
  * @return     game user has created
  */
  private static int[] getCustomGame()
  {
    System.out.println("How many piles do you want?");
    int input = getIntInput(1, Integer.MAX_VALUE);
    if(input > 20){ System.out.println("Oooh this is going to take a while..."); }
    int[] game = new int[input];
    for(int i = 0; i < game.length; i++)
    {
      System.out.println("How many coins do you want in pile "+(i+1)+"?");
      input = getIntInput(1, Integer.MAX_VALUE);
      game[i] = input;
    }
    return game;
  }


  /**
  * Creates randomly generated game
  *
  * @param level  level of difficulty for this random game
  * @return       randomly generated game
  */
  private static int[] getRandomGame(int level)
  {
    int[] game = new int[rand.nextInt(3)+3*level];
    for(int i = 0; i < game.length; i++)
    {
      game[i] = rand.nextInt(5*level)+1;
    }
    return game;
  }


  /**
  * Finds the nim sum of the current status of the game.
  *
  * @param game  game we want to find the nim sum of
  * @return      nim sum of the current game
  */
  private static int getNimSum(int[] game)
  {
    int nimSum = 0;
    for(int i = 0; i < game.length; i++)
    {
      nimSum ^= game[i];
    }
    return nimSum;
  }


  /**
  * Determines whether or not array's values are all 0.
  *
  * @param a    array in question
  * @return     true if all a's values are 0
  */
  private static boolean isEmptyArray(int[] a)
  {
    boolean empty = true;
    for(int i = 0; i < a.length && empty; i++)
    {
      if(a[i] > 0)
      {
        empty = false;
      }
    }
    return empty;
  }


  /**
  * Chooses which pile the computer should choose to take coins from.
  *
  * @param game   current game user and computer are playing
  * @param nimSum nim sum of game
  * @return       pile computer should choose for its turn
  */
  private static int nimPile(int[] game, int nimSum)
  {
    //Position of leftmost bit of nim sum
    int bitPosition = (int)(Math.log(nimSum)/Math.log(2));
    int returnPile = -1;
    for(int i = 0; returnPile < 0 && i < game.length; i++)
    {
      int pile = game[i];
      for(int pow = 0; pow < bitPosition; pow++)
      {
        pile/=2;
      }
      if(pile % 2 == 1)
      {
        returnPile = i;
      }
    }
    return returnPile;
  }


  /**
  * Prints the game as it currently is
  *
  * @param game  the game we want to print
  */
  private static void printGame(int[] game)
  {
    System.out.println("Pile:\t#coins:");
    for(int i = 0; i < game.length; i++)
    {
      System.out.println((i+1)+".\t"+game[i]);
    }
    System.out.println();
  }
}
