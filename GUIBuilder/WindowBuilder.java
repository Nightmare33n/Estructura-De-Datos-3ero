import javax.swing.*;
import java.awt.Color;
import helper_classes.*;

public class WindowBuilder {
  public static void main(String[] args) {

     JFrame frame = new JFrame("My Awesome Window");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setSize(792, 428);
     JPanel panel = new JPanel();
     panel.setLayout(null);
     panel.setBackground(Color.decode("#1e1e1e"));

     JButton element1 = new JButton("Click Me");
     element1.setBounds(281, 125, 106, 28);
     element1.setBackground(Color.decode("#2e2e2e"));
     element1.setForeground(Color.decode("#D9D9D9"));
     element1.setFont(CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 14));
     element1.setBorder(new RoundedBorder(4, Color.decode("#979797"), 1));
     element1.setFocusPainted(false);
     OnClickEventHelper.setOnClickColor(element1, Color.decode("#232323"), Color.decode("#2e2e2e"));
     panel.add(element1);

     JLabel element2 = new JLabel("Your Text");
     element2.setBounds(280, 63, 106, 17);
     element2.setFont(CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 14));
     element2.setForeground(Color.decode("#D9D9D9"));
     panel.add(element2);

     frame.add(panel);
     frame.setVisible(true);

  }
}