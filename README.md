FacebookPoster
===========

This is a simple utility for posting messages and images onto your wall on Facebook. 

Provide the message and image, and log in, and `FacebookPoster` will take care of the rest, including all publishing and OAuth authorization tasks. Users can also pass in a function which will be executed on completion or failure of the post

The example given is a simple application that takes in user input (a string or image), prompts the user to login to Facebook, and publishes the post on their behalf. On completion of the post (or failure) the example shows the user an alert indicating the result of the post. The example can be launched from `Example.java`. 

If you are interested in using `FacebookPoster` for your own project please make your own Facebook app; this one is created only for purposes of the example. Directions for making a Facebook app that is suitable for the requirements of `FacebookPoster` is below. 

Written by Nikita Zemlevskiy (naz7) as part of team DuWaldorf Salad.

## Use
Using `FacebookPoster` in will add another dimension to your project and open up the door to social media. Using it is easy:
1. Instantiate a `FacebookPoster` instance with an `APP_ID` and a `SECRET_KEY` (the app secret). See directions below on how to obtain these.
2. Optionally make a class implementing `FacebookResponse`. This will be called once the post is completed.
3. Call `post`.
4. Run the code and take a look at your Facebook wall!


## API
The `FacebookPoster` API is minimal, which provides for easy use and effortless integration into your project. The calls all done on a `FacebookPoster` instance. The API is as follows:
```
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
	public void post(String message, FacebookResponse)
	
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
	public void post(String message, File image, FacebookResponse response)
```
## Notes
1. This does not give full access to the Graph API. `FacebookPoster` only asks for the `publish_actions` permissions. Extending is easily done with the `RestFB` library.
2. At the moment, the app will only allow you to post if you are listed as a developer for the facebook app as my app has not yet been approved. If you want to be added as a developer for the sake of testing your feature with my app please email me at nikita.zemlevskiy@duke.edu. But you should make your own app to have greater control.

## Instructions to get a Facebook app set up.
1. Make a new Facebook App.
2. Select Facebook Login on the Product Setup menu.
3. Add http://127.0.0.1:8000/test to the Valid OAuth redirect URIs.
4. Turn on OAuth Client and Web login parameters.
5. Go to the app's dashboard and copy the app ID and app secret into the resources in the code.
6. Get the app approved by Facebook by submitting a review request. The permissions you need to submit for review are `publish_actions` and any others you intend to include in your app.


## Advantages
- **OAuth is handled completely.** Don't mess with user tokens and codes. We do it for you!
- **Security.** We don't store any user information and have no access to user login information. Facebook handles all login tasks.
- **Ease of use.** No need to modify your existing code, just drop `FacebookPoster` in and it will add dimensionality to your project. 
