//
//  SwipeableViewManager.m
//  reactNativeSwipeableView
//
//  Created by Artal Druk on 12/05/2016.
//  Copyright © 2016 Wix.com. All rights reserved.
//

#import "SwipeViewManager.h"
#import "RCTScrollView.h"
#import "RCTRootView.h"

#define kMinPanToCompleteUndefined -1
#define kDefaultBounceBackDuration 0.35
#define kDefaultSpringDamping 0.65

@interface SwipeView : UIView <UIGestureRecognizerDelegate>
@property (nonatomic) CGFloat minPanToComplete;
@property (nonatomic) CGPoint originalCenter;
@property (nonatomic) BOOL changeAlpha;
@property (nonatomic) BOOL removeViewOnSwipedOut;
@property (nonatomic) CGFloat bounceBackAnimDuration;
@property (nonatomic) CGFloat bounceBackAnimDamping;
@property (nonatomic, copy) RCTDirectEventBlock onSwipeStart;
@property (nonatomic, copy) RCTDirectEventBlock onWillBeSwipedOut;
@property (nonatomic, copy) RCTDirectEventBlock onSwipedOut;
@property (nonatomic, copy) RCTDirectEventBlock onWillBounceBack;
@property (nonatomic, copy) RCTDirectEventBlock onBouncedBack;

@property (nonatomic, retain) UIScrollView *containerScrollVIew;
@end

@implementation SwipeView

- (instancetype)initWithFrame:(CGRect)frame
{
  self = [super initWithFrame:frame];
  if (self)
  {
    self.changeAlpha = NO;
    self.removeViewOnSwipedOut = NO;
    self.minPanToComplete = kMinPanToCompleteUndefined;
    self.bounceBackAnimDuration = kDefaultBounceBackDuration;
    self.bounceBackAnimDamping = kDefaultSpringDamping;
    
    UIPanGestureRecognizer *panGesture = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(onPanGesture:)];
    panGesture.delegate = self;
    [self addGestureRecognizer:panGesture];
  }
  return self;
}

- (UIScrollView*)getScrollView
{
  UIView *view = self;
  while (view.superview != nil)
  {
    view = view.superview;
    if ([view isKindOfClass:[RCTScrollView class]])
    {
      return ((RCTScrollView*)view).scrollView;
    }
  }
  return nil;
}

-(void)restoreScrolling
{
  if (self.containerScrollVIew != nil)
  {
    self.containerScrollVIew.scrollEnabled = YES;
    self.containerScrollVIew = nil;
  }
}

-(RCTRootView*)getRootView
{
    UIView *view = self;
    while (view.superview != nil)
    {
        view = view.superview;
        if ([view isKindOfClass:[RCTRootView class]])
            break;
    }
    
    if ([view isKindOfClass:[RCTRootView class]])
    {
        return view;
    }
    return nil;
}

-(void)cancelCurrentTouch
{
  RCTRootView *view = [self getRootView];
  if (view != nil)
  {
    [(RCTRootView*)view cancelTouches];
  }
}

- (BOOL)gestureRecognizerShouldBegin:(UIPanGestureRecognizer *)panGesture
{
  CGPoint velocity = [panGesture velocityInView:self];
  return fabs(velocity.y) < fabs(velocity.x);
}

-(void)onPanGesture:(UIPanGestureRecognizer*)panGesture
{
  if(panGesture.state == UIGestureRecognizerStateBegan)
  {
    self.originalCenter = self.center;
    if (self.minPanToComplete == kMinPanToCompleteUndefined)
    {
      self.minPanToComplete = self.frame.size.width * 0.5;
    }
    
    UIScrollView *containerScrolView = [self getScrollView];
    if (containerScrolView != nil && containerScrolView.scrollEnabled)
    {
      containerScrolView.scrollEnabled = NO;
      self.containerScrollVIew = containerScrolView;
    }
    
    [self cancelCurrentTouch];
      
    NSString *directionString = ([panGesture velocityInView:self].x < 0) ? @"left" : @"right";
    if (_onSwipeStart)
    {
      _onSwipeStart(@{@"direction": directionString});
    }
  }
  else if(panGesture.state == UIGestureRecognizerStateChanged)
  {
    CGPoint translation = [panGesture translationInView:self.superview];
    self.center = CGPointMake(self.originalCenter.x + translation.x, self.originalCenter.y);
    
    if (self.changeAlpha)
    {
      self.alpha = 1.0 - 0.9 * MIN(1, fabs(translation.x) / self.minPanToComplete);
    }
  }
  else if (panGesture.state == UIGestureRecognizerStateEnded ||
           panGesture.state == UIGestureRecognizerStateFailed ||
           panGesture.state == UIGestureRecognizerStateCancelled)
  {
    CGFloat centerDiff = self.originalCenter.x - self.center.x;
    CGFloat velocityX = [panGesture velocityInView:self].x;
    int direction = (velocityX < 0) ? -1 : 1;
    NSString *directionString = (direction < 0) ? @"left" : @"right";
    if (fabs(centerDiff) > self.minPanToComplete || fabs(velocityX) > 2000)
    {
      if (_onWillBeSwipedOut)
      {
        _onWillBeSwipedOut(@{@"direction": directionString});
        self.onWillBeSwipedOut = nil;
      }
      
      CGFloat distanceForSwipe = fabs(centerDiff);
      UIView *parentView = [self getRootView];
      if(parentView != nil)
      {
          distanceForSwipe = parentView.frame.size.width - self.center.x + self.frame.size.width * 0.5;
      }
        
      NSTimeInterval duration = MIN(0.3, fabs(self.minPanToComplete / velocityX));
      [UIView animateWithDuration:duration
                       animations:^()
       {
         self.center = CGPointMake(self.center.x + distanceForSwipe * direction, self.center.y);
       }
                       completion:^(BOOL finished)
       {
         if (_onSwipedOut)
         {
           _onSwipedOut(@{@"direction": directionString});
           self.onSwipedOut = nil;

           if(self.removeViewOnSwipedOut)
           {
             [self removeFromSuperview];
           }
         }
         
         [self restoreScrolling];
       }];
    }
    else
    {
      [self restoreScrolling];
      
      if (_onWillBounceBack)
      {
        _onWillBounceBack(@{});
      }
      
      [UIView animateWithDuration:self.bounceBackAnimDuration
                            delay:0 usingSpringWithDamping:self.bounceBackAnimDamping
            initialSpringVelocity:0
                          options:(UIViewAnimationOptionCurveEaseOut | UIViewAnimationOptionAllowUserInteraction)
                       animations:^()
       {
         self.center = self.originalCenter;
         if (self.changeAlpha)
         {
           self.alpha = 1;
         }
       } completion:^(BOOL finished)
       {
         if (_onBouncedBack)
         {
           _onBouncedBack(@{});
         }
       }];
    }
  }
}

@end

@implementation SwipeViewManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
  return [SwipeView new];
}

RCT_REMAP_VIEW_PROPERTY(changeOpacity, changeAlpha, BOOL)
RCT_REMAP_VIEW_PROPERTY(removeViewOnSwipedOut, removeViewOnSwipedOut, BOOL)
RCT_REMAP_VIEW_PROPERTY(minPanToComplete, minPanToComplete, CGFloat)
RCT_REMAP_VIEW_PROPERTY(bounceBackAnimDuration, bounceBackAnimDuration, CGFloat)
RCT_REMAP_VIEW_PROPERTY(bounceBackAnimDamping, bounceBackAnimDamping, CGFloat)
RCT_REMAP_VIEW_PROPERTY(onSwipeStart, onSwipeStart, RCTDirectEventBlock)
RCT_REMAP_VIEW_PROPERTY(onWillBeSwipedOut, onWillBeSwipedOut, RCTDirectEventBlock)
RCT_REMAP_VIEW_PROPERTY(onSwipedOut, onSwipedOut, RCTDirectEventBlock)
RCT_REMAP_VIEW_PROPERTY(onWillBounceBack, onWillBounceBack, RCTDirectEventBlock)
RCT_REMAP_VIEW_PROPERTY(onBouncedBack, onBouncedBack, RCTDirectEventBlock)

@end
