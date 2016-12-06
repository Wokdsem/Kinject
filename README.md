
# Kinject

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Kinject-green.svg?style=true)](https://android-arsenal.com/details/1/3931)

A fast dependency injector for Android and Java.  
For Kinject's main documentation, please see [the website.](http://wokdsem.github.io/Kinject/)
 
## Installation

### Status
* ***Release version:*** **1.2.0**

### Download 
You will need to include the `kinject-${kinject.version}.jar` in your application's runtime.  
In order to activate code generation, you will need to include `kinject-compiler-${kinject.version}.jar`
in your build at compile time. 

In a gradle project, include the `kinject` artifact in the dependencies section of your
`build.gradle` and the `kinject-compiler` artifact as `provided` dependency:

```groovy
dependencies {
  compile  'com.wokdsem.kinject:kinject:${kinject.version}'
  provided 'com.wokdsem.kinject:kinject-compiler:${kinject.version}'
}
```

You can also find downloadable *.jar*s on jCenter.
[Kinject](https://bintray.com/artifact/download/wokdsem/maven/com/wokdsem/kinject/kinject/1.2.0/kinject-1.2.0.jar) /
[Kinject-Compiler](https://bintray.com/artifact/download/wokdsem/maven/com/wokdsem/kinject/kinject-compiler/1.2.0/kinject-compiler-1.2.0.jar).

##License

	Copyright 2016 Wokdsem

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.