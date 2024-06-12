import React,{ useEffect, useState }  from 'react'
import axios from 'axios'
import { useParams } from 'react-router-dom';


const ImageDispaly = () => {
    const { id } = useParams<{ id: string }>();
    const [imageSrc, setImageSrc] = useState<string | null>(null);


    useEffect( ()  => {
        const fetchImage = async () =>{
        try{
            const response = await axios.get(`http://localhost:8080/api/file/${id}`, {
            responseType: 'arraybuffer',
            headers: {
                'Content-Type': 'application/json',
            },
            });
            const blob = new Blob([response.data], { type: 'image/jpeg' });
            const imageURL = URL.createObjectURL(blob);
            setImageSrc(imageURL);
            // console.log(response);
        }catch(error){
            console.error(error);
        }
        }
        fetchImage()
        
    }, [])


  return (
    <>
      <div>
            {imageSrc ? <img src={imageSrc} alt="Fetched from GridFS" /> : <p>Loading...</p>}
        </div>
    </>
  )
}

export default ImageDispaly