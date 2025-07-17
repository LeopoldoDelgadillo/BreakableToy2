async function getProfile() {
    await fetch('http://127.0.0.1:8080/me', {
        method: 'GET',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
    })
    return (
        <div>
            <h1>Profile Loaded Successfully</h1>
        </div>
    );
}

export default getProfile;