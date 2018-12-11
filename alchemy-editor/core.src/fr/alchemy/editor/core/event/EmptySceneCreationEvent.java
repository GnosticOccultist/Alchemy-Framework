package fr.alchemy.editor.core.event;

import javafx.event.Event;
import javafx.event.EventType;

public class EmptySceneCreationEvent extends Event {

	private static final long serialVersionUID = 4170342183386341275L;
	
	/**
	 * The type for {@link EmptySceneCreationEvent}.
	 */
	public static final EventType<EmptySceneCreationEvent> SCENE_CREATION = new EventType<>(ANY, "SCENE_CREATION");
	

	public EmptySceneCreationEvent() {
		super(SCENE_CREATION);
	}
}
