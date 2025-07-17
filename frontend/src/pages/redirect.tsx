

function Redirect() {
    console.log(window.location.href.toString());
    const authCode = window.location.href.toString().split("?code=")[1];
    fetch ('http://localhost:9090/auth/spotify', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ code: authCode }),
    })
    return (
        <div>Redirecting to Spotify...</div>
    );
}

export default Redirect;