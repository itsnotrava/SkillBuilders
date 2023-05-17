document.addEventListener("DOMContentLoaded", main);

function main() {
    accesso();
    document.getElementById("otp").addEventListener("click", visualizzaProprio);
}

function accesso() {
    let body = {
        "email": "travaglini@gmail.com",
        "password": "ciao"
    };
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8080/SkillBuilders_war/accesso", true);
    request.withCredentials = true;
    // request.setRequestHeader("Content-Type", "application/json");
    request.onload = function() {
        console.log(JSON.parse(request.responseText));
    }
    request.send(JSON.stringify(body));
}


function visualizzaProprio() {
    let body = {
    };
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8080/SkillBuilders_war/visualizzaProprio", true);
    request.withCredentials = true;
    // request.setRequestHeader("Content-Type", "application/json");
    request.onload = function() {
        console.log(JSON.parse(request.responseText));
    }
    request.send(JSON.stringify(body));
}