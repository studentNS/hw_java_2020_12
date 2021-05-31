package hwbanknote;

import java.util.HashMap;
import java.util.Map;

public class ATMImpl implements ATM{

    public Map<BanknoteType, Integer> cashBank = new HashMap<>();
    public int cashBankInt;

    //внести
    public void deposit(Map<BanknoteType, Integer> money){

        int sum = 0;
        for (BanknoteType key : money.keySet()) {
            //обновить или добавить счетчик купюр
            cashBank.put(key, cashBank.getOrDefault(key, 0) + money.get(key));
            sum += key.getTypeValue() * money.get(key);
        }

    }

    //снять
    public Map<BanknoteType, Integer> withdraw(int sum) throws Exception {

        //результат выдачи наличных
        var cash = new HashMap<BanknoteType, Integer>();

        //получаем список всех элементов enum-класса
        for (BanknoteType elementsBanknoteType : BanknoteType.values()) {
            if (sum != 0)
                sum = getCash(sum, elementsBanknoteType, cash);
        }

        if (sum > 0)
            throw new Exception("Не возможно выдать запрашиваемую сумму, так как банкомат не выдает денежные средства в таких купюрах");


        return cash;

    }

    private int getCash(int sum, BanknoteType denomination, Map<BanknoteType, Integer> cash) {

        var countOfNotes = sum / denomination.getTypeValue();
        cashBank.put(denomination, cashBank.getOrDefault(denomination, 0) - countOfNotes);
        cash.put(denomination, countOfNotes);
        int result = sum % denomination.getTypeValue();
        return result;
    }

    //баланс
    public void printBalance(){

        int sum = 0;
        for (Map.Entry<BanknoteType, Integer> entry : cashBank.entrySet()) {
            sum += entry.getKey().getTypeValue() * entry.getValue();
        }
        cashBankInt = sum;
        System.out.println("Остаток денежных средств: " + sum);
    }

}
