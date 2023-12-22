import axios from 'axios';

export const createItem = async (data) => {
    try {
        const authToken = localStorage.getItem('token');
        const response = await axios.post('/api/v1/items', { data }, {
            headers: {
                Authorization: `Bearer ${authToken}`,
            },
        });
        console.log('API Response:', response.data);
    } catch (error) {
        console.error('API Error:', error);
    }
};

