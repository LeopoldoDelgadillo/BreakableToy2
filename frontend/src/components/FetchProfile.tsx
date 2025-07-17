const fetchProfile = async () => {
    try {
        let url = 'http://localhost:8080/auth/spotify';
        const response = await(await fetch(url)).json();
        
    }