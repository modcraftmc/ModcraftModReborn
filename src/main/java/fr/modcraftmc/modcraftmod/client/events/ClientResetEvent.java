//package fr.modcraftmc.modcraftmod.client.events;
//
//import fr.modcraftmc.modcraftmod.client.screen.ResetScreen;
//import net.minecraft.network.Connection;
//import net.neoforged.bus.api.Event;
//
//public class ClientResetEvent extends Event {
//
//    private Connection connection;
//    private ResetScreen resetScreen;
//
//    public ClientResetEvent(Connection networkManager, ResetScreen resetScreen) {
//        this.connection = networkManager;
//        this.resetScreen = resetScreen;
//    }
//
//    public Connection getConnection() {
//        return connection;
//    }
//
//    public ResetScreen getResetScreen() {
//        return resetScreen;
//    }
//
//    public static class Pre extends ClientResetEvent {
//        public Pre(Connection networkManager, ResetScreen resetScreen) {
//            super(networkManager, resetScreen);
//        }
//    }
//
//    public static class Post extends ClientResetEvent {
//        public Post(Connection networkManager, ResetScreen resetScreen) {
//            super(networkManager, resetScreen);
//        }
//    }
//}
