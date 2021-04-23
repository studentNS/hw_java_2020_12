package homework;

import java.util.ArrayList;
import java.util.List;

public class Benchmark implements BenchmarkMBean{

    List<Object> memoryFillIntVar = new ArrayList<>();

    public void run() throws InterruptedException {

        while(true){

            for(int i = 0; i <= 1_000_000; i++){
                memoryFillIntVar.add(new Object());
                if (i % 2 == 0){
                    memoryFillIntVar.remove(memoryFillIntVar.size() - 1);

                }
                if (i % 100_000 == 0) Thread.sleep(100);

            }

        }
    }

    @Override
    public int getSize() {
        return memoryFillIntVar.size();
    }

}
