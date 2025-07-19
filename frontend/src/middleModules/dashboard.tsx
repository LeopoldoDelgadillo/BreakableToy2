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
);

function dashboard() {
    const {top10} = useTop10()
    let top10artists: any
    if (top10 != null){
    top10artists = top10
    }
    
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
        <div className="relative border-[2px] overflow-auto">
            {top10artists ? (
                <div className="overflow-x-scroll">
                    <div className="top10artists grid grid-cols-10 grid-rows-1">
                        {top10artists?.items?.[0] ? (<div>{top10artists?.items[0].name}</div>) : (<div>No top 1</div>)}
                        {top10artists?.items?.[1] ? (<div>{top10artists?.items[1].name}</div>) : (<div>No top 2</div>)}
                        {top10artists?.items?.[2] ? (<div>{top10artists?.items[2].name}</div>) : (<div>No top 3</div>)}
                        {top10artists?.items?.[3] ? (<div>{top10artists?.items[3].name}</div>) : (<div>No top 4</div>)}
                        {top10artists?.items?.[4] ? (<div>{top10artists?.items[4].name}</div>) : (<div>No top 5</div>)}
                        {top10artists?.items?.[5] ? (<div>{top10artists?.items[5].name}</div>) : (<div>No top 6</div>)}
                        {top10artists?.items?.[6] ? (<div>{top10artists?.items[6].name}</div>) : (<div>No top 7</div>)}
                        {top10artists?.items?.[7] ? (<div>{top10artists?.items[7].name}</div>) : (<div>No top 8</div>)}
                        {top10artists?.items?.[8] ? (<div>{top10artists?.items[8].name}</div>) : (<div>No top 9</div>)}
                        {top10artists?.items?.[9] ? (<div>{top10artists?.items[9].name}</div>) : (<div>No top 10</div>)}
                    </div>
                </div>
            ) : (<h1>No top artists</h1>)
            }
            <h1>Search</h1>
            <div>
            {songList.length > 0 ? (
                <div className="SearchResults flex items-center justify-center grid grid-cols-3">
                    <div>
                        <h1 className="">Songs</h1>
                        <div className={"relative border-[2px] h-[500px] overflow-auto"}>
                        {songList.map((song, idx) => (
                            <SearchedArray
                                id={song.id}
                                key={`${song.id}-${idx}`}
                                name={song.name}
                                artists={song.artists.map((a: any) => a.name)}
                                image={song.album.images[2].url} followers={undefined}                                />
                        ))}
                        </div>
                    </div>
                    <div>
                        <h1>Artists</h1>
                        <div className={"relative border-[2px] h-[500px] overflow-auto"}>
                        {artistList.map((artist, idx) => (
                            <SearchedArray
                                id={artist.id}
                                key={`${artist.id}-${idx}`}
                                name={artist.name}
                                image={artist.images[2].url} 
                                followers={artist.followers.total}   artists={[]}                              
                                />
                        ))}
                        </div>
                    </div>
                    <div>
                        <h1>Albums</h1>
                        <div className={"relative border-[2px] h-[500px] overflow-auto"}>
                        {albumList.map((album, idx) => (
                            <SearchedArray
                                id={album.id}
                                key={`${album.id}-${idx}`}
                                name={album.name}
                                image={album.images[2].url} 
                                artists={album.artists.map((a: any) => a.name)} followers={undefined}                              
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