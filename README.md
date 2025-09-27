# Flexi - Advanced Custom UI Library for Android

A powerful and flexible Kotlin library that provides extensive customization options for Android views with beautiful borders, gradients, shadows, and corner radius effects.

## Features

### Border Customization
- **Width Control** - Set custom border widths
- **Color Options** - Solid colors or gradient borders
- **Gradient Borders** - Multi-color gradients with intelligent distribution
- **Animations** - Clockwise and anti-clockwise gradient animations with speed control
- **Individual Corner Radius** - Set different radius for each corner

### Fill Options
- **Solid Colors** - Simple background colors
- **Linear Gradients** - Smooth color transitions with angle control
- **Radial Gradients** - Circular gradient effects from center

### Shadow Effects
- **Shadow Color** - Customizable shadow colors
- **Spread Control** - Adjust shadow spread
- **Blur Effects** - Variable blur intensity
- **Offset Options** - X and Y shadow positioning

> [!IMPORTANT]
> For shadows to be visible, the parent view must have `android:clipChildren="false"`

### Available Components
- FlexiView
- FlexiTextView
- FlexiEditText
- FlexiConstraintLayout

## Installation

Add this to your module's `build.gradle`:

```gradle
dependencies {
    implementation("com.github.bhavinsomps:flexi:v1.0.0")
}
```

Add this to your `settings.gradle`:

```
repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
```

## Usage Examples

### Basic Border Setup

```xml
<com.bs.flexi.FlexiView
    android:layout_width="200dp"
    android:layout_height="100dp"
    app:borderWidth="4dp"
    app:borderColor="#FF5722"
    app:cornerRadius="16dp" />
```

### Gradient Border with Animation

First, create a colors array in `res/values/arrays.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <array name="my_gradient">
        <item>#FFFF0000</item> <!-- red -->
        <item>#FF00FF00</item> <!-- green -->
        <item>#FF0000FF</item> <!-- blue -->
        <item>#FFFFFF00</item> <!-- yellow -->
    </array>
</resources>
```

Then use in your layout:

```xml
<com.bs.flexi.FlexiView
    android:layout_width="200dp"
    android:layout_height="100dp"
    app:borderType="gradient"
    app:borderWidth="6dp"
    app:borderGradientColors="@array/gradient_colors"
    app:borderAnimation="true"
    app:borderAnimationSpeed="30"
    app:borderAnimationDirection="clockwise"
    app:cornerRadius="20dp" />
```

### Individual Corner Radius

```xml
<com.bs.flexi.FlexiTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Custom Corners"
    app:cornerRadiusTopLeft="0dp"
    app:cornerRadiusTopRight="20dp"
    app:cornerRadiusBottomRight="0dp"
    app:cornerRadiusBottomLeft="20dp"
    app:fillColor="#E3F2FD" />
```

### Linear Gradient Fill

```xml
<com.bs.flexi.FlexiView
    android:layout_width="match_parent"
    android:layout_height="200dp"
    app:fillType="linear"
    app:fillGradientStart="#FF6B6B"
    app:fillGradientCenter="#4ECDC4"
    app:fillGradientEnd="#45B7D1"
    app:fillGradientAngle="45" />
```

### Radial Gradient Fill

```xml
<com.bs.flexi.FlexiView
    android:layout_width="200dp"
    android:layout_height="200dp"
    app:fillType="radial"
    app:fillGradientStart="#FFD93D"
    app:fillGradientEnd="#FF6B6B"
    app:cornerRadius="100dp" />
```

### Shadow Effects

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:clipChildren="false"
    android:orientation="vertical">

    <com.bs.flexi.FlexiView
        android:layout_width="150dp"
        android:layout_height="100dp"
        app:fillColor="#FFFFFF"
        app:flexiShadowColor="#33000000"
        app:flexiShadowOffsetX="8dp"
        app:flexiShadowOffsetY="8dp"
        app:flexiShadowBlur="12dp"
        app:flexiShadowSpread="2dp"
        app:cornerRadius="12dp" />

</LinearLayout>
```

## Available Attributes

### Fill Attributes

| Attribute | Type | Values | Description |
|-----------|------|---------|-------------|
| `fillType` | enum | `solid`, `linear`, `radial` | Type of fill |
| `fillColor` | color | Any color | Color for solid fill |
| `fillGradientStart` | color | Any color | Start color for gradients |
| `fillGradientCenter` | color | Any color | Center color for linear gradients |
| `fillGradientEnd` | color | Any color | End color for gradients |
| `fillGradientAngle` | float | 0-360 | Angle for linear gradients |

### Border Attributes

| Attribute | Type | Values | Description |
|-----------|------|---------|-------------|
| `borderType` | enum | `none`, `fill`, `gradient` | Type of border |
| `borderWidth` | dimension | Any dp value | Border thickness |
| `borderColor` | color | Any color | Solid border color |
| `borderGradientColors` | array | Color array | Colors for gradient borders |
| `borderAnimation` | boolean | `true`, `false` | Enable border animation |
| `borderAnimationSpeed` | integer | 0-360 | Degree per second |
| `borderAnimationDirection` | enum | `clockwise`, `anticlockwise` | Animation direction |

### Corner Radius Attributes

| Attribute | Type | Description |
|-----------|------|-------------|
| `cornerRadius` | dimension | Uniform corner radius |
| `cornerRadiusTopLeft` | dimension | Top-left corner radius |
| `cornerRadiusTopRight` | dimension | Top-right corner radius |
| `cornerRadiusBottomRight` | dimension | Bottom-right corner radius |
| `cornerRadiusBottomLeft` | dimension | Bottom-left corner radius |

### Shadow Attributes

| Attribute | Type | Description |
|-----------|------|-------------|
| `flexiShadowColor` | color | Shadow color |
| `flexiShadowOffsetX` | dimension | Horizontal shadow offset |
| `flexiShadowOffsetY` | dimension | Vertical shadow offset |
| `flexiShadowBlur` | dimension | Shadow blur radius |
| `flexiShadowSpread` | dimension | Shadow spread distance |

## Important Notes

> [!NOTE]  
> For shadows to be visible, the parent view must have `android:clipChildren="false"`

***

**Star this repo if you find it helpful!** ⭐
