/*
 * Copyright 2015-2020 The OpenZipkin Authors
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
/* eslint-disable no-shadow */
import React from 'react';
import { useMount } from 'react-use';
import { Box, ClickAwayListener } from '@material-ui/core';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

import Criterion from '../Criterion';
import SuggestionList from './SuggestionList';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      cursor: 'pointer',
      '& > *:hover': {
        opacity: 0.9,
      },
    },
    deleteButton: {
      height: '100%',
      width: 30,
      color: theme.palette.common.white,
      backgroundColor: theme.palette.primary.main,
      cursor: 'pointer',
      border: 'none',
    },
    input: {
      width: 350,
      height: 40,
      padding: 10,
      boxSizing: 'border-box',
      fontSize: '1.1rem',
    },
  }),
);

interface CriterionBoxProps {
  criteria: Criterion[];
  criterion: Criterion;
  serviceNames: string[];
  remoteServiceNames: string[];
  spanNames: string[];
  autocompleteKeys: string[];
  autocompleteValues: string[];
  isLoadingServiceNames: boolean;
  isLoadingRemoteServiceNames: boolean;
  isLoadingSpanNames: boolean;
  isLoadingAutocompleteValues: boolean;
  isFocused: boolean;
  onFocus: () => void;
  onBlur: () => void;
  onDecide: () => void;
  onChange: (criterion: Criterion) => void;
  onDelete: () => void;
  loadAutocompleteValues: (autocompleteKey: string) => void;
}

const initialText = (criterion: Criterion) => {
  if (criterion.key) {
    if (criterion.value) {
      return `${criterion.key}=${criterion.value}`;
    }
    return `${criterion.key}=`;
  }
  return '';
};

const CriterionBox: React.FC<CriterionBoxProps> = ({
  criteria,
  criterion,
  serviceNames,
  remoteServiceNames,
  spanNames,
  autocompleteKeys,
  autocompleteValues,
  isLoadingServiceNames,
  isLoadingRemoteServiceNames,
  isLoadingSpanNames,
  isLoadingAutocompleteValues,
  isFocused,
  onFocus,
  onBlur,
  onDecide,
  onChange,
  onDelete,
  loadAutocompleteValues,
}) => {
  const classes = useStyles();

  const inputEl = React.useRef<HTMLInputElement>(null);

  const [text, setText] = React.useState(initialText(criterion));
  const [fixedText, setFixedText] = React.useState(initialText(criterion));

  useMount(() => {
    if (inputEl.current) {
      inputEl.current.focus();
    }
  });

  const prevIsFocused = React.useRef(isFocused);
  React.useEffect(() => {
    if (prevIsFocused.current && !isFocused) {
      if (!fixedText) {
        onDelete();
        return;
      }
      let ss = fixedText.split('=');

      // If the length is greater than 2, there is more than one "=" in the text.
      // Service names, span names, and tag's keys and values can contain '=',
      // so this is also valid.
      // In this case, treat the first "=" as a separator between key and value.
      if (ss.length > 2) {
        ss = fixedText.split(/=(.+)/);
      }
      onChange({ key: ss[0], value: ss[1] || '' });
    } else if (!prevIsFocused.current && isFocused) {
      if (inputEl.current) {
        inputEl.current.focus();
      }
    }
    prevIsFocused.current = isFocused;
  }, [isFocused, fixedText, onChange, onDelete]);

  const keyText = React.useMemo(() => {
    const ss = fixedText.split('=');
    return ss[0];
  }, [fixedText]);

  const isEnteringKey = !text.includes('=');
  const isLoadingSuggestions = React.useMemo(() => {
    if (isEnteringKey) {
      return false;
    }
    switch (keyText) {
      case 'serviceName':
        return isLoadingServiceNames;
      case 'spanName':
        return isLoadingSpanNames;
      case 'remoteServiceName':
        return isLoadingRemoteServiceNames;
      default:
        if (autocompleteKeys.includes(keyText)) {
          return isLoadingAutocompleteValues;
        }
    }
    return false;
  }, [
    keyText,
    isEnteringKey,
    isLoadingServiceNames,
    isLoadingSpanNames,
    isLoadingRemoteServiceNames,
    isLoadingAutocompleteValues,
    autocompleteKeys,
  ]);

  const suggestions = React.useMemo(() => {
    if (isEnteringKey) {
      return [
        'serviceName',
        'spanName',
        'remoteServiceName',
        'maxDuration',
        'minDuration',
        'tags',
        ...autocompleteKeys,
      ].filter((key) => !criteria.find((criterion) => criterion.key === key));
    }
    switch (keyText) {
      case 'serviceName':
        return serviceNames;
      case 'spanName':
        return spanNames;
      case 'remoteServiceName':
        return remoteServiceNames;
      default:
        if (autocompleteKeys.includes(keyText)) {
          return autocompleteValues;
        }
        return null;
    }
  }, [
    autocompleteKeys,
    autocompleteValues,
    serviceNames,
    spanNames,
    remoteServiceNames,
    isEnteringKey,
    keyText,
    criteria,
  ]);

  React.useEffect(() => {
    if (autocompleteKeys.includes(keyText)) {
      loadAutocompleteValues(keyText);
    }
  }, [keyText, autocompleteKeys, loadAutocompleteValues]);

  const [suggestionIndex, setSuggestionIndex] = React.useState(-1);

  const handleChange = React.useCallback(
    (event: React.ChangeEvent<HTMLInputElement>) => {
      setText(event.target.value);
      setFixedText(event.target.value);
      setSuggestionIndex(-1);
    },
    [],
  );

  const handleKeyDown = React.useCallback(
    (event: React.KeyboardEvent<HTMLInputElement>) => {
      switch (event.key) {
        case 'Enter':
          event.preventDefault();
          if (isEnteringKey) {
            if (!text) {
              onDecide();
              return;
            }
            const newText = `${text}=`;
            setText(newText);
            setFixedText(newText);
            setSuggestionIndex(-1);
          } else {
            setFixedText(text);
            setSuggestionIndex(-1);
            onDecide();
          }
          break;
        case 'ArrowUp': {
          event.preventDefault();
          if (
            isLoadingSuggestions ||
            !suggestions ||
            suggestions.length === 0 ||
            !(suggestionIndex > 0)
          ) {
            break;
          }
          const nextSuggestionIndex = suggestionIndex - 1;
          setSuggestionIndex(nextSuggestionIndex);
          if (isEnteringKey) {
            setText(suggestions[nextSuggestionIndex]);
          } else {
            setText(`${keyText}=${suggestions[nextSuggestionIndex]}`);
          }
          break;
        }
        case 'ArrowDown':
        case 'Tab': {
          event.preventDefault();
          if (
            isLoadingSuggestions ||
            !suggestions ||
            suggestions.length === 0
          ) {
            break;
          }
          let nextSuggestionIndex: number;
          if (suggestionIndex === suggestions.length - 1) {
            nextSuggestionIndex = 0;
          } else {
            nextSuggestionIndex = suggestionIndex + 1;
          }
          setSuggestionIndex(nextSuggestionIndex);
          if (isEnteringKey) {
            setText(suggestions[nextSuggestionIndex]);
          } else {
            setText(`${keyText}=${suggestions[nextSuggestionIndex]}`);
          }
          break;
        }
        case 'Escape': {
          onDecide();
          break;
        }
        default:
          break;
      }
    },
    [
      isEnteringKey,
      isLoadingSuggestions,
      text,
      keyText,
      suggestionIndex,
      suggestions,
      onDecide,
    ],
  );

  const handleDeleteButtonClick = React.useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.stopPropagation();
      onDelete();
    },
    [onDelete],
  );

  const handleSuggestionItemClick = (index: number) => () => {
    if (!suggestions) {
      return;
    }
    if (isEnteringKey) {
      const newText = `${suggestions[index]}=`;
      setText(newText);
      setFixedText(newText);
      setSuggestionIndex(-1);
      if (inputEl.current) {
        // When the suggestion is clicked, the focus is removed from input.
        // So need to refocus.
        inputEl.current.focus();
      }
    } else {
      const newText = `${keyText}=${suggestions[index]}`;
      setText(newText);
      setFixedText(newText);
      setSuggestionIndex(-1);
      onDecide();
    }
  };

  if (!isFocused) {
    return (
      <Box
        display="flex"
        height={40}
        borderRadius={3}
        boxShadow={1}
        overflow="hidden"
        onClick={onFocus}
        mr={1}
        fontSize="1.1rem"
        color="common.white"
        className={classes.root}
      >
        <Box
          maxWidth={150}
          height="100%"
          bgcolor="primary.dark"
          p={1}
          overflow="hidden"
          whiteSpace="nowrap"
          textOverflow="ellipsis"
        >
          {criterion.key}
        </Box>
        {criterion.value && (
          <Box
            maxWidth={200}
            height="100%"
            bgcolor="primary.main"
            p={1}
            overflow="hidden"
            whiteSpace="nowrap"
            textOverflow="ellipsis"
          >
            {criterion.value}
          </Box>
        )}
        <button
          type="button"
          onClick={handleDeleteButtonClick}
          className={classes.deleteButton}
        >
          <FontAwesomeIcon icon={faTimes} size="lg" />
        </button>
      </Box>
    );
  }

  return (
    <ClickAwayListener onClickAway={onBlur}>
      <Box mr={2} position="relative">
        <input
          ref={inputEl}
          value={text}
          onKeyDown={handleKeyDown}
          onChange={handleChange}
          className={classes.input}
        />
        {(isLoadingSuggestions ||
          (suggestions && suggestions.length !== 0)) && (
          <SuggestionList
            suggestions={suggestions || []}
            isLoadingSuggestions={isLoadingSuggestions}
            suggestionIndex={suggestionIndex}
            onItemClick={handleSuggestionItemClick}
          />
        )}
      </Box>
    </ClickAwayListener>
  );
};

export default CriterionBox;
