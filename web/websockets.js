// Timestamp of cart that page was last updated with
var lastCartUpdate = 0;
//websocket connection variable
var websocket;

//Sends a message to the servlet
function sendText(mesg) {
    console.log("sending text: " + mesg);
    //sending a message in a websocket connection
    websocket.send(mesg);
}

//Opens the connection to the websocket on startup. Connects to the servlet in the path /websocket
function init() {
    var wsUri = "ws://" + document.location.host + document.location.pathname + "websocket";
    websocket = new WebSocket(wsUri);
    // when the websocket connection is formed
    websocket.onopen = function (event) {
        console.log("Connection established");
        // send the "reload" action to get the cart details from the server
        sendText(JSON.stringify({action: "reload", item: "reload"}));
    };

    //Sets the method to execute when a message arrives
    websocket.onmessage = function (evt) {
        onMessage(evt)
    };
    //Sets the method to execute when an error occurs
    websocket.onerror = function (evt) {
        onError(evt)
    };
}

//Receives information from the servlet and passes the message to the updateCart method
function onMessage(evt) {
    // calls on the updateCart method and passes the Json string as a parameter
    updateCart(evt.data);
}

function onError(evt) {
    //Display the erro on the web page if any encountered
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function updateCart(cartJSON) {
    // Use library to parse the string back from server
    var myJsonObj = JSON.parse(cartJSON);
    // Find the value of the cartGenerated identifier
    var generated = myJsonObj.cart.Generated;
    //updating the display only if the timestamp is new
    if (generated > lastCartUpdate) {
        // assigning new timestamp to lastCartUpdate 
        lastCartUpdate = generated;
        //getting the part of the page having ID as "contents" which was defined in index.jsp previously
        var contents = document.getElementById("contents");
        // making it null, clearing that part of the page
        contents.innerHTML = "";

        var items = myJsonObj.cart.cartItems;
        for (var I = 0; I < items.length; I++) {

            var item = items[I];
            var name = item.name;
            var quantity = item.quantity;

            var listItem = document.createElement("li");
            // adding the items and quatity to the web page
            listItem.appendChild(document.createTextNode(name + " x " + quantity));
            contents.appendChild(listItem);
        }
        // adding the total value to the part of the web page having ID as "total"
        document.getElementById("total").innerHTML = myJsonObj.cart.Total;
    }
}

/*
 * Adds the specified item to the shopping cart, via Ajax call
 * itemCode - product code of the item to add
 */
function addToCart(itemCode) {
    // sending the action "add" and the itemcode to the server
    sendText(JSON.stringify({action: "add", item: itemCode}));
}

function removeFromCart(itemCode) {
    // sending the action "remove" and the itemcode to the server
    sendText(JSON.stringify({action: "remove", item: itemCode}));
}