#!/bin/bash 
# Ask users for prefix 
echo 'Please provide the username for the target org you wish to install Functions Companion.'
echo -n 'username:'
read username

sf login functions # Need to login to functions to get the orgid...
export FC_ORG_ID=`sf env list --all | grep ${username} | awk '{print $4}'`

if [ $# -eq 1 ] && [ "$1" == "test" ]
then
  export FC_HOST="http://localhost:3000"
else
  export FC_HOST="https://app.lastmileops.ai"
fi

envsubst < ./data/FC_Settings__cs.tmpl > ./data/FC_Settings__cs.json

# Install the Functions Companion Package in the org
echo 'Installing Functions Companion v1.46'
echo 'sfdx force:package:install -r --wait 10 --package 04t8c000000lD1s -u' ${username}
sfdx force:package:install -r --wait 10 --package 04t8c000000lD1s -u "${username}"
echo ''
echo 'Configuring the package and installing a dummy API key: XXXXXXX-XXXXXXX-XXXXXXX-XXXXXXX.'
echo 'Be sure to update with a valide API key after you create your Connected App.'
echo ''
echo 'To uninstall this package go to the "Installed Packages" web interface in your Salesforce org.'

echo 'sfdx force:data:tree:import -p ./data/FC_Settings__c-plan.json -u' ${username}
sfdx force:data:tree:import -p ./data/FC_Settings__c-plan.json -u "${username}"

echo 'sfdx force:data:tree:import -p ./data/fcConfig__c-plan.json -u' ${username}
sfdx force:data:tree:import -p ./data/fcConfig__c-plan.json -u "${username}" 
