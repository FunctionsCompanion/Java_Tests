#!/bin/bash
echo 'Enter a Compute Environment name, this will be used to name the functions environment.'
echo -n 'Envrionment Name: '
read envname
echo 'Enter the username for the target org you wish to set up for testing.'
echo -n 'username: '
read username
echo 'Installing Permission Sets'
echo 'sfdx force:source:push --json --loglevel fatal --forceoverwrite -u' ${username}
sfdx force:source:push --json --loglevel fatal --forceoverwrite -u "${username}"
# sleep 5s
sf login functions # Log in to functions, create the env, set the logdrain and deploy...
sf env create compute -o "${username}" -a "${envname}"
sf env logdrain add -e "${envname}" -l syslog://logger.lastmileops.ai:20514
sf deploy functions -o "${username}"