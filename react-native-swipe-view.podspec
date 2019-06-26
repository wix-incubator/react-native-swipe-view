Pod::Spec.new do |spec|
  spec.name         = "react-native-swipe-view"
  spec.version      = "2.0.1"
  spec.summary      = "Native container for a React Native view which supports swipe behavior (for swipe to delete and such)"
  spec.homepage     = "https://github.com/wix/react-native-swipe-view"
  spec.license      = { :type => "MIT", :file => "LICENSE" }
  spec.author       = "Wix.com"
  spec.platform     = :ios, "9.3"
  spec.source       = { :git => "https://github.com/wix/react-native-swipe-view.git", :tag => "#{spec.version}" }
  spec.source_files  = "ios/lib"
  spec.dependency "React"
end
