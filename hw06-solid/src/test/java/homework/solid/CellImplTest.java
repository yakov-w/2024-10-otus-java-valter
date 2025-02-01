package homework.solid;

import org.junit.jupiter.api.*;

@Order(1)
@SuppressWarnings("java:S5786")
public class CellImplTest {

    private Cell cell;

    @BeforeEach
    void before() {
        this.cell = new CellImpl(1, Banknote.FIFTY, CellSize.LOW);
        cell.add(cell.getSize() - 1);
    }

    @AfterEach
    void after() {
        cell = null;
    }

    @Test
    @DisplayName("в ячейку можно добавить купюры.")
    void testAddBanknotes1() {
        int cont = cell.getCount();
        int banknotes = 1;
        this.cell.add(banknotes);
        Assertions.assertEquals(cell.getCount(), cont + banknotes);
    }

    @Test
    @DisplayName("в ячейку нельзя положить больше купюр чем ее максимальный размер.")
    void testAddBanknotes2() {
        Assertions.assertThrows(RuntimeException.class, () -> this.cell.add(2));
    }

    @Test
    @DisplayName("в ячейку нельзя положить отрицательное количество купюр.")
    void testAddBanknotes3() {
        Assertions.assertThrows(RuntimeException.class, () -> this.cell.add(-1000));
    }

    @Test
    @DisplayName("из ячейки можно взять купюры.")
    void testTakeBanknotes1() {
        int cont = cell.getCount();
        int banknotes = 999;
        this.cell.take(banknotes);
        Assertions.assertEquals(cell.getCount(), cont - banknotes);
    }

    @Test
    @DisplayName("из ячейки нельзя взять больше купюр чем имеется в наличии.")
    void testTakeBanknotes2() {
        Assertions.assertThrows(RuntimeException.class, () -> this.cell.take(1000));
    }

    @Test
    @DisplayName("из ячейки нельзя взять отрицательное количество купюр.")
    void testTakeBanknotes3() {
        Assertions.assertThrows(RuntimeException.class, () -> this.cell.take(-100));
    }
}
