
export const getTop10Artists = (setSearchResult: (data: JSON) => void) => {
    
    fetch('http://127.0.0.1:9090/me/top/artists', {
        method: 'GET',
        credentials: 'include',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    },
    })
    .then(response => response.json())
    .then(response => {
        try{
            setSearchResult(response)
        } catch(e) {console.error("Invalid JSON", e)}
    });
}
export default getTop10Artists