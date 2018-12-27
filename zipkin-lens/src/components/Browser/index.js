import PropTypes from 'prop-types';
import React from 'react';

import TracesContainer from '../../containers/Browser/Traces/TracesContainer';

const propTypes = {
  location: PropTypes.shape({}).isRequired,
  clearTraces: PropTypes.func.isRequired,
};

class Browser extends React.Component {
  componentWillUnmount() {
    const { clearTraces } = this.props;
    clearTraces();
  }

  render() {
    const { location } = this.props;
    return (
      <div>
        <TracesContainer location={location} />
      </div>
    );
  }
}

Browser.propTypes = propTypes;

export default Browser;
