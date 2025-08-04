# Mensa Web App

This project is designed for the Software Testing course.
It provides a web application built with JSF
and shall be used as a running example for assignments throughout the course.

[![pipeline status](https://gitlab.infosun.fim.uni-passau.de/se2/teaching/software-testing/mensawebapp/badges/main/pipeline.svg)](https://gitlab.infosun.fim.uni-passau.de/se2/teaching/software-testing/mensawebapp/-/commits/main)
[![coverage report](https://gitlab.infosun.fim.uni-passau.de/se2/teaching/software-testing/mensawebapp/badges/main/coverage.svg)](https://gitlab.infosun.fim.uni-passau.de/se2/teaching/software-testing/mensawebapp/-/commits/main)

## Setup

The repository contains a [Maven](https://maven.apache.org) project
that can be imported into the IDE directly (at least with IntelliJ).
It requires *at least JDK 16* and a recent [Apache Tomcat](https://tomcat.apache.org) web server.

After importing the project to IntelliJ,
set up a run configuration through the `Edit Configurations` dialogue.
Create a `Tomcat Server / Local` configuration.
Here you have to select the Tomcat server from the location you extracted the downloaded archive.
Furthermore, in the `Deployment` tab of the dialogue, add the artifact `mensawebapp:war exploded`.
This should be it;
by hitting the `Run` button
IntelliJ will compile the sources, start the Tomcat server, and deploy the application.
Finally,
it will open a new browser tab showing the applications main page.
