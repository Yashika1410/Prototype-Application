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


export const deleteItem = async (itemId) => {
    try {
        await axios.delete(`/api/api/v1/items/${itemId}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json',
            },
        });
        
    } catch (error) {
        console.error('Error deleting item:', error);
    }
};





export const fetchDataFromAPI = async () => {
  try {
    const response = await axios.get('/api/api/v1/itemTotals', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json',
      },
    });
    console.log(response.data);
    return response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
    throw error;
  }
};

