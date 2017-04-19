package poster;

import java.util.ResourceBundle;

public class ResourceFinder {
	private ResourceBundle resources;

	public ResourceFinder() {
		resources = ResourceBundle.getBundle("resources/FacebookPoster");
	}

	public String getResource(String name) {
		return resources.getString(name);
	}
}
