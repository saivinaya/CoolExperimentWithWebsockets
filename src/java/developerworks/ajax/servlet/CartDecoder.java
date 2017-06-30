/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package developerworks.ajax.servlet;

import developerworks.ajax.store.Cart;
import developerworks.ajax.store.Item;
import java.io.StringReader;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author VinayaSaiD
 */
public class CartDecoder implements Decoder.Text<Cart> {

    private static Cart mainTempCart;

    static {
        mainTempCart = new Cart();
    }

    // this is where the control comes when a message is received, this is where the decode of the message happens
    @Override
    public Cart decode(String msg) throws DecodeException {
        // converts the Json string into a jsonObject
        JsonObject jsonObject = Json.createReader(new StringReader(msg)).readObject();
        // All the below processing can be done in WebSocketServlet.java file as well
        
        // gets the value of "action" and "item" from the jsonObject
        String action = jsonObject.getString("action");
        String item = jsonObject.getString("item");
        HashMap<Item, Integer> tempCart = new HashMap<Item, Integer>();
        // checking if both action and item are sent from the client
        if ((action != null) && (item != null)) {
            // based on the value of action either adding or deleting the item
            if ("add".equals(action)) {
                mainTempCart.addItem(jsonObject);
            } else if ("remove".equals(action)) {
                mainTempCart.removeItems(jsonObject);
            } else if ("reload".equals(action)) {
                return mainTempCart;
            }
        }
        return mainTempCart;
    }

    // to check whether the messsage can be decode or not
    @Override
    public boolean willDecode(String msg) {
        try {
            //reading a String as jsonObject
            Json.createReader(new StringReader(msg)).readObject();
            return true;
        } catch (JsonException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // to initiate the decode
    @Override
    public void init(EndpointConfig config) {
        System.out.println("init");
    }

    // to destroy the decode
    @Override
    public void destroy() {
        System.out.println("destroy");
    }

}