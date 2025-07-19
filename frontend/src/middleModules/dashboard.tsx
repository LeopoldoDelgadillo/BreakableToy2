import { useEffect, useState } from "react";
import { useSongSearch } from "../components/songSearchContext";
import { useTop10 } from "../components/Top10Context";

type searched = {
  id: string;
  name: string;
  artists: string[];
  followers: number | undefined;
  image: string;
};

type top10 = {
    name: string;
    followers: number;
    image: string;
}

const SearchedArray = ({ name, artists, image, id, followers }: searched) => (
  <div className="p-4 m-2 bg-gray-800 rounded-lg shadow-md text-white hover:bg-gray-700 transition grid grid-cols-4 flex max-h-[500px] overflow-auto">
        <div className="grid col-start-1 items-center"><img src={image} className="rounded-lg w-[64px] h-[64px]"></img></div>
        <div className="grid col-start-2 col-span-4">
            <p className="text-[13px]">{name}</p>
            {artists.length > 0 && (<p className="text-[13px]">By: {artists.join(", ")}</p>)}
            {followers != undefined && (<p className="text-[13px]">Followers: {followers}</p>)}
            <p className="text-[13px]">ID: {id}</p>
        </div>
    </div>
)

const Top10Artists = ({name, followers, image}: top10) => (
    <div className="p-4 m-3 bg-gray-800 rounded-lg shadow-md text-white hover:bg-gray-700 transition grid grid-cols-4 flex max-h-[100px] min-w-[200px]">
        <div className="grid col-start-1 items-center"><img src={image} className="rounded-lg w-[64px] h-[64px]"></img></div>
        <div className="grid col-start-2 col-span-4 ml-2">
            <p className="text-[13px]">{name}</p>
            <p className="text-[13px]">{followers} followers</p>
        </div>
    </div>
)

function dashboard() {
    const {top10} = useTop10()
    const [top10JSX, setTop10JSX] = useState<any[]>([])
    useEffect(() => {
        let top10Data: any
        if (top10 != null){top10Data=top10}
        if(top10Data?.items) {
            const uniqueArtists = top10Data.items.filter((artist: any, index: number, self: any[]) =>
                    index === self.findIndex((s) => s.id === artist.id)
                );
            setTop10JSX(uniqueArtists);
        }
    },[top10])
    const { searchedSong } = useSongSearch()
    const [songList, setSongList] = useState<any[]>([])
    const [artistList, setArtistList] = useState<any[]>([])
    const [albumList, setAlbumList] = useState<any[]>([])
    useEffect (() => {
        let searchDataString = searchedSong
        console.log(searchDataString)
        let searchData: any
        if (searchDataString != null && searchDataString != ""){
            searchData = JSON.parse(searchDataString)
        }
        if(searchData?.tracks?.items) {
            //I asked chatGPT how to de-duplicate
            const uniqueSongs = searchData.tracks.items.filter((song: any, index: number, self: any[]) =>
                index === self.findIndex((s) => s.id === song.id)
            );
            setSongList(uniqueSongs);
        }
        if(searchData?.artists?.items) {
            const uniqueArtists = searchData.artists.items.filter((artist: any, index: number, self: any[]) =>
                index === self.findIndex((s) => s.id === artist.id)
            );
            setArtistList(uniqueArtists);
        }
        if(searchData?.albums?.items) {
            const uniqueAlbums = searchData.albums.items.filter((album: any, index: number, self: any[]) =>
                index === self.findIndex((s) => s.id === album.id)
            );
            setAlbumList(uniqueAlbums);
        }

    },[searchedSong])
    
    
    return(
        <div className="relative overflow-auto">
            <h1 className="text-[25px] ml-3 mb-2 mt-2 flex justify-center items-center">Your Top 10 Artists</h1>
            <div className=" flex items-center justify-center">
            {top10JSX.length > 0 ? (
                <div className="top10artists overflow-x-scroll bg-gray-900 border-[2px] border-gray-800 inset-shadow-indigo-500 flex max-w-[900px] flex-row">
                    {top10JSX.map((artist) => (
                        <Top10Artists
                            key={artist.id}
                            name={artist.name}
                            followers={artist.followers.total}
                            image={artist.images[1].url} 
                        />  
                    ))}
                </div>
            ) : (<h1>No top artists</h1>)
            }
            </div >
            <h1 className="text-[25px] ml-3 mt-6 flex justify-center items-center">Search</h1>   
            <div className="flex items-center justify-center">
            {songList.length > 0 ? (
                <div className="SearchResults flex items-center justify-center grid grid-cols-3">
                    <div>
                        <h1 className="text-[20px] flex items-center justify-center">Songs</h1>
                        <div className={"relative border-[1px] border-gray-800 flex-col max-h-[500px] overflow-auto"}>
                        {songList.map((song, idx) => (
                            <SearchedArray
                                id={song.id}
                                key={`${song.id}-${idx}`}
                                name={song.name}
                                artists={song.artists.map((a: any) => a.name)}
                                image={song.album.images[2].url} 
                                followers={undefined}                                
                            />
                        ))}
                        </div>
                    </div>
                    <div>
                        <h1 className="text-[20px] flex items-center justify-center">Artists</h1>
                        <div className={"relative border-[1px] border-gray-800 flex-col max-h-[500px] overflow-auto"}>
                        {artistList.map((artist, idx) => (
                            <SearchedArray
                                id={artist.id}
                                key={`${artist.id}-${idx}`}
                                name={artist.name}
                                image={artist.images[2].url} 
                                followers={artist.followers.total}   
                                artists={[]}                              
                            />
                        ))}
                        </div>
                    </div>
                    <div>
                        <h1 className="text-[20px] flex items-center justify-center">Albums</h1>
                        <div className={"relative border-[1px] border-gray-800 flex-col max-h-[500px] overflow-auto"}>
                        {albumList.map((album, idx) => (
                            <SearchedArray
                                id={album.id}
                                key={`${album.id}-${idx}`}
                                name={album.name}
                                image={album.images[2].url} 
                                artists={album.artists.map((a: any) => a.name)} 
                                followers={undefined}                              
                            />
                        ))}
                        </div>
                    </div>
                </div>
                ):(<h1>Nothing searched.</h1>)
            }
            </div>
        </div>
    )
}

export default dashboard