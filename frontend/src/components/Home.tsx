import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';

const Home = () => {

    const [fileIds, setFileIds] = useState<string[]>([]);
    const navigate = useNavigate();

    const handleClick = (id: string) => {
        navigate(`/file/${id}`);
    };

    useEffect( () => {
        const fetchData = async () =>{
            try{
                const response = await axios.get<string[]>('http://localhost:8080/api/file');
                const ids = response.data.map((element: string) => {
                    const firstIndex = element.indexOf('=') + 1;
                    const lastIndex = element.length;
                    return element.slice(firstIndex, lastIndex - 1);
                });
                setFileIds(ids);
                console.log(ids);
            }catch(error){
                console.log(error);
            }

        } 
        fetchData();
    }, [])


  return (
    <>
        <h1>File IDs</h1>
        <ul>
            {fileIds.length > 0 && fileIds.map((id) =>(
                <li key={id} onClick={ () => handleClick(id)}   className="file-id">
                    {id}
                </li>
            ))}
        </ul>
    </>
  )
}

export default Home