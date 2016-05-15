/**
 * Created by artald on 15/05/2016.
 */

import React, { Component } from 'react';
import {
  requireNativeComponent
} from 'react-native';

const NativeSwipeView = requireNativeComponent('SwipeView', null);

export default class SwipeView extends React.Component {
  render() {
    return <NativeSwipeView {...this.props} />;
  }
}