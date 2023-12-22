/* eslint-disable no-undef */
/* eslint-disable no-unused-vars */
import React, { useState, useRef, useEffect } from 'react';
import axios from 'axios';
import { HotTable } from '@handsontable/react';
import { deleteItem } from '../services/Table-service';
function Table() {
    const hot = useRef(null);
    const [data, setData] = useState();
    const [columns, setColumns] = useState();
    const fetchData = async () => {

        try {
            const fetchedData = await fetchDataFromAPI();
            processFetchedData(fetchedData);
        } catch (error) {
            console.error('Error processing fetched data:', error);
        }
    };

    const processFetchedData = (fetchedData) => {
        let colData = [];
        let keyList = new Set();

        fetchedData.forEach((itemTotal) => {
            let dict = {};
            let res = setItem(itemTotal["listOfItems"]);
            colData.push(...res[1]);
            keyList = [...res[0]];
            dict[itemTotal['attribute']['attributeName']] = itemTotal['attribute']['attributeValue'];

            for (const key in itemTotal["totalValue"]) {
                dict[key] = itemTotal["totalValue"][key];
            }

            colData.push(dict);
        });

        setColumns(keyList);
        setData(colData);
    };

    useState(() => {

        fetchData();

    }, []);


    const addColumn = () => {
        let columnName;
        let category;

        if (columns.length === 0) {
            columnName = prompt('Enter name for the first column:');
            category = 'collectionName';
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
        // const insertIndex = category === 'attribute' ? 1 : columns.length;

        setColumns(prevColumns => [
            ...prevColumns.slice(0, insertIndex),
            { label: columnName, category },
            ...prevColumns.slice(insertIndex),
        ]);

        setData(prevData =>
            prevData.map(row => ({
                ...row,
                data: [
                    ...row.data.slice(0, insertIndex),
                    '',
                    ...row.data.slice(insertIndex),
                ],
            }))
        );
    };

    const addRow = (isTotalRow = false) => {
        const newRow = columns.map(() => '');
        const newRowData = { data: newRow, rowType: isTotalRow ? 'total' : 'simple' };
        setData(prevData => [...prevData, newRowData]);
    };

    const handleAfterChange = (changes, source) => {
        if (source === 'edit') {
            const [row, prop, oldValue, newValue] = changes[0];

            if (prop === columns[columns.length - 1].label) {
                const currentRow = data[row];
                if (currentRow.rowType === 'simple') {
                    localStorage.setItem('tableData', JSON.stringify(data));
                }
                return;
            }

            setData(prevData => {
                const newData = [...prevData];
                newData[row].data[prop] = newValue;
                return newData;
            });
        }
    };

    function mergeDataWithHeaders(headers, selectedData) {
        const result = {
            rowType: 'simple',
            data: {
                name: headers.find(column => column.category === 'collectionName').label,
                collectionName: selectedData[0],
                attributes: {},
                yearValue: {}
            }
        };

        headers.forEach((column, index) => {
            if (index > 0) {
                const columnName = column.label;
                const category = column.category;

                if (category === 'attribute') {
                    result.data.attributes[columnName] = selectedData[index];
                } else if (category === 'yearvalue') {
                    result.data.yearValue[columnName] = parseInt(selectedData[index]);
                }
            }
        });

        return result;
    }

    const handleCustomAction = (key, options) => {
        const selectedRange = Array.isArray(options) && options.length > 0 ? options[0] : null;

        if (selectedRange && selectedRange.start && selectedRange.start.row !== null) {
            const selectedRow = selectedRange.start.row;
            const selectedData = hot.current.hotInstance.getSourceDataAtRow(selectedRow);

            const mergedData = mergeDataWithHeaders(columns, selectedData);
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
                localStorage.setItem('tableData', JSON.stringify(updatedData));
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
                        localStorage.setItem('tableData', JSON.stringify(updatedData));
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
        {
            key: 'custom_action_3',
            name: 'Delete Item',
            callback: (key, options) => {
                const selectedRange = Array.isArray(options) && options.length > 0 ? options[0] : null;

                if (selectedRange && selectedRange.start && selectedRange.start.row !== null) {
                    const selectedRow = selectedRange.start.row;
                    const selectedData = hot.current.hotInstance.getSourceDataAtRow(selectedRow);
                    const itemId = selectedData.id;
                    deleteItem(itemId);
                    fetchData();
                }
            },
        },
    ];

    return (
        <div>
            <button onClick={addColumn}>Add Column</button>
            <button onClick={() => addRow()}>Add Row</button>
            <HotTable
                ref={hot}
                data={data}
                colHeaders={true}
                columns={columns}
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
