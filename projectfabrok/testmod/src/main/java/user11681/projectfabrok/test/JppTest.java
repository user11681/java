package user11681.projectfabrok.test;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import user11681.projectfabrok.ProjectFabrok;
import user11681.projectfabrok.annotation.Callback;
import user11681.projectfabrok.annotation.Entrypoint;
import user11681.projectfabrok.annotation.ModInterface;
import user11681.projectfabrok.annotation.Proxy;
import user11681.projectfabrok.annotation.Setter;

@ModInterface(id = ProjectFabrok.ID, type = "user11681.projectfabrok.test.ExtraStatefulInterface")
public class JppTest extends PrivateClass {
    @Setter("energy")
    public native JppTest energy(final long energy);

    @Proxy("getPrivateField")
    public native int getPrivateFieldProxy();

    @Callback(owner = ClientTickEvents.class, field = "END_CLIENT_TICK")
    public static void onClientTick(final MinecraftClient client) {
        System.out.println(client);
    }

    @Entrypoint("preLaunch")
    @Entrypoint("gfh:prePreLaunch")
    public void onPreLaunch() {
        final StatefulInterface test = new StatefulInterface() {};
        final StatefulImpl impl = new StatefulImpl();
        final StatefulImplImpl implImpl = new StatefulImplImpl(43896);

        impl.set(1);
        impl.setWidth(11);
        implImpl.set(2);
        implImpl.setWidth(22);
    }
}
