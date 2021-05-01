/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uno;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author acer
 */
public class Game {
    private int currentPlayer;
    private String[] ids;
    
    private UnoDeck deck;
    private ArrayList<ArrayList<UnoCard>> playerHand;
    private ArrayList<UnoCard> stockPile;
    
    private UnoCard.Color validColor;
    private UnoCard.Value validValue;
    
    boolean gameDirection;
    
    public Game(String[] ids)
    {
        deck = new UnoDeck();
        deck.shuffleDeck();
        stockPile = new ArrayList<UnoCard>();
        
        this.ids = ids;
        currentPlayer = 1;
        gameDirection=false;
        
        playerHand = new ArrayList<ArrayList<UnoCard>>();
        
        for(int i =0 ; i< ids.length;i++)
        {
            ArrayList<UnoCard> hand = new ArrayList<UnoCard>(Arrays.asList(deck.drawCard(7)));
            playerHand.add(hand);
        }
           
    }
     public void start(Game game)
     {
          UnoCard card = deck.drawCard();
          validColor = card.getColor();
          validValue = card.getValue();
          
          if(card.getValue() == UnoCard.Value.Wild)
          {
              start(game);
          }
          
           if(card.getValue() == UnoCard.Value.Wild_Four)
          {
              start(game);
          }
           
            if(card.getValue() == UnoCard.Value.Skip)
          {
              if(gameDirection==false){
                  currentPlayer = (currentPlayer+1)%ids.length;                  
              }
              else if(gameDirection==true){
                  currentPlayer = (currentPlayer-1)%ids.length;      
                  if(currentPlayer== -1){
                      currentPlayer = ids.length-1;
                  }
              }
          }
         
            if(card.getValue()== UnoCard.Value.Reverse){
                gameDirection ^= true;
                currentPlayer = ids.length-1;
            }
            
        stockPile.add(card);
     }
     
     public UnoCard getTopCard(){
         return new UnoCard(validColor, validValue);
     }
     
     public ImageIcon getTopCardImage()
     {
         return new ImageIcon(validColor+"_"+validValue+".png");
     }
     
     public boolean isGameOver(){
         for(String player : this.ids)
         {
             if(hasEmptyHand(player))
             {
                 return true;
             }       
         }
         return false;
     }
     
     public String getCurrentId()
     {
         return  this.ids[this.currentPlayer];
     }
     
     public String getPreviousPlayer(int i)
     {
        int index = this.currentPlayer -i;
        
        if(index ==-1){
            index = ids.length-1;
        }
        return this.ids[index];
     }
     
     public String[] getPlayer(){
         return ids;
     }
     
     public ArrayList<UnoCard> getPlayerHand(String pids){
         int index = Arrays.asList(ids).indexOf(pids);
         return playerHand.get(index);
     }
     
     public int getPlayerHandSize(String pid)
     {
         return getPlayerHand(pid).size();
     }
     
     public  UnoCard getPlayerCard(String pid,int choice)
     {
         ArrayList<UnoCard> hand = getPlayerHand(pid);
         return hand.get(choice);
     }
     
     public  boolean hasEmptyHand(String pid)
     {
         return getPlayerHand(pid).isEmpty();     
     }
     
     public boolean validCardPlay(UnoCard card)
     {
         return card.getColor() == validColor || card.getValue() == validValue;
     }
     
     public void checkPlayerTurn(String pid) throws InvalidPlayerTurnException
     {
         if(this.ids[this.currentPlayer] != pid)
         {
             throw new InvalidPlayerTurnException("It is not your turn",pid);
         }
     }
     
     public void submitDraws(String pid) throws InvalidPlayerTurnException
     {
         checkPlayerTurn(pid);
         
         if(deck.isEmpty())
         {
             deck.replaceDeckWith(stockPile);
             deck.shuffleDeck();
         }
         
         getPlayerHand(pid).add(deck.drawCard());
         if(gameDirection == false)
         {
             currentPlayer =(currentPlayer+1)%ids.length;
         }
         else if( gameDirection ==true)
         {
             currentPlayer =(currentPlayer-1)%ids.length;
              if(currentPlayer== -1){
                      currentPlayer = ids.length-1;
                  }
         }
     }
     
     public void setCardColor(UnoCard.Color color)
     {
         validColor =color;
     }
     
     public void submitPlayerCard(String pid,UnoCard card,UnoCard.Color declaredColor)
     throws InvalidColorSubmissionException,InvalidValueSubmissionException,InvalidPlayerTurnException
     {
         checkPlayerTurn(pid);
         
         ArrayList<UnoCard> pHand = getPlayerHand(pid);
         
         if(!validCardPlay(card))
         {
             if(card.getColor() == UnoCard.Color.Wild)
             {
                 validColor = card.getColor();
                 validValue = card.getValue();
             }
             if(card.getColor() != validColor)
             {
                 throw new InvalidColorSubmissionException("Invalid Color!!", card.getColor(), validColor);
             }
             else if(card.getValue() != validValue)
             {
                 throw new InvalidValueSubmissionException("Invalid Value!!", card.getValue(), validValue);
             }
         }
         pHand.remove(card);
         
         if(hasEmptyHand(this.ids[currentPlayer])){
             System.exit(0);
         }
         
         validColor = card.getColor();
         validValue = card.getValue();
         stockPile.add(card); 
         
          if(gameDirection==false){
                  currentPlayer = (currentPlayer+1)%ids.length;                  
              }
          else if(gameDirection==true){
                  currentPlayer = (currentPlayer-1)%ids.length;      
                  if(currentPlayer== -1){
                      currentPlayer = ids.length-1;
                  }
              }
          if (card.getColor() == UnoCard.Color.Wild)
          {
              validColor = declaredColor;
          }
          
          if (card.getValue() == UnoCard.Value.DrawTwo)
          {
              pid = ids[currentPlayer];
              getPlayerHand(pid).add(deck.drawCard());
              getPlayerHand(pid).add(deck.drawCard());
          }
          
          if (card.getValue() == UnoCard.Value.Wild_Four)
          {
              pid = ids[currentPlayer];
              getPlayerHand(pid).add(deck.drawCard());
              getPlayerHand(pid).add(deck.drawCard());
              getPlayerHand(pid).add(deck.drawCard());
              getPlayerHand(pid).add(deck.drawCard());
          }
          
          if(card.getValue()== UnoCard.Value.Skip)
          {
              if(gameDirection==false){
                  currentPlayer = (currentPlayer+1)%ids.length;                  
              }
          else if(gameDirection==true){
                  currentPlayer = (currentPlayer-1)%ids.length;      
                  if(currentPlayer== -1){
                      currentPlayer = ids.length-1;
                  }
              }
          }
          
          if(card.getValue() == UnoCard.Value.Reverse)
          {
              JLabel message = new JLabel (pid+"changed the direction!");
              message.setFont(new Font("Hug me tight",Font.PLAIN,48));
              JOptionPane.showMessageDialog(null, message);
              gameDirection ^= true;
              if(gameDirection==true){
                  currentPlayer = (currentPlayer-2)%ids.length;
                  if(currentPlayer== -1){
                      currentPlayer = ids.length-1;
                  }   
                  if(currentPlayer== -2){
                      currentPlayer = ids.length-2;
                  } 
              }
          else if(gameDirection==true){
                  currentPlayer = (currentPlayer+2)%ids.length;      
                  
              }
          }
     }
}
class InvalidPlayerTurnException extends Exception
{
    String ids;

    public InvalidPlayerTurnException(String message,String pid) {
       super(message);
       ids = pid;
    }
    
    public String getPid()
    {
        return ids;
    }
    
}

class InvalidColorSubmissionException extends Exception
{
    private UnoCard.Color expected;
    private UnoCard.Color actual;
    
    public InvalidColorSubmissionException(String message,UnoCard.Color actual,UnoCard.Color expected )
    {
        this.actual =actual;
        this.expected=expected;
        
    }
}

class InvalidValueSubmissionException extends Exception
{
    private UnoCard.Value expected;
    private UnoCard.Value actual;
    
    public InvalidValueSubmissionException(String message,UnoCard.Value actual,UnoCard.Value expected )
    {
        this.actual =actual;
        this.expected=expected;
        
    }
}
