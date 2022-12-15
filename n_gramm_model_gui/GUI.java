/*!
\file
\brief Файл содержащий класс графического интерфейса
*/
package ru.mirea.fin_prjct.ngramm_model_gui.n_gramm_model_gui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/// <summary>
///  Класс реализующий графический интерфейс программы
/// </summary>
public class GUI extends JFrame {
    /// Срока русских символов для экранной клавиатуры
    public static final String st = "йцукенгшщзхъфывапролджэячсмитьбю";
    //static final String st2 = "qwertyuiopasdfghjklzxcvbnm";
    /// Поле для вызова рандомного элемента массива (модель)
    public Random myRandom = new Random();
    /// Буффер для формирования строки при удалении сивмволов или добавления пробела
    public String buffer = "";
    /// Шрифт для окна ввода
    public Font font1 = new Font("TimesRoman", Font.PLAIN, 12);
    /// Шрифт для клавиатуры
    public Font font2 = new Font("TimesRoman", Font.PLAIN, 9);
    public GUI(HashMap<String, ArrayList<Tuple>> word_dict) {

        setTitle("Т9 / N-gram model");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(620, 370);
        setResizable(true);
        setLayout(null);

        JLabel label = new JLabel("Т9 / N-gram model");
        label.setBounds(235, 10, 150, 20);
        add(label);

        // Ввод текста
        final JTextArea text = new JTextArea(10, 100);
        text.setText("Текст нужно вводить сюда.");
        text.setForeground(Color.BLACK);
        text.setBounds( 110, 40, 350, 100);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(font1);
        add(text);

        // Клавиатура экранная
        int n = st.length();
        JButton[] buttonList = new JButton[n];
        for (int i = 0; i < n; i++) {
            buttonList[i] = new JButton( "" + st.charAt(i) );

            //Русская раскладка
            if(i < 12)   buttonList[i].setBounds(10 + (40 + 10)*i, 200, 40, 25);
            else if (i < 23 && i > 12) buttonList[i].setBounds(-30 + (40 + 10)*(i-12), 230, 40, 25);
            else buttonList[i].setBounds(58 + (40 + 10)*(i-23), 260, 40, 25);

            buttonList[i].setFont(font2);
            buttonList[i].setBackground(Color.WHITE);
            add(buttonList[i]);

            int finalI = i;
            buttonList[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buffer = text.getText();
                    buffer += st.charAt(finalI);
                    text.setText("" + buffer);

                }
            });
        }
        // Enter
        JButton accept = new JButton("Enter");
        //accept.setBounds(482, 230, 70, 25); // английская
        accept.setBounds(517, 230, 70, 25);
        accept.setFont(font2);
        accept.setBackground(Color.WHITE);
        add(accept);

        // Пробел
        JButton spaceBar = new JButton(" ");
        spaceBar.setBounds(140, 290, 278, 25);
        spaceBar.setFont(font2);
        spaceBar.setBackground(Color.WHITE);
        add(spaceBar);

        // BackSpace
        JButton backspace = new JButton("BackSpc");
        //backspace.setBounds(420, 260, 80, 25);
        backspace.setBounds(505, 260, 70, 25);
        backspace.setFont(font2);
        backspace.setBackground(Color.WHITE);
        add(backspace);

        JButton[] buttons = new JButton[3];
        buttons[0] = new JButton("-");
        buttons[1] = new JButton("-");
        buttons[2] = new JButton("-");

        for (int i = 0; i < 3; ++i) {
            buttons[i].setBounds(35 + (i % 3) * 170, 160, 150, 25);
            buttons[i].setFont(font1);
            buttons[i].setBackground(Color.WHITE);
            add(buttons[i]);
        }

        for (JButton el : buttons) {
            el.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!Objects.equals(el.getText(), "-")) {
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
                    if (arr.size() == 1) {
                        buttons[1].setText(arr.get(0).word);
                        buttons[0].setText("-");
                        buttons[2].setText("-");
                    } else if (arr.size() == 2) {
                        buttons[0].setText(arr.get(0).word);
                        buttons[1].setText("-");
                        buttons[2].setText(arr.get(1).word);
                    } else if (arr.size() == 3) {
                        buttons[0].setText(arr.get(0).word);
                        buttons[1].setText(arr.get(1).word);
                        buttons[2].setText(arr.get(2).word);
                    } else if (arr.size() > 3) {
                        int trichotomy = arr.size() / 3;
                        int ttrichotomy = arr.size() % 3;
                        buttons[0].setText(arr.get(myRandom.nextInt(0, trichotomy)).word);
                        buttons[1].setText(arr.get(myRandom.nextInt(trichotomy, 2 * trichotomy - ttrichotomy)).word);
                        buttons[2].setText(arr.get(myRandom.nextInt(2 * trichotomy, arr.size() - 1)).word);
                    } else {
                        buttons[0].setText("-");
                        buttons[1].setText("-");
                        buttons[2].setText("-");
                    }
                } else {
                    buttons[0].setText("-");
                    buttons[1].setText("-");
                    buttons[2].setText("-");
                }
            }
        });

        spaceBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buffer = text.getText();
                buffer += " ";
                text.setText("" + buffer);
            }
        });
        backspace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buffer = text.getText();
                buffer = buffer.substring(0,buffer.length()-1);
                text.setText("" + buffer);
            }
        });
        setVisible(true);
    }
}
