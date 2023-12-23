import axios from 'axios';
import { } from '../utils/TableUtils';

export const createItems = async (data) => {
    try {
        const authToken = localStorage.getItem('token');
        const response = await axios.post('/api/api/v1/items', data, {
            headers: {
                Authorization: `Bearer ${authToken}`,
                'Content-Type': 'application/json', // Set the content type to JSON
            },
        });
        console.log('API Response:', response.data);
    } catch (error) {
        console.error('API Error:', error);
    }
};


export const deleteItem = async (itemId, rowType) => {
    console.log('itemId', itemId);
    const url = rowType === 'simple' ? `/api/api/v1/items/${itemId}` : `/api/api/v1/itemTotals/${itemId}`;
    console.log('url', url);
    try {
        await axios.delete(url, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json',
            },
        });

    } catch (error) {
        console.error('Error deleting item:', error);
    }
};

export const fetchDataFromAPI = async (headersVal) => {
    try {
        const response = await axios.get('/api/api/v1/itemTotals', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json',
            },
        });

        const transformedData = response.data.flatMap(itemTotal => {
            // Transform list of items
            const itemList = itemTotal.listOfItems.map(item => ({
                rowType: 'simple',
                id: item.id,
                data: {
                    name: item.name,
                    collectionName: item.collectionName,
                    attributes: item.attributes,
                    yearValue: item.yearValue,
                    rowData: [] // Add an empty array for rowData
                }
            }));

            // Transform item total
            const itemTotalTransformed = {
                id: itemTotal.id,
                rowType: 'total',
                data: {
                    name: itemTotal.name,
                    collectionName: ' ', // Update this as needed
                    attributes: {
                        [itemTotal.attribute.attributeName]: itemTotal.attribute.attributeValue,
                    },
                    yearTotal: itemTotal.totalValue,
                    rowData: [] // Add an empty array for rowData
                }
            };

            // Combine the transformed data for list of items and item total
            const combinedData = [...itemList, itemTotalTransformed];

            // Map each item in the combinedData to add rowData based on headers
            combinedData.forEach(transformedItem => {
                transformedItem.data.rowData = headersVal.map(header => {
                    if (transformedItem.rowType === 'total') {
                        // Handle total row
                        if (header.category === 'collectionName') {
                            return '';
                        } else if (header.category === 'attribute') {
                            return transformedItem.data.attributes ? transformedItem.data.attributes[header.label] : '';
                        } else if (header.category === 'yearvalue') {
                            return transformedItem.data.yearTotal[header.label] || 0;
                        }
                    } else {
                        // Handle simple row
                        if (header.category === "collectionName") {
                            return transformedItem.data[header.category];
                        } else if (header.category === "attribute") {
                            return transformedItem.data.attributes ? transformedItem.data.attributes[header.label] : undefined;
                        } else if (header.category === "yearvalue") {
                            return transformedItem.data.yearValue ? transformedItem.data.yearValue[header.label] : 0;
                        }
                    }
                });
            });

            return combinedData;
        });
        return transformedData;

    } catch (error) {
        console.error('Error fetching data:', error);
        throw error;
    }
};


// export const fetchDataFromAPI = async (headersVal) => {
//     try {
//         const response = await axios.get('/api/api/v1/itemTotals', {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`,
//                 'Content-Type': 'application/json',
//             },
//         });
//         // Assuming data is the array you provided
//         const transformedData = response.data.flatMap(itemTotal => {
//             // Transform list of items
//             const itemList = itemTotal.listOfItems.map(item => ({
//                 rowType: 'simple',
//                 data: {
//                     id: item.id,
//                     name: item.name,
//                     collectionName: item.collectionName,
//                     attributes: item.attributes,
//                     yearValue: item.yearValue
//                 }
//             }));

//             // Transform item total
//             const itemTotalTransformed = {
//                 id: itemTotal.id,
//                 rowType: 'total',
//                 data: {
//                     name: itemTotal.name,
//                     collectionName: ' ', // Update this as needed
//                     attributes: {
//                         [itemTotal.attribute.attributeName]: itemTotal.attribute.attributeValue,
//                     },
//                     yearTotal: itemTotal.totalValue
//                 }
//             };

//             // Combine the transformed data for list of items and item total
//             return [...itemList, itemTotalTransformed];
//         });
//         // eslint-disable-next-line no-unused-vars
//         console.log(transformedData[3]);
//         console.log(transformedData[2]);
//         const resultArray = transformedData.map(transformedItem => {
//             return headersVal.map(header => {
//                 if (transformedItem.rowType === 'total') {
//                     // For 'total', map yearTotal to yearValue and set attributes as empty
//                     if (header.category === 'collectionName') {
//                         return '';
//                     } else if (header.category === 'attribute') {
//                         return transformedItem.data.attributes ? transformedItem.data.attributes[header.label] : '';
//                     } else if (header.category === 'yearvalue') {
//                         return transformedItem.data.yearTotal[header.label] || 0; // Map yearTotal to yearValue
//                     }
//                 } else {
//                     // For 'simple', follow the previous logic
//                     if (header.category === "collectionName") {
//                         return transformedItem.data[header.category];
//                     } else if (header.category === "attribute") {
//                         return transformedItem.data.attributes ? transformedItem.data.attributes[header.label] : undefined;
//                     } else if (header.category === "yearvalue") {
//                         return transformedItem.data.yearValue ? transformedItem.data.yearValue[header.label] : 0;
//                     }
//                 }
//             });
//         });

//         return resultArray;
//     } catch (error) {
//         console.error('Error fetching data:', error);
//         throw error;
//     }
// };

// In your TableUtils file or wherever you store utility functions

// In your TableUtils file or wherever you store utility functions

