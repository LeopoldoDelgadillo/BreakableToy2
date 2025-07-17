
function Login() {
    const loginTry = async () => {
        try{
            const loginURL = await (await fetch("http://localhost:9090/login",)).text();
            console.log("loginURL: ",loginURL);
            window.location.href = loginURL;
            
        }
        catch (error) {
            console.error("Error during login:", error);
        }
    
    }
    loginTry();
    return (
    <div>AAAAAA</div>
    );
}
export default Login;