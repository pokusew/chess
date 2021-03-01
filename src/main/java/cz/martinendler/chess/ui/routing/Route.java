package cz.martinendler.chess.ui.routing;

import cz.martinendler.chess.ui.controllers.Reloadable;
import javafx.scene.Parent;
import javafx.util.Pair;

public class Route {

	public final RouteName name;
	public final Parent view;
	public final Reloadable controller;

	public Route(RouteName name, Parent view, Reloadable controller) {
		this.name = name;
		this.view = view;
		this.controller = controller;
	}

	public Route(RouteName name, Pair<Parent, Reloadable> def) {
		this(name, def.getKey(), def.getValue());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Route route = (Route) o;
		return name == route.name;
	}

	@Override
	public int hashCode() {
		return name.index;
	}

}
