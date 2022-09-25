# Tests project

This project is simply a boilerplate for selenium testing. I will try to write a project that
can be run on any OS with both chrome and firefox. All you then need is to simply wirte the
test cases.

## Notice

For this to work on a Windows system please download appropriate chromdirver.exe and place it in
the src/main/resources/drivers/ folder. Make sure it is called chromedriver.exe.

## Current state

Currently you can write tests using only the chromedriver. I still havent added the functionallity
to run tests using the firefox driver but soon it will be done.
For now all you need to properly run the tests is provide flags as you would to the chromedriver
itself for exaple:

``` bash
tests.jar --headless --widnow-size=1920,1200
```

This will run the tests in headless mode with given window size. One flag is added by default which is the
'--incognito' flag. So that the chahe is cleaned. Without this the ability to autogenerate a Downloads/
folder does not work properly.

There is also a functionallity to check downloaded PDF files in test cases. Using the Apache PDFBox package
you can check the integrity of the PDF files. In future releases you will be able to do the same for excel
files.

## To build the .jar

If you want to build a .jar file (tests.jar) you need Maven installed and then you simply run:

``` bash
mvn clean install
```

This will create a tests.jar file inside the target folder to be used for running the written tests.

## Example

I have written an example test class called FirstTest.java. whenever I add another functionallity I will update
said class to show the new functionallity off.
