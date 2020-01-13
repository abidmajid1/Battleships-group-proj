# GroupProject--7

Depending on the operating system you're using you will have to change the natives locations.

	- Right click on the project and select "Build Path"
	- Next select "Configure build path"
	- Open up the ModulePath and the JRE System Library path
	- Highlight the native library location and click edit
	- Select workspace and within the Project folder navigate to the natives folder
	- Select your workspace and click apply and close.
	
	
Null pointer error after displaying available display modes

	- In the DisplayManager.java file
	- Change the WIDTH and HEIGHT fields to one of the resolutions selected
	
```
private static final int WIDTH = 1280;
private static final int HEIGHT = 800;
```
	
	
	- Save and Run
	
	
