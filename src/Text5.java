public class Text5 {

	public static void main(String[] args) {
		for(int i=0;i<100;i++){
			A a = new A(10000+i);
			new Thread(a).start();
		}
	}

}

class A implements Runnable{
	int j = 0;
	public A(int i){
		j = i;
	}
	
	@Override
	public void run() {
		Runtime runtime=Runtime.getRuntime();
		try{
			System.out.println(j);
			runtime.exec("cmd /c adb forward tcp:"+j+" tcp:"+j);
			Thread.sleep(60);
		}catch(Exception e){
			System.out.println("Error!");
		}
	}
	
}