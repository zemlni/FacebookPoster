package poster;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javafx.application.Platform;

/**
 * @author Nikita Zemlevskiy naz7 This class handles the callback from Facebook.
 *         Facebook redirects the user to the URL provided in the oauth request,
 *         and this handler is set up to handle that redirect and fetch the
 *         access code given in the URI of the request.
 */
public class CallbackHandler implements HttpHandler {
	private FacebookPoster poster;
	private String code;

	/**
	 * Create a new CallbackHandler.
	 * 
	 * @param poster
	 *            the FacebookPoster that initialized the server. It will be
	 *            used to finish the post once the access code has been
	 *            identified.
	 */
	public CallbackHandler(FacebookPoster poster) {
		this.poster = poster;
	}

	/**
	 * Handle an HttpExchange received by the server. Here the code is fetched
	 * from the URI of the request.
	 * 
	 * @param t
	 *            the exchange that has been received by the server
	 * @throws IOException
	 *             if the request is malformed or if something goes wrong.
	 */
	@Override
	public void handle(HttpExchange t) throws IOException {
		if (code == null) {
			code = t.getRequestURI().toString().split("code=")[1];
			t.close();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					poster.finishPost(code);
				}
			});
		}
	}
}
