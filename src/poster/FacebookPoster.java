package poster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.WebRequestor;
import com.restfb.types.GraphResponse;

/**
 * @author Nikita Zemlevskiy naz7 This is the class for the Facebook poster
 *         util. This class is the one that actually carries out the posting.
 *         The RestFB Library is used.
 * 
 *         The procedure is as follows. A signin request is sent to the Facebook
 *         server. The user logs in. Facebook then redirects to the redirect URL
 *         with an access code in the body of the request. Then, a request is
 *         made with the app ID and app secret and access code, in order to
 *         obtain a user code. Then, a request is made to Facebook to post on
 *         behalf of the user with the recently obtained user code.
 * 
 *         To receive the requests made by Facebook, a server is spun up, and
 *         killed once the post is completed.
 */
public class FacebookPoster {
	private static final String REDIRECT_URL = "http://127.0.0.1:8000/test";
	private static final String OAUTH_REQUEST_URL = "https://www.facebook.com/v2.9/dialog/oauth?client_id=";
	private Browser browser;
	private Thread serverThread;
	private String message, appId, secretKey;
	private FacebookResponse response;
	private InputStream image;
	private String imageName;

	/**
	 * Create a new Facebook Poster. The appId and secretKey can be found in
	 * your app's dashboard.
	 * 
	 * @param appId
	 *            the app Id of the app that will be posting on the behalf of
	 *            the user.
	 * @param secretKey
	 *            the app secret.
	 */
	public FacebookPoster(String appId, String secretKey) {
		this.appId = appId;
		this.secretKey = secretKey;
	}

	/**
	 * Make a message post on Facebook on behalf of some user with the app that
	 * this FacebookPoster is initialized with.
	 * 
	 * @param message
	 *            the message to be posted.
	 * @param response
	 *            the response that will be executed on completion or failure of
	 *            the post
	 */
	public void post(String message, FacebookResponse response) {
		this.message = message;
		this.response = response;
		browser = new Browser(OAUTH_REQUEST_URL + appId + "&redirect_uri=" + REDIRECT_URL + "&scope=publish_actions");
		startServer();
	}

	/**
	 * Make an image post on Facebook on behalf of some user with the app that
	 * this FacebookPoster is initialized with.
	 * 
	 * @param message
	 *            the message to be posted
	 * @param image
	 *            the image to be posted
	 * @param response
	 *            the response that will be executed on completion or failure of
	 *            the post
	 */
	public void post(String message, File image, FacebookResponse response) {
		try {
			if (image != null) {
				this.image = new FileInputStream(image);
				this.imageName = image.getName();
			}
		} catch (FileNotFoundException e) {
			finish(false);
		}
		post(message, response);
	}

	/**
	 * Post a message once the FacebookClient has been established with the
	 * correct user code.
	 * 
	 * @param facebookClient
	 *            the facebookClient which will be publishing the post on the
	 *            user's behalf.
	 * @return a GraphResponse object containing the response from Facebook
	 *         about the post.
	 */
	private GraphResponse postMessage(FacebookClient facebookClient) {
		return facebookClient.publish("me/feed", GraphResponse.class, Parameter.with("message", message));
	}

	/**
	 * Post an image once the FacebookClient has been established with the
	 * correct user code.
	 * 
	 * @param facebookClient
	 *            the facebookClient which will be publishing the post on the
	 *            user's behalf.
	 * @return a GraphResponse object containing the response from Facebook
	 *         about the post.
	 */
	private GraphResponse postImage(FacebookClient facebookClient) {
		return facebookClient.publish("me/photos", GraphResponse.class, BinaryAttachment.with(imageName, image),
				Parameter.with("message", message));
	}

	/**
	 * Finish posting once the user code has been obtained. This is called from
	 * the server.
	 * 
	 * @param code
	 *            the user code.
	 */
	protected void finishPost(String code) {
		FacebookClient facebookClient = new DefaultFacebookClient(getFacebookUserToken(code), Version.LATEST);
		GraphResponse publishMessageResponse = image == null ? postMessage(facebookClient) : postImage(facebookClient);
		serverThread.stop();
		browser.close();
		finish(true);
	}

	/**
	 * Start the server that will receive the response sent by Facebook in order
	 * to get the user access code.
	 */
	private void startServer() {
		serverThread = new Thread(new CallbackServerWrapper(this, REDIRECT_URL));
		serverThread.start();
	}

	/**
	 * Get the user code. This is called once the access code has been obtained
	 * by the server.
	 * 
	 * @param code
	 *            the access code that has been received from Facebook. It is
	 *            used to get the user code from Facebook.
	 * 
	 * @return the user code obtained from Facebook.
	 */
	private String getFacebookUserToken(String code) {
		WebRequestor wr = new DefaultWebRequestor();
		WebRequestor.Response accessTokenResponse = null;
		try {
			accessTokenResponse = wr.executeGet("https://graph.facebook.com/oauth/access_token?client_id=" + appId
					+ "&redirect_uri=" + REDIRECT_URL + "&client_secret=" + secretKey + "&code=" + code);
		} catch (IOException e) {
			finish(false);
		}
		JsonObject jsonObject = new JsonParser().parse(accessTokenResponse.getBody()).getAsJsonObject();
		return jsonObject.get("access_token").getAsString();
	}

	/**
	 * Carry out the response on completion or failure of the post. The response
	 * is provided above.
	 * 
	 * @param condition
	 *            whether the post was successful or not
	 */
	protected void finish(boolean condition) {
		if (response != null)
			response.doResponse(condition);
	}
}
