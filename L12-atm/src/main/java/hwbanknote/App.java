package hwbanknote;

import java.util.Map;

public class App{

        public static void main(String... args) {

            var atm = new ATMImpl();

            atm.deposit(Map.of(BanknoteType.FIVE_THOUSAND, 2, BanknoteType.ONE_THOUSAND,8, BanknoteType.ONE_HUNDRED, 5));
            atm.printBalance();

            //сумма, которую хотим снять
            //int sum = 19100;
            //int sum = 5050;
            int sum = 18100;
            if(atm.cashBankInt < sum){
                System.out.println("Не возможно выдать запрашиваемую сумму, так как в банкомате не достаточно средств");
            }else {
                try {
                    var cash = atm.withdraw(sum);
                    System.out.println(cash.entrySet());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                atm.printBalance();
            }
        }
}
