package io.github.h4mu.kontroll.domain;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = Route.class)
public class RouteDataOnDemand {
	public void setColor(Route obj, int index) {
        String color = Integer.toHexString(index);
        if (color.length() > 6) {
            color = color.substring(0, 6);
        }
        obj.setColor(color);
    }

	public void setTextColor(Route obj, int index) {
        String color = Integer.toHexString(index);
        if (color.length() > 6) {
            color = color.substring(0, 6);
        }
        obj.setTextColor(color);
    }
}
