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

import { Box, makeStyles } from '@material-ui/core';
import React, { useMemo } from 'react';
import { AdjustedAnnotation } from '../../../models/AdjustedTrace';
import { AnnotationTooltip } from '../AnnotationTooltip';

const useStyles = makeStyles((theme) => ({
  annotationMarker: {
    position: 'absolute',
    backgroundColor: theme.palette.common.white,
    height: 2,
    width: 2,
    top: -1,
    cursor: 'pointer',
    pointerEvents: 'auto',
  },
}));

type TimelineRowAnnotationProps = {
  selectedMinTimestamp: number;
  selectedMaxTimestamp: number;
  annotation: AdjustedAnnotation;
};

export const TimelineRowAnnotation = ({
  selectedMinTimestamp,
  selectedMaxTimestamp,
  annotation,
}: TimelineRowAnnotationProps) => {
  const classes = useStyles();

  const left = useMemo(() => {
    if (
      annotation.timestamp < selectedMinTimestamp ||
      annotation.timestamp > selectedMaxTimestamp
    ) {
      return undefined;
    }
    return (
      ((annotation.timestamp - selectedMinTimestamp) /
        (selectedMaxTimestamp - selectedMinTimestamp)) *
      100
    );
  }, [annotation.timestamp, selectedMaxTimestamp, selectedMinTimestamp]);

  if (left === undefined) {
    return null;
  }

  return (
    <AnnotationTooltip annotation={annotation}>
      <Box left={`calc(${left}% - 1px)`} className={classes.annotationMarker} />
    </AnnotationTooltip>
  );
};
