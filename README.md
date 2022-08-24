# Java Test Project
This repo contains a Salesforce Project for testing Functions Companion that includes Java functions. It also includes an installation and setup script to install Functions Companion into an org.

The Project includes two Java functions based on two examples from the [Salesforce Functions Recipes](https://github.com/trailheadapps/functions-recipes) repository. The [01_Intro_ProcessLargeData_Java](https://github.com/trailheadapps/functions-recipes/tree/main/functions/) and [03_Context_SalesforceSDK_Java](https://github.com/trailheadapps/functions-recipes/tree/main/functions/) recipes. See the Functions Companion [Developers Guide](https://functionscompanion.github.io/DevelopersGuide/) to see how they have been instrumented to work with Functions Companion.

## Prerequsites

1. A Salesforce DevHub with [Functions enabled](https://developer.salesforce.com/docs/platform/functions/guide/configure_your_org.html).
2. DevHub Org permissions to create Scratch Orgs and [Connected Apps](https://help.salesforce.com/s/articleView?id=sf.connected_app_overview.htm&type=5).
3. A Sandbox or Scratch org and the username for the target org where Functions Companion will be installed.
3. A user account for [Functions Companion](https:app.lastmileops.ai).

If a Sandbox or Scratch org is not yet available, a `build.sh` script is provide to build a scratch org for testing.

## Functions Companion Setup and Configuration

See [Installation and Configuration](https://functionscompanion.github.io/InstallAndConfig/)
documentation for details on how to install and configure Functions Companion. The [Getting Started Guide](https://functionscompanion.github.io/GettingStarted/) will show you how to set up your Connecteced App for Functions Companion and begin observing the performance of your functions
deployments.

## Java Test Deployment and Invocation
Once Functions Companion is installed in your org, you can deploy this project by running `deployTestFunctions.sh`. This script asks for a name for the compute environment it will create and reqires the username of the target org that has Functions Commander installed. The script also configures a logdrain that points to the Functions Companion platform syslog endpoint and deploys the two Java functions in this project.

**Important: Before you run any tests, update the invocation test scripts with the scratch org username
used for anonomyous Apex invocation.**

### Test Scripts
The table below includes the list of available test scripts, the functions they run, and how they are invoked.

| Test | Large Data | SDK |  Status |
|------|------------|-----|---------|
|[runAllSync.sh](runAllSync.sh)        |Sync|Sync | All Tests Pass |
|[runAllAsync.sh](runAllAsync.sh)        |Async|Async | All Tests Pass |
|[runAllSyncandAsync.sh](runAllSyncandAsync.sh)        |Sync and Async|Sync and Async | All Tests Pass |


Each script asks for an itteration count so performance and capacity tests can be performed. The QnA function is very memory intensive and will consume more than 3GB of memory, which can be used to veryify error logging of Functions Companion.

### Instrument Your Own Function

Once you have Functions Companion installed and running, you can instrument your own functions in your own Projects to get visability and control over your invocations. 

Instrumenting your functions and Project required three steps:

1. Install the Apex logger ([apexsyslogjs](https://github.com/FunctionsCompanion/Java_Tests/tree/main/functions/apexsyslogjs) in this repo)
2. Instrument your functions with the Java logger
3. Modify your Apex invocations to use the Functions Commander 'wrapper' class.

See [Developers Guide](https://functionscompanion.github.io/DevelopersGuide/) for details.
