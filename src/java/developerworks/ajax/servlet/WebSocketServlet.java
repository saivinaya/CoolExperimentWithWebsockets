/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package developerworks.ajax.servlet;

import developerworks.ajax.store.Cart;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author VinayaSaiD
 */
@ServerEndpoint(value="/websocket", encoders = {CartEncoder.class}, decoders = {CartDecoder.class})
public class WebSocketServlet {
    
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    // Adds the user to the session
    @OnOpen
    public void onOpen(Session peer) {
        peers.add(peer);
    }

    // Removes the user when the page is closed
    @OnClose
    public void onClose(Session peer) {
        peers.remove(peer);
    }

    //Returns the date to all users when one client requests for the date
    @OnMessage
    public void sendCartDetails(Cart cart, Session session) throws IOException, EncodeException {
        // sending the new cart details to all the peers , so that they can display it on their screen in the web browser
        System.out.println("Message received"); 
        for (Session peer : peers) {
            // sending the object directly as we are using encoders (where we change it to a string and send over to client)
            peer.getBasicRemote().sendObject(cart);
        }
    }
}
