#!/bin/bash
# Ask users for count
echo "Enter the number of queued jobs you want to run"
read jobs
c=1
remain=50
rm *.log
while [ $c -le $jobs ]; do 
    sfdx force:apex:execute -f test_apex/runPLDAsync.apex -u test-pu5u52eadnju@example.com >> outAsyncPLDA.log &
    sleep .5
    sfdx force:apex:execute -f test_apex/runPLDSync.apex -u test-pu5u52eadnju@example.com  >> outSyncPLDA.log &
    sleep .5
    sfdx force:apex:execute -f test_apex/runSFSDKAsync.apex -u test-pu5u52eadnju@example.com >> outAsyncSFSDK.log &
    sleep .5
    sfdx force:apex:execute -f test_apex/runSFSDKSync.apex -u test-pu5u52eadnju@example.com >> outSyncSFSDK.log &
    sleep .5
    echo 'Another 4 runs that queue function invocations.' $c 'out of' $jobs 'batches started.' 
    c=$(($c+1))

done