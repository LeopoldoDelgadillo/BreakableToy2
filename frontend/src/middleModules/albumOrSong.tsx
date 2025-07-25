import { useEffect, useState} from "react"
import fetchAlbum from "../fetches/fetchAlbum"
import { useProfile } from "../components/ProfileContext"
import { useIdentifier } from "../components/IdentifierContext"
import fetchTrack from "../fetches/fetchTrack"
import fetchArtist from "../fetches/fetchArtist"
import fetchArtistTopTracks from "../fetches/fetchArtistTopTracks"
import fetchArtistAlbums from "../fetches/fetchArtistAlbums"
import fetchAlbumTracks from "../fetches/fetchAlbumTracks"
import followArtistIcon from "../assets/followArtistIcon.svg"

type topSongs = {
    trackNumber: number;
    name: string;
    image: string;
    durationMinutes: number;
    durationSeconds: number;
}

const TopSongs = ({ trackNumber, name, image, durationMinutes, durationSeconds}: topSongs) => (
    <tr>
        <td className="min-w-[30px]">{trackNumber}&nbsp;·&nbsp;</td>
        <td className="min-w-[90px] flex justify-center "><img src={image} className="rounded-lg w-[64px] h-[64px]"></img></td>
        <td className="">{name}</td>
        <td className="min-w-[40px]">&nbsp;·&nbsp;{durationMinutes}:{durationSeconds >= 10 ? durationSeconds : "0"+durationSeconds}</td>
    </tr>
)

type displayDiscography = {
    name: string;
    image: string;
    type: string;
    release: string;
    id: string;
    identifierFunc: (val:string[]) => void

}

const DisplayDiscography = ({name,image,type,release,id,identifierFunc}: displayDiscography) => (
    <button onClick={() => clicked(id,type,identifierFunc)} className="p-4 m-2 bg-gray-800 rounded-lg shadow-md text-white hover:bg-gray-700 transition grid grid-cols-4 flex max-h-[500px] overflow-auto">
        <div className="grid col-start-1 items-center"><img src={image} className="rounded-lg w-[64px] h-[64px]"></img></div>
        <div className="grid col-start-2 col-span-4">
            <p className="text-[13px]">{name}</p>
            <p className="text-[13px]">{release}&nbsp;·&nbsp;{type}</p>
        </div>
    </button>
)

type albumSongs = {
    trackNumber: number;
    name: string;
    artists: string[];
    durationMinutes: number;
    durationSeconds: number;
}

const AlbumSongs = ({ trackNumber, name, artists, durationMinutes, durationSeconds}: albumSongs) => (
    <tr className="h-[30px]">
        <td className="min-w-[30px]">{trackNumber}&nbsp;·&nbsp;</td>
        <td className="">
            <div className="flex-col">
                <p>{name}</p>
                <div className="text-[9px]">{artists.join(", ")}</div>
            </div>
        </td>
        <td className="min-w-[40px] ">&nbsp;·&nbsp;{durationMinutes}:{durationSeconds >= 10 ? durationSeconds : "0"+durationSeconds}</td>
    </tr>
)

function clicked(id:string, type: string, idFunc: (val: string[]) => void) {
            if(type == "track" || type == "album" || type == "single" ) {
                idFunc([id,type])
            }
    }

function albumSongArtist() {
    const {profileContent} = useProfile()
    const {identifierValue,changeIdentifier} = useIdentifier()
    const[selected, setSelected] = useState<any>()
    const[topTracks, setTopTracks] = useState<any[]>([])
    const[discography, setDiscography] = useState<any[]>([])
    const[selectedAlbumSongs, setSelectedAlbumSongs] = useState<any[]>([])
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
                    fetchArtistTopTracks(identifierValue[0], profileContent)
                        .then(fetched => {
                            setTopTracks(fetched.tracks)
                        })
                    fetchArtistAlbums(identifierValue[0], profileContent)
                        .then(fetched => {
                            setDiscography(fetched.items)
                        })
                })
        }
        else if(identifierValue[1] === "album"  || identifierValue[1] ===  "single") {
            console.log(identifierValue[1],"selected")
            console.log(identifierValue[0])
            fetchAlbum(identifierValue[0], profileContent)
                .then(fetched => {
                    console.log("fetched info:",fetched)
                    setSelected(fetched)
                    
                })
        }
    },[identifierValue])
    
    useEffect (() => {
        console.log("selected info:",selected)
        console.log(identifierValue[1])
        if((identifierValue[1] === "album"  || identifierValue[1] ===  "single") && selected != undefined) {
            fetchAlbumTracks(identifierValue[0],profileContent,selected.total_tracks)
                .then(fetched => {
                    setSelectedAlbumSongs(fetched)
                })
        }
    },[selected])

    useEffect (() => {
        console.log("topTracks info:",topTracks)
    },[topTracks])

    useEffect (() => {
        console.log("Discography info:",discography)
    },[discography])

    useEffect(()=> {
        console.log("AlbumSongs info: ",selectedAlbumSongs)
    },[selectedAlbumSongs])

    return(
        <div>
            {selected ? (
                <div className="flex flex-col justify-center items-center">
                    {identifierValue[1] === "album" || identifierValue[1] === "single" ? (
                        <>
                            <div className="grid grid-cols-3 mt-[20px]">
                                <div className="flex items-center justify-center">
                                    <img src={selected?.images?.[1]?.url ? selected.images[1].url : followArtistIcon} className={"rounded-full"}></img>
                                </div>
                                <div className="ml-[20px] flex justify-center flex-col col-span-2">
                                    <div className="text-[50px] row-start-1">
                                        {selected.name}
                                    </div>
                                    <div className="text-[30px] flex-row">
                                        Artist: {selected?.artists?.[0]?.name ? selected.artists[0].name : "a"} · {selected.release_date} · {selected.total_tracks} songs
                                    </div>
                                </div>
                            </div>
                            <div className="flex justify-center flex-col mt-[10px]">
                                {selectedAlbumSongs.length > 0 ? (
                                    <div className="overflow-auto max-h-[400px] border-[1px] border-gray-800 flex justify-center">
                                        <table className="table-fixed mr-[10px] ml-[15px] mt-[5px] mb-[5px]">
                                            <tbody>
                                                {selectedAlbumSongs.map((block)=> (
                                                    block.items.map((track: any) => (
                                                    <AlbumSongs
                                                        key={track.id}
                                                        trackNumber={track.track_number}
                                                        name={track.name}
                                                        artists={track.artists.map((a: any) => a.name)}
                                                        durationMinutes={Math.floor(track.duration_ms/60000)}
                                                        durationSeconds={Math.floor((track.duration_ms/60000-Math.floor(track.duration_ms/60000))*60)}
                                                    />
                                                    ))
                                                ))}
                                            </tbody>
                                        </table>
                                    </div> ) :(<h1>Loading tracks...</h1>)
                                }
                            </div>
                        </> ) :
                     identifierValue[1] === "track" ? (
                        <div className="grid grid-cols-3 mt-[20px]">
                            <div className="flex items-center justify-center">
                                <img src={selected?.album?.images?.[1]?.url ? selected.album.images[1].url : followArtistIcon} className={"rounded-full"}></img>
                            </div>
                            <div className="ml-[20px] flex justify-center flex-col col-span-2">
                                <div className="text-[50px]">
                                    {selected.name}
                                </div>
                                <div className="text-[30px] flex-row">
                                    Artist: {selected?.artists?.[0]?.name ? selected.artists[0].name : "a"} · {selected.album.release_date}
                                </div>
                            </div>
                        </div>
                     ) :
                     identifierValue[1] === "artist" ? (
                        <>
                            <div className="grid grid-cols-3 mt-[20px]">
                                <div className="flex items-center justify-center">
                                    <img src={`${selected?.images?.[1]?.url ? selected.images[1].url : followArtistIcon}`} className={"rounded-full"}></img>
                                </div>
                                <div className="ml-[20px] flex justify-center flex-col col-span-2">
                                    <div className="text-[50px]">
                                        {selected.name}
                                    </div>
                                    <div className="text-[30px]">
                                        {selected.followers.total} followers
                                    </div>
                                </div>
                            </div>
                            <div className="max-w-5/6 flex justify-center flex-col">
                                <h1 className="text-[25px] flex justify-center mt-[20px]">Popular songs</h1>
                                <div className="overflow-auto max-h-[265px] border-[1px] border-gray-800 flex justify-center ml-[10px] mr-[10px]">
                                    { topTracks.length > 0 ? (
                                        <table className="table-fixed mr-[10px] ml-[15px] mt-[5px] mb-[5px]">
                                            <tbody >
                                                {topTracks.map((track,idx) => (
                                                    <TopSongs
                                                        key={`${track.id}-${idx}`}
                                                        trackNumber={idx+1}
                                                        name={track.name}
                                                        image={track?.album?.images?.[0]?.url ? track.album.images[0].url : followArtistIcon}
                                                        durationMinutes={Math.floor(track.duration_ms/60000)}
                                                        durationSeconds={Math.floor((track.duration_ms/60000-Math.floor(track.duration_ms/60000))*60)}

                                                    />
                                                ))}
                                            </tbody>
                                        </table>
                                        ) : (<h1>No discography.</h1>)
                                    }
                                </div>
                                </div>
                                <div className=" max-w-[95%] flex flex-col mb-[50px]">
                                    <h1 className="text-[25px] flex justify-center mt-[20px]">Discography</h1>
                                    {discography.length > 0 ? (
                                        <div className="overflow-auto max-h-[400px] border-gray-800 border-[1px]">
                                            <div className="grid grid-cols-3 ">
                                                {discography.map((trackAlbum) => (
                                                    <DisplayDiscography
                                                    key={trackAlbum.id}
                                                    name={trackAlbum.name}
                                                    image={trackAlbum?.images?.[1]?.url ? trackAlbum.images[1].url : followArtistIcon}
                                                    type={trackAlbum.type}
                                                    release={trackAlbum.release_date}
                                                    id={trackAlbum.id}
                                                    identifierFunc={changeIdentifier}
                                                    />
                                                ))}
                                            </div> 
                                        </div>
                                    ) : (<h1>Loading discography...</h1>)}
                                </div>
                        </>
                     ) : (<h1>huh?</h1>)
                    }
                </div>
            ) : (<h1>Nothing to see</h1>)
        }
        </div>
    )
}
export default albumSongArtist