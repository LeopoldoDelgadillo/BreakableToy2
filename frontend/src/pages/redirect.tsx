

function Redirect() {
    const sessionID=window.crypto.randomUUID()
    sessionStorage.setItem('sessionTimer',`${Date.now()}`)
    console.log(sessionStorage.getItem('sessionTimer'))
    console.log(window.location.href.toString());
    const authCode = window.location.href.toString().split("?code=")[1];
    console.log("Auth Code: ", authCode);
    fetch ('http://127.0.0.1:9090/auth/spotify', {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ code: authCode, ID: sessionID }),
    })
    .then(response => {
        if (!response.ok) {
            window.location.href = "http://127.0.0.1:8080/login";
            throw new Error('Network response was not ok');
        }
        window.location.href = "http://127.0.0.1:8080/";
    }) 
    return (
        <div>Redirecting to Spotify...</div>
    );
}

export default Redirect;