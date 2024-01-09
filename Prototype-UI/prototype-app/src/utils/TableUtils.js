import { createItemTotal } from "../services/Table-service";

export function mergeDataWithHeadersForItemTotal(headers, selectedData, presentRow) {
    const result = {
        rowType: presentRow.rowType,
        id: presentRow.id,
        data: {
            name: selectedData[0],
            attribute: { attributeName: "", attributeValue: "" },
            yearTotalValue: {}
        }
    };
    headers.forEach((column, index) => {
        if (index > 0) {
            const category = column.category;
            const columnName = column.label;
            if (category === 'attribute' && presentRow.data.attributes[columnName]) {
                result.data.attribute.attributeName = columnName;
                result.data.attribute.attributeValue = presentRow.data.attributes[columnName] ? presentRow.data.attributes[columnName] : '';
            } else if (category === 'yearvalue') {
                result.data.yearTotalValue[columnName] = parseInt(selectedData[index] ? selectedData[index] : 0);
            }
        }
    });

    return result;
}

export function mergeDataWithHeaders(headers, selectedData, presentRow) {
    const result = {
        rowType: presentRow.rowType,
        id: presentRow.id ? presentRow.id : '',
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
                result.yearValue[columnName] = parseInt(selectedData[index] ? selectedData[index] : 0);
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

    for (let i = data.length - 1; i >= 0; i--) {
        const currentRow = data[i];
        if (currentRow.rowType === 'total') {
            break;
        } else if (currentRow.rowType === 'simple') {
            simpleRowsData.unshift(currentRow.id);
        }
    }
    return simpleRowsData;
};

export const handleSubtotalClick = (data) => {
    return new Promise((resolve, reject) => {
        const rowIndex = data.length + 1;
        let attributeName = prompt(`For row ${rowIndex} Enter the name of the attribute:`);
        if (!attributeName) {
            reject('Attribute name not provided.');
            return;
        }

        let attributeValue = prompt(`Enter the attribute value for "${attributeName}":`);
        if (!attributeValue) {
            reject('Attribute value not provided.');
            return;
        }

        let subtotalHeading = prompt('Enter the title of the subtotal:');
        if (!subtotalHeading) {
            reject('Subtotal heading not provided.');
            return;
        }

        const itemTotal = {
            name: subtotalHeading,
            rowType: 'total',
            attribute: {
                attributeName,
                attributeValue
            },
            itemIds: getListOfSimpleRowsForSubTotal(data),
            yearTotalValue: {}
        };
    
        createItemTotal(itemTotal)
            .then(() => {
                resolve();
            });
    });
};






