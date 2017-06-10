/*
 * Copyright 2015-2017 The OpenZipkin Authors
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
import {component} from 'flightjs';
import chosen from 'chosen-npm/public/chosen.jquery.js'; // eslint-disable-line no-unused-vars

export default component(function serviceNameFilter() {
  this.onChange = function(e, params) {
    if (params.selected === '') return;

    this.trigger(document, 'uiAddServiceNameFilter', {value: params.selected});
    this.$node.val('');
    this.$node.trigger('chosen:updated');
  };

  this.after('initialize', function() {
    this.$node.chosen({search_contains: true});
    this.on('change', this.onChange);
  });
});
