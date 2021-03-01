package cz.martinendler.chess;

import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ControllerFactory implements Callback<Class<?>, Object> {

	private static final Logger log = LoggerFactory.getLogger(ControllerFactory.class);

	protected final App app;

	public ControllerFactory(App app) {
		this.app = app;
	}

	@Override
	public Object call(Class<?> type) {

		// note: in the future, implement something like dependency injection using annotations

		log.info("trying to create " + type.toString());

		// 1. try App aware constructor
		try {
			Constructor<?> constructor = type.getConstructor(App.class);
			return constructor.newInstance(app);
		} catch (NoSuchMethodException e) {
			log.info("class " + type.toString() + " has no constructor with the only arg App");
			// continues with 2. try default constructor
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			log.error("newInstance exception " + e.toString());
			return null;
		}

		// 2. try default constructor
		try {
			Constructor<?> constructor = type.getConstructor();
			return constructor.newInstance();
		} catch (NoSuchMethodException e) {
			log.info("cannot get default constructor " + e.toString());
			return null;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			log.error("newInstance exception " + e.toString());
			return null;
		}

	}

}

