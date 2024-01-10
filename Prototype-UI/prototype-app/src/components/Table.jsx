/* eslint-disable no-undef */
/* eslint-disable no-unused-vars */
import React, { useState, useRef, useEffect } from 'react';
import { HotTable } from '@handsontable/react';

import { mergeDataWithHeaders, getListOfSimpleRowsForSubTotal, handleSubtotalClick, mergeDataWithHeadersForItemTotal } from '../utils/TableUtils';
import { deleteItem, createBatchItems, updateBatchItems, fetchDataFromBackend, updateTotalItem } from '../services/Table-service';
import './Table.css'


function Table() {
    const hot = useRef(null);
    const [objectData, setObjectData] = useState([]);
    const [data, setData] = useState([]);
    const [columns, setColumns] = useState(() => {
        const storedColumns = localStorage.getItem('tableColumns');
        return storedColumns ? JSON.parse(storedColumns) : [];
    });
    const fetchData = async () => {
        try {
            const fetchedData = await fetchDataFromBackend(columns);
            setObjectData(fetchedData);
            setData(fetchedData.map(row => row.data.rowData))
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    useEffect(() => {
        localStorage.setItem('tableColumns', JSON.stringify(columns));
        fetchData();
    }, [columns]);

    const addColumn = () => {
        let columnName;
        let category;
        if (columns.length === 0) {
            columnName = prompt('Enter name for the first column:');
            category = 'collectionName';
            addRow();
        } else {
            const categoryInput = prompt('Enter category for the new column: (a for attribute, y for yearValue)').toLowerCase();

            if (categoryInput === 'a') {
                columnName = prompt('Enter name for the new attribute column:');
                category = 'attribute';
            } else if (categoryInput === 'y') {
                columnName = prompt('Enter name for the new yearValue column:');
                category = 'yearvalue';
            } else if (categoryInput === 's') {
                setSubtotalCategory(prompt('Enter category for subtotal: (a for attribute, c for collectionName)').toLowerCase());
                return;
            } else {
                alert('Invalid category. Please enter "a" for attribute or "y" for yearValue.');
                return;
            }
        }

        const insertIndex = category === 'attribute' ? 1 : columns.length;

        setColumns(prevColumns => [
            ...prevColumns.slice(0, insertIndex),
            { label: columnName, category },
            ...prevColumns.slice(insertIndex),
        ]);
    };

    const addRow = (isTotalRow = false, grandTotalData) => {
        if (isTotalRow) {
            setData(prevData => [...prevData, grandTotalData]);
        } else {
            const newRow = columns.map(() => '');
            const newRowData = { data: newRow, rowType: isTotalRow ? 'total' : 'simple' };
            setObjectData(prevData => [...prevData, newRowData])
            setData(prevData => [...prevData, newRow]);
        }
    };

    const handleAfterChange = (changes, source) => {
        if (source === 'edit') {
            const [row, prop, oldValue, newValue] = changes[0];
            setData((prevData) => {
                const updatedData = [...prevData];
                updatedData[row][prop] = newValue;
                if (objectData[row].rowType === 'total') {
                    const updatedItemTotal = mergeDataWithHeadersForItemTotal(columns, updatedData[row], objectData[row]);
                    updateTotalItem(updatedItemTotal.id, updatedItemTotal.data)
                       .then(() => { fetchData() })
                }
                return updatedData;
            });
        }
    };

    const handleCustomAction = (key, options) => {
        const selectedRange = Array.isArray(options) && options.length > 0 ? options[0] : null;

        if (selectedRange && selectedRange.start && selectedRange.start.row !== null) {
            const selectedRow = selectedRange.start.row;
            const selectedData = hot.current.hotInstance.getSourceDataAtRow(selectedRow);
            const mergedData = mergeDataWithHeaders(columns, selectedData, objectData[selectedRow]);
            console.log('Merged Data:', mergedData);
        } else {
            console.error('Invalid options:', options);
        }
    };

    const createRowAsTotal = (key, options) => {
        const selectedRange = Array.isArray(options) && options.length > 0 ? options[0] : null;

        if (selectedRange && selectedRange.start && selectedRange.start.row !== null) {
            const selectedRow = selectedRange.start.row;
            setData(prevData =>
                prevData.map((row, index) => ({
                    ...row,
                    rowType: index === selectedRow ? 'total' : row.rowType,
                }))
            );

            setData(updatedData => {
                const updatedRow = updatedData[selectedRow];

                const relevantData = updatedData.slice(0, selectedRow + 1);
                getListOfSimpleRowsForSubTotal(relevantData);
                return updatedData;
            });
        }
    };

    const calculateGrandTotal = (data, columns) => {
        const grandTotal = columns.map(column => {
            if (column.category === 'yearvalue') {
                const columnIndex = columns.indexOf(column);
                if (columnIndex >= 0) {
                    const total = data.reduce((sum, row) => {

                        if (Array.isArray(row) && row.length > columnIndex) {
                            const value = Number(row[columnIndex]) || 0;
                            return sum + value;
                        }
                        return sum;
                    }, 0);
                    return Number(total);
                }
            }
            return '';
        });
        grandTotal[0] = "GRAND TOTAL";
        return grandTotal;
    };

    const handleGrandTotalClick = () => {
        if (Array.isArray(columns) && Array.isArray(objectData)) {
            const simpleRows = objectData.filter(row => row.rowType === 'simple');
            const grandTotal = calculateGrandTotal(simpleRows.map(row => row.data.rowData), columns);
            addRow(true, grandTotal);
        }
        else {
            console.error('Invalid columns or data structure. Unable to calculate grand total.');
        }
    };

    const customContextMenu = [
        {
            key: 'row_below',
            name: 'Insert Row Below',
            callback: (key, options) => {
                const selectedRange = Array.isArray(options) && options.length > 0 ? options[0] : null;

                if (selectedRange && selectedRange.start && selectedRange.start.row !== null) {
                    const selectedRow = selectedRange.start.row;

                    setData((prevData) => {
                        const newData = [...prevData];
                        newData.splice(selectedRow + 1, 0, columns.map(() => '')); // Insert a new row
                        return newData;
                    });

                    const newRow = columns.map(() => '');
                    const newRowData = { data: newRow, rowType: 'simple' }; // Assuming it's always a simple row
                    setObjectData((prevData) => {
                        const newObjectData = [...prevData];
                        newObjectData.splice(selectedRow + 1, 0, newRowData); // Insert a new row data object
                        return newObjectData;
                    });
                }
            },
        },
        '---------',
        'copy',
        'cut',
        'paste',
        '---------',
        {
            key: 'custom_action',
            name: 'Get Row Detail',
            callback: handleCustomAction,
        },
        {
            key: 'custom_action_2',
            name: 'Create Total-Row',
            callback: createRowAsTotal,
        },
        {
            key: 'custom_action_3',
            name: 'Delete Item',
            callback: (key, options) => {
                const selectedRange = Array.isArray(options) && options.length > 0 ? options[0] : null;

                if (selectedRange && selectedRange.start && selectedRange.start.row !== null) {
                    const selectedRow = selectedRange.start.row;
                    const selectedData = hot.current.hotInstance.getSourceDataAtRow(selectedRow);
                    const itemId = objectData[selectedRow].id;
                    deleteItem(itemId, objectData[selectedRow].rowType);
                    setData((prevData) => prevData.filter((row, index) => index !== selectedRow));
                    if (data.length === 0) setObjectData([]);
                    setObjectData((prevObjectData) => prevObjectData.filter((row, index) => index !== selectedRow));
                }
            },
        },
    ];
    const handleSave = async () => {
        let simpleObjectData = [];
        let simpleData = [];
        let totalObjectData = [];
        let totalData = [];
        for (let i = 0; i < objectData.length; i++) {
            if (objectData[i].rowType === 'simple') {
                simpleObjectData.push(objectData[i]);
                simpleData.push(data[i]);
            }
            if (objectData[i].rowType === 'total') {
                totalObjectData.push(objectData[i]);
                totalData.push(data[i]);
            }
        }
        await handelItemById(simpleObjectData, simpleData).then(() => fetchData())

        async function handelItemById(simpleObjectData, simpleData) {
            let updatedSimpleObjectData = [];
            for (let i = 0; i < simpleObjectData.length; i++) {
                updatedSimpleObjectData.push(mergeDataWithHeaders(columns, simpleData[i], simpleObjectData[i]));
            }

            const createdSimpleData = updatedSimpleObjectData.filter(item => item.id === '');
            const updatedSimpleData = updatedSimpleObjectData.filter(item => item.id !== '');

            if (createdSimpleData.length !== 0) {
                console.log('creating items...');
                await createBatchItems(createdSimpleData.map(({ id, ...rest }) => rest));
            }
            if (updatedSimpleData.length !== 0) {
                console.log('updating items...');
                await updateBatchItems(updatedSimpleData);
            }

        }
    };

    return (
        <div>
            <button className="actionButton" onClick={addColumn}>Add Column</button>
            <button className="actionButton" onClick={() => addRow()}>Add Row</button>
            <button className="saveButton" onClick={handleSave}>Save Data</button>
            <button className="grandTotalButton" onClick={handleGrandTotalClick}>Grand Total</button>
            <button className="actionButton" onClick={() => { handleSubtotalClick(objectData).then(() => fetchData()) }}>Sub Total</button> <hr />

            <HotTable
                ref={hot}
                data={data}
                colHeaders={columns.map(column => column.label)}
                rowHeaders={true}
                height="auto"
                licenseKey="non-commercial-and-evaluation"
                contextMenu={customContextMenu}
                afterChange={handleAfterChange}
                cells={(row, col, prop) => {
                    const isTotalRow = objectData[row] && objectData[row].rowType === 'total';
                    const isEmptyOrUndefined = data[row][col] === '' || data[row][col] === undefined;
                    return {
                        readOnly: isTotalRow && isEmptyOrUndefined,
                    };
                }}
            />
        </div>
    );
}

export default Table;