
export const getProfile = async () => {
    
    fetch('http://127.0.0.1:9090/me', {
        method: 'GET',
        credentials: 'include',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    },
    })
    .then(response => response.text())
    .then(data => {
        sessionStorage.setItem('myProfile',`${data}`)
        console.log(data," stored")
    });
}
export default getProfile