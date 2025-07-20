import { useEffect } from "react"


export const refreshToken = () => {
    useEffect(() => {
        const intervalTimer = setInterval(() => {
            if(sessionStorage.getItem('sessionTimer')!=null){
                const Timer  = sessionStorage.getItem('sessionTimer')
                if(Timer != null){
                    const NumTimer = +Timer
                    console.log("set Timer: ",NumTimer)
                    console.log("Refresh in: ",(NumTimer+3000000)-(Date.now()))
                    if((NumTimer+3000000)-(Date.now()) <= 0) {
                        fetch("http://127.0.0.1:9090/token/refresh", {
                            method: 'GET',
                            credentials: 'include',
                            headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json',
                            
                            },
                        })
                        .then(response => {
                            if(!response.ok){return window.location.href = "http://127.0.0.1:8080/login"}
                            console.log("Access token refreshed")
                            sessionStorage.setItem('sessionTimer',`${Date.now()}`)
                            
                        });
                    }
                }
            }
        },30000)
        return () => clearInterval(intervalTimer)
    },[])
}

    