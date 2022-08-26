#!/bin/bash 
# Ask users for prefix
# Need to login to functions as well to get the orgid...
echo 'Please provide the username for the target org you wish to install Functions Companion.'
echo -n 'username:'
read username

sf login functions 
export FC_ORG_ID=`sf env list --all | grep ${username} | awk '{print $4}'`
export FC_HOST="https://app.lastmileops.ai"

# Check to see if this is local or dev install
if [ $# -eq 1 ] && [ "$1" == "local" ]
then
  export FC_HOST="http://localhost:3000"
else 
    if [ $# -eq 1 ] && [ "$1" == "dev" ]
    then
    export FC_HOST="https://prod.lastmileops.ai"
    fi
fi

envsubst < ./data/FC_Settings__cs.tmpl > ./data/FC_Settings__cs.json

# Install the Functions Companion Package in the org
echo 'Installing Functions Companion v1.24'
echo 'sfdx force:package:install -r --wait 10 --package 04t8c000001AKBA -u' ${username}
sfdx force:package:install -r --wait 10 --package 04t8c000001AKBA -u "${username}"
echo ''
echo 'Configuring the package and installing a dummy API key: XXXXXXX-XXXXXXX-XXXXXXX-XXXXXXX.'
echo 'Be sure to update with a valide API key after you create your Connected App.'
echo ''
echo 'To uninstall this package go to the "Installed Packages" web interface in your Salesforce org.'

echo 'sfdx force:data:tree:import -p ./data/FC_Settings__c-plan.json -u' ${username}
sfdx force:data:tree:import -p ./data/FC_Settings__c-plan.json -u "${username}"

echo 'sfdx force:data:tree:import -p ./data/fcConfig__c-plan.json -u' ${username}
sfdx force:data:tree:import -p ./data/fcConfig__c-plan.json -u "${username}"
