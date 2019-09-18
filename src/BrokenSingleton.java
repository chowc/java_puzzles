public class BrokenSingleton {

	private static /**volatile**/ BrokenSingleton instance;

	private BrokenSingleton(){}

	static BrokenSingleton getInstance() {
		// 如果 instance 不适用 volatile 进行修饰的话，线程 B 可能看到线程 A 构造的未完成的实例。
		// 因为此时 instance==null 的判断在同步块外，因此不受内存模型可见性影响。
		// 《JCIP》16.2.4
		// "The real problem with DCL is the assumption that the worst thing that can
		// happen when reading a shared object reference without synchronization is to
		// erroneously see a stale value (in this case, null); in that case the DCL idiom
		// compensates for this risk by trying again with the lock held. But the worst case is
		// actually considerably worse—it is possible to see a current value of the reference
		// but stale values for the object’s state, meaning that the object could be seen to be
		// in an invalid or incorrect state"
		if (instance == null) {
			synchronized (BrokenSingleton.class) {
				if (instance == null) {
					instance = new BrokenSingleton();
				}
			}
		}
		return instance;
	}
}