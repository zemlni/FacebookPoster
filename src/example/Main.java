package example;

import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import poster.FacebookPoster;
import poster.FacebookResponse;

/**
 * @author Nikita Zemlevskiy naz7. This is an example use of the Facebook
 *         Poster. It simply posts the string "Hello World!" to the wall of the
 *         Facebook user that has logged in.
 * 
 *         You will notice that there are hard coded values representing the app
 *         Id and app secret for the app I created. Please do not use these
 *         outside of this class. It is easy to create an app on Facebook. To
 *         enable it to post to user's walls please follow the instructions in
 *         the README that accompanies this util.
 */
public class Main extends Application implements FacebookResponse {

	/**
	 * CHANGE THESE TO USE YOUR OWN APP
	 */
	private static final String APP_ID = "115239122361401";
	private static final String SECRET_KEY = "5df23edcfec2a0ae080a1f5445e87728";

	private File image;

	/**
	 * Start the example.
	 * 
	 * @param primaryStage
	 *            the stage on which the browser will appear to sign the user in
	 *            on Facebook.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		FacebookPoster poster = new FacebookPoster(primaryStage, APP_ID, SECRET_KEY);
		primaryStage.setScene(setupScene(poster));
		setupStage(primaryStage);
	}

	/**
	 * Setup the stage for the example application
	 * 
	 * @param the
	 *            stage which needs to be setup.
	 */
	private void setupStage(Stage primaryStage) {
		primaryStage.setTitle("Example");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.show();
	}

	/**
	 * Setup the scene for the example application.
	 * 
	 * @param poster
	 *            the Facebook poster that will be activated on the click of the
	 *            Post button
	 * @return the scene that was set up
	 */
	private Scene setupScene(FacebookPoster poster) {
		Label label = new Label("Enter a string and post it to Facebook.");
		label.setWrapText(true);
		TextField textField = new TextField();
		textField.setPromptText("Message");
		VBox vbox = new VBox(10);
		Button imageButton = new Button("Pick Image");
		Button button = new Button("Post");
		button.setOnAction(e -> {
			if (textField.getText() != null)
				poster.post(textField.getText(), image, this);
		});
		vbox.getChildren().addAll(label, textField, imageButton, button);
		vbox.setPadding(new Insets(10));
		Scene scene = new Scene(vbox, 200, 180);
		imageButton.setOnAction(e -> {
			image = pickImage(scene);
		});
		return scene;
	}

	/**
	 * Pick an image to be posted on Facebook from the filesystem. Will be
	 * called if the user clicks the Pick Image button
	 * 
	 * @param scene
	 *            the scene on which the FileChooser will open.
	 * @return the File (image) that was picked by the user.
	 */
	private File pickImage(Scene scene) {
		FileChooser imageChooser = makeFileChooser(System.getProperty("user.dir"), "Images", "*.png", "*.jpg", "*.gif");
		return imageChooser.showOpenDialog(scene.getWindow());
	}

	/**
	 * Make a file chooser that is only able to pick the types of files given in
	 * the arguments.
	 * 
	 * @param path
	 *            the intial path of the FileChooser
	 * @param extensionName
	 *            the name of the type of files being picked
	 * @param types
	 *            the file extensions that are allowed to be picked
	 * @return a FileChooser with these parameters
	 */
	private FileChooser makeFileChooser(String path, String extensionName, String... types) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(path));
		chooser.getExtensionFilters().setAll(new ExtensionFilter(extensionName, types));
		return chooser;
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Included for the implementation of the FacebookResponse Interface. This
	 * simple response shows an alert notifying the user whether the post
	 * succeeded or not. It is run on completion (of fail) of the post attempt.
	 * 
	 * @param condition
	 *            whether or not the post succeeded.
	 */
	@Override
	public void doResponse(boolean condition) {
		Alert alert = new Alert(AlertType.INFORMATION,
				condition ? "Facebook post succeeded!" : "Facebook post failed!");
		alert.show();
	}
}
