document.addEventListener("DOMContentLoaded", main);

function main() {
    document.getElementById("otp").addEventListener("click", mandaOtp);
    mandaMail();
}

function mandaMail() {
    let body = {
        "email": "nicola1.travaglini@gmail.com"
    };
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8080/SkillBuilders_war/verificaEmail", true);
    request.setRequestHeader("Content-Type", "application/json");
    request.onload = function() {
        console.log(JSON.parse(request.responseText));
    }
    request.send(JSON.stringify(body));
}


function mandaOtp() {
    let body = {
        "otp": 43422
    };
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8080/SkillBuilders_war/controllaOtpEmail", true);
    request.setRequestHeader("Content-Type", "application/json");
    request.onload = function() {
        console.log(JSON.parse(request.responseText));
    }
    request.send(JSON.stringify(body));
}