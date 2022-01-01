[![Github All Releases](https://img.shields.io/github/downloads/SIMULATAN/meteor-notifications-addon/total.svg)](https://github.com/SIMULATAN/meteor-notifications-addon/releases) [![Verified](https://img.shields.io/badge/Verified%3F-yes-lime)](https://anticope.ml/pages/MeteorAddons.html#utilities) [![My Discord (505713760124665867)](https://img.shields.io/badge/My-Discord-%235865F2.svg)](https://discord.com/users/505713760124665867) [![wakatime](https://wakatime.com/badge/github/SIMULATAN/meteor-notifications-addon.svg)](https://wakatime.com/badge/github/SIMULATAN/meteor-notifications-addon)

# Meteor Notifications addon
This addon adds various (very customizable) notifications to the HUD of the [Meteor Client](https://github.com/MeteorDevelopment/meteor-client).

### Support
If you need any help, feel free to [DM me on Discord](https://discord.com/users/505713760124665867)

### How to build yourself
- Clone this repository
- Navigate into the folder in a shell
- Run `./gradlew build`
- Copy the `build/libs/meteor-notifications-addon-*.jar` file to your `mods` folder

### How to develop
- Clone this repository
- Import the project in IntelliJ
- Configure IntelliJ to use JDK 17
- Change the `Gradle JVM` option to `Project SDK` (`Build, Execution, Deployment -> Build Tools -> Gradle`)
- Restart IntelliJ after the initial build to load the run configurations

##### Optional: 
(Faster build times and better hot swap)

    - Open the 'Gradle Settings' dialog from the Gradle tab.
    - Change the 'Build and run using' and 'Run tests using' fields to 'IntelliJ IDEA'.
    - Go to File → Project Structure → Project and set 'Project compiler output' to $PROJECT_DIR$/out.
Source: [Fabric Wiki](https://fabricmc.net/wiki/tutorial:setup#intellij_idea)

### Contributions
... are welcome! Feel free to modify the code and submit a pull request or open up a issue.
