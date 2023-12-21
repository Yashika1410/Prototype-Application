/* eslint-disable no-unused-vars */
import React, { useState } from 'react';
import axios from 'axios';
import { HotTable } from '@handsontable/react';

function Table() {
  const initialData = [
    ['Country', 'Gender', 'Age-Group', '2019', '2020'],
    ['Country1', 'Male', '10-20', 10, 11],
    ['Country1', 'Male', '10-20', 20, 11],
    ['Country1', 'Male', '10-20', 30, 15],
  ];
  const [data, setData] = useState();
  const [columnHeaders, setColumnHeaders] = useState();
  const fetchData = async () => {
    let bearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5YXNoaWthMUBnbWFpbC5jb20iLCJpYXQiOjE3MDMxNDkxMzksImV4cCI6MTcwMzE1MDU3OX0.HfIRc3eshyWtgYQASuCUIPbQuRPXB2uHOOGNwsjS8Qc";
      try {
        const response = await axios.get('http://localhost:8080/api/v1/items',{
          headers: {
            'Authorization': `Bearer ${bearerToken}`,
            'Content-Type': 'application/json', // Adjust content type as needed
          },
        }); // Replace with your API endpoint
        const result = await response.json();
        // setData(result);
        console.log(result)
        console.log(response.status)
      } catch (error) {
        console.error('Error fetching data:', error);
      }}
 useState(()=>{
  
      fetchData()
      setColumnHeaders([
    'A',
    'B',
    'C',
    'D',
    'E',
  ])
 },[]); // Initial column headers as letters A, B, C, ...

  const addColumn = () => {
    const newColumnHeader = String.fromCharCode(65 + columnHeaders.length); // Convert ASCII code to letter
    setColumnHeaders([...columnHeaders, newColumnHeader]);
    setData((prevData) =>
      prevData.map((row) => [...row, '']) // Add empty value for the new column in each row
    );
  };

  const addRow = () => {
    const newRow = columnHeaders.map(() => ''); // Create a new row with empty values
    setData((prevData) => [...prevData, newRow]);
  };

  return (
    <div>
      <button onClick={addColumn}>Add Column</button>
      <button onClick={addRow}>Add Row</button>
      <HotTable
        data={data}
        colHeaders={columnHeaders}
        rowHeaders={true}
        height="auto"
        licenseKey="non-commercial-and-evaluation"
      />
    </div>
  );
}

export default Table;
