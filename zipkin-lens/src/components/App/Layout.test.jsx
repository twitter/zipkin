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
import React from 'react';

import render from '../../test/util/render-with-default-settings';

import Layout from './Layout';

describe('<Layout />', () => {
  it('does not render help link with default config', () => {
    // children is required so avoid warning by passing dummy children
    const { queryByTitle } = render(
      <Layout>
        <span>Test</span><span>Test</span>
      </Layout>
    );
    expect(queryByTitle('Help')).not.toBeInTheDocument();
  });

  it('does render help link when defined', () => {
    // children is required so avoid warning by passing dummy children
    const { getByTitle } = render(
      <Layout>
        <span>Test</span><span>Test</span>
      </Layout>,
      {
        uiConfig: {
          helpUrl: 'https://gitter.im/openzipkin/zipkin',
        },
    });
    const helpLink = getByTitle('Help');
    expect(helpLink).toBeInTheDocument();
    expect(helpLink.href).toEqual('https://gitter.im/openzipkin/zipkin');
  });
});
