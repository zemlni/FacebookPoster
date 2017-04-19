package poster;

import java.util.ResourceBundle;

public class ResourceFinder {
	private ResourceBundle resources;

	public ResourceFinder(String path) {
		resources = ResourceBundle.getBundle(path);
	}

	public String getResource(String name) {
		return resources.getString(name);
	}
}
