package homework.solid;

public interface Cell {

    /**
     * С купюрами какого номинала работает ячейка.
     *
     * @return Denomination
     */
    Banknote getBanknote();

    /**
     * Сколько купюр может поместиться в ячейке.
     *
     * @return int - размер ячейки
     */
    int getSize();

    /**
     * Сколько купюр находится в ячейке.
     *
     * @return int - количество купюр в ячейке
     */
    int getCount();

    /**
     * Получение свободного места в ячейке.
     *
     * @return int - количество купюр в ячейке
     */
    int getFree();

    /**
     * Добавляет купюры в ячейку
     *
     * @param count - количество купюр
     */
    void add(int count);

    /**
     * Изымает купюры из ячейки
     *
     * @param count - количество купюр
     */
    int take(int count);
}
