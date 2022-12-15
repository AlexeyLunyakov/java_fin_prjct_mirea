/*!
\file
\brief В этом файле содаржаится класс Main, с помощью которого запускается программа.
*/
package ru.mirea.fin_prjct.ngramm_model_gui.n_gramm_model_gui;

import java.io.IOException;
import java.util.Scanner;

/// <summary>
///  Класс реализующий запуск программы
/// </summary>
public class Main {
    /// Функция с помощью которой запускается программа (либо обучается модель)
    public static void main(String[] args) throws IOException {
        boolean main = true;
        while(main) {
            System.out.println("> Выберите \n> 'запуск программы (2)' или 'обучение модели (1)' ");
            Scanner sc = new Scanner(System.in);
            if(sc.nextInt() == 2) {
                System.out.println("> Запуск программы.. \n> Для завершения нажмите 'x' в верхнем правом углу..");
                int choice = 2;
                TextGenerationModel tgm = new TextGenerationModel(choice);
                tgm.train(choice);
                GUI menu = new GUI(tgm.get_word_dict());
            }
            else if (sc.nextInt() == 1) {
                System.out.println("> Обучение модели.. ");
                int choice = 1;
                TextGenerationModel tgm = new TextGenerationModel(choice);
                tgm.train(choice);
            }
            else main = false;
        }
    }
}