# Simple Web Shop

This is a simple example to show how to set up a shop using the Java Servlet framework
and Google Guice as the dependency injection framework.

The code is licensed under the GPL Version 3.

## Requirements

To run it you need [Java 8][2], [Maven][3] and (optionally) [Intellij][4] for which there is a project set up.

# Using Git
    
The repository is stored in Git. We use branches to set up the various stages of development.
This initial version is on the `initial` branch.  You can see further branches as we go.
To get started, run the command

    git checkout initial

To get the initial state of the repository.

# Get started

You must have Java and Maven installed and can run Maven with the <code>mvn</code> command at a prompt.
To check this get up a command prompt and type <code>mvn --version</code> (note two hyphens).  You should
see a version number and other stuff about the Java version. You should have at least version 7 of Java.  
If you use Java 8 that's OK, but note that the Maven POM asks for Java 7, and Java 8 or higher doesn't run
on AppEngine.

Once Maven is installed then simply type

    mvn clean package exec:java

The first time this may take a while as a lot of dependencies need to be installed.  Future runs will be quicker.
There will be a lot of output finishing with:

    INFO: Dev App Server is now running

By default you are running from the address `http://localhost:9000` on the development server. All the paths
below are relative to this, which may differ for you if you change it.

Now open a web browser and go to: `http://localhost:9000/a/b/c`. You will see a message:

    Welcome, your shop is called Demo Shop and your path is /a/b/c
    
## Technology stack in the starter

The starter uses [Jetty][1] to run an embedded web server.

The main class is `Runner`. This starts an embedded Jetty Server. 

We are using the dependency injection framework [Guice][5], and in particular [Guice Servlet][6]
in order to make wiring the application together a little simpler.  When Jetty starts it runs
a filter which does all the Jetty wiring.

The single servlet at this stage is 

    DemoServlet
    
Every servlet controlled by Guice needs the `@Singleton` annotation on the class, and the `@Inject`
annotation on the constructor.  The annotated variable `shopName` then gets the value set in the `BindingModule`. 

The `RouteModule` class says which servlets are run for which request path.  In this case all paths
run the `DemoServlet` class.

The `DemoServlet` class is wired up for Guice.  Note that servlets need the `Singleton` and `Inject` annotations,
and that the automatic wiring sets up the `shopName` variable using the `ShopName` annotation.  The value is
set in the `BindingModule`.
 




[1]: https://eclipse.org/jetty/
[2]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[3]: http://maven.apache.org/download.cgi
[4]: http://www.jetbrains.com/idea/
[5]: https://github.com/google/guice/wiki/GettingStarted
[6]: https://github.com/google/guice/wiki/Servlets