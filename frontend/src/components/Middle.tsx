import { useMiddle } from './MiddleContext'
import Profile from '../middleModules/profile'
import Dashboard from '../middleModules/dashboard'

export const Middle = () => {

    const { middleValue } = useMiddle()
    return (
        <div className="middle ">
            {middleValue === 0 ? (
                <Dashboard />
                    ) : (
                <Profile />
            )}
        </div>
    );
}
export default Middle;