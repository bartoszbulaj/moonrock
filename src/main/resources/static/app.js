function connect() {
    let btcWS = new WebSocket('wss://www.bitmex.com/realtime?subscribe=trade:XBTUSD');
    let ethWS = new WebSocket('wss://www.bitmex.com/realtime?subscribe=trade:ETHUSD');

    btcWS.onmessage = function (data) {
       updateBtcLabels(data.data);
    };

    ethWS.onmessage = function (data) {
        updateEthLabels(data.data);
    }
}

function updateBtcLabels(message) {
    let jsonMsg = JSON.parse(message);
    let refreshTimestamp = new Date(jsonMsg.data[0].timestamp);
    let refreshTime = "";
    if (refreshTimestamp.getHours() < 10) {
        refreshTime += "0";
    }
    refreshTime += refreshTimestamp.getHours() + ":";
    if (refreshTimestamp.getMinutes() < 10) {
        refreshTime += "0";
    }
    refreshTime += refreshTimestamp.getMinutes() + ":";
    if (refreshTimestamp.getSeconds() < 10) {
        refreshTime += "0";
    }
    refreshTime += refreshTimestamp.getSeconds();
    document.querySelector('#barBtcPrice').innerHTML = jsonMsg.data[0].price.toFixed(2);
    document.querySelector('#btcPrice').innerHTML = jsonMsg.data[0].price;
    document.querySelector('#btcRefreshTime').innerHTML = refreshTime;
}

function updateEthLabels(message) {
    let jsonMsg = JSON.parse(message);
    let refreshTimestamp = new Date(jsonMsg.data[0].timestamp);
    let refreshTime = "";
    if (refreshTimestamp.getHours() < 10) {
        refreshTime += "0";
    }
    refreshTime += refreshTimestamp.getHours() + ":";
    if (refreshTimestamp.getMinutes() < 10) {
        refreshTime += "0";
    }
    refreshTime += refreshTimestamp.getMinutes() + ":";
    if (refreshTimestamp.getSeconds() < 10) {
        refreshTime += "0";
    }
    refreshTime += refreshTimestamp.getSeconds();
    document.querySelector('#barEthPrice').innerHTML = jsonMsg.data[0].price.toFixed(2);
    document.querySelector('#ethPrice').innerHTML = jsonMsg.data[0].price;
    document.querySelector('#ethRefreshTime').innerHTML = refreshTime;
}
