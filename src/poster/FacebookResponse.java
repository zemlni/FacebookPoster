package poster;

/**
 * @author Nikita Zemlevskiy naz7 This is an interface which will be passed to
 *         FacebookPoster. The doResponse method will be executed when the post
 *         is completed or if it fails.
 */
public interface FacebookResponse {

	/**
	 * Do the appropriate response given the condition (whether the post to
	 * Facebook was successful or not).
	 * 
	 * @param condition
	 *            whether the post was successful or not
	 */
	void doResponse(boolean condition);
}
