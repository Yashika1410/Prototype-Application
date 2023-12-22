export function mergeDataWithHeaders(headers, selectedData) {
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
