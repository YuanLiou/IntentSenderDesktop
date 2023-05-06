# IntentSenderDesktop
A tool for sending deeplink Intent to an Android app for development

### macOS screenshot
<img width="600" src="https://user-images.githubusercontent.com/4803452/235935582-4107d675-bd4a-4d0e-ba1c-570f0f1ed609.png">

### Windows screenshot
<img width="600" src="https://user-images.githubusercontent.com/4803452/236091481-c11f6795-9ac3-4d2e-ac11-950bbe808144.png">

### Pop!_OS (22.04) screenshot
<img width="600" src="https://user-images.githubusercontent.com/4803452/236627123-7144772f-2288-4a01-8397-b5376c8d2202.jpg">

---
## How to launch the app on macOS?
This window might popup when you trying to launch the jar program.

<img width="250" src="https://user-images.githubusercontent.com/4803452/236611556-d5481950-24f0-40d7-ae98-d47d906b9707.png">

This is because macOS need the certificate which signed by a Apple Developer Program enrolled developer. However, I'm not join the program.

You can **right click** the app icon, and select **open**.

<img width="506" src="https://user-images.githubusercontent.com/4803452/236611925-7bf77075-eb6a-4491-96ec-927c76878436.png">

A popup will shown and ask you: are you sure you want to open it? Please select the Open button to open the program.

<img width="250" src="https://user-images.githubusercontent.com/4803452/236611772-de223c7a-85a1-44fa-9209-ffb48acd726d.png">

or you can build yourself. When launching the app, macOS won't ask you like above because it is you signed your own build.

### Build 

#### Jar

For Mac machines in Intel chip, you might need to build yourself.

Please pull this project into your computer, you also need

  - Gradle
  - Java 17
  
installed on your machine.

Use this command to package a jar executable:

```bash
$ ./gradlew packageReleaseUberJarForCurrentOS
```

You can find the jar file at: `<projectDirectory>\build\compose\jars\`

<img width="800" alt="image" src="https://user-images.githubusercontent.com/4803452/236612288-09ddf3df-e79c-4172-8a5c-71ee669538b7.png">

#### Dmg

You can also build Dmg installer file yourself. The command below will generate DMG install file and the app file for you.

```bash
$  ./gradlew packageReleaseDmg 
```

You can find the dmg file and app file at: `<projectDirectory>\build\binaries\main-release\`

<img width="800" alt="SCR-20230506-okka-2" src="https://user-images.githubusercontent.com/4803452/236612562-4e383f72-5ecf-4b68-bb2e-774d1508723e.png">


## other project for learning Android
 - [AndroidLifecycleExplorer](https://github.com/YuanLiou/AndroidLifecycleExplorer): Learn the lifecycle of Android's Fragment
