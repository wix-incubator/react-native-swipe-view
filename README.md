# react-native-swipe-view
A native container which provides a smooth drag interaction with any react-native view to implement a horizontal swiping behaviour, for example: swiping a "card" view out of the screen to delete it.

## Installation
Install from `npm`:

`npm i --save react-native-swipe-view`

Now link the native libraries:

#### Android
Add to the app `build.gradle` dependencies:

```
compile project(':RNSwipeView')
```

Add to `settings.gradle`:

```
include ':RNSwipeView'
project(':RNSwipeView').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-swipe-view/android')
```

Add the package to your MainApplication.java `getPackages` list:

```
import com.wix.RNSwipeView.SwipeViewPackage;

@Override
protected List<ReactPackage> getPackages() {
  return Arrays.<ReactPackage>asList(
      //add this pacakge:
      new SwipeViewPackage()
  );
}
};
```

#### iOS
In Xcode, drag the `SwipeView.xcodeproj` from your node modules to the libraries list in the Project Navigator. Then add `libSwipeView` to your app target "Linked Frameworks and Libraries"

## Usage
`react-native-swipe-view` export a Component called `SwipeView` which you can use to wrap any hirarchy of child views that you wish to be contained and interacted with a swipe behavior.

For example:

```
//import the swipe view container
import {SwipeView} from 'react-native-swipe-view';

//use it in your render function
<SwipeView changeOpacity removeViewOnSwipedOut style={{borderWidth: 4}}>
	<Text style={styles.welcome}>
		This is a swipe view!
	</Text>
	<Text style={styles.instructions}>
		Drag it to interact
	</Text>
</SwipeView>
```

## supported props

| prop | default | type | description |
| ---- | ---- | ----| ---- |
| changeOpacity | false | Boolean | Should the component change content alphw while dragging |
| removeViewOnSwipedOut |false | Boolean | Should the component be removed from the hierarchy after it is swiped out |
| minPanToComplete | 0.5 | Number | The distance from the view center which needs to be completed in percents for the "swipe out" action to happen. If the threshold is not reached it will bounce back |
| bounceBackAnimDuration | 0.35 | Number | Duration of bounce back animation when the threshold defined in minPanToComplete is not matched |
| bounceBackAnimDamping | 0.65 | Number | Damping param of iOS bounce back animation when the threshold defined in minPanToComplete is not matched |
| onSwipeStart | - | Function | Callback function which is called when the swiping action starts. A `direction` param is provided wiht `left` or `right` value  |
| onWillBeSwipedOut | - | Function | Callback function which is called right before a view is swiped out (when it passed the minPanToComplete threshold). A `direction` param is provided wiht `left` or `right` value |
| onSwipedOut | - | Function | Callback function which is called after the "swiped out" animation is done. A `direction` param is provided wiht `left` or `right` value |
| onWillBounceBack | - | Function | Callback function which is called right before a view bounces back (when it fails to pass the minPanToComplete threshold). A `direction` param is provided wiht `left` or `right` value |
| onBouncedBack | - | Function | Callback function which is called after the "bounce back" animation is done. A `direction` param is provided wiht `left` or `right` value |
