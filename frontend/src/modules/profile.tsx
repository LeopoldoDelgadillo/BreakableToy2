import { useEffect, useState } from "react";

function getProfile() {
    const [fetchResponse, setFetchResponse] = useState<any>(null);
    useEffect(() => {
    fetch('http://localhost:9090/me', {
        method: 'GET',
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
        console.log(await fetchResponse)
        
    });
    },[]);
    return(
        <div>
        {fetchResponse ? (
            JSON.stringify(fetchResponse,null,3)
        ) : (
            <h1>Nothing to see on profile</h1>
        
        )}
        </div>
    )
}

export default getProfile;