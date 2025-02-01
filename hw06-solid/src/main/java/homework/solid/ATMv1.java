package homework.solid;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S1068", "java:S1640"})
public class ATMv1 extends ATMAbstract {

    public ATMv1(List<Cell> cells) {
        super(cells);
    }

    @Override
    public void loadCash(int percent) throws ATMException {
        if (percent < 0 || percent > 100) {
            throw new ATMException("Неверно задан % заполнения ячеек.");
        }
        cells.forEach(cell -> cell.add(cell.getSize() * percent / 100));
    }

    @Override
    public int depositAmount(Deque<Banknote> banknotes) throws ATMException {
        Map<Banknote, Integer> loadedBanknotes = new HashMap<>();
        for (Banknote banknote : banknotes) {
            loadedBanknotes.compute(banknote, (b, i) -> Objects.isNull(i) ? 1 : i + 1);
        }
        checkFreeSpaceInCells(loadedBanknotes);
        return loadBanknotes(loadedBanknotes);
    }

    @Override
    public Deque<Banknote> giveAmount(int sum) throws ATMException {
        if (sum < denomination.last() || sum % denomination.last() != 0) {
            throw new ATMException("Невозможно выдать запрошенную сумму.");
        }
        Deque<Banknote> banknotes = new LinkedList<>();

        Map<Banknote, Integer> banknotes2 = sumToBanknotes(sum);
        checkAvailableBanknotes(banknotes2);
        giveBanknotes(banknotes2).keySet().stream()
                .collect(Collectors.toCollection(() -> banknotes));
        if (banknotes.isEmpty()) {
            throw new ATMException("Случилось что-то страшное и непонятное.");
        }

        return banknotes;
    }

    @Override
    public int getSum() {
        int sum = 0;
        for (Cell cell : cells) {
            sum += cell.getCount() * cell.getBanknote().getValue();
        }
        return sum;
    }
}
