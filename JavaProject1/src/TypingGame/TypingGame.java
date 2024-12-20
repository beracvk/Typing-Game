package TypingGame;

import javax.swing.*;

//Oyunun ana sınıfı burada
public class TypingGame {
public static void main(String[] args) {
   SwingUtilities.invokeLater(() -> {
       // Oyunun penceresini oluşturuyor, hazır JFrame var
       JFrame frame = new JFrame("Typing Game");
       GamePanel panel = new GamePanel();
       
       // Ana uygulama penceresi buradan. Java swing komutları
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(666, 666);
       frame.setResizable(false);
       frame.add(panel);
       frame.setVisible(true);
       
       // Oyun panelini başlatmak için
       panel.start();
       });
   }
}
