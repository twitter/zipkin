/*
 * Copyright 2015-2019 The OpenZipkin Authors
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
import React from 'react';
import moment from 'moment';
import { makeStyles } from '@material-ui/styles';
import Box from '@material-ui/core/Box';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import { spanAnnotationPropTypes } from '../../../prop-types';

const propTypes = {
  annotation: spanAnnotationPropTypes.isRequired,
};

const useStyles = makeStyles({
  cell: {
    paddingTop: '8px',
    paddingBottom: '8px',
  },
});

const SpanAnnotation = ({ annotation }) => {
  const classes = useStyles();

  return (
    <Box>
      <Box fontSize="1.1rem" mb={0.5}>
        {annotation.value}
      </Box>
      <Paper>
        <Table>
          <TableBody>
            {
              [
                {
                  label: 'Start Time',
                  // moment.js only supports millisecond precision, however our timestamps have
                  // microsecond precision. So we use moment.js to generate the human readable time
                  // with just milliseconds and then append the last 3 digits of the timestamp
                  // which are the microseconds.
                  // NOTE: a.timestamp % 1000 would save a string conversion but drops
                  // leading zeros.
                  value: moment(annotation.timestamp / 1000).format('MM/DD HH:mm:ss.SSS')
                    + annotation.timestamp.toString().slice(-3),
                },
                { label: 'Relative Time', value: annotation.relativeTime },
                { label: 'Address', value: annotation.endpoint },
              ].map(e => (
                <TableRow key={e.label}>
                  <TableCell className={classes.cell}>
                    {e.label}
                  </TableCell>
                  <TableCell className={classes.cell}>
                    {e.value}
                  </TableCell>
                </TableRow>
              ))
            }
          </TableBody>
        </Table>
      </Paper>
    </Box>
  );
};

SpanAnnotation.propTypes = propTypes;

export default SpanAnnotation;
