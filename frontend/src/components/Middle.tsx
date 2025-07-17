import { useMiddle } from './MiddleContext';
import Profile from 'D:\\SpotifyApp\\frontend\\src\\modules\\profile.tsx'

export const Middle = () => {

    const { middleValue } = useMiddle();

    return (
        <div  className="middle absolute top-[75px] w-full h-[calc(100vh-75px)] bg-gray-900">
            <div className="flex justify-center items-center h-full">
                {middleValue === 0 ? (
                    <div><h1>Main page</h1></div>
                        ) : (
                    <Profile />
                )}
            </div>
        </div>
    );
}
export default Middle;