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
import bootstrap // eslint-disable-line no-unused-vars
    from 'bootstrap-sass/assets/javascripts/bootstrap.js';

export default component(function infoPanel() {
  this.show = function() {
    this.$node.modal('show');
  };

  this.after('initialize', function() {
    this.$node.modal('hide');
    this.on(document, 'uiRequestInfoPanel', this.show);
  });
});
