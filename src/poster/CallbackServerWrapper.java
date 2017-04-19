package poster;

public class CallbackServerWrapper implements Runnable{
	private FacebookPoster poster;
	private String redirectURL;
	public CallbackServerWrapper(FacebookPoster poster, String redirectURL){
		this.poster = poster;
		this.redirectURL = redirectURL;
	}
	@Override
	public void run() {
		System.out.println("creating server wrapper " + poster);

		CallbackServer server = new CallbackServer(poster, redirectURL);
	}
	
}
