import axios from "axios";
const API_URL  = process.env.REACT_APP_API_URL;
const API_PORT = process.env.REACT_APP_API_PORT;
let url : string =`http://localhost:8080`;
if(API_URL && API_PORT) url= `http://${API_URL}:${API_PORT}`
console.log(url);
export default axios.create({
  baseURL: url
});