/* eslint-disable no-unused-vars */
import React, { useState } from 'react';
import axios from 'axios';
import { HotTable } from '@handsontable/react';

function Table() {
  const [data, setData] = useState();
  const [columnHeaders, setColumnHeaders] = useState();
  const fetchData = async () => {
    
      try {
        await axios.get('/api/api/v1/itemTotals',{
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json', // Adjust content type as needed
          },
        }).then((data)=>{
          let colData=[]
          let keyList=new Set();
          data.data.forEach((itemTotal)=>{
            let dict={}
            let res=setItem(itemTotal["listOfItems"])
            colData.push(...res[1])
            keyList=[...res[0]]
            dict[itemTotal['attribute']['attributeName']]=itemTotal['attribute']['attributeValue']
            for(const key in itemTotal["totalValue"]){
              dict[key]=itemTotal["totalValue"][key]
            }
            colData.push(dict)
          }
          )
          setColumnHeaders(keyList)
          setData(colData)
          function setItem(datas) {
            const keySet = new Set();
          let columnData=[]

            keySet.add("name");
            keySet.add("collectionName");

            datas.forEach((d, _) => {
              let vDict = {};
              // console.log(d["attributes"])
              for (const key in d["attributes"]) {
                vDict[key] = d["attributes"][key];
                keySet.add(key);
              }
              for (const key in d["yearValue"]) {
                vDict[key] = d["yearValue"][key];
                keySet.add(key);
              }
              vDict["name"] = d["name"];
              vDict["collectionName"] = d["collectionName"];
              vDict["id"] = d["id"];
              columnData.push(vDict);
            });
            let colHead = [];
            keySet.forEach((v) => {
              colHead.push({ data: v, title: v });
            });
            return [colHead,columnData];
          }
        }
        )
      } catch (error) {
        console.error('Error fetching data:', error);
      }}
 useState(()=>{
  
      fetchData();

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
        columns={columnHeaders}
        rowHeaders={true}
        colHeaders={true}
        height="auto"
        licenseKey="non-commercial-and-evaluation"
      />
    </div>
  );
}

export default Table;
