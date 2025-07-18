
function Login() {
        fetch("http://127.0.0.1:9090/login", {
            method: 'GET',
            credentials: 'include',
            headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            
            },
        })
        .then(response => response.text())
        .then(data => {
            console.log("loginURL: ",data);
            window.location.href = data;
        });
    return (
    <div>AAAAAA</div>
    );
}
export default Login;