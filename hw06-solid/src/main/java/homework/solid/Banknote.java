package homework.solid;

public enum Banknote {
    FIVE(5),
    TEN(10),
    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    TWO_THOUSAND(2000),
    FIVE_THOUSAND(5000);

    private final int value;

    Banknote(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
