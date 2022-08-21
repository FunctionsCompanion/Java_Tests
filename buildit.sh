#!/bin/bash
# Ask users for scratchOrgName
echo 'Enter a name for the new Scratch org. Make a note of the username. It will be needed to set up the Functions Companion'
echo -n 'test envrionment. Scratch org Name:'
read scratchOrgName
# Set up scratch org
echo 'sfdx force:org:create -f config/project-scratch-def.json --setalias' ${scratchOrgName} '--durationdays 30 --setdefaultusername --json --loglevel fatal'
sfdx force:org:create -f config/project-scratch-def.json --setalias "${scratchOrgName}" --durationdays 30 --setdefaultusername --json --loglevel fatal
