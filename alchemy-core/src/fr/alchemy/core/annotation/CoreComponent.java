package fr.alchemy.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.alchemy.core.scene.component.Component;
import fr.alchemy.core.scene.entity.Entity;

/**
 * The annotation to mark a {@link Component component} that it can't be detached from
 * its {@link Entity entity}.
 * 
 * @author GnosticOccultist
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CoreComponent {

}
