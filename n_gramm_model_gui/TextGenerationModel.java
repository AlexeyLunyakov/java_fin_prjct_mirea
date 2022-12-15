/*!
\file
\brief Файл содержащий класс генерирующий n-граммную модель
*/
package ru.mirea.fin_prjct.ngramm_model_gui.n_gramm_model_gui;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/// <summary>
///  Класс, который содержит работу с моделью, позволяющую создавать новые тексты
/// </summary>
public class TextGenerationModel {
    /// Приватное поле входного текста
    private String text;
    /// Массив строковых объектов, являющийся поэлементной входной строкой
    private String [] split_text;
    /// Массив выборов, предоставляемых моделью
    private ArrayList<Triple<String, String, String>> ss;
    /// Хеш-таблица слов модели - словарь
    HashMap<String, ArrayList<Tuple>> word_dict;
    /// Сканнер ввода
    private Scanner sc = new Scanner(System.in);
    /// Поле для вызова рандомного элемента словаря модели
    public Random myRandom = new Random();
    /// Функция генерации текста N-граммной модели
    public TextGenerationModel(int choice){
        if (choice == 1) {
            String[] file_names =
                    {
                            "/home/georgechuff/IdeaProjects/final_project/src/Bahchisarajskij_fontan.txt",
                            "/home/georgechuff/IdeaProjects/final_project/src/Iz_dialogov_Andreja_Martynova_s_Paulo_Kojelbo.txt",
                            "/home/georgechuff/IdeaProjects/final_project/src/model.txt",
                            "/home/georgechuff/IdeaProjects/final_project/src/oxxxymiron.txt",
                            "/home/georgechuff/IdeaProjects/final_project/src/Barhatnaja_revoljucija_v_reklame.txt"
                    };
            int i = 1;
            text = "";
            try {
                for (String s : file_names) {
                    BufferedReader reader = new BufferedReader(new FileReader(s));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (text.isEmpty()) {
                            text = line;
                        } else {
                            text = text.concat(line);
                        }
                    }
                    System.out.println(i + ") " + s + " успешно загружен и обработан");
                    i++;
                }
                text = text.replaceAll("[…1234567890!@#%&\\\"•*_.,+=/:;'?><()«»\\—\\–<>\\[\\[]", "");
                text = text.toLowerCase();
                split_text = text.split(" ");
                make_triples();
            } catch (FileNotFoundException e) {
                System.out.println("Ошибка при работе с файлом");
            } catch (IOException e) {
                System.out.println("Ошибка при работе с потоками ввода-вывода");
            }
        }
    }

    /// Метод, который высчитывает вероятность того, что после двух слов последует следующее слово
    private double MLE(String text1, String text2) {
        String text12 = text1 + " " + text2;
        try {
            int occasion1 = (text.length() - text.replaceAll(text12, "").length()) / text12.length();
            int occasion2 = (text.length() - text.replaceAll(text1, "").length()) / text1.length();
            return 100.0 * occasion1 / occasion2;
        } catch (ArithmeticException e) {
            return 0.0;
        }
    }
    /// Функция генерации массивов троек
    private void make_triples(){
        ss = new ArrayList<Triple<String, String, String>>();
        for (int i = 0; i < split_text.length - 2; i++) {
            ss.add(new Triple<String, String, String>(split_text[i], split_text[i + 1], split_text[i + 2]));
        }
    }
    /// Функция тренировки модели на предоставленных текстах
    public void train(int choice) throws IOException {
        if (choice == 1) {
            word_dict = new HashMap<>();
            ArrayList<String> keys = new ArrayList<String>();
            for (Triple<String, String, String> o : ss) {
                String word1 = o.first;
                String word2 = o.second;
                String word3 = o.third;
                if (word1.isEmpty() || word2.isEmpty() || word3.isEmpty()) {
                    continue;
                }
                String word12 = word1 + " " + word2;
                //System.out.println(word12 + " " + word3);
                if (MLE(word12, word3) > 0.0) {
                    ArrayList<Tuple> arr;
                    if (word_dict.containsKey(word12)) {
                        arr = word_dict.get(word12);
                        Boolean is_unique = true;
                        for (int i = 0; i < arr.size() && is_unique; i++){
                            if (Objects.equals(arr.get(i).word, word3)){
                                is_unique = false;
                            }
                        }
                        if (is_unique) {
                            arr.add(new Tuple(word3, MLE(word12, word3)));
                            word_dict.put(word12, arr);
                        }
                    } else {
                        arr = new ArrayList<Tuple>();
                        arr.add(new Tuple(word3, MLE(word12, word3)));
                        word_dict.put(word12, arr);
                    }
                    keys.add(word12);
                }
            }
            ArrayList<Tuple> cur = new ArrayList<Tuple>();
            // указать свой путь
            //try(FileWriter writer = new FileWriter("trained_model/model.txt", false)) // Lunal
            try(FileWriter writer = new FileWriter("trained_model/model.txt", false))
            {
                for (String key : keys){
                    cur = word_dict.get(key);
                    writer.append(key + '\n');
                    for (Tuple curs : cur){
                        writer.append(curs.word + " " + curs.probability + '\n');
                    }
                }
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }
        }
        else if (choice == 2) {
            try
            {
                // переделать строку под свои нужды
                //BufferedReader reader = new BufferedReader(new FileReader("trained_model/model.txt")); // Lunal
                BufferedReader reader = new BufferedReader(new FileReader("trained_model/model.txt"));
                String line;
                String cur_key = "";
                ArrayList<Tuple> cur_arr = new ArrayList<Tuple>();
                word_dict = new HashMap<String, ArrayList<Tuple>>();
                while ((line = reader.readLine()) != null) {
                    if (line.split(" ")[1].matches("^[0-9]*[.][0-9]+$")){
                        cur_arr.add(new Tuple(line.split(" ")[0], Double.parseDouble(line.split(" ")[1])));
                    }
                    else{
                        if (cur_key.length() != 0) {
                            word_dict.put(cur_key, cur_arr);
                            cur_arr = new ArrayList<Tuple>();
                        }
                        cur_key = line;
                    }
                }
                word_dict.put(cur_key, cur_arr);
            }
            catch(IOException ex){
                System.out.println("Ошибка ввода-вывода");
            }
        }
    }
    /// Функция создания словаря
    public HashMap<String, ArrayList<Tuple>> get_word_dict(){
        return word_dict;
    }
}
