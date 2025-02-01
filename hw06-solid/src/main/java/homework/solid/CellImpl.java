package homework.solid;

@SuppressWarnings({"java:S112", "java:S117"})
public class CellImpl implements Cell {
    private final int uniqueId; // уникальность, допустим реализованы equals и hashcode.
    private final Banknote denomination;
    private final CellSize cellSize;
    private int count = 0; // не хотелось работать с банкнотами как с объектами, достаточно как с количеством.

    public CellImpl(int unique_id, Banknote denomination, CellSize cellSize) {
        this.uniqueId = unique_id;
        this.denomination = denomination;
        this.cellSize = cellSize;
    }

    @Override
    public Banknote getBanknote() {
        return denomination;
    }

    @Override
    public int getSize() {
        return cellSize.getSize();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int getFree() {
        return cellSize.getSize() - count;
    }

    @Override
    public void add(int count) {
        if (count < 0 || this.count + count > cellSize.getSize()) {
            throw new RuntimeException("Нет места в ячейке: " + uniqueId);
        }
        this.count += count;
    }

    @Override
    public int take(int count) {
        if (count < 0 || this.count < count) {
            throw new RuntimeException("Cell ");
        }
        this.count -= count;
        return count;
    }
}
