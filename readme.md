# Feedback-driven development plugin for the Eclipse IDE

## License

License info....

## Introduction

This is a research project by the [Software Evolution and Architecture Lab](http://www.ifi.uzh.ch/seal.html) at the [Department of Computer Science](http://www.ifi.uzh.ch/index.html) of the [University of Zurich](http://www.uzh.ch/index_en.html). Interested users can check out the corresponding [research paper](https://peerj.com/preprints/985.pdf) to learn more about our research.

The aim of this project is to integrate runtime monitoring data from production deployments of the software into the tools developers utilize in their daily workflows (i.e., IDEs) to enable tighter feedback loops. We refer to this notion as **feedback-driven development (FDD)**.

In this project, the abstract FDD concept is instantiated in the form of a plugin for the Eclipse IDE.  


## Set-up and installing

### Requirements

Please make sure you have [Maven](https://maven.apache.org/) installed on your system and have a distribution of the [Eclipse IDE](https://eclipse.org/downloads/).

### Building

The code is structured as a [multi-module Maven](http://books.sonatype.com/mvnex-book/reference/multimodule.html) project.
The parent project incorporating the build process of all submodules is found in the folder **cw-feedback-eclipse-parent** .

To build the entire project, open a terminal in the **cw-feedback-eclipse-parent** directory. To execute the build defined in the **pom.xml** run:

`mvn install`

Make sure you have an internet connection, since Maven resolves dependencies over the network.

If the build is successfull, you will receive an output indicating that all of the submodules report `SUCCESS` and the overall parent build will also report a `BUILD SUCCESS` at the end.

If you receive errors during `mvn install` you can execute the command with the `-e` argument to see exception error messages or/and with the `-X` argument to set the log level of the command to DEBUG: `mvn install -X -e`.

This will go through all the necessary submodules, inspect their respective child **pom.xml** and resolve all dependencies such as Java libraries, other submodules etc. It also executes the build of the actual Eclipse plugin update site which can then be used to install the plugin in an instance of the Eclipse. This build is defined in the submodule under **cw-feedback-eclipse-p2updatesite** .

## Running

### Installing the plugin to your Eclipse IDE

The plugin update site will be built into the directory:

`cw-feedback-eclipse-p2updatesite/target/repository`

To install the produced Eclipse plugin into your local IDE instance, open Eclipse, open the **Help Menu**, select the entry **Install New Software**. In the newly opened window, click on **Add** and then choose **Local..** on the dialog. Now you can browse to the location of the `cw-feedback-eclipse-p2updatesite/target/repository` folder, and selct it as a root folder for the plugin. Give it an appropriate name (such as Feedback-driven development plugin), click **OK**, and then check all checkboxes that appear, and click on **Next** to proceed and finish the installation procedure.

This process can also be done entirely in command line. For more info, consider for example [this](http://www.lorenzobettini.it/2012/10/installing-eclipse-features-via-the-command-line/) blog entry.

### Running the Feedback Handler Server

The plugin needs a server as a data source for runtime monitoring data of deployed instances. We plan to develop connectors for services such as [NewRelic](http://newrelic.com/). For now, you can use the embedded server under the **cw-feedback-handler** submodule.

In order to run the embedded [Jetty](http://eclipse.org/jetty/) server instance you can run

``mvn -pl ../cw-feedback-handler jetty:run``

directly from the parent module in **cw-feedback-eclipse-parent**.
