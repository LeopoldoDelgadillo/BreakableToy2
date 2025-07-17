
async function Redirect() {
    console.log(window.location.href.toString());
    const authCode = window.location.href.toString().split("?code=")[1];
    console.log("Auth Code: ", authCode);
    await fetch ('http://localhost:9090/auth/spotify', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ code: authCode }),
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