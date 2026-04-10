//Factory Pattern
public class RoomFactory {
    public static Room createRoom(String roomId, RoomType type, double pricePerNight) {
        return new Room(roomId, type, pricePerNight);
    }
}