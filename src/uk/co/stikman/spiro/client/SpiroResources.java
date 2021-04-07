package uk.co.stikman.spiro.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

interface SpiroResources extends ClientBundle {
	public static SpiroResources INSTANCE = GWT.create(SpiroResources.class);

	@Source(value = {"samples.txt"})
	TextResource samples();
}
