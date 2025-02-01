package homework.solid;

public enum CellSize {
    LOW(1000),
    MEDIUM(2000),
    LARGE(3000);

    private final int size;

    CellSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
