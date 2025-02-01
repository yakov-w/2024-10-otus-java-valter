package homework.solid;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Order(2)
@SuppressWarnings("java:S2699")
class ATMv1Test {

    private ATM atm;

    @BeforeEach
    void before() {
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

        atm = new ATMv1(cells);
    }

    @AfterEach
    void after() {
        atm = null;
    }

    @Test
    @DisplayName("заполнение банкомата начальным набором купюр")
    void testLoadCache1() throws ATMException {
        atm.loadCash(99);
    }

    @Test
    @DisplayName("нельзя указать процент заполнения банкомата за пороговыми значениями")
    void testLoadCache2() {
        Stream.of(-1, 101).forEach(i -> Assertions.assertThrows(ATMException.class, () -> atm.loadCash(i)));
    }

    @Test
    @DisplayName("Можно вносить наборы банкнот в банкомат.")
    void testDepositAmount1() throws ATMException {
        Deque<Banknote> depositBanknotes = new LinkedList<>();
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.TWO_THOUSAND);
        depositBanknotes.add(Banknote.TWO_THOUSAND);
        depositBanknotes.add(Banknote.TWO_THOUSAND);
        depositBanknotes.add(Banknote.TWO_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_HUNDRED);
        depositBanknotes.add(Banknote.FIVE_HUNDRED);
        depositBanknotes.add(Banknote.FIVE_HUNDRED);
        depositBanknotes.add(Banknote.FIVE_HUNDRED);
        depositBanknotes.add(Banknote.FIVE_HUNDRED);
        depositBanknotes.add(Banknote.FIVE);
        depositBanknotes.add(Banknote.TEN);
        depositBanknotes.add(Banknote.FIVE);
        depositBanknotes.add(Banknote.FIVE);
        depositBanknotes.add(Banknote.FIVE);
        depositBanknotes.add(Banknote.ONE_HUNDRED);

        atm.depositAmount(depositBanknotes);
    }

    @Test
    @DisplayName("В банкомат нельзя внести средства если нет места хотя бы для одной купюры.")
    void testDepositAmount2() throws ATMException {
        atm.loadCash(99);

        int sumB = atm.getSum();

        Deque<Banknote> depositBanknotes = new LinkedList<>();
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_THOUSAND);
        depositBanknotes.add(Banknote.FIVE_HUNDRED);
        depositBanknotes.add(Banknote.FIVE);
        depositBanknotes.add(Banknote.TEN);
        depositBanknotes.add(Banknote.FIVE);
        depositBanknotes.add(Banknote.FIVE);
        depositBanknotes.add(Banknote.FIVE);
        depositBanknotes.add(Banknote.ONE_HUNDRED);

        Assertions.assertThrows(ATMException.class, () -> atm.depositAmount(depositBanknotes));
        Assertions.assertEquals(sumB, atm.getSum());
    }

    @Test
    @DisplayName("Из банкомата можно снять сумму.")
    void testGiveAmount1() throws ATMException {
        atm.loadCash(99);
        int sumB = atm.getSum();
        int give = 100;
        atm.giveAmount(give);
        Assertions.assertEquals(sumB - give, atm.getSum());
    }

    @Test
    @DisplayName("Из банкомата нельзя снять сумму больше чем в банкомате имеется.")
    void testGiveAmount2() throws ATMException {
        atm.loadCash(1);
        int sumB = atm.getSum();
        int give = 55000;
        Assertions.assertThrows(ATMException.class, () -> atm.giveAmount(give));
        Assertions.assertEquals(sumB, atm.getSum());
    }

    @Test
    @DisplayName("Из банкомата нельзя снять сумму меньше чем минимальный номинал или не кратную ему")
    void testGiveAmount3() throws ATMException {
        atm.loadCash(1);
        int sumB = atm.getSum();
        int give = 3;
        Assertions.assertThrows(ATMException.class, () -> atm.giveAmount(give));
        Assertions.assertEquals(sumB, atm.getSum());
    }
}
