import { buildQueryParameters } from './api';

export const lookbackDurations = {
  '1h': 3600000,
  '2h': 7200000,
  '6h': 21600000,
  '12h': 43200000,
  '1d': 86400000,
  '2d': 172800000,
  '7d': 604800000,
};

export const orderedConditionKeyList = autocompleteKeys => ([
  'serviceName',
  'spanName',
  'minDuration',
  'maxDuration',
  ...autocompleteKeys,
  'annotationQuery',
]);

export const isAutocompleteKey = (conditionKey) => {
  switch (conditionKey) {
    case 'serviceName':
    case 'spanName':
    case 'minDuration':
    case 'maxDuration':
    case 'annotationQuery':
      return false;
    default:
      return true;
  }
};

export const defaultConditionValues = (conditionKey) => {
  switch (conditionKey) {
    case 'serviceName':
      return undefined;
    case 'spanName':
      return undefined;
    case 'minDuration':
      return 10;
    case 'maxDuration':
      return 100;
    case 'annotationQuery':
      return '';
    default: // autocompleteKeys
      return undefined;
  }
};

// Returns a key of search condition to be generated next.
export const nextInitialConditionKey = (conditions, autocompleteKeys) => {
  const conditionKeyList = orderedConditionKeyList(autocompleteKeys);
  const existingConditionsMemo = {};
  conditions.forEach((condition) => {
    existingConditionsMemo[condition.key] = true;
  });

  for (let i = 0; i < conditionKeyList.length; i += 1) {
    const conditionKey = conditionKeyList[i];
    if (!existingConditionsMemo[conditionKey]) {
      return conditionKey;
    }
  }
  return 'annotationQuery';
};

export const buildQueryParametersWithConditions = (
  conditions, lookbackCondition, limitCondition,
) => {
  const annotationQueryConditions = [];
  const autocompleteTags = [];
  const conditionMap = {};

  conditions.forEach((condition) => {
    switch (condition.key) {
      case 'serviceName':
      case 'spanName':
      case 'minDuration':
      case 'maxDuration':
        conditionMap[condition.key] = condition.value;
        break;
      case 'annotationQuery':
        annotationQueryConditions.push(condition.value);
        break;
      default: // autocompleteTags
        autocompleteTags.push(`${condition.key}=${condition.value}`);
        break;
    }
  });
  conditionMap.annotationQuery = annotationQueryConditions.join(' and ');
  conditionMap.autocompleteTags = autocompleteTags.join(' and ');
  conditionMap.limit = limitCondition;
  conditionMap.lookback = lookbackCondition.value;
  conditionMap.endTs = lookbackCondition.endTs;
  if (lookbackCondition.value === 'custom') {
    conditionMap.startTs = lookbackCondition.startTs;
  }

  return buildQueryParameters(conditionMap);
};

// Build query parameters of api/v2/traces API with query parameters of the
// trace page URL.
export const buildApiQueryParameters = (queryParameters) => {
  const result = {};
  let annotationQuery;
  Object.keys(queryParameters).forEach((conditionKey) => {
    const conditionValue = queryParameters[conditionKey];
    switch (conditionKey) {
      case 'serviceName':
      case 'spanName':
      case 'minDuration':
      case 'maxDuration':
      case 'limit':
        result[conditionKey] = conditionValue;
        break;
      case 'annotationQuery':
        if (typeof annotationQuery === 'undefined') {
          annotationQuery = conditionValue;
        } else {
          annotationQuery = annotationQuery.concat(' and ', conditionValue);
        }
        break;
      case 'autocompleteTags':
        if (typeof annotationQuery === 'undefined') {
          annotationQuery = conditionValue;
        } else {
          annotationQuery = annotationQuery.concat(' and ', conditionValue);
        }
        break;
      case 'lookback':
        switch (conditionValue) {
          case '1h':
          case '2h':
          case '6h':
          case '12h':
          case '1d':
          case '2d':
          case '7d':
            result.endTs = queryParameters.endTs;
            result.lookback = String(lookbackDurations[conditionValue]);
            break;
          case 'custom':
            result.endTs = queryParameters.endTs;
            result.lookback = String(
              parseInt(queryParameters.endTs, 10) - parseInt(queryParameters.startTs, 10),
            );
            break;
          default:
            break;
        }
        break;
      default:
        break;
    }
  });
  result.annotationQuery = annotationQuery;
  return result;
};

export const extractConditionsFromQueryParameters = (queryParameters) => {
  const conditions = [];
  const lookbackCondition = {};
  let limitCondition = 0;

  Object.keys(queryParameters).forEach((conditionKey) => {
    const conditionValue = queryParameters[conditionKey];
    switch (conditionKey) {
      case 'serviceName':
      case 'spanName':
        conditions.push({
          key: conditionKey,
          value: conditionValue,
        });
        break;
      case 'minDuration':
      case 'maxDuration':
        conditions.push({
          key: conditionKey,
          value: parseInt(conditionValue, 10),
        });
        break;
      case 'annotationQuery':
        conditionValue.split(' and ').forEach((annotationQuery) => {
          conditions.push({
            key: 'annotationQuery',
            value: annotationQuery,
          });
        });
        break;
      case 'autocompleteTags':
        conditionValue.split(' and ').forEach((autocompleteTag) => {
          const splitted = autocompleteTag.split('=');
          conditions.push({
            key: splitted[0],
            value: splitted[1],
          });
        });
        break;
      case 'limit':
        limitCondition = parseInt(conditionValue, 10);
        break;
      case 'lookback':
        switch (conditionValue) {
          case '1h':
          case '2h':
          case '6h':
          case '12h':
          case '1d':
          case '2d':
          case '7d': {
            lookbackCondition.value = conditionValue;
            lookbackCondition.endTs = parseInt(queryParameters.endTs, 10);
            break;
          }
          case 'custom':
            lookbackCondition.value = conditionValue;
            lookbackCondition.endTs = parseInt(queryParameters.endTs, 10);
            lookbackCondition.startTs = parseInt(queryParameters.startTs, 10);
            break;
          default:
            break;
        }
        break;
      default:
        break;
    }
  });
  return { conditions, lookbackCondition, limitCondition };
};

// Make the availability of the already specified conditions being false,
// the condition not specified yet being true.
export const getConditionKeyListWithAvailability = (
  currentConditionKey, conditions, autocompleteKeys,
) => {
  const existingConditionsMemo = {};

  // Memo the keys which is already used.
  conditions.forEach((condition) => {
    if (condition.key === 'annotationQuery') {
      return;
    }
    existingConditionsMemo[condition.key] = true;
  });

  const conditionKeyList = orderedConditionKeyList(autocompleteKeys);
  const result = [];
  for (let i = 0; i < conditionKeyList.length; i += 1) {
    const conditionKey = conditionKeyList[i];

    // The currently focused conditionKey is also avilable.
    if (conditionKey === currentConditionKey) {
      result.push({
        conditionKey,
        isAvailable: true,
      });
      continue;
    }

    let isAvailable = false;
    if (!existingConditionsMemo[conditionKey]) {
      isAvailable = true;
    }
    result.push({
      conditionKey,
      isAvailable,
    });
  }
  return result;
};
