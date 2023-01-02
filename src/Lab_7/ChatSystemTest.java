package Lab_7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println();
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method[] mts = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String[] params = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,params);
                    }
                }
            }
        }
    }

}

class ChatRoom{
    String name;
    TreeSet<String> users;
    ChatRoom(String name) {
        this.name = name;
        this.users = new TreeSet<>();
    }

    public void addUser(String username) {
        this.users.add(username);
    }

    public void removeUser(String username) {
        this.users.remove(username);
    }

    public String toString() {
        if(this.users.size() == 0) {
            return this.name + "\nEMPTY\n";
        }
        StringBuilder tempString = new StringBuilder();
        tempString.append(this.name);
        tempString.append("\n");
        users.forEach(el -> tempString.append(el).append("\n"));
        return tempString.toString();
    }

    public boolean hasUser(String user) {
        return this.users.contains(user);
    }

    public int numUsers() {
        return this.users.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return name.equals(chatRoom.name)&&users.equals(chatRoom.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, users);
    }
}

class ChatSystem {
    TreeMap<String, ChatRoom> rooms;

    ChatSystem() {
        this.rooms = new TreeMap<>();
    }

    public void addRoom(String name) {
        this.rooms.put(name, new ChatRoom(name));
    }
    ChatRoom getRoom(String roomName) {
        return this.rooms.get(roomName);
    }
    public void register(String userName) {
        int min = Integer.MAX_VALUE;
        LinkedList<ChatRoom> min_rooms = new LinkedList<>();
        for(ChatRoom cr  : rooms.values()){
            if(cr.numUsers() < min) {
                min_rooms = new LinkedList<>();
                min = cr.numUsers();
            }
            if(cr.numUsers() == min)
                min_rooms.add(cr);
        }
        if(min_rooms.isEmpty())
            return;
        min_rooms.iterator().next().addUser(userName);


        //LinkedHashSet<ChatRoom> collect = rooms.entrySet().stream()
        //        .sorted((elem1, elem2)  -> {
        //            if(elem1.getValue().numUsers() == elem2.getValue().numUsers())
        //                return elem1.getKey().compareTo(elem2.getKey());
        //            else
        //                return Integer.compare(elem1.getValue().numUsers(), elem2.getValue().numUsers());
        //        })
        //        .map(Map.Entry::getValue)
        //        .collect(Collectors.toCollection(LinkedHashSet::new));
//
//
        //if(collect.size() > 0)
        //    collect.iterator().next().addUser(userName);
        //else {
        //    this.rooms.put("", new ChatRoom(""));
        //    this.rooms.get("").addUser(userName);
        //}
    }

    public void registerAndJoin(String userName, String roomName) throws NoSuchRoomException {
        this.addUserToRoom(userName, roomName);
    }

    public void removeRoom(String roomName) {
        this.rooms.remove(roomName);
    }

    private void addUserToRoom(String userName, String roomName) {
        this.rooms.get(roomName).addUser(userName);
    }

    private boolean userExists(String username) {
        return this.rooms
                .values()
                .stream()
                .anyMatch(room -> room.users.contains(username));
    }

    private boolean roomExists(String roomName) {
        return this.rooms.containsKey(roomName);
    }
    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!roomExists(roomName))
            throw new NoSuchRoomException(roomName);

        //if(!userExists(userName))
        //    throw new NoSuchUserException(userName);
        this.rooms.get(roomName).addUser(userName);
    }

    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(!roomExists(roomName))
            throw new NoSuchRoomException(roomName);
        //if(!userExists(username))
        //    throw new NoSuchUserException(username);

        this.rooms.get(roomName).removeUser(username);
    }

    public void followFriend(String username, String friend_username) throws NoSuchUserException {
        //if(!userExists(username))
        //    throw new NoSuchUserException(username);

        this.rooms
                .values()
                .forEach(room -> {
                    if(room.hasUser(friend_username)) room.addUser(username);
                });
    }

}

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String message) {
        super(message);
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String message) {
        super(message);
    }
}
