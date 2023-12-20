/* eslint-disable no-unused-vars */
import React from 'react'
import { HotTable } from '@handsontable/react';

function Table() {
    return (
        <HotTable
            data={[
                ['Country', 'Gender', 'Age-Group', '2019', '2020'],
                ['Country1', 'Male', '10-20', 10, 11],
                ['Country1', 'Male', '10-20', 20, 11],
                ['Country1', 'Male', '10-20', 30, 15]
            ]}
            rowHeaders={true}
            colHeaders={true}
            height="auto"
            licenseKey="non-commercial-and-evaluation" // for non-commercial use only
        />
    )
}

export default Table