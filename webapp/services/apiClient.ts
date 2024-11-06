import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // Replace with your API URL
    timeout: 10000,
});

export default apiClient;
