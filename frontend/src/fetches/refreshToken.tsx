

export const refreshToken = async () => {
    const Timer  = sessionStorage.getItem('sessionTimer')
    if(Timer != null){
        const NumTimer = +Timer
        console.log(NumTimer)
        while(( NumTimer+3600000)-(Date.now()) > 0){
        }
        console.log("passed while");
        if((NumTimer+3500000)-(Date.now()) <= 0) {
            fetch("http://127.0.0.1:9090/token/refresh", {
                method: 'GET',
                credentials: 'include',
                headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                
                },
            })
            .then(response => {
                console.log("Access token refreshed")
            });
        }
    }
}

    