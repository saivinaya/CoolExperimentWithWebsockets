/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package developerworks.ajax.servlet;

import developerworks.ajax.store.Cart;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author VinayaSaiD
 */
public class CartEncoder implements Encoder.Text<Cart>{

    @Override
    public String encode(Cart cart) throws EncodeException {
        //Converting the object to a Json string and then sending it to the peers
        return cart.toJson().toString();
    }

    // to initiate the encoder
    @Override
    public void init(EndpointConfig config) {
        System.out.println("init");
    }

    // to destroy the encoder
    @Override
    public void destroy() {
        System.out.println("destroy");
    }
    
}
