function generateActivationCode(email){
    const baseUrl = window.location.origin;
    const generateCodeUri = "/activate";

  fetch(baseUrl+generateCodeUri, {
        method: "PATCH",
        header: {
            "Content-Type": "application/json"
        },
        body: email
    })
      .then(response => generatedCode(response));
}

function generatedCode(response){
    if(response.status === 200){
        const element = document.querySelector(".generate-code");
        const timer = document.querySelector(".generate-code-timer");
        const button = document.querySelector("#generate-button");

        element.classList.remove("hide");
        button.disabled = true;

        const timerMsg = timer.textContent;
        let timeLeft = 120;
        timer.textContent = timerMsg + timeLeft;

        const countdownInterval = setInterval(() => {
            timeLeft--;
            timer.textContent  = timerMsg + timeLeft;

            if (timeLeft <= 0) {
                clearInterval(countdownInterval);
                button.disabled = false;
                element.classList.add("hide");
            }
        }, 1000);
    }
}