public class BrokenVolatileIntCounter {
	private volatile int count;

	/**
	 * 《Java Concurrency in Practice》3.1.4
	 *  "For example, the semantics of volatile are not strong enough to make
	 *  the increment operation(count++) atomic, unless you can guarantee that
	 *  the variable is written only from one single thread."
	 *
	 *  volatile 的使用场景需要满足以下所有条件：
	 *  1. Writes to the variable do not depend on its current value, or you can
	 *     ensure that only a single thread ever updates the value;
	 *  2. The variable does not participate in invariants with other state variables
	 *     and
	 *  3. Locking is not required for any other reason while the variable is being
	 *     accessed.
	 */
	public void increment() {
		count += 1;
	}

	public static void main(String[] args) throws InterruptedException {
		BrokenIntCounter counter = new BrokenIntCounter();
		Thread t1 = new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < 10000; i++) {
				counter.increment();
			}
		});
		Thread t2 = new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < 10000; i++) {
				counter.increment();
			}
		});
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println(counter.count);
		// jdk1.8.0_221.jdk
		// 11418
		
	}
}
