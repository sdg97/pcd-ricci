package soph;

/**
 * Dato un link prende i link correlati
 */
public class Tasks implements Runnable {

	private Result result;
	private String myName;
	
	public Tasks(Result result, String name) {
		this.result = result;
		this.myName = name;
	}
	
	@Override
	public void run() {
		log("I'm "+ myName + " execute");
//	    try {
//			//Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	
	private void log(String msg) {
		System.out.println(msg);
	}

}
