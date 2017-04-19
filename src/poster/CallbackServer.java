package poster;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import sun.net.httpserver.HttpServerImpl;
import java.util.concurrent.ExecutorService;

/**
 * @author Nikita Zemlevskiy naz7 This class is the server that will handle the
 *         responses from Facebook. It is spun up and killed from
 *         FacebookPoster. The HttpServer Java Library is used. The server will
 *         expect a request from Facebook, and will handle it through the
 *         CallbackHandler.
 */
public class CallbackServer{
	private HttpServer server;
	private ExecutorService threadPool;

	/**
	 * Create a new CallbackServer. A CallbackHandler will be created in order
	 * to handle the request.
	 * 
	 * @param poster
	 *            the Facebook poster that will carry out the post once the
	 *            access code has been received by this server and handled by
	 *            the CallbackHandler.
	 * 
	 * @param redirectURL
	 *            the URL to which Facebook will redirect to. The server will be
	 *            listening on this URL, in order to figure out the access code.
	 */
	public CallbackServer(FacebookPoster poster, String redirectURL) {
		try {
			server = HttpServerImpl.create(new InetSocketAddress(8000), 0);
		} catch (IOException e) {
			poster.finish(false);
		}
		server.createContext("/", new CallbackHandler(poster));
		threadPool = Executors.newFixedThreadPool(1);
		server.setExecutor(this.threadPool);
		server.start();
	}

	/**
	 * Kill this server. Is called from FacebookPoster, once the post has
	 * completed.
	 */
	public void stop() {
		server.stop(1);
		threadPool.shutdownNow();
	}
}
