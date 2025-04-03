```cpp
process P {
    Data_t data;
    int taskId;
    string typ;

    while (true) {
        if (tsTryFetch("Ticket ?d ?D", &taskId, &data)) {
            tsRead("Data Data %d ?D", taskid, &data);
            przetworz(taskId, data);
        } else {
            tsFetch("?s ?d ?D", &typ, &taskId, &data);
            if (typ == "Ticket") {
                tsRead("Data Data %d ?D", taskId, &data);
                przetworz(taskId);
            } else {  // Order
                tsPut("Data Data %d %D", taskId, data);
                for (int i=1; i<K; i++) {
                    tsPut("Ticket %d ?D", taskId);
                }
                tsPut("Summary %d %d %d[%d]", taskId, 0, K, tab);
                przetworz(taskId, data);
            }
        }
    }
}

void przetworz(int taskId, Data_t data) {
    long wynik = compute(data);
    int count;
    long tab[K];
    tsFetch("Summary %d ?d %d[?d]", taskId, &count, K, tab);
    tab[count] = wynik;
    count++;
    if(count == K) {
        tab.sort();
        long mediana = tab[K/2];
        tsPut("Order %d %li", orderId, median);
        tsFetch("Data Data %d ?D", taskID, &data);
    } else {
        tsPut("Summary %d %d %d[%d]", taskId, count, K, tab);
    }
}
```