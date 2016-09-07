/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    ScrollView,
    AppRegistry,
    StyleSheet,
    Text,
    View, TouchableOpacity
} from 'react-native';

import {SwipeView} from 'react-native-swipe-view';

class example extends Component {
  render() {
    return (
        <ScrollView contentContainerStyle={styles.container}>
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
          {this.renderSwipeView()}
        </ScrollView>
    );
  }

  renderSwipeView() {
    return (
      <SwipeView changeOpacity removeViewOnSwipedOut style={{borderWidth: 4}}
                 onSwipeStart={(event) => console.warn('Started swipe!')}
                 onWillBeSwipedOut={(event) => console.warn('onWillBeSwipedOut!')}
                 onSwipedOut={(event) => console.warn('onSwipedOut!')}
                 onWillBounceBack={(event) => console.warn('onWillBounceBack!')}
                 onBouncedBack={(event) => console.warn('onBouncedBack!')}
      >
        <TouchableOpacity onPress={()=>console.error('BAM')}>
          <View>
          <Text style={styles.welcome}>
            Welcome to React Native!
          </Text>
          <Text style={styles.instructions}>
            To get started, edit index.ios.js
          </Text>
          <Text style={styles.instructions}>
            Press Cmd+R to reload,{'\n'}
            Cmd+D or shake for dev menu
          </Text>
            </View>
        </TouchableOpacity>
      </SwipeView>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF'
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5
  }
});

AppRegistry.registerComponent('example', () => example);
