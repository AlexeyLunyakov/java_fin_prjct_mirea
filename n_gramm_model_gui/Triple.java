/*!
\file
\brief Файл содержащий класс Triple
*/
package ru.mirea.fin_prjct.ngramm_model_gui.n_gramm_model_gui;

/// <summary>
///  Массив-тройка (вспомогательный файл)
/// </summary>
public class Triple <E, V, T>{
    public E first;
    public V second;
    public T third;

    public Triple(E first, V second, T third){
        this.first = first;
        this.second = second;
        this.third = third;
    }
}