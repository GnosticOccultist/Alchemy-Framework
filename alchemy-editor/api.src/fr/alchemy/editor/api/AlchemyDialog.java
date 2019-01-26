package fr.alchemy.editor.api;

import java.awt.Point;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <code>AlchemyDialog</code> is an abstract layer which can be used in any dialog construction.
 * It is designed to easily add components to it, configure validation or close button, key event
 * listening...
 * <p>
 * For example you can create a simple information dialog, a directory browser dialog, a creation file dialog
 * and potentially many others.
 * 
 * @author GnosticOccultist
 */
public abstract class AlchemyDialog {
	
	/**
	 * The default dialog size (0;0).
	 */
	protected static final Point DEFAULT_SIZE = new Point(0, 0);
	
	/**
	 * The stage of this dialog.
	 */
	private final Stage dialog;
	/**
	 * The container of the dialog.
	 */
	private final VBox root;
	/**
	 * The OK button.
	 */
	protected Button okButton;
	/**
	 * The close button.
	 */
	protected Button closeButton;
	/**
	 * Whether the window is ready.
	 */
	private volatile boolean ready;

	public AlchemyDialog() {
		this.root = new VBox();
		this.root.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(root);
		
		this.dialog = new Stage();
		this.dialog.setTitle(getTitleText());
		this.dialog.initStyle(StageStyle.UTILITY);
		this.dialog.initModality(Modality.WINDOW_MODAL);
		this.dialog.setResizable(isResizable());
		this.dialog.setScene(scene);
	}
	
	/**
	 * Show the <code>AlchemyDialog</code> if it was previously hiding.
	 */
	public void show() {
		postConstruct();
	
		dialog.show();
		dialog.requestFocus();
		dialog.toFront();
		dialog.setOnCloseRequest(event -> hide());
		
		Platform.runLater(dialog::sizeToScene);
	}

	/**
	 * Constructs the <code>AlchemyDialog</code> after instantiation.
	 * It is called when showing the dialog the first time.
	 */
	public void postConstruct() {
		if(ready) {
			return;
		}
		
		createControls(root);
		configureSize(root);
		
		ready = true;
	}
	
	/**
	 * Hide the <code>AlchemyDialog</code> if it was previously showing.
	 */
	public void hide() {
		dialog.hide();
	}
	
	/**
	 * Reload the <code>AlchemyDialog</code> by setting its construction flag
	 * to false and calling {@link #show()}.
	 */
	public void reload() {
		ready = false;
		show();
	}
	
	/**
	 * Creates the sub-layout of the dialog and process the key event.
	 * 
	 * @param root The root to add sub-layout to.
	 */
	protected void createControls(VBox root) {
		VBox container = new VBox();
		createContent(container);
		root.getChildren().add(container);
		
		VBox actionsContainer = new VBox();
		createActions(actionsContainer);
		root.getChildren().add(actionsContainer);
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, this::processKey);
	}
	
	/**
	 * Process the key event.
	 * <p>
	 * It simply hides the <code>AlchemyDialog</code> when the {@link KeyCode#ESCAPE}
	 * is released.
	 * 
	 * @param event The key event to process.
	 */
	protected void processKey(KeyEvent event) {
		event.consume();
		
		// Process hiding action.
		if(event.getCode() == KeyCode.ESCAPE) {
			hide();
		}
		
		// Process validation action.
		if (okButton != null && event.getCode() == KeyCode.ENTER && !okButton.isDisable()) {
            processOK();
        }
	}
	
	/**
	 * Creates the content of the <code>AlchemyDialog</code>.
	 * By default this method doesn't do anything, so you need to override
	 * the function.
	 * 
	 * @param root The root to add content to.
	 */
	protected void createContent(VBox root) {}
	
	/**
	 * Creates the actions of the <code>AlchemyDialog</code>.
	 * It simply adds and configures the 'Close' and 'OK' button if specified so in the 
	 * implementation using {@link #needCloseButton()} or {@link #needOKButton()}.
	 * 
	 * @param root The root to add actions to.
	 */
	protected void createActions(VBox root) {
		
		HBox container = null;
		
		if(needOKButton()) {
			okButton = new Button(getOKButtonText());
            okButton.setOnAction(event -> processOK());
		}
		
		if(needCloseButton()) {
			closeButton = new Button(getCloseButtonText());
			closeButton.setOnAction(event -> processClose());
		}
		
		if(needOKButton()) {
			if(container == null) {
				container = new HBox();
			}
			container.getChildren().add(okButton);
		}
		
		if(needCloseButton()) {
			if(container == null) {
				container = new HBox();
			}
			container.getChildren().add(closeButton);
		}
		
		if(container != null && !container.getChildren().isEmpty()) {
			root.getChildren().add(container);
		}
	}

	/**
	 * Return the readable-only width property of the <code>AlchemyDialog</code>.
	 * 
	 * @return The width property of the dialog.
	 */
	protected ReadOnlyDoubleProperty widthProperty() {
		return getContainer().widthProperty();
	}
	
	/**
	 * Return the readable-only height property of the <code>AlchemyDialog</code>.
	 * 
	 * @return The height property of the dialog.
	 */
	protected ReadOnlyDoubleProperty heightProperty() {
		return getContainer().heightProperty();
	}
	
	/**
	 * Configures the size of the <code>AlchemyDialog</code> and the provided
	 * container to the one specified in {@link #getSize()}. By default {@link #DEFAULT_SIZE}.
	 * 
	 * @param container The container to configure the size.
	 */
	protected void configureSize(VBox container) {
		configureSize(container, getSize());
	}
	
	/**
	 * Configure the size of the <code>AlchemyDialog</code> and the provided
	 * container to the one specified. Note that the size only applied if 
	 * width &ge; 1 and height &ge; 1.
	 * 
	 * @param container The container to configure the size.
	 * @param size		The size to set.
	 */
	private void configureSize(VBox container, Point size) {
		Stage dialog = getDialog();
		
		double width = size.getX();
		double height = size.getY();
		
		if(width >= 1D) {
			container.setMaxWidth(width);
			container.setMinWidth(width);
			dialog.setMinWidth(width);
			dialog.setMaxWidth(width);
		}
		
		if(height >= 1D) {
			container.setMaxHeight(height);
			container.setMinHeight(height);
			dialog.setMinHeight(height);
			dialog.setMaxHeight(height);
		}
	}
	
	/**
	 * Return the text used by the 'OK' button.
	 * By default : OK.
	 * 
	 * @return The text used by the 'OK' button.
	 */
	protected String getOKButtonText() {
		return "OK";
	}
	
	/**
	 * Whether the <code>AlchemyDialog</code> needs an 'OK' button.
	 * 
	 * @return Whether an 'OK' button is needed.
	 */
	protected boolean needOKButton() {
		return false;
	}
	
	/**
	 * The action invoked when the 'OK' button is pressed,
	 * if it exists.
	 * By default: {@link #hide()}
	 */
	protected void processOK() {
		hide();
	}
	
	/**
	 * Return the text used by the 'Close' button.
	 * By default : Close.
	 * 
	 * @return The text used by the 'Close' button.
	 */
	protected String getCloseButtonText() {
		return "Close";
	}
	
	/**
	 * Whether the <code>AlchemyDialog</code> needs an 'Close' button.
	 * 
	 * @return Whether an 'Close' button is needed.
	 */
	protected boolean needCloseButton() {
		return false;
	}
	
	/**
	 * The action invoked when the 'Close' button is pressed,
	 * if it exists.
	 * By default: {@link #hide()}
	 */
	protected void processClose() {
		hide();
	}

	/**
	 * Return the text used as the title of the <code>AlchemyDialog</code>.
	 * By default: Unknown.
	 * 
	 * @return The text used as a title.
	 */
	protected String getTitleText() {
		return "Unknown";
	}
	
	/**
	 * Sets the title of the <code>AlchemyDialog</code>.
	 * 
	 * @param title The title of the dialog.
	 */
	public void setTitleText(String title) {
		dialog.setTitle(title);
	}
	
	/**
	 * Return the stage used by the <code>AlchemyDialog</code>.
	 * 
	 * @return The stage of the dialog.
	 */
	public Stage getDialog() {
		return dialog;
	}
	
	/**
	 * Return the root container used by the <code>AlchemyDialog</code>.
	 * 
	 * @return The root container of the dialog.
	 */
	public VBox getContainer() {
		return root;
	}

	/**
	 * Whether the <code>AlchemyDialog</code> is resizable.
	 * By default: true.
	 * 
	 * @return Whether the dialog is resizable.
	 */
	protected boolean isResizable() {
		return true;
	}
	
	/**
	 * Return the size used by the <code>AlchemyDialog</code>. 
	 * By default: {@link #DEFAULT_SIZE}.
	 * 
	 * @return The size of the dialog.
	 */
	protected Point getSize() {
		return DEFAULT_SIZE;
	}
}
