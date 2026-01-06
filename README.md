# rotten-apple

this is a simple mod for Minecraft version rd-132211 that adds a video animation of [bad apple](https://www.youtube.com/watch?v=FtutLA63Cp8).

the source code is visible, and you can compile it yourself with IntelliJ.

## using the mod

because rd-132211 doesn't have a mod loader, you must inject the .jar file manually. The easiest way to do this is using a custom minecraft launcher like [prism launcher](https://prismlauncher.org/).

**step 1:** create an instance of rd-132211 in prism launcher after you finish setting up.

![launcher setup](screenshots/launcher.png)

**step 2:** download `rotten-apple.jar` and `frames.zip` from [releases](https://github.com/yorunoken/rotten-apple/releases/latest).

**step 3:** on your instance, right click -> edit then navigate to `Version`.

**step 4:** click on `Add to Minecraft.jar`, and select the `rotten-apple.jar` you downloaded, and close the tab.

![adding the jar file](screenshots/adding_jar.png)

**step 5:** on your  instance, right click -> folder and navigate into the `minecraft` folder. There, unzip your `frames.zip` file you downloaded. Make sure the images are in the `frames` folder, the final path should look like `minecraft/frames/image_001.png`.

![adding the frames](screenshots/frames.png)

you're done! you can now boot your minecraft, and wait until all the frames load, and click `B` to start the animation. The black tile you can see when spawning in is the top most left pixel of the animation, so you can position yourself accordingly.