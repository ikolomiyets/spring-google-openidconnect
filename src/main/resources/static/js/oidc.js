function check_session() {
    if (client_id !== null && client_id.length != 0 && client_id !== 'null' && session_state !== null &&
        session_state.length != 0 && session_state != 'null') {
        var win = document.getElementById("opIFrame").contentWindow;
        win.postMessage(mes, targetOrigin);
    }
}

function setTimer() {
    check_session();
    setInterval("check_session()", 4 * 1000);
}

window.addEventListener("message", receiveMessage, false);

function receiveMessage(e) {

    if (targetOrigin.indexOf(e.origin) < 0) {
        return;
    }

    if (e.data == "changed") {
        console.log("[RP] session state has changed. sending passive request");
        if (authorizationEndpoint !== null && authorizationEndpoint.length != 0 && authorizationEndpoint !==
            'null') {

            var clientId = client_id;
            var scope = 'openid';
            var responseType = 'code';
            var prompt = 'none';

            window.top.location.href = authorizationEndpoint + '?client_id=' + clientId + "&scope=" + scope +
                "&response_type=" + responseType + "&redirect_uri=" + redirectUri + "&prompt=" + prompt;
        }
    }
    else if (e.data == "unchanged") {
        console.log("[RP] session state has not changed");
    }
    else {
        console.log("[RP] error while checking session status");
    }
}
