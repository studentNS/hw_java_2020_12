package hwbanknote;

public enum BanknoteType {

    FIVE_THOUSAND(5000),
    TWO_THOUSAND(2000),
    ONE_THOUSAND(1000),
    FIVE_HUNDRED(500),
    TWO_HUNDRED(200),
    ONE_HUNDRED(100);
    //обратный порядок, чтобы выполнять деление с самой большой

    private final int typeValue;

    BanknoteType(int typeValue) {
        this.typeValue = typeValue;
    }

    public int getTypeValue() {
        return typeValue;
    }
}
