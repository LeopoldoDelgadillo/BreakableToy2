import { useEffect } from "react"


export const refreshToken = () => {
    useEffect(() => {
        const intervalTimer = setInterval(() => {
            if(sessionStorage.getItem('sessionTimer')!=null){
                const Timer  = sessionStorage.getItem('sessionTimer')
                if(Timer != null){
                    const NumTimer = +Timer
                    console.log("Refresh in: ",(NumTimer+3500000)-(Date.now()))
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
                            sessionStorage.setItem('sessionTimer',`${Date.now()}`)
                        });
                    }
                }
            }
        },1000)
        return () => clearInterval(intervalTimer)
    },[])
}

    