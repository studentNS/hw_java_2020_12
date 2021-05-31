package hwbanknote;

import java.util.Map;

public interface ATM {

    //внести
    void deposit(Map<BanknoteType, Integer> money);
    //снять
    Map<BanknoteType, Integer> withdraw(int sum) throws Exception;
    //баланс
    void printBalance();

}
