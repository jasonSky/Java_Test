import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class test {

	public static void main(String[] args)  {
		ArrayList<Future> al = new ArrayList<Future>();
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 5; i++) {
			threadclass tmp = new threadclass(String.valueOf(i));
//			MyTask tmp = new MyTask();  
			al.add(fixedThreadPool.submit(tmp));
		}
		System.out.println("start all...");
		
		for (Future<String> future : al) {  
				try {  
					// 等待计算结果，最长等待timeout秒，timeout秒后中止任务  
					future.get(3, TimeUnit.SECONDS);  
				}catch (TimeoutException e) {  
					fixedThreadPool.shutdownNow();
					break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		System.out.println("finish main proc....");
    }  
}  

//class MyTask implements Callable<Boolean> {  
//	  
//    @Override  
//    public Boolean call() throws Exception {  
//        // 总计耗时约10秒  
//    	for (int i = 0; i < 10; i++) {
//			try {
//				long begin = System.currentTimeMillis();
//				long t=0;
//				for(int k=0;k<10000;k++){
//					for(int j=0;j<100000;j++){
//						int f = k*j;
//						if(j%2==0){
//							t += f;
//						}else{
//							t -= f;
//						}
//					}
//				}
//				System.out.println(Thread.currentThread() + "---" + String.valueOf(System.currentTimeMillis()-begin));
//			} catch (Exception e) {
//				e.printStackTrace();
////				 Thread.currentThread().interrupt();
////				 System.out.println("关闭"+Thread.currentThread());
////				 break;
//			}
//		}
//        return Boolean.TRUE;  
//    }  
//}  

class threadclass extends Thread {
	String name;
	threadclass(String na){
		name = na;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			long begin = System.currentTimeMillis();
			long t=0;
			for(int k=0;k<10000;k++){
				for(int j=0;j<100000;j++){
					int f = k*j;
					if(j%2==0){
						t += f;
					}else{
						t -= f;
					}
				}
			}
			System.out.println("当前是："+name+"---"+i+"---"+Thread.currentThread() + "---" + String.valueOf(System.currentTimeMillis()-begin));
			
			if (Thread.interrupted()){
					// TODO Auto-generated catch block
					System.out.println("关闭"+Thread.currentThread());
					Thread.currentThread().stop();
//					break;
			}
		}
	}
}