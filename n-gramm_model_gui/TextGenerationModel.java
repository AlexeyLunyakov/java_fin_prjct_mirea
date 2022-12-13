import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class TextGenerationModel {
    private String text;
    private String [] split_text;
    private ArrayList<Triple<String, String, String>> ss;

    private ArrayList<String> cur_answers;
    HashMap<String, ArrayList<Tuple>> word_dict;
    private Scanner sc = new Scanner(System.in);
    Random myRandom = new Random();

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

    // переделать под private
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

    private void make_triples(){
        ss = new ArrayList<Triple<String, String, String>>();
        for (int i = 0; i < split_text.length - 2; i++) {
            ss.add(new Triple<String, String, String>(split_text[i], split_text[i + 1], split_text[i + 2]));
        }
    }

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
            try(FileWriter writer = new FileWriter("/home/georgechuff/IdeaProjects/final_project/src/newHashMap.txt", false))
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
                BufferedReader reader = new BufferedReader(new FileReader("/home/georgechuff/IdeaProjects/final_project/src/newHashMap.txt"));
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
    public HashMap<String, ArrayList<Tuple>> get_word_dict(){
        return word_dict;
    }
    public void build_sequences(String word, String cur_sequence) {

        //String first_word = keys.get(myRandom.nextInt(0, keys.size()) - 1);/
        System.out.println("Current sequence: " + cur_sequence);

        ArrayList<Tuple> arr = word_dict.get(word);
        String cur_word;
        if (arr != null) {
            if (arr.size() == 2) {
                System.out.println("1) " + arr.get(0).word);
                System.out.println("2) " + arr.get(1).word);
                System.out.println("Выберите одно из этих слов: ");
                cur_word = sc.next();
                if (Objects.equals(arr.get(0).word, cur_word) || Objects.equals(arr.get(1).word, cur_word)) {
                    if (word.split(" ").length == 2) {
                        build_sequences(word.split(" ")[1] + " " + cur_word, cur_sequence);
                    } else {
                        build_sequences(word + " " + cur_word, cur_sequence.concat(" " + cur_word));
                    }
                }
            } else if (arr.size() == 1) {
                cur_word = arr.get(0).word;
                System.out.println(cur_word);
                System.out.println("Продолжить выбор? (yes/no)");
                String choice = sc.next();
                if (Objects.equals("yes", choice) || Objects.equals("no", choice)) {
                    if (word.split(" ").length == 2) {
                        build_sequences(word.split(" ")[1] + " " + cur_word, cur_sequence.concat(" " + cur_word));
                    } else {
                        build_sequences(word + " " + cur_word, cur_sequence.concat(" " + cur_word));
                    }
                }
            } else if (arr.size() >= 3) {
                int trichotomy = arr.size() / 3;
                System.out.println("1) " + arr.get(myRandom.nextInt(0, trichotomy - 1)).word);
                System.out.println("2) " + arr.get(myRandom.nextInt(trichotomy, 2 * trichotomy - 1)).word);
                System.out.println("3) " + arr.get(myRandom.nextInt(2 * trichotomy, arr.size() - 1)).word);
                System.out.println("Выберите одно из этих слов: ");
                cur_word = sc.next();
                if (Objects.equals(cur_word, arr.get(0).word) || Objects.equals(arr.get(1).word, cur_word) || Objects.equals(arr.get(2).word, cur_word)) {
                    if (word.split(" ").length == 2) {
                        build_sequences(word.split(" ")[1] + " " + cur_word, cur_sequence.concat(" " + cur_word));
                    } else {
                        build_sequences(word + " " + cur_word, cur_sequence.concat(" " + cur_word));
                    }
                }
            }
        }
    }
}
