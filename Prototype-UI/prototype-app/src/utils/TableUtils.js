export function mergeDataWithHeaders(headers, selectedData, presentRow) {
    const result = {
        rowType: presentRow.rowType,
        id: presentRow.id ? presentRow.id : '',
        // data: {
        name: headers.find(column => column.category === 'collectionName').label,
        collectionName: selectedData[0],
        attributes: {},
        yearValue: {}
        // }
    };

    headers.forEach((column, index) => {
        if (index > 0) {
            const columnName = column.label;
            const category = column.category;

            if (category === 'attribute') {
                result.attributes[columnName] = selectedData[index];
            } else if (category === 'yearvalue') {
                result.yearValue[columnName] = parseInt(selectedData[index]);
            }
        }
    });

    return result;
}

export function createItemJSON(headers, selectedData, presentRow) {
    const result = {
        rowType: presentRow.rowType,
        name: headers.find(column => column.category === 'collectionName').label,
        collectionName: selectedData[0],
        attributes: {},
        yearValue: {}
    };

    headers.forEach((column, index) => {
        if (index > 0) {
            const columnName = column.label;
            const category = column.category;

            if (category === 'attribute') {
                result.attributes[columnName] = selectedData[index];
            } else if (category === 'yearvalue') {
                result.yearValue[columnName] = parseInt(selectedData[index]);
            }
        }
    });

    return result;
}

export const getListOfSimpleRowsForSubTotal = (data) => {
    const simpleRowsData = [];

    for (let i = data.length - 2; i >= 0; i--) {
        const currentRow = data[i];
        if (currentRow.rowType === 'total') {
            break;
        } else if (currentRow.rowType === 'simple') {
            simpleRowsData.unshift(currentRow.data);
        }
    }
    console.log(simpleRowsData);
    return simpleRowsData;
};





