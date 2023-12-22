/* eslint-disable no-undef */
/* eslint-disable no-unused-vars */
import React, { useState, useRef, useEffect } from 'react';
import { HotTable } from '@handsontable/react';
import { mergeDataWithHeaders, getListOfSimpleRowsForSubTotal } from '../utils/TableUtils';
import { fetchDataFromAPI } from '../services/Table-service';

function Table() {
    const hot = useRef(null);
    const [data, setData] = useState([]);
    const [columns, setColumns] = useState(() => {
        const storedColumns = localStorage.getItem('tableColumns');
        return storedColumns ? JSON.parse(storedColumns) : [];
    });

    useEffect(() => {
        localStorage.setItem('tableColumns', JSON.stringify(columns));

        const fetchData = async () => {
            try {
                const fetchedData = await fetchDataFromAPI(columns);
                setData(fetchedData); // Assuming the API returns the data directly
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

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
            } else {
                alert('Invalid category. Please enter "a" for attribute or "y" for yearValue.');
                return;
            }
        }

        // Determine the index to insert the new column
        const insertIndex = category === 'attribute' ? 1 : columns.length;

        setColumns(prevColumns => [
            ...prevColumns.slice(0, insertIndex),
            { label: columnName, category },
            ...prevColumns.slice(insertIndex),
        ]);

        // setData(prevData =>
        //     prevData.map(row => ({
        //         ...row,
        //         data: [
        //             ...row.data.slice(0, insertIndex),
        //             '',
        //             ...row.data.slice(insertIndex),
        //         ],
        //     }))
        // );
    };

    const addRow = (isTotalRow = false) => {
        const newRow = columns.map(() => '');
        const newRowData = { data: newRow, rowType: isTotalRow ? 'total' : 'simple' };
        setData(prevData => [...prevData, newRowData]);
    };

    const handleAfterChange = (changes, source) => {
        if (source === 'edit') {
            const [row, prop, oldValue, newValue] = changes[0];
            console.log(newValue);
            setData((prevData) => {
                prevData[row][prop] = newValue;
                console.log(prevData);
                return prevData;
            });
        }
    };

    const handleCustomAction = (key, options) => {
        const selectedRange = Array.isArray(options) && options.length > 0 ? options[0] : null;

        if (selectedRange && selectedRange.start && selectedRange.start.row !== null) {
            const selectedRow = selectedRange.start.row;
            const selectedData = hot.current.hotInstance.getSourceDataAtRow(selectedRow);
            console.log('selectedData', selectedData);
            const mergedData = mergeDataWithHeaders(columns, selectedData, selectedRow);
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

    const customContextMenu = [
        'row_below',
        '---------',
        {
            key: 'remove_row',
            name: 'Remove Row',
            callback: (key, options) => {
                const selectedRange = Array.isArray(options) && options.length > 0 ? options[0] : null;

                if (selectedRange && selectedRange.start && selectedRange.start.row !== null) {
                    const selectedRow = selectedRange.start.row;

                    setData(prevData =>
                        prevData.filter((row, index) => index !== selectedRow)
                    );

                    // Using the callback form of setData to ensure the state has been updated
                    setData(updatedData => {

                        return updatedData; // Return the updated data to set it in the state
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
    ];

    return (
        <div>
            <button onClick={addColumn}>Add Column</button>
            <button onClick={() => addRow()}>Add Row</button>
            <HotTable
                ref={hot}
                data={data}
                //data={data.map(row => row.data.rowData)}
                colHeaders={columns.map(column => column.label)}
                rowHeaders={true}
                height="auto"
                licenseKey="non-commercial-and-evaluation"
                contextMenu={customContextMenu}
                afterChange={handleAfterChange}
            />
        </div>
    );
}

export default Table;