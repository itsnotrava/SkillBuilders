document.addEventListener("DOMContentLoaded", main);

function main() {
    document.getElementById("otp").addEventListener("click", mandaOtp);
    mandaMail();
}

function mandaMail() {
    let body = {
        "email": "nicola.travaglini@gmail.com",
        "password": "ciao"
    };
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8080/SkillBuilders_war/accesso", true);
    // request.setRequestHeader("Content-Type", "application/json");
    request.onload = function() {
        console.log(JSON.parse(request.responseText));
    }
    request.send(JSON.stringify(body));
}


function mandaOtp() {
    let body = {
    };
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8080/SkillBuilders_war/visualizzaProprio", true);
    // request.setRequestHeader("Content-Type", "application/json");
    request.onload = function() {
        console.log(JSON.parse(request.responseText));
    }
    request.send(JSON.stringify(body));
}