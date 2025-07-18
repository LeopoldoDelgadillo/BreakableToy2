import { useEffect, useState } from "react";



export const getProfile = () => {
    
    const [fetchResponse, setFetchResponse] = useState<any>(null);
    useEffect(() => {
    fetch('http://127.0.0.1:9090/me', {
        method: 'GET',
        credentials: 'include',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    },
    })
    .then(async response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        setFetchResponse(await response.json())
        sessionStorage.setItem('myProfile',`${JSON.stringify(fetchResponse)}`)
        console.log(JSON.stringify(fetchResponse)," stored")
    });
    },[]);
}
export default getProfile