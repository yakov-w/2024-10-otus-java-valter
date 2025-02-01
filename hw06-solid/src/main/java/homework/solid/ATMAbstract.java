package homework.solid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@SuppressWarnings("java:S1640")
public abstract class ATMAbstract implements ATM {
    private static final Logger LOGGER = LoggerFactory.getLogger(ATMAbstract.class);
    protected final List<Cell> cells;
    private final Map<Banknote, List<Integer>> blockCellsInfo = HashMap.newHashMap(Banknote.values().length);
    protected static final NavigableSet<Integer> denomination;

    static {
        var tempSet = new TreeSet<Integer>();
        Collections.addAll(
                tempSet, Arrays.stream(Banknote.values()).map(Banknote::getValue).toArray(Integer[]::new));
        denomination = tempSet.reversed();
    }

    protected ATMAbstract(List<Cell> cells) {
        this.cells = cells;
        this.loadBlockCellsInfo();
    }

    private void loadBlockCellsInfo() {
        for (int i = 0; i < cells.size(); i++) {
            List<Integer> list = blockCellsInfo.get(cells.get(i).getBanknote());
            if (Objects.isNull(list)) {
                list = new ArrayList<>();
                blockCellsInfo.put(cells.get(i).getBanknote(), list);
            }
            list.add(i);
        }
    }

    protected int loadBanknotes(Map<Banknote, Integer> banknotes) {
        int sum = 0;
        for (var entry : banknotes.entrySet()) {
            Cell cell = cells.stream()
                    .filter(c -> c.getBanknote().equals(entry.getKey()))
                    .min(Comparator.comparingInt(Cell::getCount))
                    .get();
            cell.add(entry.getValue());
            sum += entry.getKey().getValue() * entry.getValue();
        }
        LOGGER.info("Зачислено средств на сумму {}.", sum);
        return sum;
    }

    protected Map<Banknote, Integer> sumToBanknotes(int sum) {
        Map<Banknote, Integer> banknotes = new HashMap<>();
        for (int v : denomination) {
            final int i = sum / v;
            if (i > 0) {
                Banknote banknote = Arrays.stream(Banknote.values())
                        .filter(p -> p.getValue() == v)
                        .findFirst()
                        .get();
                banknotes.compute(banknote, (b, count) -> Objects.isNull(count) ? i : count + i);
                sum = sum % v;
            }
        }
        return banknotes;
    }

    protected void checkAvailableBanknotes(Map<Banknote, Integer> banknotes) throws ATMException {
        for (Map.Entry<Banknote, Integer> entry : banknotes.entrySet()) {
            Banknote banknote = entry.getKey();
            Integer countB = entry.getValue();
            int count = blockCellsInfo.get(banknote).stream()
                    .map(integer1 -> cells.get(integer1).getCount())
                    .reduce(Integer::sum)
                    .get();
            if (count < countB) {
                throw new ATMException("В банкомате недостаточно средств.");
            }
        }
    }

    protected void checkFreeSpaceInCells(Map<Banknote, Integer> banknotes) throws ATMException {
        Map<Banknote, Integer> freeSpace = HashMap.newHashMap(Banknote.values().length);
        cells.forEach(cell -> freeSpace.compute(cell.getBanknote(), (k, v) -> Objects.isNull(v) ? cell.getFree() : v + cell.getFree()));
        for (var entry : banknotes.entrySet()) {
            if ((freeSpace.get(entry.getKey()) - entry.getValue()) < 0) {
                throw new ATMException("Внос денежных средств недоступен.");
            }
        }
    }

    protected Map<Banknote, Integer> giveBanknotes(Map<Banknote, Integer> banknotes) {
        Map<Banknote, Integer> outBanknotes = new HashMap<>();
        banknotes.forEach((b, i) -> {
            Cell cell = cells.stream()
                    .filter(c -> c.getBanknote().equals(b))
                    .max(Comparator.comparingInt(Cell::getCount))
                    .get();
            outBanknotes.put(b, cell.take(i));
        });
        return outBanknotes;
    }
}
