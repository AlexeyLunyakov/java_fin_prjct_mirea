import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GUI extends JFrame {
    private JTextField text;
    Random myRandom = new Random();
    Font font1 = new Font("TimesRoman", Font.PLAIN, 24);
    public GUI(HashMap<String, ArrayList<Tuple>> word_dict){
        setTitle("Т9");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(855, 400);
        setResizable(false);
        setLayout(null);
        /*
        JLabel label = new JLabel("Калькулятор");
        label.setFont(font1);
        label.setBounds(150, 20, 200, 30);
         */

        JTextArea text = new JTextArea("Текст нужно вводить сюда.");
        text.setBounds(40, 20, 620, 210);
        text.setFont(font1);

        JButton[] buttons = new JButton[3];
        buttons[0] = new JButton("-");
        buttons[1] = new JButton("-");
        buttons[2] = new JButton("-");

        JButton accept = new JButton("Подтвердить");
        for (JButton el: buttons){
            el.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!Objects.equals(el.getText(), "-")){
                        text.setText(text.getText() + " " + el.getText());
                    }
                }
            });
        }
        accept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] stxt = text.getText().split(" ");
                System.out.println(stxt[stxt.length - 2] + " " + stxt[stxt.length - 1]);
                ArrayList<Tuple> arr = word_dict.get(stxt[stxt.length - 2] + " " + stxt[stxt.length - 1]);
                if (arr != null && !text.getText().isEmpty()) {
                    if (arr.size() == 1){
                        buttons[1].setText(arr.get(0).word);
                        buttons[0].setText("-");
                        buttons[2].setText("-");
                    }
                    else if (arr.size() == 2) {
                        buttons[0].setText(arr.get(0).word);
                        buttons[1].setText("-");
                        buttons[2].setText(arr.get(1).word);
                    }
                    else if (arr.size() == 3){
                        buttons[0].setText(arr.get(0).word);
                        buttons[1].setText(arr.get(1).word);
                        buttons[2].setText(arr.get(2).word);
                    }
                    else if (arr.size() >= 3){
                        int trichotomy = arr.size() / 3;
                        buttons[0].setText(arr.get(myRandom.nextInt(0, trichotomy - 1)).word);
                        buttons[1].setText(arr.get(myRandom.nextInt(trichotomy, 2 * trichotomy - 1)).word);
                        buttons[2].setText(arr.get(myRandom.nextInt(2 * trichotomy, arr.size() - 1)).word);
                    }
                    else {
                        buttons[0].setText("-");
                        buttons[1].setText("-");
                        buttons[2].setText("-");
                    }
                }
                else{
                    buttons[0].setText("-");
                    buttons[1].setText("-");
                    buttons[2].setText("-");
                }
            }
        });
        for (int i = 0; i < 3; ++i){
            buttons[i].setBounds(40 + (i % 3) * 210, 250, 200, 45);
            buttons[i].setFont(font1);
            add(buttons[i]);
        }
        accept.setBounds(675, 20, 155, 275);
        add(accept);
        add(text);
        setVisible(true);
    }
}
