export const loginCheck = () => {
    fetch('http://127.0.0.1:9090/login/check', {
        method: 'GET',
        credentials: 'include',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        
    },
    })
    .then(response => response.text())
    .then(data => {
        console.log("Session: ",data);
        if(data == "Invalid Session"){
        window.location.href = "http://127.0.0.1:8080/login";
        }
    });
}
  export default loginCheck