- 关闭一个线程的推荐做法是发送 `interrupt` 信号给线程，所以线程中执行的任务，尤其是循环任务必须
根据业务场景合理的处理当前线程接受的中断信号。

- 提交给线程池的循环任务若不处理(忽略) `interrupt` 信号会导致线程池无法关闭。