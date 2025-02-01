package homework.solid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@SuppressWarnings("java:S1640")
public class MainApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws ATMException {

        // Подготовка блока ячеек перед установкой в банкомат.
        List<Cell> cells = new ArrayList<>();
        cells.add(new CellImpl(1, Banknote.FIVE, CellSize.LARGE));
        cells.add(new CellImpl(2, Banknote.TEN, CellSize.LARGE));
        cells.add(new CellImpl(3, Banknote.FIVE_THOUSAND, CellSize.LOW));
        cells.add(new CellImpl(4, Banknote.TWO_THOUSAND, CellSize.LOW));
        cells.add(new CellImpl(5, Banknote.FIFTY, CellSize.LARGE));
        cells.add(new CellImpl(6, Banknote.ONE_HUNDRED, CellSize.LARGE));
        cells.add(new CellImpl(7, Banknote.FIVE_HUNDRED, CellSize.MEDIUM));
        cells.add(new CellImpl(8, Banknote.ONE_THOUSAND, CellSize.MEDIUM));
        cells.add(new CellImpl(9, Banknote.TWO_HUNDRED, CellSize.LARGE));
        cells.add(new CellImpl(10, Banknote.FIVE, CellSize.LARGE));

        // Сборка банкомата, установка ячеек
        ATM atm = new ATMv1(cells);
        // Загрузка купюр, по идее банкомат надо в режим обслуживания перевести, но это уже избыточно :)
        atm.loadCash(75);

        LOGGER.info("Пришел юзер1 и начал работу.");
        LOGGER.info("Юзер1 запросил сумму в банкомате {}", atm.getSum());
        int u1Sum = 155;
        Deque<Banknote> u1Banknotes = null;
        try {
            u1Banknotes = atm.giveAmount(u1Sum);
        } catch (ATMException e) {
            LOGGER.info("Юзер1 не получил запрошенную сумму {} в банкомате.", u1Sum);
        }
        if (Objects.isNull(u1Banknotes) || !u1Banknotes.isEmpty()) {
            LOGGER.info("Юзер1 снял сумму {} в банкомате, купюрами: {}", u1Sum, u1Banknotes);
        }
        LOGGER.info("Юзер1 закончил работу, ушел.");

        // Юзер2 откуда-то получил нал... и хочет положить его на счет.
        Deque<Banknote> uBanknotes = new LinkedList<>();
        uBanknotes.add(Banknote.FIVE_THOUSAND);
        uBanknotes.add(Banknote.FIVE_THOUSAND);
        uBanknotes.add(Banknote.FIVE_THOUSAND);
        uBanknotes.add(Banknote.TWO_THOUSAND);
        uBanknotes.add(Banknote.TWO_THOUSAND);
        uBanknotes.add(Banknote.TWO_THOUSAND);
        uBanknotes.add(Banknote.TWO_THOUSAND);
        uBanknotes.add(Banknote.FIVE_HUNDRED);
        uBanknotes.add(Banknote.FIVE_HUNDRED);
        uBanknotes.add(Banknote.FIVE_HUNDRED);
        uBanknotes.add(Banknote.FIVE_HUNDRED);
        uBanknotes.add(Banknote.FIVE_HUNDRED);
        uBanknotes.add(Banknote.FIVE);
        uBanknotes.add(Banknote.TEN);
        uBanknotes.add(Banknote.FIVE);
        uBanknotes.add(Banknote.FIVE);
        uBanknotes.add(Banknote.FIVE);
        uBanknotes.add(Banknote.ONE_HUNDRED);

        LOGGER.info("Пришел юзер2 и начал работу.");
        LOGGER.info("Юзер2 запросил сумму в банкомате {}", atm.getSum());
        LOGGER.info("Юзер2 положил купюры {} в банкомат, на сумму {}", uBanknotes, atm.depositAmount(uBanknotes));
        LOGGER.info("Юзер2 запросил сумму в банкомате {}", atm.getSum());
        LOGGER.info("Юзер2 закончил работу, ушел.");

        LOGGER.info("На конец дня в банкомате банкомате {}", atm.getSum());
    }
}
