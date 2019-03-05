package org.ws2ten1.loggings;

import lombok.experimental.UtilityClass;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Slf4j log markers.
 */
@UtilityClass
public class Markers {
	
	/**
	 * Alert marker.
	 */
	public static final Marker ALERT = MarkerFactory.getMarker("ALERT");
	
	/**
	 * Audit log marker.
	 */
	public static final Marker AUDIT = MarkerFactory.getMarker("AUDIT");
}
