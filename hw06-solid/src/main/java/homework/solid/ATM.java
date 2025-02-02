package homework.solid;

import java.util.Deque;

public interface ATM {
    /**
     * @param percent
     * @throws ATMException
     */
    void loadCash(int percent) throws ATMException;

    /**
     * Подсчитывает сумму купюр в автомате и возвращает ее.
     *
     * @return
     */
    int getSum();

    /**
     * Добавляет набор купюр в банкомат.
     * Идея в том что пользователь приносит случайный, не сортированный набор банкнот.
     *
     * @param banknotes
     * @return
     * @throws ATMException
     */
    int depositAmount(Deque<Banknote> banknotes) throws ATMException;

    /**
     * Выдает набор купюр клиенту.
     *
     * @param sum
     * @return
     * @throws ATMException
     */
    Deque<Banknote> giveAmount(int sum) throws ATMException;
}
