package jqsx.Net;

import KanapkaEngine.Net.Router.RouteManager;

public class Router {
    public static PositionSync positionSync = new PositionSync();
    public static NodeSync nodeSync = new NodeSync();

    static {
        RouteManager.defineRoute(positionSync);
        RouteManager.defineRoute(nodeSync);
    }
}
