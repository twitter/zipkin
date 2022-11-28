/*
 * Copyright 2015-2022 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import { makeStyles } from '@material-ui/core';
import { DataGrid, GridColDef } from '@material-ui/data-grid';
import React from 'react';
import { AdjustedSpan } from '../../../models/AdjustedTrace';
import { formatDuration, formatTimestamp } from '../../../util/timestamp';

const useStyles = makeStyles((theme) => ({
  dataGrid: {
    border: 'none',
    borderRadius: 0,
    backgroundColor: theme.palette.background.paper,
    borderBottom: `1px solid ${theme.palette.divider}`,
  },
}));

const COLUMN_DEFS: GridColDef[] = [
  { field: 'spanId', headerName: 'Span ID', width: 200 },
  { field: 'serviceName', headerName: 'Service name', width: 200 },
  { field: 'spanName', headerName: 'Span name', width: 200 },
  {
    field: 'timestamp',
    headerName: 'Start time',
    valueGetter: (params) =>
      formatTimestamp(parseInt(params.value!.toString(), 10)),
    width: 200,
  },
  {
    field: 'duration',
    headerName: 'Duration',
    valueGetter: (params) =>
      formatDuration(parseInt(params.value!.toString(), 10)),
    width: 150,
  },
];

type SpanTableProps = { spans: AdjustedSpan[] };

export const SpanTable = ({ spans }: SpanTableProps) => {
  const classes = useStyles();

  return (
    <DataGrid
      className={classes.dataGrid}
      density="compact"
      rows={spans}
      columns={COLUMN_DEFS}
      getRowId={(span) => span.spanId}
    />
  );
};
