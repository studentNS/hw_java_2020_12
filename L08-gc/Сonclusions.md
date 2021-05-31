Наименование        | Память, mb | Области              | Кол-во сборок  | Время, ms
--------------------|------------|----------------------|----------------|-----------
G1                  | 256        | G1 Young Generation  | 30             | 1043  
G1                  | 256        | Old Generation       | 10             | 3514
G1                  | 5120       | G1 Young Generation  | 34             | 8491
G1                  | 5120       | Old Generation       | 2              | 12547
Parallel Collector  | 256        | PS Scavenge          | 6              | 835
Parallel Collector  | 256        | PS MarkSweep         | 5              | 5234
Parallel Collector  | 5120       | PS Scavenge          | 5              | 17037
Parallel Collector  | 5120       | PS MarkSweep         | 2              | 50993
Serial Collector    | 256        | Copy                 | 6              | 604
Serial Collector    | 256        | MarkSweepCompact     | 46             | 36748
Serial Collector    | 5120       | Copy                 | 6              | 13685
Serial Collector    | 5120       | MarkSweepCompact     | 5              | 82920

Получается, что если оценивать эффективность сборщиков мусора по параметру STW (Stop The World), то в данном случае сборщик Serial
будет получше, чем Parallel.
При этом получается, что G1 будет лучше, чем Serial и Parallel, но необходимы большие затраты ресурсов процессора.