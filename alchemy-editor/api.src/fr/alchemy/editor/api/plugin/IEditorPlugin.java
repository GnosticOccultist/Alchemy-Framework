package fr.alchemy.editor.api.plugin;

import fr.alchemy.editor.api.editor.FileEditorRegistry;

public interface IEditorPlugin {

	void initialize();
	
	void register(FileEditorRegistry registry);
}
