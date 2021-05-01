/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uno;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author acer
 */
public class UnoDeck {
    private UnoCard[] cards;
    private int cardInDeck;
    
    public UnoDeck()
    {        
        
        cards = new UnoCard[108];
        reset();
    }
    
    public void reset() {
    //creates an array of colors to hold the values of the enum Color
    UnoCard.Color[] colors = UnoCard.Color.values();
    //index of the cards array
    cardInDeck = 0;
    //traversing the array of colors. minus one because we are excluding the Wild Color
    for (int i = 0; i < colors.length-1; i++)
    {
      //the card color will be the current index of the card array
      UnoCard.Color color = colors[i];

      // Add 1 zero
      cards[cardInDeck++] = new UnoCard(color, UnoCard.Value.getValue(0));
      // Add 2 cards for 1-9
      for (int j = 1; j < 10; j++)
      {
        cards[cardInDeck++] = new UnoCard(color, UnoCard.Value.getValue(j));
        cards[cardInDeck++] = new UnoCard(color, UnoCard.Value.getValue(j));
      }
      // Add Draw Two, Skip, and Reverse
      UnoCard.Value[] values = new UnoCard.Value[]{UnoCard.Value.DrawTwo, UnoCard.Value.Skip, UnoCard.Value.Reverse};
      for (UnoCard.Value value : values)
      {
        cards[cardInDeck++] = new UnoCard(color, value);
        cards[cardInDeck++] = new UnoCard(color, value);
      }
    }

    // Add Wild Cards: Wild Wild and Wild DrawFour
    UnoCard.Value[] values = new UnoCard.Value[]{UnoCard.Value.Wild, UnoCard.Value.Wild_Four};
    for (UnoCard.Value value : values)
    {
      for (int i = 0; i < 4; i++)
      {
        cards[cardInDeck++] = new UnoCard(UnoCard.Color.Wild, value);
      }
    }
  }
    
    //replace card with arraylist
    public void  replaceDeckWith(ArrayList<UnoCard> cards)
    {
        this.cards = cards.toArray(new UnoCard[cards.size()]);
        this.cardInDeck =this.cards.length;
    }
    
    public boolean isEmpty()
    {
        return cardInDeck==0;
    }
    
    public void shuffleDeck(){
        int n = cards.length;
        Random random = new Random();
        
        for (int i = 0; i < cards.length; i++) {
            int randomValue = i + random.nextInt(n-i);
            UnoCard randomCard = cards[randomValue];
            cards[randomValue] = cards[i];
            cards[i] = randomCard;
        }
    }
    
    public UnoCard drawCard() throws IllegalArgumentException
    {
        if(isEmpty())
        {
            throw new IllegalArgumentException("Cannot draw a card cause there is no card in a deck (drawCard)");
        }
        return  cards[--cardInDeck];
    }
    
    public ImageIcon drawCardImage() throws IllegalArgumentException
    {
        if(isEmpty())
        {
            throw new IllegalArgumentException("Cannot draw a card cause there is no card in a deck (Image)");
        }
        return new ImageIcon(cards[--cardInDeck].toString() + ".png");
    }
    
    //draw many cards
    public UnoCard[] drawCard(int n) throws IllegalArgumentException
    {
        if(n < 0)
        {
            throw new IllegalArgumentException("Must draw positive cards but tried to draw" + n + " cards.");
        }
        if(n > cardInDeck)
        {
            throw new IllegalArgumentException("Cannot draw " + n + " cards since there are only " + cardInDeck + " cards.");
        }
        UnoCard[] ret = new  UnoCard[n];
        
        for (int i = 0; i < n; i++) {
            ret[i] = cards[--cardInDeck];
            
        }
        return ret;
    }
}
