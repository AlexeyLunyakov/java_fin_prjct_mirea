/*!
\file
\brief Файл содержащий класс the Tuple
*/
package ru.mirea.fin_prjct.ngramm_model_gui.n_gramm_model_gui;

import java.util.HashMap;


/// <summary>
///  Кортеж (вспомогательный файл)
/// </summary>
public class Tuple {//extends JsonSerializer<Tuple> {

    public String word;
    public double probability;

    public Tuple(String word, double probability) {
        this.word = word;
        this.probability = probability;
    }

    @Override
    public String toString() {
        return String.format("(%s:%s)", word, probability);
    }
}
