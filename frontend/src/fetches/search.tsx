
export const fetchSearch = (searchParam: string, setSearchResult: (data: string) => void, profile:JSON) => {
    if(searchParam != ""){
    let myProfile: any
    if (profile != null){
    myProfile = profile
    }
    if(searchParam != "" && searchParam != null){searchParam = encodeURIComponent(searchParam)
    console.log(searchParam)
    fetch('http://127.0.0.1:9090/search', {
        method: 'POST',
        credentials: 'include',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        
        },
        body: JSON.stringify({searchString: searchParam, userCountry: myProfile?.country})
    })
    .then(response => response.text())
    .then(data => {
        try{
            setSearchResult(data)
        } catch(e) {console.error("Invalid JSON", e)}
    });}
}}
export default fetchSearch