import { useEffect, useState } from "react"
import fetchAlbum from "../fetches/fetchAlbum"
import { useProfile } from "../components/ProfileContext"
import { useIdentifier } from "../components/IdentifierContext"
import fetchTrack from "../fetches/fetchTrack"
import fetchArtist from "../fetches/fetchArtist"

function albumSongArtist() {
    const {profileContent} = useProfile()
    const {identifierValue} = useIdentifier()
    const[selected, setSelected] = useState<any>()
    let selectedJSX: any
    useEffect (() => {
        if(identifierValue[1] === "track") {
            console.log(identifierValue[1],"selected")
            fetchTrack(identifierValue[0], profileContent)
                .then(fetched => {
                    console.log("fetched info:",fetched)
                    setSelected(fetched)
                })
        }
        else if(identifierValue[1] === "artist") {
            console.log(identifierValue[1],"selected")
            fetchArtist(identifierValue[0])
                .then(fetched => {
                    console.log("fetched info:",fetched)
                    setSelected(fetched)
                })
        }
        if(identifierValue[1] === "album") {
            console.log(identifierValue[1],"selected")
            fetchAlbum(identifierValue[0], profileContent)
                .then(fetched => {
                    console.log("fetched info:",fetched)
                    setSelected(fetched)
                })
        }
    },[identifierValue])
    
    useEffect (() => {
        console.log("selected info:",selected)
    },[selected])
    return(
        <div>
            {selected ? (
                <div>
                    {identifierValue[1] === "album" ? (
                        <div className="border-[2px] grid grid-cols-2 flex items-center">
                            <div >
                                <img src={selected.images[0].url} className={`rounded-full max-w-${selected.images[0].width} max-h-${selected.images[0].height}`}></img>
                            </div>
                            <div className="text-[100px] ml-[20px] grid-rows-3">
                                <div className="row-start-1">
                                    {selected.name}
                                </div>
                                <div className="row-start-3 text-[35px] flex-row">
                                    Artist: {selected.artists[0].name}, {selected.release_date}, {selected.total_tracks} songs
                                </div>
                            </div>
                        </div> ) :
                     identifierValue[1] === "track" ? (
                        <div className="border-[2px] grid grid-cols-2 flex items-center">
                            <div >
                                <img src={selected.album.images[0].url} className={`rounded-full max-w-${selected.album.images[0].width} max-h-${selected.album.images[0].height}`}></img>
                            </div>
                            <div className="text-[100px] ml-[20px] grid-rows-3">
                                <div className="row-start-1">
                                    {selected.name}
                                </div>
                                <div className="row-start-3 text-[35px] flex-row">
                                    Artist: {selected.artists[0].name}, {selected.album.release_date}
                                </div>
                            </div>
                        </div>
                     ) :
                     identifierValue[1] === "artist" ? (
                        <div className="border-[2px] grid grid-cols-2 flex items-center">
                            <div >
                                <img src={`${selected.images[0].url}`} className={`rounded-full max-w-${selected.images[0].width} max-h-${selected.images[0].height}`}></img>
                            </div>
                            <div className="text-[100px] ml-[20px] grid-rows-3">
                                <div className="row-start-1">
                                    {selected.name}
                                </div>
                                <div className="row-start-2 text-[50px]">
                                    {selected.followers.total} followers
                                </div>
                            </div>
                        </div>
                     ) : (<h1>huh?</h1>)
                    }
                </div>
            ) : (<h1>Nothing to see</h1>)
        }
        </div>
    )
}
export default albumSongArtist